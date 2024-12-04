package com.spring.dlearning.repository;

import com.spring.dlearning.entity.WithdrawalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<WithdrawalHistory, Long> {

    List<WithdrawalHistory> findByUserId(Long userId);

}
