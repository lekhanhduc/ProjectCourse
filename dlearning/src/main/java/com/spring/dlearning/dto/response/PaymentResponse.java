package com.spring.dlearning.dto.response;

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
public class PaymentResponse {
    String email;
    String title;
    PaymentMethod paymentMethod;
    PaymentStatus status;
    BigDecimal points;
    BigDecimal price;
}
