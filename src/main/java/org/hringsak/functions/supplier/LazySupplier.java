package org.hringsak.functions.supplier;

import java.util.function.Supplier;

class LazySupplier<T> {

    private final Supplier<T> supplier;
    private boolean retrieved;
    private T instance;

    private LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    static <T> Supplier<T> newLazySupplier(Supplier<T> supplier) {
        return new LazySupplier<>(supplier)::get;
    }

    private T get() {
        if (!retrieved) {
            instance = supplier.get();
            retrieved = true;
        }
        return instance;
    }
}
