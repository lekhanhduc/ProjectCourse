package com.spring.dlearning.service.specitification;

import com.spring.dlearning.entity.Payment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class PaymentSpecification {

    private PaymentSpecification() {}

    public static Specification<Payment> byUserAndCreateAtRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            Predicate userPredicate = criteriaBuilder.equal(root.get("user").get("id"), userId);

            if (startDate != null && endDate != null) {
                Predicate datePredicate = criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
                return criteriaBuilder.and(userPredicate, datePredicate);
            } else if (startDate != null) {
                Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate);
                return criteriaBuilder.and(userPredicate, datePredicate);
            } else if (endDate != null) {
                Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
                return criteriaBuilder.and(userPredicate, datePredicate);
            } else {
                return userPredicate;
            }
        };
    }
}
