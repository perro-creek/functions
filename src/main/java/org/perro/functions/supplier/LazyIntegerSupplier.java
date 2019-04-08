package org.perro.functions.supplier;

import java.util.function.IntSupplier;

class LazyIntegerSupplier {

    private final IntSupplier supplier;
    private boolean retrieved;
    private int value;

    private LazyIntegerSupplier(IntSupplier supplier) {
        this.supplier = supplier;
    }

    static IntSupplier newLazyIntegerSupplier(IntSupplier supplier) {
        return new LazyIntegerSupplier(supplier)::get;
    }

    private int get() {
        if (!retrieved) {
            value = supplier.getAsInt();
            retrieved = true;
        }
        return value;
    }
}
