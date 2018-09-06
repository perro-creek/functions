package org.hringsak.functions;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.LongMapperUtils.pairLongWithIndex;
import static org.hringsak.functions.PredicateUtils.extractAndFilter;

public final class LongStreamUtils {

    private LongStreamUtils() {
    }

    public static LongPredicate longDistinctByKey(LongFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static LongPredicate longDistinctByKeyParallel(LongFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Sets.newConcurrentHashSet();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static long findAnyLongDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static long findAnyLongDefault(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static long findFirstLongDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static long findFirstLongDefaultSupplier(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static FindLongWithDefault findLongDefault(LongPredicate predicate, long defaultValue) {
        return FindLongWithDefault.of(predicate, defaultValue);
    }

    public static FindLongWithDefaultSupplier findLongDefaultSupplier(LongPredicate predicate, LongSupplier defaultSupplier) {
        return FindLongWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    public static long indexOfFirstLong(long[] longs, Predicate<Long> predicate) {
        return defaultLongStream(longs)
                .mapToObj(pairLongWithIndex())
                .filter(extractAndFilter(Pair::getLeft, predicate))
                .mapToLong(Pair::getRight)
                .findFirst()
                .orElse(-1);
    }

    public static boolean longAnyMatch(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs).anyMatch(predicate);
    }

    public static boolean longNoneMatch(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs).noneMatch(predicate);
    }

    public static String longJoin(long[] longs, LongFunction<CharSequence> mapper) {
        return longJoin(longs, mapper, ",");
    }

    public static String longJoin(long[] longs, LongFunction<CharSequence> mapper, CharSequence delimiter) {
        return longJoin(longs, mapper, joining(delimiter));
    }

    public static String longJoin(long[] longs, LongFunction<CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return defaultLongStream(longs)
                .mapToObj(mapper)
                .collect(joiner);
    }

    public static List<long[]> toPartitionedLongList(long[] longs, int partitionSize) {
        return defaultLongStream(longs)
                .boxed()
                .collect(toPartitionedList(partitionSize, LongStreamUtils::toListOfArrays));
    }

    private static List<long[]> toListOfArrays(List<List<Long>> partitions) {
        return partitions.stream()
                .map(LongStreamUtils::listToArray)
                .collect(toList());
    }

    private static long[] listToArray(List<Long> longs) {
        return longs.stream()
                .mapToLong(Long::longValue)
                .toArray();
    }

    public static Stream<long[]> toPartitionedLongStream(long[] longs, int partitionSize) {
        return toPartitionedLongList(longs, partitionSize).stream();
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
