package com.spring.dlearning.dto.request;

import com.spring.dlearning.common.CourseLevel;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCreationRequest {
    String title;
    String description;
    Integer duration;
    String language;
    CourseLevel courseLevel;

    @Min(value = 1, message = "POINT_INVALID")
    Long points;
}
