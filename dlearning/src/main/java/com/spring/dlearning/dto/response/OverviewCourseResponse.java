package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OverviewCourseResponse {
    String title;
    String thumbnail;
    BigDecimal avgReview;
    Long quantity;
    BigDecimal totalPrice;
}
