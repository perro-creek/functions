package org.hringsak.functions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SupplierUtils {

    private SupplierUtils() {
    }

    public static <T, R> Supplier<R> supplier(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U, R> Supplier<R> supplier(BiFunction<T, U, R> function, T left, U right) {
        return () -> function.apply(left, right);
    }
}
