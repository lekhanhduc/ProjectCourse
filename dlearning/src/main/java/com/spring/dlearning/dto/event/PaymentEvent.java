package com.spring.dlearning.dto.event;

import com.spring.dlearning.common.PaymentMethodName;
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
    BigDecimal points;
    BigDecimal price;
    PaymentStatus status;
    PaymentMethod paymentMethod;

}
