package org.hringsak.functions;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

class TransformerCollector<T, U, C extends Collection<U>> {

    private final Function<T, U> transformer;
    private final Collector<U, ?, C> collector;

    private TransformerCollector(Function<T, U> transformer, Collector<U, ?, C> collector) {
        this.transformer = transformer;
        this.collector = collector;
    }

    static <T, U, C extends Collection<U>> TransformerCollector<T, U, C> of(Function<T, U> transformer, Collector<U, ?, C> collector) {
        return new TransformerCollector<>(transformer, collector);
    }

    Function<T, U> getTransformer() {
        return transformer;
    }

    Collector<U, ?, C> getCollector() {
        return collector;
    }
}
