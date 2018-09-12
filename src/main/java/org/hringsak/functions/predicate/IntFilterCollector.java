package org.hringsak.functions.predicate;

import java.util.Collection;
import java.util.function.IntPredicate;
import java.util.stream.Collector;

import static org.hringsak.functions.predicate.IntPredicateUtils.intPredicate;

class IntFilterCollector<C extends Collection<Integer>> {

    private final IntPredicate filter;
    private final Collector<Integer, ?, C> collector;

    private IntFilterCollector(IntPredicate filter, Collector<Integer, ?, C> collector) {
        this.filter = filter;
        this.collector = collector;
    }

    static <C extends Collection<Integer>> IntFilterCollector<C> of(IntPredicate filter, Collector<Integer, ?, C> collector) {
        return new IntFilterCollector<>(filter, collector);
    }

    IntPredicate getFilter() {
        return intPredicate(filter);
    }

    Collector<Integer, ?, C> getCollector() {
        return collector;
    }
}
