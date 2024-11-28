package com.spring.dlearning.repository;

import com.spring.dlearning.entity.InvalidatedToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsById(@NonNull String id);
}
