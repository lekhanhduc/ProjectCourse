package com.spring.dlearning.dto.request;

import com.spring.dlearning.exception.validation.constraint.DateOfBirth;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object for creating a new user. Contains necessary user details such as email, password, name, and date of birth.")
public class UserCreationRequest {

    @Schema(description = "The email address of the user", example = "user@example.com")
    String email;

    @Schema(description = "The password for the user account. It must be at least 6 characters long.", example = "123456")
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @Schema(description = "The first name of the user", example = "John")
    String firstName;

    @Schema(description = "The last name of the user", example = "Doe")
    String lastName;

    @Schema(description = "The date of birth of the user. The format should be 'yyyy/MM/dd'.", example = "1990/05/15")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @DateOfBirth(message = "INVALID_DOB")
    LocalDate dob;
}
