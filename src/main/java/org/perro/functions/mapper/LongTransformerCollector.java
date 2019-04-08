package org.perro.functions.mapper;

import java.util.Collection;
import java.util.function.LongFunction;
import java.util.stream.Collector;

import static org.perro.functions.mapper.LongMapperUtils.longMapper;

class LongTransformerCollector<U, C extends Collection<U>> {

    private final LongFunction<U> transformer;
    private final Collector<U, ?, C> collector;

    private LongTransformerCollector(LongFunction<U> transformer, Collector<U, ?, C> collector) {
        this.transformer = transformer;
        this.collector = collector;
    }

    static <U, C extends Collection<U>> LongTransformerCollector<U, C> of(LongFunction<U> transformer, Collector<U, ?, C> collector) {
        return new LongTransformerCollector<>(transformer, collector);
    }

    LongFunction<U> getTransformer() {
        return longMapper(transformer);
    }

    Collector<U, ?, C> getCollector() {
        return collector;
    }
}
