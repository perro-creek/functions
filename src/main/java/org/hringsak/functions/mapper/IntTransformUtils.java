package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.collector.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.IntMapperUtils.flatMapperToInt;
import static org.hringsak.functions.mapper.IntMapperUtils.intFlatMapper;
import static org.hringsak.functions.mapper.IntMapperUtils.intMapper;
import static org.hringsak.functions.mapper.IntMapperUtils.intPairOf;
import static org.hringsak.functions.stream.IntStreamUtils.defaultIntStream;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

/**
 * Methods to transform an array of ints to a collection of elements of an arbitrary type, or to transform a collection
 * of elements of an arbitrary type to an array of ints.
 */
public final class IntTransformUtils {

    private IntTransformUtils() {
    }

    /**
     * Given an array of ints, and an <code>IntUnaryOperator</code> that transforms each of those ints to another int,
     * returns an array of the resulting <code>int</code> values.
     *
     * @param ints     An array of ints to be transformed.
     * @param operator An IntUnaryOperator that takes an int and transforms it to another int.
     * @return An array of primitive transformed ints.
     */
    public static int[] intUnaryTransform(int[] ints, IntUnaryOperator operator) {
        return defaultIntStream(ints)
                .map(operator)
                .toArray();
    }

    /**
     * Given an array of ints, and an <code>IntUnaryOperator</code> that transforms each of those ints to another
     * int, returns an array of the resulting <code>int</code> values, which will contain only distinct values.
     *
     * @param ints     An array of ints to be transformed.
     * @param operator An IntUnaryOperator that takes an int and transforms it to another int.
     * @return An array of distinct, primitive, transformed ints.
     */
    public static int[] intUnaryTransformDistinct(int[] ints, IntUnaryOperator operator) {
        return defaultIntStream(ints)
                .map(operator)
                .distinct()
                .toArray();
    }

    /**
     * Given an array of ints, and an <code>IntFunction&lt;R&gt;</code> that transforms those ints into elements of type
     * &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements.
     *
     * @param ints        An array of ints to be transformed.
     * @param transformer An IntFunction&lt;R&gt; that takes an int and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive ints.
     */
    public static <R> List<R> intTransform(int[] ints, IntFunction<R> transformer) {
        return intTransform(ints, IntTransformerCollector.of(transformer, toList()));
    }

    /**
     * Given an array of ints, and an <code>IntFunction&lt;R&gt;</code> that transforms those ints into elements of type
     * &lt;R&gt;, returns a <code>Set&lt;R&gt;</code> of those elements.
     *
     * @param ints        An array of ints to be transformed.
     * @param transformer An IntFunction&lt;R&gt; that takes an int and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting Set.
     * @return A Set&lt;R&gt; of elements transformed from an array of primitive ints.
     */
    public static <R> Set<R> intTransformToSet(int[] ints, IntFunction<R> transformer) {
        return intTransform(ints, IntTransformerCollector.of(transformer, toSet()));
    }

    /**
     * Given an array of ints, and a <code>Collector&lt;Integer, ?, C&gt;</code>, returns a
     * <code>Collection&lt;C&gt;</code> of <code>Integer</code> elements.
     *
     * @param ints      An array of ints to be collected.
     * @param collector A Collector&lt;Integer, ?, C&gt; that appends Integer values into a collection of type
     *                  &lt;C&gt;.
     * @param <C>       The type of the collection of Integer values returned.
     * @return A collection of type &lt;C&gt; of Integer values.
     */
    public static <C extends Collection<Integer>> C intTransform(int[] ints, Collector<Integer, ?, C> collector) {
        return intTransform(ints, IntTransformerCollector.of(Integer::valueOf, collector));
    }

    /**
     * Given an array of ints, and an <code>IntFunction&lt;R&gt;</code> that transforms those ints into elements of type
     * &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements, which will contain only unique values
     * (according to <code>Object.equals(Object)</code>).
     *
     * @param ints        An array of ints to be transformed.
     * @param transformer An IntFunction&lt;R&gt; that takes an int and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive ints, containing only distinct
     * elements.
     */
    public static <R> List<R> intTransformDistinct(int[] ints, IntFunction<R> transformer) {
        return defaultIntStream(ints)
                .mapToObj(intMapper(transformer))
                .distinct()
                .collect(toList());
    }

    /**
     * Given an array of ints, and an <code>IntTransformerCollector&lt;U, C&gt;</code> that contains an
     * <code>IntFunction&lt;U&gt;</code> to transform int values into elements of type &lt;U&gt;, and a
     * <code>Collector&lt;U, ?, C&gt;</code>, returns a collection of type &lt;C&gt; of those elements. The
     * <code>transformerCollection</code> passed into this method should be created via a call to
     * {@link #intTransformAndThen(IntFunction, Collector)}.
     *
     * @param ints                 An array of ints to be transformed.
     * @param transformerCollector A object containing a transformer IntFunction&lt;U&gt; to transform int values into
     *                             elements of type &lt;U&gt;, and a Collector&lt;U, ?, C&gt; to put those elements into
     *                             a resulting collection.
     * @param <U>                  The type of the elements in the resulting Collection.
     * @param <C>                  The type of the Collection of elements of type &lt;U&gt; returned.
     * @return A collection of type &lt;C&gt; of elements &lt;U&gt;, transformed from an array of primitive ints.
     */
    public static <U, C extends Collection<U>> C intTransform(int[] ints, IntTransformerCollector<U, C> transformerCollector) {
        return defaultIntStream(ints)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    /**
     * Given an array of ints, and an <code>IntFunction&lt;int[]&gt;</code> (a function that takes an int and returns an
     * array of ints), returns an array of ints.
     *
     * @param ints     An array of primitive ints to be flat-mapped into another array of ints.
     * @param function An IntFunction that takes an int and returns an array of ints.
     * @return An array of flat-mapped primitive ints.
     */
    public static int[] intFlatMap(int[] ints, IntFunction<int[]> function) {
        return defaultIntStream(ints)
                .flatMap(intFlatMapper(function))
                .toArray();
    }

    /**
     * Given an array of ints, and an <code>IntFunction&lt;int[]&gt;</code> (a function that takes an int and returns an
     * array of ints), returns an array of distinct primitive int values.
     *
     * @param ints     An array of primitive ints to be flat-mapped into another array of ints.
     * @param function An IntFunction that takes an int and returns an array of ints.
     * @return An array of distinct, primitive, flat-mapped ints.
     */
    public static int[] intFlatMapDistinct(int[] ints, IntFunction<int[]> function) {
        return defaultIntStream(ints)
                .flatMap(intFlatMapper(function))
                .distinct()
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, int[]&gt;</code>, returns an array
     * of primitive ints.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive ints.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive int array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of primitive flat-mapped ints.
     */
    public static <T> int[] flatMapToInt(Collection<T> objects, Function<T, int[]> function) {
        return defaultStream(objects)
                .flatMapToInt(flatMapperToInt(function))
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, int[]&gt;</code>, returns an array of
     * distinct primitive ints.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive ints.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive int array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of distinct, primitive, flat-mapped ints.
     */
    public static <T> int[] flatMapToIntDistinct(Collection<T> objects, Function<T, int[]> function) {
        return defaultStream(objects)
                .flatMapToInt(flatMapperToInt(function))
                .distinct()
                .toArray();
    }

    /**
     * Builds an <code>IntTransformerCollector&lt;U, C&gt;</code> that contains an <code>IntFunction&lt;U&gt;</code> to
     * transform primitive int values into elements of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to
     * collect those elements into a collection of type &lt;C&gt;. Used to build the second parameter to
     * {@link #intTransform(int[], IntTransformerCollector)}.
     *
     * @param transformer An <code>IntFunction&lt;U&gt;</code> to transform primitive int values into elements of type
     *                    &lt;U&gt;.
     * @param collector   A Collector&lt;U, ?, C&gt; to append elements into a collection of type &lt;C&gt;.
     * @param <U>         The type of the elements into which int values are to be transformed.
     * @param <C>         The type of a collection of elements of type &lt;U&gt;, into which a given collector will
     *                    append elements.
     * @return An object containing an <code>IntFunction&lt;U&gt;</code> to transform primitive int values into elements
     * of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to append those elements into a collection of type
     * &lt;C&gt;.
     */
    public static <U, C extends Collection<U>> IntTransformerCollector<U, C> intTransformAndThen(IntFunction<U> transformer, Collector<U, ?, C> collector) {
        return IntTransformerCollector.of(transformer, collector);
    }

    /**
     * Given an array of primitive ints, and an <code>IntKeyValueMapper</code> containing an <code>IntFunction</code> to
     * transform an int to a key of type &lt;K&gt;, and another <code>IntFunction</code> to transform an int to a value
     * of type &lt;V&gt;, returns a <code>Map&lt;K, V&gt;</code>.
     *
     * @param ints           An array of primitive ints to be transformed into keys and values for the resulting
     *                       Map&lt;K, V&gt;.
     * @param keyValueMapper An object containing an IntFunction to transform an int to a key of type &lt;K&gt;,
     *                       and another IntFunction to transform an int to a value of type &lt;V&gt;.
     * @param <K>            The type of the key of the resulting Map&lt;K, V&gt;.
     * @param <V>            The type of the value of the resulting Map&lt;K, V&gt;.
     * @return A map whose keys and values are transformed from an array of ints.
     */
    public static <K, V> Map<K, V> intTransformToMap(int[] ints, IntKeyValueMapper<K, V> keyValueMapper) {
        return defaultIntStream(ints)
                .mapToObj(intPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
