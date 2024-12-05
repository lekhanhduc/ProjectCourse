package com.spring.dlearning.service;

import com.spring.dlearning.constant.PaymentType;
import com.spring.dlearning.dto.response.CourseSoldByTeacherResponse;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.PaymentResponse;
import com.spring.dlearning.dto.response.VNPAYResponse;
import com.spring.dlearning.entity.Payment;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.model.PaymentInfo;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final UserRepository userRepository;
    PaymentRepository paymentRepository;
    private final VNPayUtil vnPayUtil;

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
                        .status(String.valueOf(p.getStatus()))
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
                .status(String.valueOf(p.getStatus()))
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
                        .status(p.getStatus() != null ? p.getStatus().toString() : "")
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
}
