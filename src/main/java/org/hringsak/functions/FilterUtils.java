package org.hringsak.functions;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.StreamUtils.defaultStream;

public final class FilterUtils {

    private FilterUtils() {
    }

    public static <T> List<T> filter(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, predicate, toList());
    }

    public static <T> Set<T> filterToSet(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, predicate, toSet());
    }

    public static <T> List<T> filterDistinct(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .filter(predicate)
                .distinct()
                .collect(toList());
    }

    public static <T, C extends Collection<T>> C filter(Collection<T> objects, Predicate<T> predicate, Collector<T, ?, C> collector) {
        return defaultStream(objects)
                .filter(predicate)
                .collect(collector);
    }
}
