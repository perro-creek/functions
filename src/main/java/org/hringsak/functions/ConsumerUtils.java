package org.hringsak.functions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;

public final class ConsumerUtils {

    private ConsumerUtils() {
    }

    @SuppressWarnings("unused")
    public static <T> Consumer<T> consumer(Consumer<T> consumer) {
        return consumer;
    }

    public static <T, U> Consumer<T> consumer(BiConsumer<? super T, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    public static <T, U> Consumer<T> consumer(U value, BiConsumer<? super U, ? super T> biConsumer) {
        return t -> biConsumer.accept(value, t);
    }

    @SuppressWarnings("unused")
    public static DoubleConsumer doubleConsumer(DoubleConsumer consumer) {
        return consumer;
    }

    public static <U> DoubleConsumer doubleConsumer(BiConsumer<Double, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    public static <U> DoubleConsumer doubleConsumer(U value, ObjDoubleConsumer<? super U> biConsumer) {
        return t -> biConsumer.accept(value, t);
    }

    @SuppressWarnings("unused")
    public static IntConsumer intConsumer(IntConsumer consumer) {
        return consumer;
    }

    public static <U> IntConsumer intConsumer(BiConsumer<Integer, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    public static <U> IntConsumer intConsumer(U value, ObjIntConsumer<? super U> biConsumer) {
        return t -> biConsumer.accept(value, t);
    }

    @SuppressWarnings("unused")
    public static LongConsumer longConsumer(LongConsumer consumer) {
        return consumer;
    }

    public static <U> LongConsumer longConsumer(BiConsumer<Long, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    public static <U> LongConsumer longConsumer(U value, ObjLongConsumer<? super U> biConsumer) {
        return t -> biConsumer.accept(value, t);
    }

    public static <T, U> Consumer<T> setter(BiConsumer<? super T, ? super U> consumer, U value) {
        return t -> {
            if (t != null) {
                consumer.accept(t, value);
            }
        };
    }
}
