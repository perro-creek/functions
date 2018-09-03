package org.hringsak.functions;

import java.util.function.Predicate;

class FindWithDefault<T> {

    private final Predicate<T> predicate;
    private final T defaultValue;

    private FindWithDefault(Predicate<T> predicate, T defaultValue) {
        this.predicate = predicate;
        this.defaultValue = defaultValue;
    }

    static <T> FindWithDefault<T> of(Predicate<T> predicate, T defaultValue) {
        return new FindWithDefault<>(predicate, defaultValue);
    }

    Predicate<T> getPredicate() {
        return predicate;
    }

    T getDefaultValue() {
        return defaultValue;
    }
}
