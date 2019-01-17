package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.collector.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.MapperUtils.flatMapper;
import static org.hringsak.functions.mapper.MapperUtils.pairOf;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

/**
 * Methods to transform a collection of elements of a given type, to those of another type, in some cases to another
 * type of collection as well.
 */
public final class TransformUtils {

    private TransformUtils() {
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a transformer function that converts those elements to
     * objects of type &lt;R&gt;, this method transforms the given collection to a <code>List&lt;R&gt;</code>.
     *
     * @param objects     A collection of elements of type &lt;T&gt;.
     * @param transformer A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;R&gt;.
     * @param <T>         The type of the elements of the passed objects Collection.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; consisting of the elements of the passed objects Collection, transformed into objects of
     * type &lt;R&gt;.
     */
    public static <T, R> List<R> transform(Collection<T> objects, Function<T, R> transformer) {
        return transform(objects, TransformerCollector.of(transformer, toList()));
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a transformer function that converts those elements to
     * objects of type &lt;R&gt;, this method transforms the given collection to a <code>Set&lt;R&gt;</code>.
     *
     * @param objects     A collection of elements of type &lt;T&gt;.
     * @param transformer A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;R&gt;.
     * @param <T>         The type of the elements of the passed objects Collection.
     * @param <R>         The type of the elements in the resulting Set.
     * @return A Set&lt;R&gt; consisting of the elements of the passed objects Collection, transformed into objects of
     * type &lt;R&gt;.
     */
    public static <T, R> Set<R> transformToSet(Collection<T> objects, Function<T, R> transformer) {
        return transform(objects, TransformerCollector.of(transformer, toSet()));
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a transformer function that converts those elements to
     * objects of type &lt;R&gt;, this method transforms the given collection to a <code>List&lt;R&gt;</code> of
     * distinct elements (according to <code>Object.equals(Object)</code>).
     *
     * @param objects     A collection of elements of type &lt;T&gt;.
     * @param transformer A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;R&gt;.
     * @param <T>         The type of the elements of the passed objects Collection.
     * @param <R>         The type of the elements in the resulting List.
     * @return A List&lt;R&gt; consisting of the elements of the passed objects Collection, transformed into objects of
     * type &lt;R&gt;. The resulting List will contain only distinct elements of type &lt;R&gt;.
     */
    public static <T, R> List<R> transformDistinct(Collection<T> objects, Function<T, R> transformer) {
        return defaultStream(objects)
                .map(transformer)
                .distinct()
                .collect(toList());
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and an object that represents a transformer
     * <code>Function&lt;T, U&gt;</code> along with a <code>Collector&lt;U, ?, C&gt;</code>, this method transforms the
     * given collection to one of type &lt;C&gt;, with elements of type &lt;U&gt;.
     *
     * @param objects              A collection of elements of type &lt;T&gt;.
     * @param transformerCollector An object that represents a transformer <code>Function&lt;T, U&gt;</code> along with
     *                             a <code>Collector&lt;U, ?, C&gt;</code>.
     * @param <T>                  The type of the elements in the passed objects Collection.
     * @param <U>                  The type of the elements in the resulting collection.
     * @param <C>                  The type of the resulting Collection.
     * @return A Collection of type &lt;C&gt;, containing elements of type &lt;U&gt;, transformed from the elements of
     * the passed objects Collection.
     */
    public static <T, U, C extends Collection<U>> C transform(Collection<T> objects, TransformerCollector<T, U, C> transformerCollector) {
        return defaultStream(objects)
                .map(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and an object that represents a transformer of those elements
     * into <code>Map</code> keys and values, of type &lt;K&gt; and &lt;V&gt; respectively, this method transforms that
     * collection into a Map&lt;K, V&gt;.
     *
     * @param objects        A collection of elements of type &lt;T&gt;.
     * @param keyValueMapper An object consisting of a pair of functions, one transforming an element of type &lt;T&gt;
     *                       into a key value of type &lt;K&gt;, and the other transforming &lt;T&gt; into a value of
     *                       &lt;V&gt;.
     * @param <T>            The type of the elements in the passed objects Collection.
     * @param <K>            The type of the keys in the resulting Map.
     * @param <V>            The type of the values in the resulting Map.
     * @return A Map&lt;K, V&gt; with the keys and values both transformed from elements of type &lt;T&gt;.
     */
    public static <T, K, V> Map<K, V> transformToMap(Collection<T> objects, KeyValueMapper<T, K, V> keyValueMapper) {
        return defaultStream(objects)
                .map(pairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a <code>Function</code> that transforms each of those
     * elements into collections of type &lt;U&gt;, this method collects all of those individual collections into one
     * resulting <code>List</code> consisting of all of the elements.
     *
     * @param objects  A collection of elements of type &lt;T&gt;.
     * @param function A transformer Function from objects of type &lt;T&gt;, to a <code>Collection</code> of elements
     *                 of type &lt;U&gt;.
     * @param <T>      The type of the elements in the passed objects Collection.
     * @param <U>      The type of the elements in the resulting collection.
     * @return A List consisting of all of the elements, of all the collections transformed from individual elements of
     * the passed objects collection.
     */
    public static <T, U> List<U> flatMap(Collection<T> objects, Function<T, Collection<U>> function) {
        FlatMapCollector<T, U, List<U>> flatMapCollector = FlatMapCollector.of(function, toList());
        return flatMap(objects, flatMapCollector);
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a <code>Function</code> that transforms each of those
     * elements into collections of type &lt;R&gt;, this method collects all of those individual collections into one
     * resulting <code>Set</code> consisting of all of the elements.
     *
     * @param objects  A collection of elements of type &lt;T&gt;.
     * @param function A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;R&gt;.
     * @param <T>      The type of the elements in the passed objects Collection.
     * @param <R>      The type of the elements in the resulting collection.
     * @return A Set consisting of all of the elements, of all the collections transformed from individual elements of
     * the passed objects collection.
     */
    public static <T, R> Set<R> flatMapToSet(Collection<T> objects, Function<T, Collection<R>> function) {
        return flatMap(objects, FlatMapCollector.of(function, toSet()));
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and a <code>Function</code> that transforms each of those
     * elements into collections of type &lt;U&gt;, this method collects all of those individual collections into one
     * resulting <code>List</code> consisting of all of the distinct elements of type &lt;U&gt; (according to
     * <code>Object.equals(Object)</code>).
     *
     * @param objects  A collection of elements of type &lt;T&gt;.
     * @param function A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;U&gt;.
     * @param <T>      The type of the elements in the passed objects Collection.
     * @param <U>      The type of the elements in the resulting collection.
     * @return A List consisting of all of the distinct elements, of all the collections transformed from individual
     * elements of the passed objects collection.
     */
    public static <T, U> List<U> flatMapDistinct(Collection<T> objects, Function<T, Collection<U>> function) {
        return defaultStream(objects)
                .flatMap(flatMapper(function))
                .distinct()
                .collect(toList());
    }

    /**
     * Given a collection of elements of type &lt;T&gt;, and an object that represents a transformer
     * <code>Function&lt;T, Collection&lt;U&gt;&gt;</code> along with a <code>Collector&lt;U, ?, C&gt;</code>, this
     * method collects all of those individual collections into one resulting <code>Collection</code> of type &lt;C&gt;,
     * consisting of all of the elements.
     *
     * @param objects          A collection of elements of type &lt;T&gt;.
     * @param flatMapCollector An object that represents a transformer <code>Function&lt;T,
     *                         Collection&lt;U&gt;&gt;</code> along with a <code>Collector&lt;U, ?, C&gt;</code>.
     * @param <T>              The type of the elements in the passed objects Collection.
     * @param <U>              The type of the elements in the resulting collection.
     * @param <C>              The type of the resulting Collection.
     * @return A Collection of type &lt;C&gt;, containing elements of type &lt;U&gt;, transformed from the elements of
     * the passed objects Collection.
     */
    public static <T, U, C extends Collection<U>> C flatMap(Collection<T> objects, FlatMapCollector<T, U, C> flatMapCollector) {
        return defaultStream(objects)
                .flatMap(flatMapCollector.getFlatMapper())
                .collect(flatMapCollector.getCollector());
    }

    /**
     * Given a <code>Function&lt;T, U&gt;</code>, and a <code>Collector&lt;U, ?, C&gt;</code>, this method builds an
     * object consisting of both of those parameters. This method is meant to build the second parameter of the
     * {@link #transform(Collection, TransformerCollector)} method. For example, suppose you want to retrieve an
     * <code>EnumSet</code> consisting of all the unique <code>ProductLine</code> enumerated values in the line items of
     * an order:
     * <pre>
     *     private Set&lt;ProductLine&gt; getUniqueProductLines(Order order) {
     *         return TransformUtils.transform(order.getLineItems(),
     *             TransformUtils.transformAndThen(LineItem::getProductLine, CollectorUtils.toEnumSet(ProductLine.class)));
     *     }
     * </pre>
     * Or, using static imports:
     * <pre>
     *     private Set&lt;ProductLine&gt; getUniqueProductLines(Order order) {
     *         return transform(order.getLineItems(), transformAndThen(LineItem::getProductLine, toEnumSet(ProductLine.class)));
     *     }
     * </pre>
     *
     * @param transformer A transformer Function from objects of type &lt;T&gt;, to objects of type &lt;U&gt;.
     * @param collector   A Collector&lt;U, ?, C&gt; to return a Collection of elements of type &lt;C&gt;.
     * @param <T>         The type of the target element to be passed into the transformer function.
     * @param <U>         The type of the return value of the transformer function.
     * @param <C>         The type of the resulting collection of the passed Collector.
     * @return Builds an object consisting of a transformer Function, along with a Collector.
     */
    public static <T, U, C extends Collection<U>> TransformerCollector<T, U, C> transformAndThen(Function<T, U> transformer, Collector<U, ?, C> collector) {
        return TransformerCollector.of(transformer, collector);
    }

    /**
     * Given a <code>Function&lt;T, Collection&lt;U&gt;&gt;</code>, and a <code>Collector&lt;U, ?, C&gt;</code>, this
     * method builds an object consisting of both of those parameters. This method is meant to build the second parameter of the
     * {@link #flatMap(Collection, FlatMapCollector)} method. For example, suppose you want to retrieve an
     * <code>EnumSet</code> consisting of all the unique <code>ProductLine</code> enumerated values for a
     * <code>Collection</code> of customer orders:
     * <pre>
     *     private Set&lt;ProductLine&gt; getUniqueProductLines(Collection&lt;Order&gt; customerOrders) {
     *         return TransformUtils.flatMap(order.getLineItems(),
     *             TransformUtils.flatMapAndThen(Order::getProductLines, CollectorUtils.toEnumSet(ProductLine.class)));
     *     }
     * </pre>
     * Or, using static imports:
     * <pre>
     *     private Set&lt;ProductLine&gt; getUniqueProductLines(Collection&lt;Order&gt; customerOrders) {
     *         return flatMap(order.getLineItems(), flatMapAndThen(Order::getProductLines, toEnumSet(ProductLine.class)));
     *     }
     * </pre>
     *
     * @param flatMapper A transformer Function from objects of type &lt;T&gt;, to a <code>Collection</code> of elements
     *                   of type &lt;U&gt;.
     * @param collector  A Collector&lt;U, ?, C&gt; to return a Collection of elements of type &lt;C&gt;.
     * @param <T>        The type of the target element to be passed into the transformer function.
     * @param <U>        The type of the elements in the Collection to be returned from the transformer function.
     * @param <C>        The type of the resulting collection of the passed Collector.
     * @return Builds an object consisting of a transformer Function, along with a Collector.
     */
    public static <T, U, C extends Collection<U>> FlatMapCollector<T, U, C> flatMapAndThen(Function<T, Collection<U>> flatMapper, Collector<U, ?, C> collector) {
        return FlatMapCollector.of(flatMapper, collector);
    }
}
