package org.hringsak.functions.supplier;

import org.hringsak.functions.internal.ConstantValues;

import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public final class DblSupplierUtils {

    private DblSupplierUtils() {
    }

    public static DoubleSupplier dblSupplier(DoubleSupplier function) {
        return function;
    }

    public static <T> DoubleSupplier dblSupplier(ToDoubleFunction<T> function, T value) {
        return () -> function.applyAsDouble(value);
    }

    public static <T, U> DoubleSupplier dblSupplier(ToDoubleBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsDouble(constants.getLeft(), constants.getRight());
    }
}
