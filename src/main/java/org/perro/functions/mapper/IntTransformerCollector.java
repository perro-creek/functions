package org.perro.functions.mapper;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.stream.Collector;

import static org.perro.functions.mapper.IntMapperUtils.intMapper;

class IntTransformerCollector<U, C extends Collection<U>> {

    private final IntFunction<U> transformer;
    private final Collector<U, ?, C> collector;

    private IntTransformerCollector(IntFunction<U> transformer, Collector<U, ?, C> collector) {
        this.transformer = transformer;
        this.collector = collector;
    }

    static <U, C extends Collection<U>> IntTransformerCollector<U, C> of(IntFunction<U> transformer, Collector<U, ?, C> collector) {
        return new IntTransformerCollector<>(transformer, collector);
    }

    IntFunction<U> getTransformer() {
        return intMapper(transformer);
    }

    Collector<U, ?, C> getCollector() {
        return collector;
    }
}
