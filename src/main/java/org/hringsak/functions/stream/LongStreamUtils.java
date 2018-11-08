package org.hringsak.functions.stream;

import org.hringsak.functions.mapper.LongIndexPair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hringsak.functions.collector.CollectorUtils.toPartitionedList;
import static org.hringsak.functions.mapper.LongMapperUtils.longPairWithIndex;
import static org.hringsak.functions.predicate.LongPredicateUtils.mapToLongAndFilter;

/**
 * Methods that are shortcuts to creating streams, specifically methods involving primitive long types.
 */
public final class LongStreamUtils {

    private LongStreamUtils() {
    }

    /**
     * Given an array of longs and a <code>LongPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>all</i> values in the array match the predicate.
     *
     * @param longs     An array of longs to be checked whether the given LongPredicate matches all of its values.
     * @param predicate A LongPredicate to match against the values in the input array.
     * @return A boolean indication of whether all of the values in a given array match a given predicate.
     */
    public static boolean longAllMatch(long[] longs, LongPredicate predicate) {
        return longs != null && Arrays.stream(longs).allMatch(predicate);
    }

    /**
     * Given an array of longs and a <code>LongPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>any</i> of the values in the array match the predicate.
     *
     * @param longs     An array of longs to be checked whether the given LongPredicate matches any of its values.
     * @param predicate A LongPredicate to match against the values in the input array.
     * @return A boolean indication of whether any of the values in the array match the given predicate.
     */
    public static boolean longAnyMatch(long[] longs, LongPredicate predicate) {
        return longs != null && Arrays.stream(longs).anyMatch(predicate);
    }

    /**
     * Given an array of longs and a <code>LongPredicate</code>, returns a <code>boolean</code> value indicating whether
     * <i>none</i> of the values in the array match the predicate.
     *
     * @param longs     An array of longs to be checked whether the given LongPredicate matches none of its values.
     * @param predicate A LongPredicate to match against the values in the input array.
     * @return A boolean indication of whether none of the values in the array match the given predicate.
     */
    public static boolean longNoneMatch(long[] longs, LongPredicate predicate) {
        return longs != null && Arrays.stream(longs).noneMatch(predicate);
    }

    /**
     * Given an array of longs and a <code>LongPredicate</code>, returns a <code>long</code> value indicating the
     * number of values in the array that match the predicate.
     *
     * @param longs     An array of longs to be counted for the number of them that match the given LongPredicate.
     * @param predicate A LongPredicate to match against the values in the input array.
     * @return A long value indicating the number of values a given array that match a given predicate.
     */
    public static long longCount(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .count();
    }

    /**
     * Given an array of longs, and an object representing a <code>LongPredicate</code> along with a default value,
     * this method returns the maximum long value in the array that matches the predicate, or the default value if no
     * matching values are found.
     *
     * @param longs           An array of longs from which to get the maximum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing a LongPredicate along with a default value.
     * @return The maximum long value in the array that matches the predicate, or the default value if the array is
     * null or empty, or if no values in it match the predicate.
     */
    public static long longMaxDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .max()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of longs, and an object representing a <code>LongPredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the maximum long value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param longs                   An array of longs from which to get the maximum value or the default value from
     *                                the supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing a LongPredicate along with a Supplier of a default value.
     * @return The maximum long value in the array that matches the predicate, or the default value from the supplier
     * if the array is null or empty, or if no values in it match the predicate.
     */
    public static long longMaxDefault(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .max()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Given an array of longs, and an object representing a <code>LongPredicate</code> along with a default value, this
     * method returns the minimum long value in the array that matches the predicate, or the default value if no
     * matching values are found.
     *
     * @param longs           An array of longs from which to get the minimum value or the default value in the given
     *                        findWithDefault object.
     * @param findWithDefault An object representing a LongPredicate along with a default value.
     * @return The minimum long value in the array that matches the predicate, or the default value if the array is
     * null or empty, or if no values in it match the predicate.
     */
    public static long longMinDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .min()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given an array of longs, and an object representing a <code>LongPredicate</code> along with a
     * <code>Supplier</code> for a default value, this method returns the minimum long value in the array that matches
     * the predicate, or a default value from the supplier if no matching values are found.
     *
     * @param longs                   An array of longs from which to get the minimum value or the default value from
     *                                the supplier in the given findWithDefaultSupplier object.
     * @param findWithDefaultSupplier An object representing a LongPredicate along with a Supplier of a default value.
     * @return The minimum long value in the array that matches the predicate, or the default value from the supplier
     * if the array is null or empty, or if no values in it match the predicate.
     */
    public static long longMinDefault(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .min()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
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
     * @param longs     An array of primitive long values.
     * @param predicate A predicate for finding a Long value.
     * @return A Long value if one is found, otherwise null if the longs array is null or empty, or if no values in it
     * match the predicate.
     */
    public static Long findAnyLongDefaultNull(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .boxed()
                .findAny()
                .orElse(null);
    }

    /**
     * Attempt to find any matching long value in an array of longs using a predicate, returning a default value if
     * one is not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findAnyLongDefault(longArray, LongStreamUtils.findLongDefault(LongPredicateUtils.isLongEqual(2L, Function.identity()), -1L));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyLongDefault(longArray, findLongDefault(isLongEqual(2L, identity()), -1L));
     *     }
     * </pre>
     *
     * @param longs           An array of primitive long values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     *                        Use the {@link #findLongDefault(LongPredicate, long)} to provide this parameter.
     * @return A long value if one is found, otherwise a default value from findWithDefault if the longs array is null
     * or empty, or if no values in it match the predicate.
     */
    public static long findAnyLongDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find any matching long value in an array of longs using a predicate, returning a default value
     * retrieved using a <code>LongSupplier</code> if one is not found. Here is a contrived example of how this method
     * would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findAnyLongDefault(longArray, LongStreamUtils.findLongDefaultSupplier(
     *                 LongPredicateUtils.isLongEqual(2L, Function.identity()), -1L));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyLongDefault(longArray, findLongDefaultSupplier(isLongEqual(2L, identity()), -1L));
     *     }
     * </pre>
     *
     * @param longs                   An array of primitive long values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default
     *                                LongSupplier if one is not found. Use the
     *                                {@link #findLongDefaultSupplier(LongPredicate, LongSupplier)} method to provide
     *                                this parameter.
     * @return A long value if one is found, otherwise a default value from the LongSupplier in findWithDefaultSupplier
     * if the longs array is null or empty, or if no values in it match the predicate.
     */
    public static long findAnyLongDefault(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find the first matching long value in an array of longs using a predicate, returning <code>null</code>
     * if one is not found. This method does all filtering with a primitive <code>LongStream</code>, boxing the stream
     * and calling <code>Stream.findAny()</code> only after it has been filtered. Here is a contrived example of how
     * this method would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findFirstLongDefaultNull(longArray, LongPredicateUtils.isLongEqual(2L, Function.identity()));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstLongDefaultNull(longArray, isLongEqual(2, identity()));
     *     }
     * </pre>
     *
     * @param longs     An array of primitive long values.
     * @param predicate A predicate for finding a Long value.
     * @return A Long value if one is found, otherwise null if the longs array is null or empty, or if no values in it
     * match the predicate.
     */
    public static Long findFirstLongDefaultNull(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .boxed()
                .findFirst()
                .orElse(null);
    }

    /**
     * Attempt to find the first matching long value in an array of longs using a predicate, returning a default value
     * if one is not found. Here is a contrived example of how this method would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findFirstLongDefault(longArray, LongStreamUtils.findLongDefault(LongPredicateUtils.isLongEqual(2L, Function.identity()), -1L));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findFirstLongDefault(longArray, findLongDefault(isLongEqual(2L, identity()), -1L));
     *     }
     * </pre>
     *
     * @param longs           An array of primitive long values.
     * @param findWithDefault An object representing a predicate for finding a value, and a default if one is not found.
     *                        Use the {@link #findLongDefault(LongPredicate, long)} to provide this parameter.
     * @return A long value if one is found, otherwise a default value from findWithDefault if the longs array is null
     * or empty, or if no values in it match the predicate.
     */
    public static long findFirstLongDefault(long[] longs, FindLongWithDefault findWithDefault) {
        return defaultLongStream(longs)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find the first matching long value in an array of longs using a predicate, returning a default value
     * retrieved using a <code>LongSupplier</code> if one is not found. Here is a contrived example of how this method
     * would be called:
     * <pre>
     *     {
     *         ...
     *         return LongStreamUtils.findAnyLongDefault(longArray, LongStreamUtils.findLongDefaultSupplier(
     *                 LongPredicateUtils.isLongEqual(2L, Function.identity()), -1L));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         ...
     *         return findAnyLongDefault(longArray, findLongDefaultSupplier(isLongEqual(2L, identity()), -1L));
     *     }
     * </pre>
     *
     * @param longs                   An array of primitive long values.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default LongSupplier
     *                                if one is not found. Use the
     *                                {@link #findLongDefaultSupplier(LongPredicate, LongSupplier)} method to provide
     *                                this parameter.
     * @return A long value if one is found, otherwise a default value from the LongSupplier in findWithDefaultSupplier
     * if the longs array is null or empty, or if no values in it match the predicate.
     */
    public static long findFirstLongDefault(long[] longs, FindLongWithDefaultSupplier findWithDefaultSupplier) {
        return defaultLongStream(longs)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default long value if one is not found.
     * See {@link #findAnyLongDefault(long[], FindLongWithDefault)} or
     * {@link #findFirstLongDefault(long[], FindLongWithDefault)} for usage examples.
     *
     * @param predicate    A predicate used in finding a particular long value.
     * @param defaultValue A default long value to return if one could not be found using the above predicate.
     * @return An object containing a long predicate and default value.
     */
    public static FindLongWithDefault findLongDefault(LongPredicate predicate, long defaultValue) {
        return FindLongWithDefault.of(predicate, defaultValue);
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default <code>LongSupplier</code> if one
     * is not found. See {@link #findAnyLongDefault(long[], FindLongWithDefaultSupplier)} or
     * {@link #findFirstLongDefault(long[], FindLongWithDefaultSupplier)} for usage examples.
     *
     * @param predicate       A predicate used in finding a particular long value.
     * @param defaultSupplier A default LongSupplier used to return if one could not be found using the above predicate.
     * @return An object containing a long predicate and default LongSupplier.
     */
    public static FindLongWithDefaultSupplier findLongDefaultSupplier(LongPredicate predicate, LongSupplier defaultSupplier) {
        return FindLongWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    /**
     * Finds the index of the first long in an array that matches a <code>LongPredicate</code>.
     *
     * @param longs     Array of primitive long values.
     * @param predicate A LongPredicate used to find a matching value.
     * @return An index of the first value in longs matching the predicate. Returns -1 if no matches are found.
     */
    public static long indexOfFirstLong(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .mapToObj(longPairWithIndex())
                .filter(mapToLongAndFilter(LongIndexPair::getLongValue, predicate))
                .mapToLong(LongIndexPair::getIndex)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Given an array of longs, and a partition size, this method divides the array into a list of long arrays, each of
     * whose length is at most <code>partitionSize</code>.
     *
     * @param longs         An array of longs to be partitioned up into a list of long arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned list.
     * @return A List of long arrays, each of whose length is at most partitionSize. The last array in the list may have
     * a length that is less than partitionSize.
     */
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

    /**
     * Given an array of longs, and a partition size, this method divides the array into a stream of long arrays, each
     * of whose length is at most <code>partitionSize</code>.
     *
     * @param longs         An array of longs to be partitioned up into a stream of long arrays.
     * @param partitionSize The maximum length of the individual arrays in the returned stream.
     * @return A Stream of long arrays, each of whose length is at most partitionSize. The last array in the stream may
     * have a length that is less than partitionSize.
     */
    public static Stream<long[]> toPartitionedLongStream(long[] longs, int partitionSize) {
        return toPartitionedLongList(longs, partitionSize).stream();
    }

    /**
     * Given an array of <code>long</code> values, returns a <code>LongStream</code> of them.
     *
     * @param array An array of longs from which to return a LongStream.
     * @return A LongStream from the given long array. If the array is null, an empty LongStream is returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(long[] array) {
        return array == null ? LongStream.empty() : Arrays.stream(array);
    }

    /**
     * Given a <code>LongStream</code> returns that stream if it is not <code>null</code>, otherwise returns an empty
     * <code>LongStream</code>.
     *
     * @param stream An input LongStream.
     * @return The given LongStream if it is not null, otherwise an empty LongStream.
     */
    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(LongStream stream) {
        return stream == null ? LongStream.empty() : stream;
    }

    /**
     * Given a <code>Collection</code> of <code>Long</code> instances, returns a <code>LongStream</code> of them.
     *
     * @param objects A Collection of Long instances.
     * @return A LongStream from the given objects Collection. If objects is null or empty, an empty LongStream is
     * returned.
     */
    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(Collection<Long> objects) {
        return objects == null ? LongStream.empty() : objects.stream().mapToLong(Long::longValue);
    }

    /**
     * Given a <code>Stream</code> of <code>Long</code> instances, returns a <code>LongStream</code> from it, or an
     * empty <code>LongStream</code> if it is null.
     *
     * @param stream A Collection of Long instances.
     * @return A LongStream from the given Stream of Long instances, or an empty LongStream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static LongStream defaultLongStream(Stream<Long> stream) {
        return stream == null ? LongStream.empty() : stream.mapToLong(Long::longValue);
    }
}
