package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

class FlatMapCollector<T, U, C extends Collection<U>> {

    private final Function<T, Collection<U>> flatMapper;
    private final Collector<U, ?, C> collector;

    private FlatMapCollector(Function<T, Collection<U>> flatMapper, Collector<U, ?, C> collector) {
        this.flatMapper = flatMapper;
        this.collector = collector;
    }

    static <T, U, C extends Collection<U>> FlatMapCollector<T, U, C> of(Function<T, Collection<U>> flatMapper, Collector<U, ?, C> collector) {
        return new FlatMapCollector<>(flatMapper, collector);
    }

    Function<T, Collection<U>> getFlatMapper() {
        return flatMapper;
    }

    Collector<U, ?, C> getCollector() {
        return collector;
    }
}
