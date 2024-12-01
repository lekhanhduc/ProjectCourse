package com.spring.dlearning.service.specitification;

import com.spring.dlearning.entity.Course;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.spring.dlearning.service.specitification.SearchOperation.ZERO_OR_MORE_REGEX;

public class CourseSpecificationBuilder {

    private final List<SearchCriteria> param;

    public CourseSpecificationBuilder() {
        this.param = new ArrayList<>();
    }

    public CourseSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix) {
        return with(null, key, operation,  value,  prefix,  suffix);
    }

    public CourseSpecificationBuilder with(String orPredicate, String key, String operation, String value, String prefix, String suffix) {
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

    public Specification<Course> build () {
        if(param.isEmpty())
            return null;
        Specification<Course> specification = new CourseSpecification(param.getFirst());
        if(param.size() > 1) {
            for(int i = 1; i < param.size(); i++) {
                specification = param.get(i).getOrPredicate() ? Specification.where(specification)
                        .or(new CourseSpecification(param.get(i)))
                        : Specification.where(specification).and(new CourseSpecification(param.get(i)));
            }
        }
        return specification;
    }
}
