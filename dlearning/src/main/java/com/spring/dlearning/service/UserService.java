package com.spring.dlearning.service;

import com.spring.dlearning.constant.PredefinedRole;
import com.spring.dlearning.dto.event.NotificationEvent;
import com.spring.dlearning.dto.request.EmailRequest;
import com.spring.dlearning.dto.request.PasswordCreationRequest;
import com.spring.dlearning.dto.request.UserCreationRequest;
import com.spring.dlearning.dto.request.VerifyOtpRequest;
import com.spring.dlearning.dto.response.*;
import com.spring.dlearning.entity.Role;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.UserMapper;
import com.spring.dlearning.repository.RoleRepository;
import com.spring.dlearning.repository.UserRepository;
import com.spring.dlearning.utils.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import static com.spring.dlearning.utils.SecurityUtils.generateOtp;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    EmailService emailService;
    OtpService otpService;
    KafkaTemplate<String, Object> kafkaTemplate;

    public boolean findByEmail( EmailRequest request){
       return userRepository.existsByEmail(request.getEmail());
    }

    @Transactional
    public UserResponse createUser(UserCreationRequest request, String otp)  {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        String storedOtp = otpService.getOtp(request.getEmail());
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        User user = userMapper.toUser(request);

        Role role = roleRepository.findByName(PredefinedRole.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setName(request.getFirstName() + " " + request.getLastName());
        userRepository.save(user);

        otpService.deleteOtp(request.getEmail());

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(user.getEmail())
                .templateCode("welcome-email")
                .subject("Welcome to DLearning")
                .build();

        kafkaTemplate.send("notification-delivery", event);

        return userMapper.toUserResponse(user);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public void createPassword(PasswordCreationForFirstRequest request){

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        log.info("Email --> {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("isAuthenticated()")
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);

        userResponse.setNoPassword(! StringUtils.hasText(user.getPassword()));
        //true => noPassword => chưa có password
        //false => có password
        return userResponse;
    }

    @Transactional
    public void sendOtpForgotPassword(EmailRequest request)
            throws MessagingException, UnsupportedEncodingException {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String otp = generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        user.setOtp(otp);
        user.setOtpExpiryDate(expiryDate);

        String subject = "Your OTP Code";
        String content = String.format(
                "<p>Hello,</p>" +
                        "<p>We received a request to reset your password. Use the following OTP to reset it:</p>" +
                        "<h2>%s</h2>" +
                        "<p>If you did not request this, please ignore this email.</p>" +
                        "<p>Best regards,<br/>Your Company</p>",
                otp
        );
        emailService.sendEmail(subject, content, List.of(user.getEmail()));
    }

    public void sendOtpRegister(EmailRequest request)
            throws MessagingException, UnsupportedEncodingException {
        String otp = generateOtp();

        otpService.saveOtp(request.getEmail(), otp);

        String subject = "Your OTP Code for Account Registration";

        StringBuilder content = new StringBuilder();
        content.append("<html>")
                .append("<body style='font-family: Arial, sans-serif; line-height: 1.6;'>")
                .append("<h2 style='color: #4CAF50;'>Welcome to DLearning!</h2>")
                .append("<p>Dear <strong>")
                .append(request.getEmail())
                .append("</strong>,</p>")
                .append("<p>Thank you for registering with <strong>DLearning</strong>. We are excited to have you on board!</p>")
                .append("<p style='font-size: 18px;'><strong>Your OTP Code is:</strong> ")
                .append("<span style='font-size: 22px; color: #FF5733;'><strong>")
                .append(otp)
                .append("</strong></span></p>")
                .append("<p><strong>Note:</strong> This OTP is valid for <em>5 minutes</em>. Please enter it as soon as possible to complete your registration.</p>")
                .append("<p>If you did not request this code, please ignore this email. For your security, do not share this code with anyone.</p>")
                .append("<br/>")
                .append("<p>Best regards,</p>")
                .append("<p><strong>DLearning Team</strong></p>")
                .append("</body>")
                .append("</html>");

        String emailContent = content.toString();
        emailService.sendEmail(subject, emailContent, List.of(request.getEmail()));
    }


    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(user.getOtp() == null || ! user.getOtp().equals(request.getOtp())){
            return VerifyOtpResponse.builder()
                    .valid(false)
                    .build();
        }

        if(user.getOtpExpiryDate() == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())){
            return VerifyOtpResponse.builder()
                    .valid(false)
                    .build();
        }

        return VerifyOtpResponse.builder()
                .valid(true)
                .build();
    }

    @Transactional
    public void resetPassword(PasswordCreationRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        if (user.getOtpExpiryDate() == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(null);
        user.setOtpExpiryDate(null);
        userRepository.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    public GetPointsCurrentLogin getPointsCurrentLogin(){
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return GetPointsCurrentLogin.builder()
                .points(user.getPoints())
                .build();
    }
}

