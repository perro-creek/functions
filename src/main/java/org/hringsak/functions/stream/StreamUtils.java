package org.hringsak.functions.stream;

import org.hringsak.functions.CollectorUtils;
import org.hringsak.functions.internal.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.Comparator.naturalOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.mapper.MapperUtils.pairWithIndex;
import static org.hringsak.functions.predicate.PredicateUtils.inverseContains;
import static org.hringsak.functions.predicate.PredicateUtils.mapAndFilter;
import static org.hringsak.functions.predicate.PredicateUtils.not;

public final class StreamUtils {

    private StreamUtils() {
    }

    public static <T> boolean allMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().allMatch(predicate);
    }

    public static <T> boolean anyMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().anyMatch(predicate);
    }

    public static <T> boolean noneMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().noneMatch(predicate);
    }

    public static <T> long count(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(predicate)
                .count();
    }

    public static <T extends Comparable<T>> T maxDefault(Collection<T> objects, Predicate<T> predicate) {
        return maxDefault(objects, findWithDefault(predicate));
    }

    public static <T extends Comparable<T>> T maxDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .max(naturalOrder())
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T extends Comparable<T>> T maxDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .max(naturalOrder())
                .orElseGet(findWithDefault.getDefaultSupplier());
    }

    public static <T extends Comparable<T>> T minDefault(Collection<T> objects, Predicate<T> predicate) {
        return minDefault(objects, findWithDefault(predicate));
    }

    public static <T extends Comparable<T>> T minDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .min(naturalOrder())
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T extends Comparable<T>> T minDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .min(naturalOrder())
                .orElseGet(findWithDefault.getDefaultSupplier());
    }

    public static <T> T findAnyDefaultNull(Collection<T> objects, Predicate<T> predicate) {
        return findAnyWithDefault(objects, findWithDefault(predicate));
    }

    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static <T> T findFirst(Collection<T> objects, Predicate<T> predicate) {
        return findFirstWithDefault(objects, findWithDefault(predicate));
    }

    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
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
        return keyStream.filter(not(inverseContains(nonNullToSubtract, identity())))
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
