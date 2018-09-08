package org.hringsak.functions.doubles;

import java.util.function.DoublePredicate;

class FindDoubleWithDefault {

    private final DoublePredicate predicate;
    private final double defaultValue;

    private FindDoubleWithDefault(DoublePredicate predicate, double defaultValue) {
        this.predicate = predicate;
        this.defaultValue = defaultValue;
    }

    static FindDoubleWithDefault of(DoublePredicate predicate, double defaultValue) {
        return new FindDoubleWithDefault(predicate, defaultValue);
    }

    DoublePredicate getPredicate() {
        return predicate;
    }

    double getDefaultValue() {
        return defaultValue;
    }
}
