package org.hringsak.functions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ConsumerUtils {

    private ConsumerUtils() {
    }

    public static <T> Consumer<T> consumer(Consumer<T> consumer) {
        return consumer;
    }

    public static <T, U> Consumer<T> consumer(BiConsumer<? super T, U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    public static <T, U> Consumer<T> consumer(U value, BiConsumer<U, ? super T> biConsumer) {
        return t -> biConsumer.accept(value, t);
    }

    public static <T, U> Consumer<T> setter(BiConsumer<T, U> consumer, U value) {
        return t -> {
            if (t != null) {
                consumer.accept(t, value);
            }
        };
    }
}
