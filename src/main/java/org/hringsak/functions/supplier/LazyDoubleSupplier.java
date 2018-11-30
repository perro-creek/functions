package org.hringsak.functions.supplier;

import java.util.function.DoubleSupplier;

class LazyDoubleSupplier {

    private final DoubleSupplier supplier;
    private boolean retrieved;
    private double value;

    private LazyDoubleSupplier(DoubleSupplier supplier) {
        this.supplier = supplier;
    }

    static DoubleSupplier newLazyDoubleSupplier(DoubleSupplier supplier) {
        return new LazyDoubleSupplier(supplier)::get;
    }

    private double get() {
        if (!retrieved) {
            value = supplier.getAsDouble();
            retrieved = true;
        }
        return value;
    }
}
