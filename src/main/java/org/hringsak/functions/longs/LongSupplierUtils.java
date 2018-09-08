package org.hringsak.functions.longs;

import org.hringsak.functions.internal.ConstantValues;

import java.util.function.LongSupplier;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

public final class LongSupplierUtils {

    private LongSupplierUtils() {
    }

    public static LongSupplier longSupplier(LongSupplier function) {
        return function;
    }

    public static <T> LongSupplier longSupplier(ToLongFunction<T> function, T value) {
        return () -> function.applyAsLong(value);
    }

    public static <T, U> LongSupplier longSupplier(ToLongBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsLong(constants.getLeft(), constants.getRight());
    }
}
