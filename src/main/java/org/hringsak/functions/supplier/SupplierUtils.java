package org.hringsak.functions.supplier;

import org.hringsak.functions.internal.ConstantValues;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SupplierUtils {

    private SupplierUtils() {
    }

    public static <R> Supplier<R> supplier(Supplier<R> supplier) {
        return supplier;
    }

    public static <T, R> Supplier<R> supplier(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U, R> Supplier<R> supplier(BiFunction<T, U, R> function, ConstantValues<T, U> constants) {
        return () -> function.apply(constants.getLeft(), constants.getRight());
    }

    public static <T, U> ConstantValues<T, U> constantValues(T left, U right) {
        return ConstantValues.of(left, right);
    }
}
