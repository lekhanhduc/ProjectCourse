package com.spring.dlearning.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalRequest {

    Long points;
    String bank;
    String bankNumber;
}
