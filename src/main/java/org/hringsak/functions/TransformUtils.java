package org.hringsak.functions;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.StreamUtils.defaultStream;

public final class TransformUtils {

    private TransformUtils() {
    }

    public static <T, R> List<R> transform(Collection<T> objects, Function<T, R> transformer) {
        return transform(objects, transformer, toList());
    }

    public static <T, R> Set<R> transformToSet(Collection<T> collection, Function<T, R> transformer) {
        return transform(collection, transformer, toSet());
    }

    public static <T, C extends Collection<T>> C transform(Collection<T> objects, Collector<T, ?, C> collector) {
        return transform(objects, identity(), collector);
    }

    public static <T, R> List<R> transformDistinct(Collection<T> objects, Function<T, R> transformer) {
        return defaultStream(objects)
                .map(transformer)
                .distinct()
                .collect(toList());
    }

    public static <T, R, C extends Collection<R>> C transform(Collection<T> objects, Function<T, R> transformer, Collector<R, ?, C> collector) {
        return defaultStream(objects)
                .map(transformer)
                .collect(collector);
    }
}
