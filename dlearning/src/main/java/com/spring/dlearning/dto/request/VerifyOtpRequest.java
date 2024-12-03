package com.spring.dlearning.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request body for OTP verification containing the user's email and OTP")
public class VerifyOtpRequest {


    @Schema(description = "Email of the user attempting to verify OTP", example = "user@example.com")
    String email;

    @Schema(description = "OTP provided by the user for verification", example = "123456")
    String otp;
}
