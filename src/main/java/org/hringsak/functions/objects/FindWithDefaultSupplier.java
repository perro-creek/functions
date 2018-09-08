package org.hringsak.functions.objects;

import java.util.function.Predicate;
import java.util.function.Supplier;

class FindWithDefaultSupplier<T> {

    private final Predicate<T> predicate;
    private final Supplier<T> defaultSupplier;

    private FindWithDefaultSupplier(Predicate<T> predicate, Supplier<T> defaultSupplier) {
        this.predicate = predicate;
        this.defaultSupplier = defaultSupplier;
    }

    static <T> FindWithDefaultSupplier<T> of(Predicate<T> predicate, Supplier<T> defaultSupplier) {
        return new FindWithDefaultSupplier<>(predicate, defaultSupplier);
    }

    Predicate<T> getPredicate() {
        return predicate;
    }

    Supplier<T> getDefaultSupplier() {
        return defaultSupplier;
    }
}
