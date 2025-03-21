package com.spring.dlearning.dto.event;

import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.entity.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentEvent {
    Long userId;
    Long courseId;
    BigDecimal price;
    PaymentStatus status;
    Long orderCode;
    PaymentMethod paymentMethod;
}
