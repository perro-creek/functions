package org.hringsak.functions;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.MapperUtils.pairWithIndex;
import static org.hringsak.functions.PredicateUtils.contains;
import static org.hringsak.functions.PredicateUtils.extractAndFilter;
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
                .filter(extractAndFilter(Pair::getLeft, predicate))
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

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDoubleStream(Collection<Double> objects) {
        return objects == null ? DoubleStream.empty() : objects.stream().mapToDouble(Double::doubleValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDoubleStream(Stream<Double> stream) {
        return stream == null ? DoubleStream.empty() : stream.mapToDouble(Double::doubleValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDoubleStream(DoubleStream stream) {
        return stream == null ? DoubleStream.empty() : stream;
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDoubleStream(double[] array) {
        return array == null ? DoubleStream.empty() : Arrays.stream(array);
    }

    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(Collection<Integer> objects) {
        return objects == null ? IntStream.empty() : objects.stream().mapToInt(Integer::intValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(Stream<Integer> stream) {
        return stream == null ? IntStream.empty() : stream.mapToInt(Integer::intValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(IntStream stream) {
        return stream == null ? IntStream.empty() : stream;
    }

    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(int[] array) {
        return array == null ? IntStream.empty() : Arrays.stream(array);
    }

    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(Collection<Long> objects) {
        return objects == null ? LongStream.empty() : objects.stream().mapToLong(Long::longValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(Stream<Long> stream) {
        return stream == null ? LongStream.empty() : stream.mapToLong(Long::longValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(LongStream stream) {
        return stream == null ? LongStream.empty() : stream;
    }

    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(long[] array) {
        return array == null ? LongStream.empty() : Arrays.stream(array);
    }
}
