package org.hringsak.functions;

import java.util.function.IntPredicate;

class FindIntWithDefault {

    private final IntPredicate predicate;
    private final int defaultValue;

    private FindIntWithDefault(IntPredicate predicate, int defaultValue) {
        this.predicate = predicate;
        this.defaultValue = defaultValue;
    }

    static FindIntWithDefault of(IntPredicate predicate, int defaultValue) {
        return new FindIntWithDefault(predicate, defaultValue);
    }

    IntPredicate getPredicate() {
        return predicate;
    }

    int getDefaultValue() {
        return defaultValue;
    }
}
