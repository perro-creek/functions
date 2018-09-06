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
import static org.hringsak.functions.DoubleMapperUtils.pairDoubleWithIndex;
import static org.hringsak.functions.PredicateUtils.extractAndFilter;

public final class DoubleStreamUtils {

    private DoubleStreamUtils() {
    }

    public static DoublePredicate doubleDistinctByKey(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static DoublePredicate doubleDistinctByKeyParallel(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Sets.newConcurrentHashSet();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static double findAnyDoubleDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDoubleStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double findAnyDoubleDefault(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDoubleStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static double findFirstDoubleDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDoubleStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double findFirstDoubleDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDoubleStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    public static FindDoubleWithDefault findDoubleDefault(DoublePredicate predicate, double defaultValue) {
        return FindDoubleWithDefault.of(predicate, defaultValue);
    }

    public static FindDoubleWithDefaultSupplier findDoubleDefaultSupplier(DoublePredicate predicate, DoubleSupplier defaultSupplier) {
        return FindDoubleWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    public static int indexOfFirstDouble(double[] doubles, Predicate<Double> predicate) {
        return defaultDoubleStream(doubles)
                .mapToObj(pairDoubleWithIndex())
                .filter(extractAndFilter(Pair::getLeft, predicate))
                .mapToInt(Pair::getRight)
                .findFirst()
                .orElse(-1);
    }

    public static boolean doubleAnyMatch(double[] doubles, DoublePredicate predicate) {
        return defaultDoubleStream(doubles).anyMatch(predicate);
    }

    public static boolean doubleNoneMatch(double[] doubles, DoublePredicate predicate) {
        return defaultDoubleStream(doubles).noneMatch(predicate);
    }

    public static String doubleJoin(double[] doubles, DoubleFunction<CharSequence> mapper) {
        return doubleJoin(doubles, mapper, ",");
    }

    public static String doubleJoin(double[] doubles, DoubleFunction<CharSequence> mapper, CharSequence delimiter) {
        return doubleJoin(doubles, mapper, joining(delimiter));
    }

    public static String doubleJoin(double[] doubles, DoubleFunction<CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return defaultDoubleStream(doubles)
                .mapToObj(mapper)
                .collect(joiner);
    }

    public static List<double[]> toPartitionedDoubleList(double[] doubles, int partitionSize) {
        return defaultDoubleStream(doubles)
                .boxed()
                .collect(toPartitionedList(partitionSize, DoubleStreamUtils::toListOfArrays));
    }

    private static List<double[]> toListOfArrays(List<List<Double>> partitions) {
        return partitions.stream()
                .map(DoubleStreamUtils::listToArray)
                .collect(toList());
    }

    private static double[] listToArray(List<Double> doubles) {
        return doubles.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public static Stream<double[]> toPartitionedDoubleStream(double[] doubles, int partitionSize) {
        return toPartitionedDoubleList(doubles, partitionSize).stream();
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
}
