package com.spring.dlearning.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spring.dlearning.constant.PaymentType;
import com.spring.dlearning.dto.request.CreatePaymentLinkRequestBody;
import com.spring.dlearning.dto.response.*;
import com.spring.dlearning.entity.Advertisement;
import com.spring.dlearning.entity.Payment;
import com.spring.dlearning.entity.PaymentMethod;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.repository.AdvertisementRepository;
import com.spring.dlearning.repository.PaymentMethodRepository;
import com.spring.dlearning.repository.PaymentRepository;
import com.spring.dlearning.repository.UserRepository;
import com.spring.dlearning.service.PaymentService;
import com.spring.dlearning.common.AdsStatus;
import com.spring.dlearning.common.PaymentMethodName;
import com.spring.dlearning.common.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;
    PaymentRepository paymentRepository;
    UserRepository userRepository;
    AdvertisementRepository advertisementRepository;
    PaymentMethodRepository paymentMethodRepository;

    @GetMapping("/vn-pay")
    public ApiResponse<VNPAYResponse> pay(HttpServletRequest request) {
        return ApiResponse.<VNPAYResponse>builder()
                .code(HttpStatus.OK.value())
                .message("OK")
                .result(paymentService.createVnPayPayment(request))
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public void handleVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String redirectUrl;
        String transactionStatus = request.getParameter("vnp_ResponseCode");

        BigDecimal amountInVNPay = new BigDecimal(request.getParameter("vnp_Amount"));
        BigDecimal actualAmount = amountInVNPay.divide(new BigDecimal(100));

        if ("00".equals(transactionStatus)) {
            String paymentRef = request.getParameter("vnp_TxnRef");
            String[] refParts = paymentRef.split("_");
            String paymentType = refParts[0];

            switch (paymentType) {
                case PaymentType.DEPOSIT: {
                    User user = userRepository.findById(Long.parseLong(refParts[1]))
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

                    recordPaymentTransaction(user, actualAmount, PaymentStatus.COMPLETED);
                    BigDecimal totalPoints = actualAmount.divide(new BigDecimal(1000));

                    long pointsToAdd = totalPoints.longValue();

                    if (user.getPoints() == null) {
                        user.setPoints(pointsToAdd);
                    } else {
                        user.setPoints(user.getPoints() + pointsToAdd);
                    }

                    userRepository.save(user);
                        break;
                }
                case PaymentType.ADVERTISEMENT: {
                    Advertisement advertisement = advertisementRepository.findById(Long.parseLong(refParts[1]))
                            .orElseThrow(() -> new AppException(ErrorCode.ADVERTISEMENT_ID_INVALID));
                    if (actualAmount.compareTo(advertisement.getPrice()) >= 0) {
                        advertisement.setApprovalStatus(AdsStatus.ACTIVE);
                    }
                    advertisementRepository.save(advertisement);
                    break;
                }
                default:
                    throw new AppException(ErrorCode.INVALID_PAYMENT_TYPE);
            }

            redirectUrl = "http://localhost:3000/payment-success";
        } else if ("24".equals(transactionStatus)) {
            redirectUrl = "http://localhost:3000/payment-cancel";
        } else {
            redirectUrl = "http://localhost:3000/payment-failed";
        }
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/transaction/user-current")
    ApiResponse<PageResponse<PaymentResponse>> fetchTransactionByUserLogin(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ) {

        return ApiResponse.<PageResponse<PaymentResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(paymentService.getPaymentByUserLogin(page, size))
                .build();
    }

    @GetMapping("/transaction/teacher-buy-course")
    ApiResponse<List<CourseSoldByTeacherResponse>> getCoursesSoldByTeacher() {

        return ApiResponse.<List<CourseSoldByTeacherResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(paymentService.getCoursesSoldByTeacher())
                .build();
    }

    @GetMapping("/transaction/search")
    ApiResponse<PageResponse<PaymentResponse>> searchTransaction(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate, formatter) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate, formatter) : null;

        return ApiResponse.<PageResponse<PaymentResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(paymentService.getPaymentsByDateRange(page, size, start, end))
                .build();
    }

    private void recordPaymentTransaction(User user, BigDecimal amount, PaymentStatus status) {

        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName(PaymentMethodName.BANK_TRANSFER)
                .orElseGet(() -> paymentMethodRepository.save(
                        PaymentMethod.builder()
                                .methodName(PaymentMethodName.BANK_TRANSFER)
                                .build()
                ));

        Payment payment = Payment.builder()
                .user(user)
                .price(amount)
                .points(amount.divide(BigDecimal.valueOf(1000)))
                .paymentMethod(paymentMethod)
                .paymentStatus(status)
                .build();

        paymentRepository.save(payment);
    }

    @PostMapping("/create")
    ObjectNode createPaymentLink(@RequestBody CreatePaymentLinkRequestBody requestBody) {
        return paymentService.createPaymentLink(requestBody);
    }

    @GetMapping( "/{orderId}")
    ObjectNode getOrderById(@PathVariable("orderId") Long orderId) {
        return paymentService.getOrderById(orderId);
    }

    @PostMapping("/confirm-webhook")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException, IllegalArgumentException {
        return paymentService.payosTransferHandler(body);
    }

}
