package com.spring.dlearning.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.dlearning.common.Bank;
import com.spring.dlearning.common.TransactionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalHistoryResponse {

    Long id;

    String name;

    Long points;

    Long money;

    Bank bank;

    String bankNumber;

    TransactionStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdAt;
}
