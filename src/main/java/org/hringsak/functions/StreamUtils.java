package org.hringsak.functions;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.MapperUtils.pairWithIndex;
import static org.hringsak.functions.PredicateUtils.contains;
import static org.hringsak.functions.PredicateUtils.mapAndFilter;
import static org.hringsak.functions.PredicateUtils.not;

public final class StreamUtils {

    private StreamUtils() {
    }

    public static <T> T findAny(Collection<T> objects, Predicate<T> predicate) {
        return findAnyWithDefault(objects, findWithDefault(predicate));
    }

    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static <T> T findFirst(Collection<T> objects, Predicate<T> predicate) {
        return findFirstWithDefault(objects, findWithDefault(predicate));
    }

    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    private static <T> FindWithDefault<T> findWithDefault(Predicate<T> predicate) {
        return FindWithDefault.of(predicate, null);
    }

    public static <T> FindWithDefault<T> findWithDefault(Predicate<T> predicate, T defaultValue) {
        return FindWithDefault.of(predicate, defaultValue);
    }

    public static <T> FindWithDefaultSupplier<T> findWithDefault(Predicate<T> predicate, Supplier<T> defaultSupplier) {
        return FindWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    public static <T> int indexOfFirst(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .map(pairWithIndex())
                .filter(mapAndFilter(Pair::getLeft, predicate))
                .mapToInt(Pair::getRight)
                .findFirst()
                .orElse(-1);
    }

    public static <T> boolean anyMatch(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects).anyMatch(predicate);
    }

    public static <T> boolean noneMatch(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects).noneMatch(predicate);
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
        return keyStream.filter(not(contains(nonNullToSubtract, identity())))
                .collect(toSet());
    }

    public static <T> List<List<T>> toPartitionedList(Collection<T> collection, int partitionSize) {
        return defaultStream(collection).collect(CollectorUtils.toPartitionedList(partitionSize));
    }

    public static <T> Stream<List<T>> toPartitionedStream(Collection<T> collection, int partitionSize) {
        return defaultStream(collection).collect(CollectorUtils.toPartitionedStream(partitionSize));
    }

    public static <T> Stream<T> fromIterator(Iterator<T> iterator) {
        if (iterator != null) {
            Iterable<T> iterable = () -> iterator;
            return StreamSupport.stream(iterable.spliterator(), false);
        }
        return Stream.empty();
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(Collection<T> objects) {
        return objects == null ? Stream.empty() : objects.stream();
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(Stream<T> stream) {
        return stream == null ? Stream.empty() : stream;
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(T[] array) {
        return array == null ? Stream.empty() : Arrays.stream(array);
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(T target) {
        return target == null ? Stream.empty() : Stream.of(target);
    }
}
