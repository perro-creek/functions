package org.hringsak.functions.stream;

import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

class FindIntWithDefaultSupplier {

    private final IntPredicate predicate;
    private final IntSupplier defaultSupplier;

    private FindIntWithDefaultSupplier(IntPredicate predicate, IntSupplier defaultSupplier) {
        this.predicate = predicate;
        this.defaultSupplier = defaultSupplier;
    }

    static <T> FindIntWithDefaultSupplier of(IntPredicate predicate, IntSupplier defaultSupplier) {
        return new FindIntWithDefaultSupplier(predicate, defaultSupplier);
    }

    IntPredicate getPredicate() {
        return predicate;
    }

    IntSupplier getDefaultSupplier() {
        return defaultSupplier;
    }
}
