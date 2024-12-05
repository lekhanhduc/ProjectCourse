package com.spring.dlearning.repository;

import com.spring.dlearning.common.TransactionStatus;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.entity.WithdrawalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<WithdrawalHistory, Long>, CustomWithdrawalRepository {

    List<WithdrawalHistory> findByUserId(Long userId);

    Page<WithdrawalHistory> findByStatus(TransactionStatus status, Pageable pageable);

}
