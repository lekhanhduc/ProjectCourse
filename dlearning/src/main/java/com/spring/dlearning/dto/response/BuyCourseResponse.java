package com.spring.dlearning.dto.response;

import com.spring.dlearning.common.CourseLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuyCourseResponse {
    Long courseId;
    String title;
    String author;
    CourseLevel courseLevel;
    String thumbnail;
    BigDecimal price;
    LocalDateTime createAt;
}
