package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request data for resetting a user's password.")
public class PasswordCreationRequest {

    @NotBlank(message = "EMAIL_INVALID")
    @Schema(description = "User's email address to reset password.", example = "user@example.com")
    String email;

    @NotBlank(message = "INVALID_OTP")
    @Schema(description = "OTP code sent to the user's email for verification.", example = "123456")
    String otp;

    @Size(min = 6, message = "INVALID_PASSWORD")
    @Schema(description = "The new password that the user wants to set.", example = "newPassword123")
    String password;
}
