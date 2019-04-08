package org.perro.functions.predicate;

import java.util.Collection;
import java.util.function.LongPredicate;
import java.util.stream.Collector;

import static org.perro.functions.predicate.LongPredicateUtils.longPredicate;

class LongFilterCollector<C extends Collection<Long>> {

    private final LongPredicate filter;
    private final Collector<Long, ?, C> collector;

    private LongFilterCollector(LongPredicate filter, Collector<Long, ?, C> collector) {
        this.filter = filter;
        this.collector = collector;
    }

    static <C extends Collection<Long>> LongFilterCollector<C> of(LongPredicate filter, Collector<Long, ?, C> collector) {
        return new LongFilterCollector<>(filter, collector);
    }

    LongPredicate getFilter() {
        return longPredicate(filter);
    }

    Collector<Long, ?, C> getCollector() {
        return collector;
    }
}
