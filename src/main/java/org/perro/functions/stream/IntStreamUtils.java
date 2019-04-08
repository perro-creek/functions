package org.perro.functions.stream;

import org.perro.functions.mapper.IntIndexPair;
import org.perro.functions.mapper.IntMapperUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.perro.functions.collector.CollectorUtils.toPartitionedList;
import static org.perro.functions.mapper.IntMapperUtils.intPairWithIndex;
import static org.perro.functions.predicate.IntPredicateUtils.mapToIntAndFilter;

/**
 * Methods that are shortcuts to creating streams, specifically methods involving primitive int types.
 */
public final class IntStreamUtils {

    private IntStreamUtils() {
    }

    /**
     * Given an array of ints and an <code>IntPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>all</i> values in the array match the predicate.
     *
     * @param ints      An array of ints to be checked whether the given IntPredicate matches all of its values.
     * @param predicate An IntPredicate to match against the values in the input array.
     * @return A boolean indication of whether all of the values in a given array match a given predicate.
     */
    public static boolean intAllMatch(int[] ints, IntPredicate predicate) {
        return ints != null && Arrays.stream(ints).allMatch(predicate);
    }

    /**
     * Given an array of ints and an <code>IntPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>any</i> of the values in the array match the predicate.
     *
     * @param ints      An array of ints to be checked whether the given IntPredicate matches any of its values.
     * @param predicate An IntPredicate to match against the values in the input array.
     * @return A boolean indication of whether any of the values in the array match the given predicate.
     */
    public static boolean intAnyMatch(int[] ints, IntPredicate predicate) {
        return ints != null && Arrays.stream(ints).anyMatch(predicate);
    }

    /**
     * Given an array of ints and an <code>IntPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>none</i> of the values in the array match the predicate.
     *
     * @param ints      An array of ints to be checked whether the given IntPredicate matches none of its values.
     * @param predicate A IntPredicate to match against the values in the input array.
     * @return A boolean indication of whether none of the values in the array match the given predicate.
     */
    public static boolean intNoneMatch(int[] ints, IntPredicate predicate) {
        return ints != null && Arrays.stream(ints).noneMatch(predicate);
    }

    /**
     * Given an array of ints and an <code>IntPredicate</code>, returns a <code>long</code> value indicating the
     * number of values in the array that match the predicate.
     *
     * @param ints      An array of ints to be counted for the number of them that match the given IntPredicate.
     * @param predicate An IntPredicate to match against the values in the input array.
     * @return A long value indicating the number of values a given array that match a given predicate.
     */
    public static long intCount(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints)
                .filter(predicate)
                .count();
    }

    /**
     * Given an array of ints, and an object representing an <code>IntPredicate</code> along with a default value, this
     * method returns the maximum int value in the array that matches the predicate, or the default value if no matching
     * values are found.
     *
     * @param ints            An array of ints from which to get the maximum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing an IntPredicate along with a default value.
     * @return The maximum int value in the array that matches the predicate, or the default value if the array is
     * null or empty, or if no values in it match the predicate.
     */
    public static int intMaxDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .max()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of ints, and an object representing an <code>IntPredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the maximum int value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param ints                    An array of ints from which to get the maximum value or the default value from the
     *                                supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing an IntPredicate along with a Supplier of a default value.
     * @return The maximum int value in the array that matches the predicate, or the default value from the supplier if
     * the array is null or empty, or if no values in it match the predicate.
     */
    public static int intMaxDefault(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .max()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Given an array of ints, and an object representing an <code>IntPredicate</code> along with a default value, this
     * method returns the minimum int value in the array that matches the predicate, or the default value if no matching
     * values are found.
     *
     * @param ints            An array of ints from which to get the minimum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing an IntPredicate along with a default value.
     * @return The minimum int value in the array that matches the predicate, or the default value if the array is
     * null or empty, or if no values in it match the predicate.
     */
    public static int intMinDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .min()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of ints, and an object representing an <code>IntPredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the minimum int value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param ints                    An array of ints from which to get the minimum value or the default value from the
     *                                supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing an IntPredicate along with a Supplier of a default value.
     * @return The minimum int value in the array that matches the predicate, or the default value from the supplier
     * if the array is null or empty, or if no values in it match the predicate.
     */
    public static int intMinDefault(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .min()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find any matching int value in an array of ints using a predicate, returning <code>null</code>
     * if one is not found. This method does all filtering with a primitive <code>IntStream</code>, boxing the stream
     * and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived example of how
     * this method would be called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findAnyIntDefaultNull(intArray, IntPredicateUtils.isIntEqual(2, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyIntDefaultNull(intArray, isIntEqual(2, identity()));
     *     }
     * </pre>
     *
     * @param ints      An array of primitive int values.
     * @param predicate A predicate for finding an Integer value.
     * @return An Integer value if one is found, otherwise null if the ints array is null or empty, or if no values in
     * it match the predicate.
     */
    public static Integer findAnyIntDefaultNull(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints)
                .filter(predicate)
                .boxed()
                .findAny()
                .orElse(null);
    }

    /**
     * Attempt to find any matching int value in an array of ints using a predicate, returning a default value if one is
     * not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findAnyIntDefault(intArray, IntStreamUtils.findIntDefault(IntPredicateUtils.isIntEqual(2, Function.identity()), -1));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyIntDefault(intArray, findIntDefault(isIntEqual(2, identity()), -1));
     *     }
     * </pre>
     *
     * @param ints            An array of primitive int values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     *                        Use the {@link #findIntDefault(IntPredicate, int)} to provide this parameter.
     * @return An int value if one is found, otherwise a default value from findWithDefault if the ints array is null or
     * empty, or if no values in it match the predicate.
     */
    public static int findAnyIntDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find any matching int value in an array of ints using a predicate, returning a default value retrieved
     * using an <code>IntSupplier</code> if one is not found. Here is a contrived example of how this method would be
     * called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findAnyIntDefault(intArray, IntStreamUtils.findIntDefault(
     *                 IntPredicateUtils.isIntEqual(2, Function.identity()), -1));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyIntDefault(intArray, findIntDefault(isIntEqual(2.0D, identity()), -1));
     *     }
     * </pre>
     *
     * @param ints                    An array of primitive int values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default IntSupplier
     *                                if one is not found. Use the
     *                                {@link #findIntDefault(IntPredicate, IntSupplier)} method to provide this
     *                                parameter.
     * @return An int value if one is found, otherwise a default value from the IntSupplier in findWithDefaultSupplier
     * if the ints array is null or empty, or if no values in it match the predicate.
     */
    public static int findAnyIntDefault(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find the first matching int value in an array of ints using a predicate, returning <code>null</code>
     * if one is not found. This method does all filtering with a primitive <code>IntStream</code>, boxing the stream
     * and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived example of how
     * this method would be called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findFirstIntDefaultNull(intArray, IntPredicateUtils.isIntEqual(2, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstIntDefaultNull(intArray, isIntEqual(2, identity()));
     *     }
     * </pre>
     *
     * @param ints      An array of primitive int values.
     * @param predicate A predicate for finding an Integer value.
     * @return An Integer value if one is found, otherwise null if the ints array is null or empty, or if no values in
     * it match the predicate.
     */
    public static Integer findFirstIntDefaultNull(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints)
                .filter(predicate)
                .boxed()
                .findFirst()
                .orElse(null);
    }

    /**
     * Attempt to find the first matching int value in an array of ints using a predicate, returning a default
     * value if one is not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findFirstIntDefault(intArray, IntStreamUtils.findIntDefault(IntPredicateUtils.isIntEqual(2, Function.identity()), -1));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstIntDefault(intArray, findIntDefault(isIntEqual(2, identity()), -1));
     *     }
     * </pre>
     *
     * @param ints            An array of primitive int values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     *                        Use the {@link #findIntDefault(IntPredicate, int)} method to provide this parameter.
     * @return An int value if one is found, otherwise a default value from findWithDefault if the ints array is null or
     * empty, or if no values in it match the predicate.
     */
    public static int findFirstIntDefault(int[] ints, FindIntWithDefault findWithDefault) {
        return defaultIntStream(ints)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find the first matching int value in an array of ints using a predicate, returning a default value
     * retrieved using an <code>IntSupplier</code> if one is not found. Here is a contrived example of how this method
     * would be called:
     * <pre>
     *     {
     *         ...
     *         return IntStreamUtils.findAnyIntDefault(intArray, IntStreamUtils.findIntDefault(
     *                 IntPredicateUtils.isIntEqual(2, Function.identity()), -1));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyIntDefault(intArray, findIntDefault(isIntEqual(2, identity()), -1));
     *     }
     * </pre>
     *
     * @param ints                    An array of primitive int values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default IntSupplier
     *                                if one is not found. Use the
     *                                {@link #findIntDefault(IntPredicate, IntSupplier)} method to provide this
     *                                parameter.
     * @return An int value if one is found, otherwise a default value from the IntSupplier in findWithDefaultSupplier
     * if the ints array is null or empty, or if no values in it match the predicate.
     */
    public static int findFirstIntDefault(int[] ints, FindIntWithDefaultSupplier findWithDefaultSupplier) {
        return defaultIntStream(ints)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default int value if one is not found.
     * See {@link #findAnyIntDefault(int[], FindIntWithDefault)} or
     * {@link #findFirstIntDefault(int[], FindIntWithDefaultSupplier)} for usage examples.
     *
     * @param predicate    A predicate used in finding a particular int value.
     * @param defaultValue A default int value to return if one could not be found using the above predicate.
     * @return An object containing a int predicate and default value.
     */
    public static FindIntWithDefault findIntDefault(IntPredicate predicate, int defaultValue) {
        return FindIntWithDefault.of(predicate, defaultValue);
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default <code>DoubleSupplier</code> if
     * one is not found. See {@link #findAnyIntDefault(int[], FindIntWithDefault)} or
     * {@link #findFirstIntDefault(int[], FindIntWithDefaultSupplier)} for usage examples.
     *
     * @param predicate       A predicate used in finding a particular double value.
     * @param defaultSupplier A default DoubleSupplier used to return if one could not be found using the above
     *                        predicate.
     * @return An object containing a double predicate and default DoubleSupplier.
     */
    public static FindIntWithDefaultSupplier findIntDefault(IntPredicate predicate, IntSupplier defaultSupplier) {
        return FindIntWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    /**
     * Finds the index of the first int in an array that matches an <code>IntPredicate</code>.
     *
     * @param ints      Array of primitive int values.
     * @param predicate An IntPredicate used to find a matching value.
     * @return An index of the first value in ints matching the predicate. Returns -1 if no matches are found.
     */
    public static int indexOfFirstInt(int[] ints, IntPredicate predicate) {
        return defaultIntStream(ints)
                .mapToObj(intPairWithIndex())
                .filter(mapToIntAndFilter(IntIndexPair::getIntValue, predicate))
                .mapToInt(IntIndexPair::getIndex)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Given an array of ints, and a partition size, this method divides the array into a list of int arrays, each of
     * whose length is at most <code>partitionSize</code>.
     *
     * @param ints          An array of ints to be partitioned up into a list of int arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned list.
     * @return A List of int arrays, each of whose length is at most partitionSize. The last array in the list may
     * have a length that is less than partitionSize.
     */
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

    /**
     * Given an array of ints, and a partition size, this method divides the array into a stream of int arrays, each of
     * whose length is at most <code>partitionSize</code>.
     *
     * @param ints          An array of ints to be partitioned up into a stream of int arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned stream.
     * @return A Stream of int arrays, each of whose length is at most partitionSize. The last array in the stream
     * may have a length that is less than partitionSize.
     */
    public static Stream<int[]> toPartitionedIntStream(int[] ints, int partitionSize) {
        return toPartitionedIntList(ints, partitionSize).stream();
    }

    /**
     * Given an array of <code>int</code> values, returns an <code>IntStream</code> of them.
     *
     * @param array An array of ints from which to return an IntStream.
     * @return An IntStream from the given int array. If the array is null, an empty IntStream is returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(int[] array) {
        return array == null ? IntStream.empty() : Arrays.stream(array);
    }

    /**
     * Given an <code>IntStream</code> returns that stream if it is not <code>null</code>, otherwise returns an empty
     * <code>IntStream</code>.
     *
     * @param stream An input IntStream.
     * @return The given IntStream if it is not null, otherwise an empty IntStream.
     */
    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(IntStream stream) {
        return stream == null ? IntStream.empty() : stream;
    }

    /**
     * Given a <code>Collection</code> of <code>Integer</code> instances, returns an <code>IntStream</code> of them.
     *
     * @param objects A Collection of Integer instances.
     * @return An IntStream from the given objects Collection. If objects is null or empty, an empty IntStream is
     * returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(Collection<Integer> objects) {
        return objects == null ? IntStream.empty() : objects.stream().mapToInt(Integer::intValue);
    }

    /**
     * Given a <code>Stream</code> of <code>Integer</code> instances, returns an <code>IntStream</code> from it, or an
     * empty <code>IntStream</code> if it is null.
     *
     * @param stream A Collection of Integer instances.
     * @return An IntStream from the given Stream of Integer instances, or an empty IntStream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static IntStream defaultIntStream(Stream<Integer> stream) {
        return stream == null ? IntStream.empty() : stream.mapToInt(Integer::intValue);
    }
}
