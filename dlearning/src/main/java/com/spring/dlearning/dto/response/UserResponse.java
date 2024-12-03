package com.spring.dlearning.dto.response;

import com.spring.dlearning.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing user information")
public class UserResponse {

    @Schema(description = "User's email address", example = "user@example.com")
    String email;

    @Schema(description = "User's first name", example = "John")
    String firstName;

    @Schema(description = "User's last name", example = "Doe")
    String lastName;

    @Schema(description = "User's date of birth", example = "1990-01-01")
    LocalDate dob;

    @Schema(description = "Indicates whether the user has set a password", example = "true")
    Boolean noPassword;

    @Schema(description = "List of roles assigned to the user")
    Set<Role> roles = new HashSet<>();

}
