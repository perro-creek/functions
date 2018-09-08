package org.hringsak.functions.doubles;

import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;

class FindDoubleWithDefaultSupplier {

    private final DoublePredicate predicate;
    private final DoubleSupplier defaultSupplier;

    private FindDoubleWithDefaultSupplier(DoublePredicate predicate, DoubleSupplier defaultSupplier) {
        this.predicate = predicate;
        this.defaultSupplier = defaultSupplier;
    }

    static FindDoubleWithDefaultSupplier of(DoublePredicate predicate, DoubleSupplier defaultSupplier) {
        return new FindDoubleWithDefaultSupplier(predicate, defaultSupplier);
    }

    DoublePredicate getPredicate() {
        return predicate;
    }

    DoubleSupplier getDefaultSupplier() {
        return defaultSupplier;
    }
}
