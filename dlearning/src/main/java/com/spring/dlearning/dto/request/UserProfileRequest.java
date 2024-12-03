package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.dlearning.common.CourseLevel;
import com.spring.dlearning.common.Gender;
import com.spring.dlearning.exception.validation.constraint.DateOfBirth;
import com.spring.dlearning.exception.validation.constraint.PhoneNumber;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request object for updating user profile")
public class UserProfileRequest {

    @Schema(description = "The avatar URL of the user", example = "https://example.com/avatar.jpg")
    String avatar;

    @Schema(description = "The user's first name", example = "John")
    String firstName;

    @Schema(description = "The user's last name", example = "Doe")
    String lastName;

    @Schema(description = "The user's gender", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    Gender gender;

    @Schema(description = "The user's phone number", example = "0123456789")
    @PhoneNumber(message = "PHONE_NUMBER_INVALID")
    String phone;

    @Schema(description = "The user's date of birth", example = "1990-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateOfBirth(message = "INVALID_DOB")
    LocalDate dob;

    @Schema(description = "The user's address", example = "1234 Elm Street, Springfield, IL")
    String address;

    @Schema(description = "A brief description about the user", maxLength = 400, example = "I am a passionate software developer with a love for learning new technologies.")
    @Size(max = 400)
    String description;

    @Schema(description = "The user's current course level", example = "BEGINNER", allowableValues = {"BEGINNER", "INTERMEDIATE", "ADVANCED"})
    CourseLevel courseLevel;
}
