package org.hringsak.functions;

import java.util.function.LongPredicate;

class FindLongWithDefault {

    private final LongPredicate predicate;
    private final long defaultValue;

    private FindLongWithDefault(LongPredicate predicate, long defaultValue) {
        this.predicate = predicate;
        this.defaultValue = defaultValue;
    }

    static FindLongWithDefault of(LongPredicate predicate, long defaultValue) {
        return new FindLongWithDefault(predicate, defaultValue);
    }

    LongPredicate getPredicate() {
        return predicate;
    }

    long getDefaultValue() {
        return defaultValue;
    }
}
