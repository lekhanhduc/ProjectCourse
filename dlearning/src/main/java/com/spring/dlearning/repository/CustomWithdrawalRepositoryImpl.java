package com.spring.dlearning.repository;

import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.WithdrawalHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.IntStream;

@Repository
public class CustomWithdrawalRepositoryImpl implements CustomWithdrawalRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<WithdrawalHistory> searchByMultipleKeywords(String[] keywords, Pageable pageable) {
        if (keywords == null || keywords.length == 0) {
            return Page.empty(pageable);
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT w FROM WithdrawalHistory w WHERE 1=1 ");

        queryBuilder.append("AND (");
        IntStream.range(0, keywords.length).forEach(i -> {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("(LOWER(w.user.name) LIKE LOWER(CONCAT('%', :keyword")
                    .append(i)
                    .append(", '%')) OR LOWER(w.bank) LIKE LOWER(CONCAT('%', :keyword")
                    .append(i)
                    .append(", '%')))");
        });
        queryBuilder.append(")");

        // Main query
        TypedQuery<WithdrawalHistory> query = entityManager.createQuery(queryBuilder.toString(), WithdrawalHistory.class);
        IntStream.range(0, keywords.length).forEach(i -> query.setParameter("keyword" + i, keywords[i]));

        // Count query for total rows
        String countQueryStr = queryBuilder.toString().replaceFirst("SELECT w", "SELECT COUNT(w)");
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);
        IntStream.range(0, keywords.length).forEach(i -> countQuery.setParameter("keyword" + i, keywords[i]));
        long totalRows = countQuery.getSingleResult();

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<WithdrawalHistory> withdrawalHistories = query.getResultList();
        return new PageImpl<>(withdrawalHistories, pageable, totalRows);
    }
}
