package com.spring.dlearning.service.specitification;

import lombok.Getter;

@Getter
public enum SearchOperation {

    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, START_WITH, ENDS_WITH, CONTAINS;

    public static final String ZERO_OR_MORE_REGEX = "*";
    public static final String OR_PREDICATE_FLAG= "'";

    public static SearchOperation getOperation (char input) {
        return switch (input) {
            case ':' -> EQUALITY;
            case '!' -> NEGATION;
            case '>' -> GREATER_THAN;
            case '<' -> LESS_THAN;
            case '~' -> LIKE;
            default -> null;
        };
    }
}
