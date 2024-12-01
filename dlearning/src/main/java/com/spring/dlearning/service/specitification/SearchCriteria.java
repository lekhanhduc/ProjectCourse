package com.spring.dlearning.service.specitification;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.spring.dlearning.service.specitification.SearchOperation.OR_PREDICATE_FLAG;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCriteria {
    String key;
    SearchOperation operation;
    Object value;
    Boolean orPredicate;

    public SearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
