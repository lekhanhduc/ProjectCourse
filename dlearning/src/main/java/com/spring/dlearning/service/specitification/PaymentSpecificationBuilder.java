package com.spring.dlearning.service.specitification;

import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.Payment;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.spring.dlearning.service.specitification.SearchOperation.ZERO_OR_MORE_REGEX;

public class PaymentSpecificationBuilder {

    private final List<SearchCriteria> param;

    public PaymentSpecificationBuilder() {
        this.param = new ArrayList<>();
    }

    public PaymentSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix) {
        return with(null, key, operation,  value,  prefix,  suffix);
    }

    public PaymentSpecificationBuilder with(String orPredicate, String key, String operation, String value, String prefix, String suffix) {
        SearchOperation searchOperation = SearchOperation.getOperation(operation.charAt(0));
        if(searchOperation == SearchOperation.EQUALITY) {
            boolean startWith = prefix != null && Objects.equals(prefix, ZERO_OR_MORE_REGEX);
            boolean endWith = suffix != null && Objects.equals(prefix, ZERO_OR_MORE_REGEX);

            if(startWith && endWith) {
                searchOperation = SearchOperation.CONTAINS;
            } else if (startWith) {
                searchOperation = SearchOperation.ENDS_WITH;
            } else
                searchOperation = SearchOperation.START_WITH;
        }

        param.add(new SearchCriteria(orPredicate, key, searchOperation, value));
        return this;
    }

    public Specification<Payment> build () {
        if(param.isEmpty())
            return null;
        Specification<Payment> specification = new PaymentSpecification(param.getFirst());
        if(param.size() > 1) {
            for(int i = 1; i < param.size(); i++) {
                specification = param.get(i).getOrPredicate() ? Specification.where(specification)
                        .or(new PaymentSpecification(param.get(i)))
                        : Specification.where(specification).and(new PaymentSpecification(param.get(i)));
            }
        }
        return specification;
    }
}
