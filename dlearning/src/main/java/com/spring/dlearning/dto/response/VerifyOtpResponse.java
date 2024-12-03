package com.spring.dlearning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response body for OTP verification indicating if the OTP is valid")
public class VerifyOtpResponse {

    @Schema(description = "Indicates whether OTP verification was successful", example = "true")
    boolean valid;
}
