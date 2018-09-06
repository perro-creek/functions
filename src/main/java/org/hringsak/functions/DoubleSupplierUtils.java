package org.hringsak.functions;

import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public final class DoubleSupplierUtils {

    private DoubleSupplierUtils() {
    }

    public static DoubleSupplier doubleSupplier(DoubleSupplier function) {
        return function;
    }

    public static <T> DoubleSupplier doubleSupplier(ToDoubleFunction<T> function, T value) {
        return () -> function.applyAsDouble(value);
    }

    public static <T, U> DoubleSupplier doubleSupplier(ToDoubleBiFunction<T, U> function, Arguments<T, U> arguments) {
        return () -> function.applyAsDouble(arguments.getLeft(), arguments.getRight());
    }
}
