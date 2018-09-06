package org.hringsak.functions;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public final class BooleanSupplierUtils {

    private BooleanSupplierUtils() {
    }

    public static BooleanSupplier booleanSupplier(BooleanSupplier supplier) {
        return supplier;
    }

    public static <T> BooleanSupplier booleanSupplier(Function<T, Boolean> function, T value) {
        return () -> function.apply(value);
    }

    public static <T, U> BooleanSupplier booleanSupplier(BiFunction<T, U, Boolean> function, Arguments<T, U> arguments) {
        return () -> function.apply(arguments.getLeft(), arguments.getRight());
    }
}
