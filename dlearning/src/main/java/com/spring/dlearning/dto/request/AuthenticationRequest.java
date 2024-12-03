package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object containing user's email and password for authentication")
public class AuthenticationRequest {

    @Schema(description = "The email address of the user", example = "user@example.com")
    String email;

    @Schema(description = "The password of the user", example = "password123")
    String password;
}
