package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CourseSoldByTeacherResponse {

    String email;
    String title;
    String thumbnail;
    BigDecimal price;
    String status;

    @JsonFormat(pattern = "EEEE, dd MM yyyy")
    LocalDateTime time;
}
