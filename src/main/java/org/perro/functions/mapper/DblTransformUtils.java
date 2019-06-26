package org.perro.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.perro.functions.collector.CollectorUtils.toMapFromEntry;
import static org.perro.functions.mapper.DblMapperUtils.*;
import static org.perro.functions.stream.DblStreamUtils.defaultDblStream;
import static org.perro.functions.stream.StreamUtils.defaultStream;

/**
 * Methods to transform an array of doubles to a collection of elements of an arbitrary type, or to transform a
 * collection of elements of an arbitrary type to an array of doubles.
 */
public final class DblTransformUtils {

    private DblTransformUtils() {
    }

    /**
     * Given an array of doubles, and a <code>DoubleUnaryOperator</code> that transforms each of those doubles to
     * another double, returns an array of the resulting <code>double</code> values.
     *
     * @param doubles  An array of doubles to be transformed.
     * @param operator A DoubleUnaryOperator that takes a double and transforms it to another double.
     * @return An array of primitive transformed doubles.
     */
    public static double[] dblUnaryTransform(double[] doubles, DoubleUnaryOperator operator) {
        return defaultDblStream(doubles)
                .map(operator)
                .toArray();
    }

    /**
     * Given an array of doubles, and a <code>DoubleUnaryOperator</code> that transforms each of those doubles to
     * another double, returns an array of the resulting <code>double</code> values, which will contain only distinct
     * values.
     *
     * @param doubles  An array of doubles to be transformed.
     * @param operator A DoubleUnaryOperator that takes a double and transforms it to another double.
     * @return An array of distinct, primitive, transformed doubles.
     */
    public static double[] dblUnaryTransformDistinct(double[] doubles, DoubleUnaryOperator operator) {
        return defaultDblStream(doubles)
                .map(operator)
                .distinct()
                .toArray();
    }

    /**
     * Given an array of doubles, and a <code>DoubleFunction&lt;R&gt;</code> that transforms those doubles into elements
     * of type &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements.
     *
     * @param doubles     An array of doubles to be transformed.
     * @param transformer A DoubleFunction&lt;R&gt; that takes a double and transforms it to an element of type
     *                    &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive doubles.
     */
    public static <R> List<R> dblTransform(double[] doubles, DoubleFunction<R> transformer) {
        return dblTransform(doubles, DblTransformerCollector.of(transformer, toList()));
    }

    /**
     * Given an array of doubles, and a <code>DoubleFunction&lt;R&gt;</code> that transforms those doubles to an element
     * of type &lt;R&gt;, returns a <code>Set&lt;R&gt;</code> of those elements.
     *
     * @param doubles     An array of doubles to be transformed.
     * @param transformer A DoubleFunction&lt;R&gt; that takes a double and transforms it to an element of type
     *                    &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting Set.
     * @return A Set&lt;R&gt; of elements transformed from an array of primitive doubles.
     */
    public static <R> Set<R> dblTransformToSet(double[] doubles, DoubleFunction<R> transformer) {
        return dblTransform(doubles, DblTransformerCollector.of(transformer, toSet()));
    }

    /**
     * Given an array of doubles, and a <code>Collector&lt;Double, ?, C&gt;</code>, returns a
     * <code>Collection&lt;C&gt;</code> of <code>Double</code> elements.
     *
     * @param doubles   An array of doubles to be collected.
     * @param collector A Collector&lt;Double, ?, C&gt; that appends Double values into a collection of type &lt;C&gt;.
     * @param <C>       The type of the collection of Double values returned.
     * @return A collection of type &lt;C&gt; of Double values.
     */
    public static <C extends Collection<Double>> C dblTransform(double[] doubles, Collector<Double, ?, C> collector) {
        return dblTransform(doubles, DblTransformerCollector.of(Double::valueOf, collector));
    }

    /**
     * Given an array of doubles, and a <code>DoubleFunction&lt;R&gt;</code> that transforms those doubles to elements
     * of type &lt;R&gt;, returns a <code>List&lt;R&gt;</code> of those elements, which will contain only unique values
     * (according to <code>Object.equals(Object)</code>).
     *
     * @param doubles     An array of doubles to be transformed.
     * @param transformer A DoubleFunction&lt;R&gt; that takes a double and transforms it to an element of type
     *                    &lt;R&gt;.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; of elements transformed from an array of primitive doubles, containing only distinct
     * elements.
     */
    public static <R> List<R> dblTransformDistinct(double[] doubles, DoubleFunction<R> transformer) {
        return defaultDblStream(doubles)
                .mapToObj(dblMapper(transformer))
                .distinct()
                .collect(toList());
    }

    /**
     * Given an array of doubles, and a <code>DblTransformerCollector&lt;U, C&gt;</code> that contains a
     * <code>DoubleFunction&lt;U&gt;</code> to transform double values into elements of type &lt;U&gt;, and a
     * <code>Collector&lt;U, ?, C&gt;</code>, returns a collection of type &lt;C&gt; of those elements. The
     * <code>transformerCollection</code> passed into this method should be created via a call to
     * {@link #dblTransformAndThen(DoubleFunction, Collector)}.
     *
     * @param doubles              An array of doubles to be transformed.
     * @param transformerCollector A object containing a transformer DoubleFunction&lt;U&gt; to transform double values
     *                             into elements of type &lt;U&gt;, and a Collector&lt;U, ?, C&gt; to put those elements
     *                             into a resulting collection.
     * @param <U>                  The type of the elements in the resulting Collection.
     * @param <C>                  The type of the Collection of elements of type &lt;U&gt; returned.
     * @return A collection of type &lt;C&gt; of elements &lt;U&gt;, transformed from an array of primitive doubles.
     */
    public static <U, C extends Collection<U>> C dblTransform(double[] doubles, DblTransformerCollector<U, C> transformerCollector) {
        return defaultDblStream(doubles)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    /**
     * Given an array of doubles, and a <code>DoubleFunction&lt;double[]&gt;</code> (a function that takes a double and
     * returns an array of doubles), returns an array of doubles.
     *
     * @param doubles  An array of primitive doubles to be flat-mapped into another array of doubles.
     * @param function A DoubleFunction that takes a double and returns an array of doubles.
     * @return An array of flat-mapped primitive doubles.
     */
    public static double[] dblFlatMap(double[] doubles, DoubleFunction<double[]> function) {
        return defaultDblStream(doubles)
                .flatMap(dblFlatMapper(function))
                .toArray();
    }

    /**
     * Given an array of doubles, and a <code>DoubleFunction&lt;double[]&gt;</code> (a function that takes a double and
     * returns an array of doubles), returns an array of distinct primitive double values.
     *
     * @param doubles  An array of primitive doubles to be flat-mapped into another array of doubles.
     * @param function A DoubleFunction that takes a double and returns an array of doubles.
     * @return An array of distinct, primitive, flat-mapped doubles.
     */
    public static double[] dblFlatMapDistinct(double[] doubles, DoubleFunction<double[]> function) {
        return defaultDblStream(doubles)
                .flatMap(dblFlatMapper(function))
                .distinct()
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, double[]&gt;</code>, returns an array
     * of primitive doubles.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive doubles.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive double array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of primitive flat-mapped doubles.
     */
    public static <T> double[] flatMapToDbl(Collection<T> objects, Function<T, double[]> function) {
        return defaultStream(objects)
                .flatMapToDouble(flatMapperToDbl(function))
                .toArray();
    }

    /**
     * Given a collection of objects of type &lt;T&gt;, and a <code>Function&lt;T, double[]&gt;</code>, returns an array
     * of distinct primitive doubles.
     *
     * @param objects  A collection of objects of type &lt;T&gt;, to be flat-mapped into an array of primitive doubles.
     * @param function A Function that takes an element of type &lt;T&gt; and returns a primitive double array.
     * @param <T>      The type of the elements in the given objects collection.
     * @return An array of distinct, primitive, flat-mapped doubles.
     */
    public static <T> double[] flatMapToDblDistinct(Collection<T> objects, Function<T, double[]> function) {
        return defaultStream(objects)
                .flatMapToDouble(flatMapperToDbl(function))
                .distinct()
                .toArray();
    }

    /**
     * Builds a <code>DblTransformerCollector&lt;U, C&gt;</code> that contains a <code>DoubleFunction&lt;U&gt;</code> to
     * transform primitive double values into elements of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to
     * collect those elements into a collection of type &lt;C&gt;. Used to build the second parameter to
     * {@link #dblTransform(double[], DblTransformerCollector)}.
     *
     * @param transformer A <code>DoubleFunction&lt;U&gt;</code> to transform primitive double values into elements of
     *                    type &lt;U&gt;.
     * @param collector   A Collector&lt;U, ?, C&gt; to append elements into a collection of type &lt;C&gt;.
     * @param <U>         The type of the elements into which double values are to be transformed.
     * @param <C>         The type of a collection of elements of type &lt;U&gt;, into which a given collector will
     *                    append elements.
     * @return An object containing a <code>DoubleFunction&lt;U&gt;</code> to transform primitive double values into
     * elements of type &lt;U&gt;, and a <code>Collector&lt;U, ?, C&gt;</code> to append those elements into a
     * collection of type &lt;C&gt;.
     */
    public static <U, C extends Collection<U>> DblTransformerCollector<U, C> dblTransformAndThen(DoubleFunction<U> transformer, Collector<U, ?, C> collector) {
        return DblTransformerCollector.of(transformer, collector);
    }

    /**
     * Given an array of primitive doubles, and a <code>DblKeyValueMapper</code> containing a
     * <code>DoubleFunction</code> to transform a double to a key of type &lt;K&gt;, and another
     * <code>DoubleFunction</code> to transform a double to a value of type &lt;V&gt;, returns a
     * <code>Map&lt;K, V&gt;</code>.
     *
     * @param doubles        An array of primitive doubles to be transformed into keys and values for the resulting
     *                       Map&lt;K, V&gt;.
     * @param keyValueMapper An object containing a DoubleFunction to transform a double to a key of type &lt;K&gt;,
     *                       and another DoubleFunction to transform a double to a value of type &lt;V&gt;.
     * @param <K>            The type of the key of the resulting Map&lt;K, V&gt;.
     * @param <V>            The type of the value of the resulting Map&lt;K, V&gt;.
     * @return A map whose keys and values are transformed from an array of doubles.
     */
    public static <K, V> Map<K, V> dblTransformToMap(double[] doubles, DblKeyValueMapper<K, V> keyValueMapper) {
        return defaultDblStream(doubles)
                .mapToObj(dblPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
