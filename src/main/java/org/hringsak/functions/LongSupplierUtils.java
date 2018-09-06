package org.hringsak.functions;

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

    public static <T, U> LongSupplier longSupplier(ToLongBiFunction<T, U> function, Arguments<T, U> arguments) {
        return () -> function.applyAsLong(arguments.getLeft(), arguments.getRight());
    }
}
