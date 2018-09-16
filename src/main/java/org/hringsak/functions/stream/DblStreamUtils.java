package org.hringsak.functions.stream;

import org.hringsak.functions.mapper.DoubleIndexPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.mapper.DblMapperUtils.dblPairWithIndex;
import static org.hringsak.functions.predicate.DblPredicateUtils.mapToDblAndFilter;

/**
 * Methods that are shortcuts to creating streams, specifically methods involving primitive double types.
 */
public final class DblStreamUtils {

    private DblStreamUtils() {
    }

    public static boolean dblAllMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).allMatch(predicate);
    }

    public static boolean dblAnyMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).anyMatch(predicate);
    }

    public static boolean dblNoneMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).noneMatch(predicate);
    }

    public static long dblCount(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(predicate)
                .count();
    }

    public static double dblMaxDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .max()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double dblMaxDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .max()
                .orElseGet(findWithDefault.getDefaultSupplier());
    }

    public static double dblMinDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .min()
                .orElse(findWithDefault.getDefaultValue());
    }

    public static double dblMinDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .min()
                .orElseGet(findWithDefault.getDefaultSupplier());
    }

    /**
     * Attempt to find any matching double value in an array of doubles using a predicate, returning <code>null</code>
     * if one is not found. This method does all filtering with a primitive <code>DoubleStream</code>, boxing the stream
     * and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived example of how
     * this method would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findAnyDblDefaultNull(doubleArray, DblPredicateUtils.isDblEqual(2.0D, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefaultNull(doubleArray, isDblEqual(2.0D, identity()));
     *     }
     * </pre>
     *
     * @param doubles   An array of primitive double values.
     * @param predicate A predicate for finding a Double value.
     * @return A Double value if one is found, otherwise null.
     */
    public static Double findAnyDblDefaultNull(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(predicate)
                .boxed()
                .findAny()
                .orElse(null);
    }

    /**
     * Attempt to find any matching double value in an array of doubles using a predicate, returning a default value if
     * one is not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findAnyDblDefault(doubleArray, DblStreamUtils.findDblDefault(DblPredicateUtils.isDblEqual(2.0D, Function.identity()), -1.0D));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefault(doubleArray, findDblDefault(isDblEqual(2.0D, identity()), -1.0D));
     *     }
     * </pre>
     *
     * @param doubles         An array of primitive double values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     * @return A double value if one is found, otherwise a default value.
     */
    public static double findAnyDblDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find any matching double value in an array of doubles using a predicate, returning a default value
     * retrieved using a <code>DoubleSupplier</code> if one is not found. Here is a contrived example of how this method
     * would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findAnyDblDefault(doubleArray, DblStreamUtils.findDblDefaultSupplier(
     *                 DblPredicateUtils.isDblEqual(2.0D, Function.identity()), new Double(-1.0D)::valueOf));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefault(doubleArray, findDblDefault(isDblEqual(2.0D, identity()), new Double(-1.0D)::valueOf));
     *     }
     * </pre>
     *
     * @param doubles                 An array of primitive double values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default
     *                                DoubleSupplier if one is not found.
     * @return A double value if one is found, otherwise a default value.
     */
    public static double findAnyDblDefault(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find the first matching double value in an array of doubles using a predicate, returning
     * <code>null</code> if one is not found. This method does all filtering with a primitive <code>DoubleStream</code>,
     * boxing the stream and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived
     * example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findFirstDblDefaultNull(doubleArray, DblPredicateUtils.isDblEqual(2.0D, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstDblDefaultNull(doubleArray, isDblEqual(2.0D, identity()));
     *     }
     * </pre>
     *
     * @param doubles   An array of primitive double values.
     * @param predicate A predicate for finding a Double value.
     * @return A Double value if one is found, otherwise null.
     */
    public static Double findFirstDblDefaultNull(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(predicate)
                .boxed()
                .findFirst()
                .orElse(null);
    }

    /**
     * Attempt to find the first matching double value in an array of doubles using a predicate, returning a default
     * value if one is not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findFirstDblDefault(doubleArray, DblStreamUtils.findDblDefault(DblPredicateUtils.isDblEqual(2.0D, Function.identity()), -1.0D));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstDblDefault(doubleArray, findDblDefault(isDblEqual(2.0D, identity()), -1.0D));
     *     }
     * </pre>
     *
     * @param doubles         An array of primitive double values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     * @return A double value if one is found, otherwise a default value.
     */
    public static double findFirstDblDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find the first matching double value in an array of doubles using a predicate, returning a default
     * value retrieved using a <code>DoubleSupplier</code> if one is not found. Here is a contrived example of how this
     * method would be called:
     * <pre>
     *     {
     *         ...
     *         return DblStreamUtils.findAnyDblDefault(doubleArray, DblStreamUtils.findDblDefaultSupplier(
     *                 DblPredicateUtils.isDblEqual(2.0D, Function.identity()), new Double(-1.0D)::valueOf));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefault(doubleArray, findDblDefault(isDblEqual(2.0D, identity()), new Double(-1.0D)::valueOf));
     *     }
     * </pre>
     *
     * @param doubles                 An array of primitive double values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default
     *                                DoubleSupplier if one is not found.
     * @return A double value if one is found, otherwise a default value.
     */
    public static double findFirstDblDefault(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default double value if one is not found.
     * See {@link #findAnyDblDefault(double[], FindDoubleWithDefault)} or
     * {@link #findFirstDblDefault(double[], FindDoubleWithDefault)} for usage examples.
     *
     * @param predicate    A predicate used in finding a particular double value.
     * @param defaultValue A default double value to return if one could not be found using the above predicate.
     * @return An object containing a double predicate and default value.
     */
    public static FindDoubleWithDefault findDblDefault(DoublePredicate predicate, double defaultValue) {
        return FindDoubleWithDefault.of(predicate, defaultValue);
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default <code>DoubleSupplier</code> if
     * one is not found. See {@link #findAnyDblDefault(double[], FindDoubleWithDefaultSupplier)} or
     * {@link #findFirstDblDefault(double[], FindDoubleWithDefaultSupplier)} for usage examples.
     *
     * @param predicate       A predicate used in finding a particular double value.
     * @param defaultSupplier A default DoubleSupplier used to return if one could not be found using the above
     *                        predicate.
     * @return An object containing a double predicate and default DoubleSupplier.
     */
    public static FindDoubleWithDefaultSupplier findDblDefaultSupplier(DoublePredicate predicate, DoubleSupplier defaultSupplier) {
        return FindDoubleWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    /**
     * Finds the index of the first double in an array that matches a <code>DoublePredicate</code>.
     *
     * @param doubles   Array of primitive double values
     * @param predicate A DoublePredicate used to find a matching value.
     * @return An index of the first value in doubles matching predicate. Returns -1 if no matches are found.
     */
    public static int indexOfFirstDbl(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .mapToObj(dblPairWithIndex())
                .filter(mapToDblAndFilter(DoubleIndexPair::getLeft, predicate))
                .mapToInt(DoubleIndexPair::getRight)
                .findFirst()
                .orElse(-1);
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
