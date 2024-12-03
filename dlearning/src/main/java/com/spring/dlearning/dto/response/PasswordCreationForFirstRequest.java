package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new password for the first time")
public class PasswordCreationForFirstRequest {
    @Size(min = 6, message = "INVALID_PASSWORD")
    @Schema(description = "User's password, must be at least 6 characters long", example = "password123")
    String password;
}
