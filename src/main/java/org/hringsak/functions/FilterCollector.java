package org.hringsak.functions;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static org.hringsak.functions.PredicateUtils.predicate;

class FilterCollector<T, C extends Collection<T>> {

    private final Predicate<T> filter;
    private final Collector<T, ?, C> collector;

    private FilterCollector(Predicate<T> filter, Collector<T, ?, C> collector) {
        this.filter = filter;
        this.collector = collector;
    }

    static <T, C extends Collection<T>> FilterCollector<T, C> of(Predicate<T> filter, Collector<T, ?, C> collector) {
        return new FilterCollector<>(filter, collector);
    }

    Predicate<T> getFilter() {
        return predicate(filter);
    }

    Collector<T, ?, C> getCollector() {
        return collector;
    }
}
