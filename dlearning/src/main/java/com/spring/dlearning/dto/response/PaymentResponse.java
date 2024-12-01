package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.entity.PaymentMethod;
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
public class PaymentResponse {
    String email;
    String title;
    String paymentMethod;
    String status;
    BigDecimal points;
    BigDecimal price;
}
