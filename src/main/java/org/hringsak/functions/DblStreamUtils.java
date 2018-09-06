package org.hringsak.functions;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.DblMapperUtils.pairDblWithIndex;
import static org.hringsak.functions.PredicateUtils.extractAndFilter;

public final class DblStreamUtils {

    private DblStreamUtils() {
    }

    public static DoublePredicate dblDistinctByKey(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static DoublePredicate dblDistinctByKeyParallel(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Sets.newConcurrentHashSet();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static double findAnyDblDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double findAnyDblDefault(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static double findFirstDblDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double findFirstDblDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static FindDoubleWithDefault findDblDefault(DoublePredicate predicate, double defaultValue) {
        return FindDoubleWithDefault.of(predicate, defaultValue);
    }

    public static FindDoubleWithDefaultSupplier findDblDefaultSupplier(DoublePredicate predicate, DoubleSupplier defaultSupplier) {
        return FindDoubleWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    public static int indexOfFirstDbl(double[] doubles, Predicate<Double> predicate) {
        return defaultDblStream(doubles)
                .mapToObj(pairDblWithIndex())
                .filter(extractAndFilter(Pair::getLeft, predicate))
                .mapToInt(Pair::getRight)
                .findFirst()
                .orElse(-1);
    }

    public static boolean dblAnyMatch(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles).anyMatch(predicate);
    }

    public static boolean dblNoneMatch(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles).noneMatch(predicate);
    }

    public static String dblJoin(double[] doubles, DoubleFunction<CharSequence> mapper) {
        return dblJoin(doubles, mapper, ",");
    }

    public static String dblJoin(double[] doubles, DoubleFunction<CharSequence> mapper, CharSequence delimiter) {
        return dblJoin(doubles, mapper, joining(delimiter));
    }

    public static String dblJoin(double[] doubles, DoubleFunction<CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return defaultDblStream(doubles)
                .mapToObj(mapper)
                .collect(joiner);
    }

    public static List<double[]> toPartitionedDblList(double[] doubles, int partitionSize) {
        return defaultDblStream(doubles)
                .boxed()
                .collect(toPartitionedList(partitionSize, DblStreamUtils::toListOfArrays));
    }

    private static List<double[]> toListOfArrays(List<List<Double>> partitions) {
        return partitions.stream()
                .map(DblStreamUtils::listToArray)
                .collect(toList());
    }

    private static double[] listToArray(List<Double> doubles) {
        return doubles.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public static Stream<double[]> toPartitionedDblStream(double[] doubles, int partitionSize) {
        return toPartitionedDblList(doubles, partitionSize).stream();
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(Collection<Double> objects) {
        return objects == null ? DoubleStream.empty() : objects.stream().mapToDouble(Double::doubleValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(Stream<Double> stream) {
        return stream == null ? DoubleStream.empty() : stream.mapToDouble(Double::doubleValue);
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(DoubleStream stream) {
        return stream == null ? DoubleStream.empty() : stream;
    }

    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(double[] array) {
        return array == null ? DoubleStream.empty() : Arrays.stream(array);
    }
}
