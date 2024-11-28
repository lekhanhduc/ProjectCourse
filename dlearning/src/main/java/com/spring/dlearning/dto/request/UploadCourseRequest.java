package com.spring.dlearning.dto.request;

import com.spring.dlearning.common.CourseLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadCourseRequest {

    @NotBlank(message = "TITLE_NOT_BLANK")
    String title;

    @NotBlank(message = "DESCRIPTION_NOT_BLANK")
    String description;

    @NotNull(message = "COURSES_LEVEL_INVALID")
    CourseLevel courseLevel;

    @NotNull(message = "DURATION_INVALID")
    @Min(value = 0, message = "DURATION_INVALID")
    Integer duration;

    @NotNull(message = "PRICE_INVALID")
    @Min(value = 0, message = "PRICE_INVALID")
    BigDecimal price;

}
