package org.hringsak.functions.supplier;

import java.util.function.LongSupplier;

class LazyLongSupplier {

    private final LongSupplier supplier;
    private boolean retrieved;
    private long value;

    private LazyLongSupplier(LongSupplier supplier) {
        this.supplier = supplier;
    }

    static LongSupplier newLazyLongSupplier(LongSupplier supplier) {
        return new LazyLongSupplier(supplier)::get;
    }

    private long get() {
        if (!retrieved) {
            value = supplier.getAsLong();
            retrieved = true;
        }
        return value;
    }
}
