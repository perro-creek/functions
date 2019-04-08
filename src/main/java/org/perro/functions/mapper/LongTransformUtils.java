package org.perro.functions.mapper;

import org.perro.functions.collector.CollectorUtils;
import org.perro.functions.stream.LongStreamUtils;
import org.perro.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.perro.functions.collector.CollectorUtils.toMapFromEntry;
import static org.perro.functions.mapper.LongMapperUtils.*;
import static org.perro.functions.stream.LongStreamUtils.defaultLongStream;
import static org.perro.functions.stream.StreamUtils.defaultStream;

/**
 * Methods to transform an array of longs to a collection of elements of an arbitrary type, or to transform a collection
 * of elements of an arbitrary type to an array of longs.
 */
public final class LongTransformUtils {

    private LongTransformUtils() {
    }

    /**
     * Given an array of longs, and a <code>LongUnaryOperator</code> that transforms each of those longs to another long,
     * returns an array of the resulting <code>long</code> values.
     *
     * @param longs    An array of ints to be transformed.
     * @param operator A LongUnaryOperator that takes a long and transforms it to another long.
     * @return An array of primitive transformed longs.
     */
    public static long[] longUnaryTransform(long[] longs, LongUnaryOperator operator) {
        return defaultLongStream(longs)
                .map(operator)
                .toArray();
    }

    /**
     * Given an array of longs, and a <code>LongUnaryOperator</code> that transforms each of those longs to another
     * long, returns an array of the resulting <code>long</code> values, which will contain only distinct values.
     *
     * @param longs    An array of ints to be transformed.
     * @param operator A LongUnaryOperator that takes a long and transforms it to another long.
     * @return An array of distinct, primitive, transformed longs.
     */
    public static long[] longUnaryTransformDistinct(long[] longs, LongUnaryOperator operator) {
        return defaultLongStream(longs)
                .map(operator)
                .distinct()
                .toArray();
    }

    /**
     * Given an array of longs, and a <code>LongFunction&lt;R&gt;</code> that transforms those longs into elements of
     * type &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements.
     *
     * @param longs       An array of longs to be transformed.
     * @param transformer A LongFunction&lt;R&gt; that takes a long and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive longs.
     */
    public static <R> List<R> longTransform(long[] longs, LongFunction<R> transformer) {
        return longTransform(longs, LongTransformerCollector.of(transformer, toList()));
    }

    /**
     * Given an array of longs, and a <code>LongFunction&lt;R&gt;</code> that transforms those longs into elements of
     * type &lt;R&gt;, returns a <code>Set&lt;R&gt;</code> of those elements.
     *
     * @param longs       An array of longs to be transformed.
     * @param transformer A LongFunction&lt;R&gt; that takes a long and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting Set.
     * @return A Set&lt;R&gt; of elements transformed from an array of primitive longs.
     */
    public static <R> Set<R> longTransformToSet(long[] longs, LongFunction<R> transformer) {
        return longTransform(longs, LongTransformerCollector.of(transformer, toSet()));
    }

    /**
     * Given an array of longs, and a <code>Collector&lt;Long, ?, C&gt;</code>, returns a
     * <code>Collection&lt;C&gt;</code> of <code>Long</code> elements.
     *
     * @param longs     An array of longs to be collected.
     * @param collector A Collector&lt;Long, ?, C&gt; that appends Long values into a collection of type &lt;C&gt;.
     * @param <C>       The type of the collection of Long values returned.
     * @return A collection of type &lt;C&gt; of Long values.
     */
    public static <C extends Collection<Long>> C longTransform(long[] longs, Collector<Long, ?, C> collector) {
        return longTransform(longs, LongTransformerCollector.of(Long::valueOf, collector));
    }

    /**
     * Given an array of longs, and a <code>LongFunction&lt;R&gt;</code> that transforms those longs into elements of
     * type &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements, which will contain only unique values
     * (according to <code>Object.equals(Object)</code>).
     *
     * @param longs       An array of longs to be transformed.
     * @param transformer A LongFunction&lt;R&gt; that takes a long and transforms it to an element of type &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive longs, containing only distinct
     * elements.
     */
    public static <R> List<R> longTransformDistinct(long[] longs, LongFunction<R> transformer) {
        return defaultLongStream(longs)
                .mapToObj(longMapper(transformer))
                .distinct()
                .collect(toList());
    }

    /**
     * Given an array of longs, and a <code>LongTransformerCollector&lt;U, C&gt;</code> that contains a
     * <code>LongFunction&lt;U&gt;</code> to transform long values into elements of type &lt;U&gt;, and a
     * <code>Collector&lt;U, ?, C&gt;</code>, returns a collection of type &lt;C&gt; of those elements. The
     * <code>transformerCollection</code> passed into this method should be created via a call to
     * {@link #longTransformAndThen(LongFunction, Collector)}.
     *
     * @param longs                An array of longs to be transformed.
     * @param transformerCollector A object containing a transformer LongFunction&lt;U&gt; to transform long values into
     *                             elements of type &lt;U&gt;, and a Collector&lt;U, ?, C&gt; to put those elements into
     *                             a resulting collection.
     * @param <U>                  The type of the elements in the resulting Collection.
     * @param <C>                  The type of the Collection of elements of type &lt;U&gt; returned.
     * @return A collection of type &lt;C&gt; of elements &lt;U&gt;, transformed from an array of primitive longs.
     */
    public static <U, C extends Collection<U>> C longTransform(long[] longs, LongTransformerCollector<U, C> transformerCollector) {
        return defaultLongStream(longs)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    /**
     * Given an array of longs, and a <code>LongFunction&lt;int[]&gt;</code> (a function that takes a long and returns
     * an array of longs), returns an array of longs.
     *
     * @param longs    An array of primitive longs to be flat-mapped into another array of longs.
     * @param function A LongFunction that takes a long and returns an array of longs.
     * @return An array of flat-mapped primitive longs.
     */
    public static long[] longFlatMap(long[] longs, LongFunction<long[]> function) {
        return defaultLongStream(longs)
                .flatMap(longFlatMapper(function))
                .toArray();
    }

    /**
     * Given an array of longs, and a <code>LongFunction&lt;long[]&gt;</code> (a function that takes a long and returns
     * an array of longs), returns an array of distinct primitive long values.
     *
     * @param longs    An array of primitive longs to be flat-mapped into another array of longs.
     * @param function A LongFunction that takes a long and returns an array of longs.
     * @return An array of distinct, primitive, flat-mapped longs.
     */
    public static long[] longFlatMapDistinct(long[] longs, LongFunction<long[]> function) {
        return defaultLongStream(longs)
                .flatMap(longFlatMapper(function))
                .distinct()
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, long[]&gt;</code>, returns an array
     * of primitive longs.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive longs.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive long array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of primitive flat-mapped longs.
     */
    public static <T> long[] flatMapToLong(Collection<T> objects, Function<T, long[]> function) {
        return defaultStream(objects)
                .flatMapToLong(flatMapperToLong(function))
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, long[]&gt;</code>, returns an array
     * of distinct primitive longs.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive longs.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive long array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of distinct, primitive, flat-mapped longs.
     */
    public static <T> long[] flatMapToLongDistinct(Collection<T> objects, Function<T, long[]> function) {
        return defaultStream(objects)
                .flatMapToLong(flatMapperToLong(function))
                .distinct()
                .toArray();
    }

    /**
     * Builds a <code>LongTransformerCollector&lt;U, C&gt;</code> that contains a <code>LongFunction&lt;U&gt;</code> to
     * transform primitive long values into elements of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to
     * collect those elements into a collection of type &lt;C&gt;. Used to build the second parameter to
     * {@link #longTransform(long[], LongTransformerCollector)}.
     *
     * @param transformer A <code>LongFunction&lt;U&gt;</code> to transform primitive long values into elements of type
     *                    &lt;U&gt;.
     * @param collector   A Collector&lt;U, ?, C&gt; to append elements into a collection of type &lt;C&gt;.
     * @param <U>         The type of the elements into which int values are to be transformed.
     * @param <C>         The type of a collection of elements of type &lt;U&gt;, into which a given collector will
     *                    append elements.
     * @return An object containing a <code>LongFunction&lt;U&gt;</code> to transform primitive long values into
     * elements of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to append those elements into a
     * collection of type &lt;C&gt;.
     */
    public static <U, C extends Collection<U>> LongTransformerCollector<U, C> longTransformAndThen(LongFunction<U> transformer, Collector<U, ?, C> collector) {
        return LongTransformerCollector.of(transformer, collector);
    }

    /**
     * Given an array of primitive longs, and a <code>LongKeyValueMapper</code> containing a <code>LongFunction</code>
     * to transform a long to a key of type &lt;K&gt;, and another <code>LongFunction</code> to transform a long to a
     * value of type &lt;V&gt;, returns a <code>Map&lt;K, V&gt;</code>.
     *
     * @param longs          An array of primitive longs to be transformed into keys and values for the resulting
     *                       Map&lt;K, V&gt;.
     * @param keyValueMapper An object containing a LongFunction to transform a long to a key of type &lt;K&gt;,
     *                       and another LongFunction to transform a long to a value of type &lt;V&gt;.
     * @param <K>            The type of the key of the resulting Map&lt;K, V&gt;.
     * @param <V>            The type of the value of the resulting Map&lt;K, V&gt;.
     * @return A map whose keys and values are transformed from an array of longs.
     */
    public static <K, V> Map<K, V> longTransformToMap(long[] longs, LongKeyValueMapper<K, V> keyValueMapper) {
        return defaultLongStream(longs)
                .mapToObj(longPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
