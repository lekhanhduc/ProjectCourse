package com.spring.dlearning.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spring.dlearning.common.PaymentMethodName;
import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.constant.PaymentType;
import com.spring.dlearning.dto.event.PaymentEvent;
import com.spring.dlearning.dto.request.CreatePaymentLinkRequestBody;
import com.spring.dlearning.dto.response.CourseSoldByTeacherResponse;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.PaymentResponse;
import com.spring.dlearning.dto.response.VNPAYResponse;
import com.spring.dlearning.entity.Enrollment;
import com.spring.dlearning.entity.Payment;
import com.spring.dlearning.entity.PaymentMethod;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.model.PaymentInfo;
import com.spring.dlearning.repository.EnrollmentRepository;
import com.spring.dlearning.repository.PaymentRepository;
import com.spring.dlearning.repository.UserRepository;
import com.spring.dlearning.service.specitification.PaymentSpecification;
import com.spring.dlearning.utils.SecurityUtils;
import com.spring.dlearning.utils.ServletHelper;
import com.spring.dlearning.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {

    PayOS payOS;
    ObjectMapper objectMapper;
    PaymentRepository paymentRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    UserRepository userRepository;
    VNPayUtil vnPayUtil;
    EnrollmentRepository enrollmentRepository;

    public VNPAYResponse createVnPayPayment(HttpServletRequest request) {
        BigDecimal amount = new BigDecimal(request.getParameter("amount")).multiply(BigDecimal.valueOf(100));
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PaymentInfo paymentInfo = new PaymentInfo()
                .setReference(PaymentType.DEPOSIT + "_" + user.getId() + "_" + VNPayUtil.getRandomNumber(6))
                .setAmount(amount)
                .setDescription("Thanh toan")
                .setExpiresIn(Duration.ofMinutes(15))
                .setIpAddress(ServletHelper.extractIPAddress(request));
        String paymentUrl = vnPayUtil.getPaymentURL(paymentInfo);

        return VNPAYResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public PageResponse<PaymentResponse> getPaymentByUserLogin (int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Payment> payments = paymentRepository.findPaymentByUserLogin(user.getId(), pageable);
        List<PaymentResponse> data = payments.getContent().stream()
                .map(p -> PaymentResponse.builder()
                        .email(p.getUser().getEmail())
                        .thumbnail(p.getCourse() != null ? p.getCourse().getThumbnail() : "")
                        .title(p.getCourse() != null ? p.getCourse().getTitle() : "")
                        .price(p.getPrice())
                        .points(p.getPoints())
                        .paymentMethod(String.valueOf(p.getPaymentMethod().getMethodName()))
                        .status(String.valueOf(p.getPaymentStatus()))
                        .createAt(p.getCreatedAt())
                        .build()).toList();

        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalElements(payments.getTotalElements())
                .totalPages(payments.getTotalPages())
                .data(data)
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    public List<CourseSoldByTeacherResponse> getCoursesSoldByTeacher () {
        var email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var payments = paymentRepository.getCoursesSoldByTeacher(user.getId());

        return payments.stream().map(p -> CourseSoldByTeacherResponse.builder()
                .email(p.getUser().getEmail())
                .title(p.getCourse() != null ? p.getCourse().getTitle() : "")
                .thumbnail(p.getCourse().getThumbnail())
                .price(p.getPrice())
                .status(String.valueOf(p.getPaymentStatus()))
                .time(p.getCreatedAt())
                .build()).toList();
    }

    public PageResponse<PaymentResponse> getPaymentsByDateRange(int page, int size, LocalDate startDate, LocalDate endDate) {
        var email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Payment> payments = paymentRepository.findAll(
                PaymentSpecification.byUserAndCreateAtRange(user.getId(), startDateTime, endDateTime),
                pageable
        );

        List<PaymentResponse> paymentResponses = payments.getContent().stream()
                .map(p -> PaymentResponse.builder()
                        .email(p.getUser().getEmail())
                        .thumbnail(p.getCourse() != null ? p.getCourse().getThumbnail() : null)
                        .title(p.getCourse() != null ? p.getCourse().getTitle() : "Deposit")
                        .price(p.getPrice())
                        .points(p.getPoints())
                        .status(p.getPaymentStatus() != null ? p.getPaymentStatus().toString() : "")
                        .createAt(p.getCreatedAt())
                        .build())
                .toList();

        return PageResponse.<PaymentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(payments.getTotalElements())
                .totalPages(payments.getTotalPages())
                .data(paymentResponses)
                .build();
    }

    public ObjectNode createPaymentLink(CreatePaymentLinkRequestBody requestBody) {
        ObjectNode response = objectMapper.createObjectNode();
        final int quantity = requestBody.getQuantity();
        final String productName = requestBody.getProductName();
        final String description = requestBody.getDescription();
        final String returnUrl = requestBody.getReturnUrl();
        final String cancelUrl = requestBody.getCancelUrl();
        final int price = requestBody.getPrice() * quantity;

        long orderCode = requestBody.getOrderCode();
        ItemData item = ItemData.builder()
                .name(productName)
                .price(price)
                .quantity(quantity).build();

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .orderCode(orderCode)
                .userId(user.getId())
                .courseId(requestBody.getCourseId())
                .price(BigDecimal.valueOf(price))
                .paymentMethod(PaymentMethod.builder()
                        .methodName(PaymentMethodName.BANK_TRANSFER)
                        .build())
                .build();
        kafkaTemplate.send("payment-pending", paymentEvent);

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .description(description)
                .amount(price)
                .item(item)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();
        try {
            CheckoutResponseData data = payOS.createPaymentLink(paymentData);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;
        } catch (Exception e) {
            log.error("Error when create payment link: {}", e.getMessage());
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode payosTransferHandler(ObjectNode body) throws JsonProcessingException {
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
        try {
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);

            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            String code = data.getCode();
            Long orderCode = data.getOrderCode();
            Payment payment = paymentRepository.findByOrderCode(orderCode)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_CODE_NOT_EXISTED));

            if(Objects.equals(code, "00")) {
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
                enrollmentRepository.save(Enrollment.builder()
                        .user(payment.getUser())
                        .course(payment.getCourse())
                        .build());
                log.info("Enrolment created");
            } else {
                payment.setPaymentStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);
            log.info("Payment created");

            response.put("error", 0);
            response.put("message", "success");
            return response;

        } catch (Exception e) {
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode getOrderById(Long orderId) {
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            log.error("Error when get order by id: {}", e.getMessage());
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

}
