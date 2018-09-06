package org.hringsak.functions;

import java.util.function.LongPredicate;
import java.util.function.LongSupplier;

class FindLongWithDefaultSupplier {

    private final LongPredicate predicate;
    private final LongSupplier defaultSupplier;

    private FindLongWithDefaultSupplier(LongPredicate predicate, LongSupplier defaultSupplier) {
        this.predicate = predicate;
        this.defaultSupplier = defaultSupplier;
    }

    static <T> FindLongWithDefaultSupplier of(LongPredicate predicate, LongSupplier defaultSupplier) {
        return new FindLongWithDefaultSupplier(predicate, defaultSupplier);
    }

    LongPredicate getPredicate() {
        return predicate;
    }

    LongSupplier getDefaultSupplier() {
        return defaultSupplier;
    }
}
