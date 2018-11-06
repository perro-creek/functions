package org.hringsak.functions.stream;

import org.hringsak.functions.mapper.DoubleIndexPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.IntPredicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.collector.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.mapper.DblMapperUtils.dblPairWithIndex;
import static org.hringsak.functions.predicate.DblPredicateUtils.mapToDblAndFilter;

/**
 * Methods that are shortcuts to creating streams, specifically methods involving primitive double types.
 */
public final class DblStreamUtils {

    private DblStreamUtils() {
    }

    /**
     * Given an array of doubles and a <code>DoublePredicate</code>, returns a <code>boolean</code> value indicating
     * whether <i>all</i> values in the array match the predicate.
     *
     * @param doubles   An array of doubles to be checked whether the given DoublePredicate matches all of its values.
     * @param predicate A DoublePredicate to match against the values in the input array.
     * @return A boolean indication of whether all of the values in a given array match a given predicate.
     */
    public static boolean dblAllMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).allMatch(predicate);
    }

    /**
     * Given an array of doubles and a <code>DoublePredicate</code>, returns a <code>boolean</code> value indicating
     * whether <i>any</i> of the values in the array match the predicate.
     *
     * @param doubles   An array of doubles to be checked whether the given DoublePredicate matches any of its values.
     * @param predicate A DoublePredicate to match against the values in the input array.
     * @return A boolean indication of whether any of the values in the array match the given predicate.
     */
    public static boolean dblAnyMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).anyMatch(predicate);
    }

    /**
     * Given an array of doubles and a <code>DoublePredicate</code>, returns a <code>boolean</code> value indicating
     * whether <i>none</i> of the values in the array match the predicate.
     *
     * @param doubles   An array of doubles to be checked whether the given DoublePredicate matches none of its values.
     * @param predicate A DoublePredicate to match against the values in the input array.
     * @return A boolean indication of whether none of the values in the array match the given predicate.
     */
    public static boolean dblNoneMatch(double[] doubles, DoublePredicate predicate) {
        return doubles != null && Arrays.stream(doubles).noneMatch(predicate);
    }

    /**
     * Given an array of doubles and a <code>DoublePredicate</code>, returns a <code>long</code> value indicating the
     * number of values in the array that match the predicate.
     *
     * @param doubles   An array of doubles to be counted for the number of them that match the given DoublePredicate.
     * @param predicate A DoublePredicate to match against the values in the input array.
     * @return A long value indicating the number of values a given array that match a given predicate.
     */
    public static long dblCount(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(predicate)
                .count();
    }

    /**
     * Given an array of doubles, and an object representing a <code>DoublePredicate</code> along with a default value,
     * this method returns the maximum double value in the array that matches the predicate, or the default value if no
     * matching values are found.
     *
     * @param doubles         An array of doubles from which to get the maximum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing a DoublePredicate along with a default value.
     * @return The maximum double value in the array that matches the predicate, or the default value if the
     * array is <code>null</code> or empty, or if no values in it match the predicate.
     */
    public static double dblMaxDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .max()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of doubles, and an object representing a <code>DoublePredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the maximum double value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param doubles                 An array of doubles from which to get the maximum value or the default value from
     *                                the supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing a DoublePredicate along with a Supplier of a default value.
     * @return The maximum double value in the array that matches the predicate, or the default value from the supplier
     * if the array is <code>null</code> or empty, or if no values in it match the predicate.
     */
    public static double dblMaxDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .max()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Given an array of doubles, and an object representing a <code>DoublePredicate</code> along with a default value,
     * this method returns the minimum double value in the array that matches the predicate, or the default value if no
     * matching values are found.
     *
     * @param doubles         An array of doubles from which to get the minimum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing a DoublePredicate along with a default value.
     * @return The minimum double value in the array that matches the predicate, or the default value if the
     * array is <code>null</code> or empty, or if no values in it match the predicate.
     */
    public static double dblMinDefault(double[] doubles, FindDoubleWithDefault findWithDefault) {
        return defaultDblStream(doubles)
                .filter(findWithDefault.getPredicate())
                .min()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of doubles, and an object representing a <code>DoublePredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the minimum double value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param doubles                 An array of doubles from which to get the minimum value or the default value from
     *                                the supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing a DoublePredicate along with a Supplier of a default value.
     * @return The minimum double value in the array that matches the predicate, or the default value from the supplier
     * if the array is <code>null</code> or empty, or if no values in it match the predicate.
     */
    public static double dblMinDefaultSupplier(double[] doubles, FindDoubleWithDefaultSupplier findWithDefaultSupplier) {
        return defaultDblStream(doubles)
                .filter(findWithDefaultSupplier.getPredicate())
                .min()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
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
     *                        Use the {@link #findDblDefault(DoublePredicate, double)} to provide this parameter.
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
     *                 DblPredicateUtils.isDblEqual(2.0D, Function.identity()), -1.0D));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefault(doubleArray, findDblDefaultSupplier(isDblEqual(2.0D, identity()), -1.0D));
     *     }
     * </pre>
     *
     * @param doubles                 An array of primitive double values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default
     *                                DoubleSupplier if one is not found. Use the
     *                                {@link #findDblDefaultSupplier(DoublePredicate, DoubleSupplier)} method to provide
     *                                this parameter.
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
     *                        Use the {@link #findDblDefault(DoublePredicate, double)} to provide this parameter.
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
     *                 DblPredicateUtils.isDblEqual(2.0D, Function.identity()), -1.0D));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyDblDefault(doubleArray, findDblDefaultSupplier(isDblEqual(2.0D, identity()), -1.0D));
     *     }
     * </pre>
     *
     * @param doubles                 An array of primitive double values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default
     *                                DoubleSupplier if one is not found. Use the
     *                                {@link #findDblDefaultSupplier(DoublePredicate, DoubleSupplier)} method to provide
     *                                this parameter.
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
     * @param doubles   Array of primitive double values.
     * @param predicate A DoublePredicate used to find a matching value.
     * @return An index of the first value in doubles matching the predicate. Returns -1 if no matches are found.
     */
    public static int indexOfFirstDbl(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .mapToObj(dblPairWithIndex())
                .filter(mapToDblAndFilter(DoubleIndexPair::getDoubleValue, predicate))
                .mapToInt(DoubleIndexPair::getIndex)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Given an array of doubles, and a partition size, this method divides the array into a list of double arrays, each
     * of whose length is at most <code>partitionSize</code>.
     *
     * @param doubles       An array of doubles to be partitioned up into a list of double arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned list.
     * @return A List of double arrays, each of whose length is at most partitionSize. The last array in the list may
     * have a length that is less than partitionSize.
     */
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

    /**
     * Given an array of doubles, and a partition size, this method divides the array into a stream of double arrays,
     * each of whose length is at most <code>partitionSize</code>.
     *
     * @param doubles       An array of doubles to be partitioned up into a stream of double arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned stream.
     * @return A Stream of double arrays, each of whose length is at most partitionSize. The last array in the stream
     * may have a length that is less than partitionSize.
     */
    public static Stream<double[]> toPartitionedDblStream(double[] doubles, int partitionSize) {
        return toPartitionedDblList(doubles, partitionSize).stream();
    }

    /**
     * Given an array of <code>double</code> values, returns a <code>DoubleStream</code> of them.
     *
     * @param array An array of doubles from which to return a DoubleStream.
     * @return A DoubleStream from the given double array. If the array is null, an empty DoubleStream is returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(double[] array) {
        return array == null ? DoubleStream.empty() : Arrays.stream(array);
    }

    /**
     * Given a <code>DoubleStream</code> returns that stream if it is not <code>null</code>, otherwise returns an empty
     * <code>DoubleStream</code>.
     *
     * @param stream An input DoubleStream.
     * @return The given DoubleStream if it is not null, otherwise an empty DoubleStream.
     */
    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(DoubleStream stream) {
        return stream == null ? DoubleStream.empty() : stream;
    }

    /**
     * Given a <code>Collection</code> of <code>Double</code> instances, returns a <code>DoubleStream</code> of them.
     *
     * @param objects A Collection of Double instances.
     * @return A DoubleStream from the given objects Collection. If objects is null or empty, an empty DoubleStream is
     * returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(Collection<Double> objects) {
        return objects == null ? DoubleStream.empty() : objects.stream().mapToDouble(Double::doubleValue);
    }

    /**
     * Given a <code>Stream</code> of <code>Double</code> instances, returns a <code>DoubleStream</code> from it, or an
     * empty <code>DoubleStream</code> if it is null.
     *
     * @param stream A Collection of Double instances.
     * @return A DoubleStream from the given Stream of Double instances, or an empty DoubleStream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static DoubleStream defaultDblStream(Stream<Double> stream) {
        return stream == null ? DoubleStream.empty() : stream.mapToDouble(Double::doubleValue);
    }
}
