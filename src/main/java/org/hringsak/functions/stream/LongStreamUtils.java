package org.hringsak.functions.stream;

import com.google.common.collect.Sets;
import org.hringsak.functions.mapper.LongIndexPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.stream.Collector;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.mapper.LongMapperUtils.longPairWithIndex;
import static org.hringsak.functions.predicate.LongPredicateUtils.mapToLongAndFilter;

public final class LongStreamUtils {

    private LongStreamUtils() {
    }

    /**
     * Attempt to find any matching long value in an array of longs using a predicate, returning <code>null</code>
     * if one is not found. This method does all filtering with a primitive <code>LongStream</code>, boxing the stream
     * and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived example of how
     * this method would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findAnyLongDefaultNull(longArray, LongPredicateUtils.isLongEqual(2L, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyLongDefaultNull(longArray, isLongEqual(2L, identity()));
     *     }
     * </pre>
     *
     * @param longs   An array of primitive long values.
     * @param predicate A predicate for finding a Long value.
     * @return A Long value if one is found, otherwise null.
     */
    public static Long findAnyLongDefaultNull(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .boxed()
                .findAny()
                .orElse(null);
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

    public static long indexOfFirstLong(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .mapToObj(longPairWithIndex())
                .filter(mapToLongAndFilter(LongIndexPair::getLeft, predicate))
                .mapToLong(LongIndexPair::getRight)
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
