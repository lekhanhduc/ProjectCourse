package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.entity.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    String email;
    String thumbnail;
    String title;
    String paymentMethod;
    String status;
    BigDecimal points;
    BigDecimal price;

    @JsonFormat(pattern = "EEEE, dd MM yy hh:mm:ss")
    LocalDateTime createAt;
}
