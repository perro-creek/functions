package org.hringsak.functions;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.PredicateUtils.contains;
import static org.hringsak.functions.PredicateUtils.not;

public final class StreamUtils {

    private StreamUtils() {
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> uniqueKeys = new HashSet<>();
        return t -> uniqueKeys.add(keyExtractor.apply(t));
    }

    public static <T> Predicate<T> distinctByKeyParallel(Function<? super T, Object> keyExtractor) {
        Set<Object> uniqueKeys = Sets.newConcurrentHashSet();
        return t -> uniqueKeys.add(keyExtractor.apply(t));
    }

    public static <T, R> List<R> transform(Collection<T> objects, Function<T, R> transformer) {
        return transform(objects, transformer, toList());
    }

    public static <T, R> Set<R> transformToSet(Collection<T> collection, Function<T, R> transformer) {
        return transform(collection, transformer, toSet());
    }

    public static <T, C extends Collection<T>> C transform(Collection<T> objects, Collector<T, ?, C> collector) {
        return transform(objects, Function.identity(), collector);
    }

    public static <T, R, C extends Collection<R>> C transform(Collection<T> objects, Function<T, R> transformer, Collector<R, ?, C> collector) {
        return defaultStream(objects)
                .map(transformer)
                .collect(collector);
    }

    public static <T, R> List<R> transformDistinct(Collection<T> objects, Function<T, R> transformer) {
        return defaultStream(objects)
                .map(transformer)
                .distinct()
                .collect(toList());
    }

    public static <T> List<T> filter(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .filter(predicate)
                .collect(toList());
    }

    public static <T> String join(Collection<T> objects, Function<T, CharSequence> mapper) {
        return join(objects, mapper, ",");
    }

    public static <T> String join(Collection<T> objects, Function<T, CharSequence> mapper, CharSequence delimiter) {
        return join(objects, mapper, joining(delimiter));
    }

    public static <T> String join(Collection<T> objects, Function<T, CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return defaultStream(objects)
                .map(mapper)
                .collect(joiner);
    }

    public static <T> Set<T> subtract(Set<T> from, Set<T> toSubtract) {
        Stream<T> keyStream = defaultStream(from);
        Set<T> nonNullToSubtract = toSubtract == null ? emptySet() : toSubtract;
        return keyStream.filter(not(contains(nonNullToSubtract, Function.identity())))
                .collect(toSet());
    }

    public static <T> Stream<T> defaultStream(Collection<T> objects) {
        return objects == null ? Stream.empty() : objects.stream();
    }

    public static <T> Stream<T> defaultStream(Stream<T> stream) {
        return stream == null ? Stream.empty() : stream;
    }

    public static <T> Stream<T> defaultStream(T[] array) {
        return array == null ? Stream.empty() : Arrays.stream(array);
    }

    public static <T> Stream<T> fromIterator(Iterator<T> iterator) {
        if (iterator != null) {
            Iterable<T> iterable = () -> iterator;
            return StreamSupport.stream(iterable.spliterator(), false);
        }
        return Stream.empty();
    }

    public static <T> List<List<T>> toPartitionedList(Collection<T> collection, int partitionSize) {
        return defaultStream(collection).collect(CollectorUtils.toPartitionedList(partitionSize));
    }

    public static <T> Stream<List<T>> toPartitionedStream(Collection<T> collection, int partitionSize) {
        return defaultStream(collection).collect(CollectorUtils.toPartitionedStream(partitionSize));
    }
}
