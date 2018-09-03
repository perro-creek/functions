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

    public static <T, R> Supplier<R> supplier(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U, R> Supplier<R> supplier(BiFunction<T, U, R> function, Arguments<T, U> arguments) {
        return () -> function.apply(arguments.getLeft(), arguments.getRight());
    }

    public static <T> BooleanSupplier booleanSupplier(Function<T, Boolean> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U> BooleanSupplier booleanSupplier(BiFunction<T, U, Boolean> function, Arguments<T, U> arguments) {
        return () -> function.apply(arguments.getLeft(), arguments.getRight());
    }

    public static <T> DoubleSupplier doubleSupplier(ToDoubleFunction<T> function, T value) {
        return () -> function.applyAsDouble(value);
    }

    public static <T, U> DoubleSupplier doubleSupplier(ToDoubleBiFunction<T, U> function, Arguments<T, U> arguments) {
        return () -> function.applyAsDouble(arguments.getLeft(), arguments.getRight());
    }

    public static <T> IntSupplier intSupplier(ToIntFunction<T> function, T value) {
        return () -> function.applyAsInt(value);
    }

    public static <T, U> IntSupplier intSupplier(ToIntBiFunction<T, U> function, Arguments<T, U> arguments) {
        return () -> function.applyAsInt(arguments.getLeft(), arguments.getRight());
    }

    public static <T> LongSupplier longSupplier(ToLongFunction<T> function, T value) {
        return () -> function.applyAsLong(value);
    }

    public static <T, U> LongSupplier longSupplier(ToLongBiFunction<T, U> function, Arguments<T, U> arguments) {
        return () -> function.applyAsLong(arguments.getLeft(), arguments.getRight());
    }

    public static <T, U> Arguments<T, U> arguments(T left, U right) {
        return Arguments.of(left, right);
    }
}
