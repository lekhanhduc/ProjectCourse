package com.spring.dlearning.dto.request;

import com.spring.dlearning.exception.validation.constraint.PhoneNumber;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterTeacherRequest {

    String email;
    String name;

    @PhoneNumber
    String phone;

    @NotBlank(message = "EXPERTISE_INVALID")
    String expertise;

    @Min(value = 1, message = "YEARS_OF_EXPERIENCE_INVALID")
    Double yearsOfExperience;
    String bio;
    String facebookLink;

    @NotBlank(message = "CERTIFICATE_INVALID")
    String certificate;

    @NotBlank(message = "CV_INVALID")
    String cvUrl;

}
