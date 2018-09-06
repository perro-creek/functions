package org.hringsak.functions;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

public final class SupplierUtils {

    private SupplierUtils() {
    }

    public static <R> Supplier<R> supplier(Supplier<R> supplier) {
        return supplier;
    }

    public static <T, R> Supplier<R> supplier(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U, R> Supplier<R> supplier(BiFunction<T, U, R> function, Arguments<T, U> arguments) {
        return () -> function.apply(arguments.getLeft(), arguments.getRight());
    }

    public static <T, U> Arguments<T, U> arguments(T left, U right) {
        return Arguments.of(left, right);
    }
}
