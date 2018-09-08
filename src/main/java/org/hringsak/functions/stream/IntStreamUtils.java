package org.hringsak.functions.stream;

import com.google.common.collect.Sets;
import org.hringsak.functions.mapper.IntIndexPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.mapper.IntMapperUtils.intPairWithIndex;
import static org.hringsak.functions.predicate.IntPredicateUtils.mapToIntAndFilter;

public final class IntStreamUtils {

    private IntStreamUtils() {
    }

    public static IntPredicate intDistinctByKey(IntFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static IntPredicate intDistinctByKeyParallel(IntFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Sets.newConcurrentHashSet();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static int findAnyIntDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static int findAnyIntDefault(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static int findFirstIntDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static int findFirstIntDefaultSupplier(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static FindIntWithDefault findIntDefault(IntPredicate predicate, int defaultValue) {
        return FindIntWithDefault.of(predicate, defaultValue);
    }

    public static FindIntWithDefaultSupplier findIntDefaultSupplier(IntPredicate predicate, IntSupplier defaultSupplier) {
        return FindIntWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    public static int indexOfFirstInt(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints)
                .mapToObj(intPairWithIndex())
                .filter(mapToIntAndFilter(IntIndexPair::getLeft, predicate))
                .mapToInt(IntIndexPair::getRight)
                .findFirst()
                .orElse(-1);
    }

    public static boolean intAnyMatch(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints).anyMatch(predicate);
    }

    public static boolean intNoneMatch(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints).noneMatch(predicate);
    }

    public static String intJoin(int[] ints, IntFunction<CharSequence> mapper) {
        return intJoin(ints, mapper, ",");
    }

    public static String intJoin(int[] ints, IntFunction<CharSequence> mapper, CharSequence delimiter) {
        return intJoin(ints, mapper, joining(delimiter));
    }

    public static String intJoin(int[] ints, IntFunction<CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return defaultIntStream(ints)
                .mapToObj(mapper)
                .collect(joiner);
    }

    public static List<int[]> toPartitionedIntList(int[] ints, int partitionSize) {
        return defaultIntStream(ints)
                .boxed()
                .collect(toPartitionedList(partitionSize, IntStreamUtils::toListOfArrays));
    }

    private static List<int[]> toListOfArrays(List<List<Integer>> partitions) {
        return partitions.stream()
                .map(IntStreamUtils::listToArray)
                .collect(toList());
    }

    private static int[] listToArray(List<Integer> ints) {
        return ints.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public static Stream<int[]> toPartitionedIntStream(int[] ints, int partitionSize) {
        return toPartitionedIntList(ints, partitionSize).stream();
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
}
