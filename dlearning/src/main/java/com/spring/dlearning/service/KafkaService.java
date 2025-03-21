package com.spring.dlearning.service;

import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.dto.event.CertificateCreationEvent;
import com.spring.dlearning.dto.event.NotificationEvent;
import com.spring.dlearning.dto.event.PaymentEvent;
import com.spring.dlearning.dto.response.CertificateResponse;
import com.spring.dlearning.dto.response.PaymentResponse;
import com.spring.dlearning.entity.*;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class KafkaService {

    UserRepository userRepository;
    CourseRepository courseRepository;
    CertificateRepository certificateRepository;
    EnrollmentRepository enrollmentRepository;
    PaymentRepository paymentRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics ="certificate-creation", groupId = "certificate-group")
    public void createCertificate(CertificateCreationEvent creationEvent) {
        log.info("Processing certificate creation for userId: {}, courseId: {}", creationEvent.getUserId(), creationEvent.getCourseId());

        Certificate certificate = Certificate.builder()
                .name("DLearning Certificate of Completion")
                .user(userRepository.findById(creationEvent.getUserId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .course(courseRepository.findById(creationEvent.getCourseId())
                        .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED)))
                .issueDate(LocalDateTime.now())
                .build();
        certificateRepository.save(certificate);

        Map<String, Object> data = new HashMap<>();
        data.put("recipient", certificate.getUser().getEmail());
        data.put("courseName", certificate.getCourse().getTitle());
        data.put("issueDate", certificate.getIssueDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - hh:mm:ss a")));
        data.put("author", certificate.getCourse().getAuthor().getName());

        NotificationEvent event =  NotificationEvent.builder()
                .channel("EMAIL")
                .subject("DLearning Certificate of Completion")
                .recipient(certificate.getUser().getEmail())
                .templateCode("certificate-template")
                .param(data)
                .build();

        kafkaTemplate.send("notification-delivery", event);
    }

    @KafkaListener(topics = "payment-creation", groupId = "payment-group")
    public void creationPayment (PaymentEvent paymentEvent) {
        log.info("Processing payment creation for userId: {}, courseId: {}", paymentEvent.getUserId(), paymentEvent.getCourseId());

        Payment payment =Payment.builder()
                .user(userRepository.findById(paymentEvent.getUserId()
                        ).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .course(courseRepository.findById(paymentEvent.getCourseId())
                        .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED)))
                .paymentMethod(paymentEvent.getPaymentMethod())
                .paymentStatus(paymentEvent.getStatus())
                .price(paymentEvent.getPrice())
                .build();

        paymentRepository.save(payment);

        Map<String, Object> data = new HashMap<>();
        data.put("recipient", payment.getUser().getEmail());
        data.put("email", payment.getUser().getEmail());
        data.put("courseName", payment.getCourse().getTitle());
        data.put("points", payment.getPoints());
        data.put("status", payment.getPaymentStatus());
        data.put("link", "http://localhost:3000/course-detail/" + payment.getCourse().getId());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("Send Email")
                .recipient(payment.getUser().getEmail())
                .templateCode("payment-creation-template")
                .subject("Thanks you...")
                .param(data)
                .build();

        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @KafkaListener(topics = "payment-pending", groupId = "payment-group")
    public void createPaymentPending(PaymentEvent paymentEvent) {
        log.info("Payment pending starting...");
        Course course = courseRepository.findById(paymentEvent.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));
        User user = userRepository.findById(paymentEvent.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Payment payment = Payment.builder()
                .course(course)
                .user(user)
                .paymentStatus(PaymentStatus.PENDING)
                .orderCode(paymentEvent.getOrderCode())
                .price(paymentEvent.getPrice())
                .build();


        paymentRepository.save(payment);

        log.info("Payment pending created");
    }

}
