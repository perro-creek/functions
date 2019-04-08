package org.perro.functions.predicate;

import java.util.Collection;
import java.util.function.DoublePredicate;
import java.util.stream.Collector;

import static org.perro.functions.predicate.DblPredicateUtils.dblPredicate;

class DoubleFilterCollector<C extends Collection<Double>> {

    private final DoublePredicate filter;
    private final Collector<Double, ?, C> collector;

    private DoubleFilterCollector(DoublePredicate filter, Collector<Double, ?, C> collector) {
        this.filter = filter;
        this.collector = collector;
    }

    static <C extends Collection<Double>> DoubleFilterCollector<C> of(DoublePredicate filter, Collector<Double, ?, C> collector) {
        return new DoubleFilterCollector<>(filter, collector);
    }

    DoublePredicate getFilter() {
        return dblPredicate(filter);
    }

    Collector<Double, ?, C> getCollector() {
        return collector;
    }
}
