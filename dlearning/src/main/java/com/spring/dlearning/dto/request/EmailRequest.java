package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object containing user's email address for OTP operations")
public class EmailRequest {

    @Schema(description = "The email address of the user", example = "user@example.com")
    String email;
}
