package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.function.DoubleFunction;
import java.util.stream.Collector;

import static org.hringsak.functions.mapper.DblMapperUtils.dblMapper;

class DblTransformerCollector<U, C extends Collection<U>> {

    private final DoubleFunction<U> transformer;
    private final Collector<U, ?, C> collector;

    private DblTransformerCollector(DoubleFunction<U> transformer, Collector<U, ?, C> collector) {
        this.transformer = transformer;
        this.collector = collector;
    }

    static <T, U, C extends Collection<U>> DblTransformerCollector<U, C> of(DoubleFunction<U> transformer, Collector<U, ?, C> collector) {
        return new DblTransformerCollector<>(transformer, collector);
    }

    DoubleFunction<U> getTransformer() {
        return dblMapper(transformer);
    }

    Collector<U, ?, C> getCollector() {
        return collector;
    }
}
