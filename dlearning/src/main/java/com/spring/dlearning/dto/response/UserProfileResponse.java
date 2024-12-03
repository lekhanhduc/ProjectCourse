package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.dlearning.common.CourseLevel;
import com.spring.dlearning.common.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {

    @Schema(description = "URL of the user's avatar image", example = "https://example.com/avatar.jpg")
    String avatar;

    @Schema(description = "User's first name", example = "John")
    String firstName;

    @Schema(description = "User's last name", example = "Doe")
    String lastName;

    @Schema(description = "User's gender")
    Gender gender;

    @Schema(description = "User's phone number", example = "123-456-7890")
    String phone;

    @Schema(description = "User's date of birth", example = "1990-01-01")
    LocalDate dob;

    @Schema(description = "User's address", example = "123 Main St, Springfield")
    String address;

    @Schema(description = "A brief description about the user", example = "Passionate developer with experience in Java and Spring Boot")
    String description;

    @Schema(description = "User's course level", example = "INTERMEDIATE")
    CourseLevel courseLevel;
}
