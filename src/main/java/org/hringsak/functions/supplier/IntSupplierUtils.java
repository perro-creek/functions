package org.hringsak.functions.supplier;

import java.util.function.IntSupplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public final class IntSupplierUtils {

    private IntSupplierUtils() {
    }

    public static IntSupplier intSupplier(IntSupplier function) {
        return function;
    }

    public static <T> IntSupplier intSupplier(ToIntFunction<T> function, T value) {
        return () -> function.applyAsInt(value);
    }

    public static <T, U> IntSupplier intSupplier(ToIntBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsInt(constants.getLeft(), constants.getRight());
    }
}
