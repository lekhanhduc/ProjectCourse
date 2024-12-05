package com.spring.dlearning.repository;

import com.spring.dlearning.entity.WithdrawalHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomWithdrawalRepository {
    Page<WithdrawalHistory> searchByMultipleKeywords(String[] keywords, Pageable pageable);
}
