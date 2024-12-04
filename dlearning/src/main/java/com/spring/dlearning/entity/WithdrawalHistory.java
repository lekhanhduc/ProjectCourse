package com.spring.dlearning.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.dlearning.common.Bank;
import com.spring.dlearning.common.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "withdrawal_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalHistory extends AbstractEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    @Column(name = "points", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    Long points;

    @Column(name = "money", columnDefinition = "BIGINT DEFAULT 0", nullable = false)
    Long money;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank", nullable = false)
    Bank bank;

    @Column(name = "bank_number", nullable = false)
    String bankNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    TransactionStatus status;
}
