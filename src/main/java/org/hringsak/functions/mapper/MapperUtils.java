package org.hringsak.functions.mapper;

import org.hringsak.functions.internal.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

/**
 * Methods that build functions to map a target element into another result.
 */
public final class MapperUtils {

    private MapperUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt; and returns &lt;R&gt;, to a
     * <code>Function</code>. This could be useful in a situation where methods of the <code>Function</code> interface
     * are to be called on a method reference. In the following example, assume that the <code>Widget.getProductType
     * </code> method returns an enum instance, and we ultimately want to get the enum name:
     * <pre>
     *     Collection&lt;Widget&gt; widgets = ...
     *     Collection&lt;String&gt; productTypeNames = widgets.stream()
     *         .map(MapperUtils.mapper(Widget::getProductType)
     *             .andThen(ProductType::name))
     *         .collect(toList());
     * </pre>
     * The <code>Function.andThen()</code> method can only be called on the method reference because of the cast. An
     * additional benefit of calling this method is the null-checking performed on the target element passed to the
     * resulting function.
     *
     * @param function A method reference to be cast to a Function.
     * @param <T>      The type of the single parameter to the Function.
     * @param <R>      The type of the result of the Function.
     * @return A method reference cast to a Function.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T, R> Function<T, R> mapper(Function<T, R> function) {
        return t -> t == null ? null : function.apply(t);
    }

    /**
     * Builds a mapper <code>Function</code> that, if the target element is <code>null</code>, or the result of the
     * <code>Function</code> call on the target element is <code>null</code>, then the passed default value is returned.
     * In the following example, assume that the <code>Widget.getProductType</code> method returns an enum instance, and
     * that if its value is <code>null</code>, then it should default to <code>ProductType.NONE</code>:
     * <pre>
     *     Collection&lt;Widget&gt; widgets = ...
     *     Collection&lt;ProductType&gt; productTypes = widgets.stream()
     *         .map(MapperUtils.mapperDefault(Widget::getProductType, ProductType.NONE))
     *         .collect(toList());
     * </pre>
     *
     * @param function     A method reference which takes a single parameter of type &lt;T&gt;, and returns a value of
     *                     type &lt;R&gt;.
     * @param defaultValue A default value of type &lt;R&gt;, to be returned in case the target element, or the result
     *                     of the Function call on the target element is null.
     * @param <T>          The type of the target element on which the mapper Function is to be called.
     * @param <R>          The type of the result to be returned from the mapper Function built by this method.
     * @return A mapper Function that returns a default value if the target element, or the result of the Function call
     * on the target element is null.
     */
    public static <T, R> Function<T, R> mapperDefault(Function<? super T, ? extends R> function, R defaultValue) {
        return t -> (t == null || function.apply(t) == null) ? defaultValue : function.apply(t);
    }

    /**
     * Builds a <code>Function</code> from a passed <code>BiFunction</code>, which can be very useful in the common
     * situation where you are streaming through a collection of elements, and have a method to call that takes two
     * parameters - the first one being the element on which you are streaming, and the second being some constant value
     * that will be passed to all invocations. This would typically be called from within a chain of method calls based
     * on a <code>Stream</code>. In the following example, assume the <code>LineItemRequest</code> objects come in from
     * a rest API call, and we want to transform them into a collection of <code>OrderLineItem</code> objects:
     * <pre>
     *     private Collection&lt;OrderLineItem&gt; buildOrderLineItems(Collection&lt;LineItemRequest&gt; lineItemRequests, String orderId) {
     *         return Collection&lt;OrderLineItem&gt; orderLineItems = lineItemRequests.stream()
     *             .map(MapperUtils.mapper(this::createOrderLineItem, orderId))
     *             .collect(toList());
     *     }
     *
     *     private OrderLineItem createOrderLineItem(LineItemRequest request, String orderId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return Collection&lt;OrderLineItem&gt; orderLineItems = requestLineItems.stream()
     *         .map(mapper(this::createOrderLineItem, orderId))
     *         .collect(toList());
     * </pre>
     *
     * @param biFunction A method reference (a BiFunction) which takes two parameters - the first of type &lt;T&gt;, and
     *                   the second of type &lt;U&gt;, either of which can be any type. The method reference will be
     *                   converted by this method to a Function, taking a single parameter of type &lt;T&gt;. Behind the
     *                   scenes, this BiFunction will be called, passing the constant value to each invocation as the
     *                   second parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <T>        The target type of the first parameter to the passed biFunction.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the Function built by this method.
     * @return A Function taking a single parameter of type &lt;T&gt;.
     */
    public static <T, U, R> Function<T, R> mapper(BiFunction<? super T, ? super U, ? extends R> biFunction, U value) {
        return t -> biFunction.apply(t, value);
    }

    /**
     * As in the {@link #mapper(BiFunction, Object)} method, builds a <code>Function</code> from a passed
     * <code>BiFunction</code>, which can be very useful in the common situation where you are streaming through a
     * collection elements, and have a method to call that takes two parameters. In the <code>BiFunction</code> passed
     * to this method, the parameters are basically the same as in {@link #mapper(BiFunction, Object)}, but in the
     * inverse order. Here, the first parameter is a constant value that will be passed to all invocations of the
     * method, and the second parameter is the target element on which you are streaming. This would typically be called
     * from within a chain of method calls based on a <code>Stream</code>. In the following example, assume the
     * <code>LineItemRequest</code> objects come in from a rest API call, and we want to transform them into a
     * collection of <code>OrderLineItem</code> objects:
     * <pre>
     *     private Collection&lt;OrderLineItem&gt; buildOrderLineItems(String orderId, Collection&lt;LineItemRequest&gt; requestLineItems) {
     *         return Collection&lt;OrderLineItem&gt; orderLineItems = requestLineItems.stream()
     *             .map(MapperUtils.inverseMapper(this::createOrderLineItem, orderId))
     *             .collect(toList());
     *     }
     *
     *     private OrderLineItem createOrderLineItem(String orderId, RequestLineItem request) {
     *         ...
     *     }
     * </pre>
     * Note how the parameters to the <code>createOrderLineItem(...)</code> are in the inverse order as the ones in the
     * example for the {@link #mapper(BiFunction, Object)} method.
     * <p>
     * Or, with static imports:
     * <pre>
     *     return Collection&lt;OrderLineItem&gt; orderLineItems = requestLineItems.stream()
     *         .map(inverseMapper(this::createOrderLineItem, orderId))
     *         .collect(toList());
     * </pre>
     *
     * @param biFunction A method reference (a BiFunction) which takes two parameters - the first of type &lt;U&gt;, and
     *                   the second of type &lt;T&gt;, either of which can be any type. The method reference will be
     *                   converted by this method to a Function, taking a single parameter of type &lt;T&gt;. Behind the
     *                   scenes, this BiFunction will be called, passing the constant value to each invocation as the
     *                   first parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <T>        The target type of the second parameter to the passed biFunction.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the Function built by this method.
     * @return A Function taking a single parameter of type &lt;T&gt;.
     */
    public static <T, U, R> Function<T, R> inverseMapper(BiFunction<? super U, ? super T, ? extends R> biFunction, U value) {
        return t -> biFunction.apply(value, t);
    }

    /**
     * Applies a series to two <code>Function</code> method references, the first taking a parameter of type &lt;T&gt;
     * and returning a value of type &lt;U&gt;, and the second taking a parameter of type &lt;U&gt; and returning a
     * value of type &lt;R&gt;. The following example illustrates its usage:
     * <pre>
     *     List&lt;ObjectThree&gt; transformed = StreamUtils.transform(objects, MapperUtils.mapper(ObjectOne::getObjectTwo, ObjectTwo::getObjectThree));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     List&lt;ObjectThree&gt; transformed = transform(objects, mapper(ObjectOne::getObjectTwo, ObjectTwo::getObjectThree));
     * </pre>
     * The above are equivalent to:
     * <pre>
     *     List&lt;ObjectThree&gt; transformed = StreamUtils.transform(objects, MapperUtils.mapper(ObjectOne::getObjectTwo).andThen(ObjectTwo::getObjectThree));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     List&lt;ObjectThree&gt; transformed = transform(objects, mapper(ObjectOne::getObjectTwo).andThen(ObjectTwo::getObjectThree));
     * </pre>
     * The first example calling this method is slightly more concise, but which of the above is more readable is up to
     * the individual developer. This method provides an alternative way of accomplishing the above transformation. That
     * being said, there is an additional benefit of using this method. Not only is there null-checking on the target
     * element, there is also null-checking on the result of the transformation using the <code>left Function</code>.
     *
     * @param left  A Function to perform a first, preliminary transformation.
     * @param right A Function to perform a second, final transformation.
     * @param <T>   The type of a target element to be passed to the resulting function.
     * @param <U>   The type of an interim result from a first transformation.
     * @param <R>   The type of a final result from a second transformation.
     * @return A Function transforming an element of type &lt;T&gt; to a result of type &lt;R&gt;. If the target element
     * is null, or the result of the transformation using the left Function is null, then the Function will return null.
     */
    public static <T, U, R> Function<T, R> mapper(Function<? super T, ? extends U> left, Function<? super U, ? extends R> right) {
        return t -> {
            U value = t == null ? null : left.apply(t);
            return value == null ? null : right.apply(value);
        };
    }

    /**
     * Retrieves a value of type &lt;R&gt; from a <code>Map</code>, using a key retrieved from an element of type
     * &lt;T&gt; using a passed <code>Function</code>. The following example illustrates its usage:
     * <pre>
     *     private List&lt;Customer&gt; getCustomers(Collection&lt;Order&gt; orders) {
     *         Map&lt;String, Customer&gt; customerById = ...
     *         return StreamUtils.transform(orders, MapperUtils.getValue(customerById, Order::getCustomerId));
     *     }
     * </pre>
     * Or, using static imports:
     * <pre>
     *         return transform(orders, getValue(customerById, Order::getCustomerId));
     * </pre>
     *
     * @param map      A map from which a value will be retrieved.
     * @param function A Function whose result will be used as a key to retrieve a value from a Map.
     * @param <T>      The type of the target element on which a Function is to be called to provide a key value.
     * @param <K>      The type of a key value for a passed Map.
     * @param <V>      The type of the value to be returned from the Function built by this method.
     * @return A Function that retrieves a value from a passed Map. If passed map, or the target element passed to this
     * Function are null, then the result of this Function will be null.
     */
    public static <T, K, V> Function<T, V> getValue(Map<K, V> map, Function<T, K> function) {
        return t -> (map == null || t == null) ? null : map.get(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a
     * <code>Collection&lt;R&gt;</code>, this method builds a <code>Function</code> that takes the same argument type,
     * and returns a <code>Stream&lt;R&gt;</code>. This is useful in the <code>Stream.flatMap()</code> method. For
     * example, let's say you want to retrieve all the line items from all of a customer's orders, assuming that
     * <code>Order.getLineItems()</code> returns a <code>Collection</code> of type &lt;R&gt;:
     * <pre>
     *     private List&lt;OrderLineItem&gt; getCustomerLineItems(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMap(MapperUtils.flatMapper(Order::getLineItems))
     *             .collect(Collectors.toList());
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         return customerOrders.stream()
     *             .flatMap(flatMapper(Order::getLineItems))
     *             .collect(toList());
     * </pre>
     *
     * @param function A function that returns a Collection of type &lt;R&gt;.
     * @param <T>      The type of the target element on which a Function is to be called to provide a Collection.
     * @param <R>      The type of the element of a Collection returned by the given function.
     * @return A Function that, given a target element of type &lt;T&gt;, returns a Stream of type &lt;R&gt;. Returns an
     * empty Stream if the target element is null, or the Collection returned by the given function is null.
     */
    public static <T, R> Function<T, Stream<R>> flatMapper(Function<? super T, ? extends Collection<R>> function) {
        return t -> t == null ? Stream.empty() : defaultStream(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of type &lt;R&gt;,
     * this method builds a <code>Function</code> that takes the same argument type, and returns a
     * <code>Stream&lt;R&gt;</code>. This is useful in the <code>Stream.flatMap()</code> method. For example, let's say
     * you want to retrieve all the line items from all of a customer's orders, assuming that
     * <code>Order.getLineItems()</code> returns an array of OrderLineItem:
     * <pre>
     *     private List&lt;OrderLineItem&gt; getCustomerLineItems(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMap(MapperUtils.flatArrayMapper(Order::getLineItems))
     *             .collect(Collectors.toList());
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         return customerOrders.stream()
     *             .flatMap(flatArrayMapper(Order::getLineItems))
     *             .collect(toList());
     * </pre>
     *
     * @param function A function that returns an array of type &lt;R&gt;.
     * @param <T>      The type of the target element on which a Function is to be called to provide an array.
     * @param <R>      The type of the element of an array returned by the given function.
     * @return A Function that, given a target element of type &lt;T&gt;, returns a Stream of type &lt;R&gt;. Returns an
     * empty Stream if the target element is null, or the array returned by the given function is null.
     */
    public static <T, R> Function<T, Stream<R>> flatArrayMapper(Function<? super T, ? extends R[]> function) {
        return t -> t == null ? Stream.empty() : defaultStream(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;U&gt;, this
     * method builds a <code>Function</code> that takes the same argument type and returns a value of type
     * <code>Pair&lt;T, U&gt;</code>. This pair will consist of the target element itself, and a value returned by the
     * passed <code>rightFunction</code>.
     *
     * @param rightFunction A Function to extract the right value in the Pair&lt;T, U&gt; to be returned by the Function
     *                      built by this method.
     * @param <T>           The type of the target element on which the rightFunction is to be called.
     * @param <U>           The type of the right element of the Pair to be returned by the Function built by this
     *                      method.
     * @return A Function that, given a target element of type &lt;T&gt;, returns a Pair of the target element, along
     * with a value returned by the passed rightFunction.
     */
    public static <T, U> Function<T, Pair<T, U>> pairOf(Function<? super T, ? extends U> rightFunction) {
        return pairOf(identity(), rightFunction);
    }

    /**
     * Given an object consisting of a pair of functions, one that takes an element of type &lt;T&gt; and returns a
     * value of type &lt;U&gt;, and the other that takes an element of type &lt;T&gt; and returns a value of type
     * &lt;V&gt;, this method builds a <code>Function</code> that takes the same argument type and returns a value of
     * type <code>Pair&lt;U, V&gt;</code>. This Pair will consist of the values returned by each of the functions in the
     * passed <code>keyValueMapper</code>. This method does the same thing as the overload that takes a
     * <code>leftFunction</code> and <code>rightFunction</code>, and is included as a convenience when a method already
     * takes a <code>KeyValueMapper</code>. For example, the implementation of the
     * {@link TransformUtils#transformToMap(Collection, KeyValueMapper)} method is:
     * <pre>
     *     public static &lt;T, K, V&gt; Map&lt;K, V&gt; transformToMap(Collection&lt;T&gt; objects, KeyValueMapper&lt;T, K, V&gt; keyValueMapper) {
     *         return defaultStream(objects)
     *             .map(pairOf(keyValueMapper))
     *             .collect(toMapFromEntry());
     *     }
     * </pre>
     * This works because the {@link Pair} object implements the Java <code>Map.Entry</code> interface.
     *
     * @param keyValueMapper An object consisting of a pair of functions that will be used to retrieve a left and right
     *                       value for a Pair that is a result of the Function built by this method.
     * @param <T>            The type of the target element on which a pair of functions, represented by the passed
     *                       keyValueMapper, will be called.
     * @param <U>            The type of the left element of the Pair to be returned by the Function built by this
     *                       method.
     * @param <V>            The type of the right element of the Pair to be returned by the Function built by this
     *                       method.
     * @return A Function that, given a target element of type &lt;T&gt;, returns a Pair whose values will be retrieved
     * by a pair of functions represented by the passed keyValueMapper.
     */
    public static <T, U, V> Function<T, Pair<U, V>> pairOf(KeyValueMapper<T, U, V> keyValueMapper) {
        return pairOf(keyValueMapper.getKeyMapper(), keyValueMapper.getValueMapper());
    }

    /**
     * Given a pair of functions, one that takes an element of type &lt;T&gt; and returns a value of type &lt;U&gt;, and
     * the other that takes an element of type &lt;T&gt; and returns a value of type &lt;V&gt;, this method builds a
     * <code>Function</code> that takes the same argument type and returns a value of type
     * <code>Pair&lt;U, V&gt;</code>. This Pair will consist of the values returned by each of the functions passed to
     * this method.
     *
     * @param leftFunction  A Function that will be used to retrieve a left value for a Pair that is a result of the
     *                      Function built by this method.
     * @param rightFunction A Function that will be used to retrieve a right value for a Pair that is a result of the
     *                      Function built by this method.
     * @param <T>           The type of the target element on which a pair of passed functions will be called.
     * @param <U>           The type of the left element of the Pair to be returned by the Function built by this
     *                      method.
     * @param <V>           The type of the right element of the Pair to be returned by the Function built by this
     *                      method.
     * @return A Function that, given a target element of type &lt;T&gt;, returns a Pair whose values will be retrieved
     * by a pair of functions passed to this method.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T, U, V> Function<T, Pair<U, V>> pairOf(Function<T, U> leftFunction, Function<? super T, ? extends V> rightFunction) {
        return t -> Pair.of(mapper(leftFunction).apply(t), mapper(rightFunction).apply(t));
    }

    /**
     * Given a <code>List&lt;R&gt;</code>, this methods builds a <code>Function</code> that returns a
     * <code>Pair&lt;T, R&gt;</code>. It is intended to be used in a stream. The <code>Pair&lt;T, R&gt;</code> built by
     * this <code>Function</code> will consist of a target element, and an object of type &lt;R&gt; whose element in the
     * passed <code>List</code> is associated with the current element, in encounter order. This is most useful in a
     * situation where an ordered collection is being streamed. The function returned from this method is <i>not</i>
     * intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the collection being streamed, the extra elements are
     * ignored. If it has fewer elements, any target elements being streamed that do not have associated values in the
     * list will be paired with a <code>null</code> value.
     *
     * @param pairedList A List whose elements are to be paired with collection elements being streamed, by the Function
     *                   built by this method.
     * @param <T>        The type of the target element being streamed.
     * @param <R>        The type of the elements in the passed pairedList parameter.
     * @return A Function that, given an element of type &lt;T&gt;, will return a Pair of that element, along with an
     * associated element from the passed pairedList.
     */
    public static <T, R> Function<T, Pair<T, R>> pairWith(List<R> pairedList) {
        return pairWith(identity(), pairedList);
    }

    /**
     * Given a <code>Function&lt;T, U&gt;</code>, and a <code>List&lt;V&gt;</code>, this method builds a
     * <code>Function</code> that takes an element of type &lt;T&gt;, and returns a <code>Pair&lt;U, V&gt;</code>. It is
     * intended to be used in a stream. The <code>Pair&lt;U, V&gt;</code> built by this <code>Function</code> will
     * consist of an element returned by the passed <code>function</code>, and an object of type &lt;V&gt; whose element
     * in the passed <code>List</code> is associated with the current element, in encounter order. This is most useful
     * in a situation where an ordered collection is being streamed. The function returned from this method is
     * <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the
     * collection being streamed, the extra elements are ignored. If it has fewer elements, any values returned by the
     * passed <code>function</code>, that do not have associated values in the list, will be paired with a
     * <code>null</code> value.
     *
     * @param function   A Function that will return a value of type &lt;U&gt;, which will become the left element in a
     *                   Pair, returned by the Function built by this method.
     * @param pairedList A List whose elements are to be paired with elements retrieved by the passed function.
     * @param <T>        The type of the target element being streamed.
     * @param <U>        The type of the left element, retrieved by the passed function.
     * @param <V>        The type of the right element, retrieved from the passed List.
     * @return A Function that, given an element of type &lt;T&gt;, will return a Pair of a value retrieved from the
     * passed function, along with an associated element from the passed pairedList.
     */
    public static <T, U, V> Function<T, Pair<U, V>> pairWith(Function<? super T, ? extends U> function, List<V> pairedList) {
        List<V> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return t -> {
            U extracted = t == null ? null : function.apply(t);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    /**
     * Builds a <code>Function</code> that, given an element of type &lt;T&gt;, returns an object that represents a pair
     * of values, one being the element itself, and the other a primitive zero-based index of the object in encounter
     * order. The <code>Function</code> built by this method is intended to be used in a stream, and is most useful in a
     * situation where an ordered collection is being streamed. It is <i>not</i> intended to be used with parallel
     * streams.
     *
     * @param <T> The type of the target elements being streamed.
     * @return A Function that takes an element of type &lt;T&gt;, and returns an object representing a pair of the
     * element itself, along with the primitive zero-based int index of the element.
     */
    public static <T> Function<T, ObjectIndexPair<T>> pairWithIndex() {
        return pairWithIndex(identity());
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this
     * method builds a <code>Function</code> that, given an element of type &lt;T&gt;, returns an object that represents
     * a pair of values, one being a value returned from the passed <code>function</code>, and the other a primitive
     * zero-based index of the object in encounter order. The <code>Function</code> built by this method is intended to
     * be used in a stream, and is most useful in a situation where an ordered collection is being streamed. It is
     * <i>not</i> intended to be used with parallel streams.
     *
     * @param function A Function that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;.
     * @param <T>      The type of the target elements being streamed.
     * @param <R>      The type of a value retrieved from the passed function.
     * @return A Function that takes an element of type &lt;T&gt;, and returns an object representing a pair of a value
     * returned from the passed function, along with the primitive zero-based int index of the target element.
     */
    public static <T, R> Function<T, ObjectIndexPair<R>> pairWithIndex(Function<? super T, ? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> ObjectIndexPair.of(function.apply(t), idx.getAndIncrement());
    }

    /**
     * Given a Predicate&lt;T&gt; and an object consisting of a pair of functions, each taking an element of type
     * &lt;T&gt; and returning a value of type &lt;R&gt;, one to return a value if the predicate is true, the other
     * returning an alternate value if the predicate is false, this method builds a <code>Function</code> that
     * evaluates the predicate and returns a value produced by one or the other of the pair. For example, suppose you
     * have a collection of objects that represent nodes in a tree. The nodes each represent a folder or document in a
     * hierarchy. You want to collect a set of only the root node ids:
     * <pre>
     *     private Set&lt;String&gt; getRootNodeIds(Collection&lt;TreeNode&gt; treeNodes) {
     *         return treeNodes.stream()
     *             .map(MapperUtils.ternary(TreeNode::isRoot, MapperUtils.trueFalseMappers(TreeNode::getId, TreeNode::getRootId)))
     *             .collect(Collectors.toSet());
     *     }
     * </pre>
     * Or, using static imports:
     * <pre>
     *     private Set&lt;String&gt; getRootNodeIds(Collection&lt;TreeNode&gt; treeNodes) {
     *         return treeNodes.stream()
     *             .map(ternary(TreeNode::isRoot, trueFalseMappers(TreeNode::getId, TreeNode::getRootId)))
     *             .collect(Collectors.toSet());
     *     }
     * </pre>
     *
     * @param predicate     A predicate to be called on an element of type &lt;T&gt;.
     * @param ternaryMapper An object consisting of a pair of functions, one to return a value if the passed predicate
     *                      evaluates to true, and the other to return an alternate value if it evaluates to false.
     * @param <T>           The type of the target element for the passed predicate.
     * @param <R>           The type of the values returned by the pair of functions in the passed trueFalseMappers.
     * @return A value of type &lt;R&gt; returned by one or the other of a pair of functions, depending on whether the
     * passed predicate evaluates to true or false.
     */
    public static <T, R> Function<T, R> ternary(Predicate<T> predicate, TrueFalseMappers<T, R> ternaryMapper) {
        return t -> t != null && predicate.test(t) ? ternaryMapper.getTrueMapper().apply(t) : ternaryMapper.getFalseMapper().apply(t);
    }

    /**
     * Builds an object representing a pair of functions, one to return a value if a predicate evaluates to true, the
     * other to return an alternate value if it evaluates to false. This method is meant to be used to build the second
     * argument to the {@link #ternary(Predicate, TrueFalseMappers)} method.
     *
     * @param trueExtractor  Retrieves a value to be returned by the {@link #ternary(Predicate, TrueFalseMappers)}
     *                       method when its predicate evaluates to true.
     * @param falseExtractor Retrieves a value to be returned by the {@link #ternary(Predicate, TrueFalseMappers)}
     *                       method when its predicate evaluates to false.
     * @param <T>            The type of the target element on which the extractor methods below are to be called.
     * @param <R>            The type of the value to be returned by the extractor methods below.
     * @return An object representing a pair of functions, one to be called if a predicate evaluates to true, the other
     * to be called if it evaluates to false.
     */
    public static <T, R> TrueFalseMappers<T, R> trueFalseMappers(Function<T, R> trueExtractor, Function<T, R> falseExtractor) {
        return TrueFalseMappers.of(trueExtractor, falseExtractor);
    }

    /**
     * Builds an object representing a pair of functions, one to return a key in a <code>Map</code>, and the other to
     * return its associated value. This method is meant to be used to build the second parameter to the
     * {@link TransformUtils#transformToMap(Collection, KeyValueMapper)} method.
     *
     * @param keyMapper   Function to retrieve a value to be used as a key in a Map
     * @param valueMapper Function to retrieve a value associated with a key in a Map
     * @param <T>         The type of the target element on which the functions below are to be called.
     * @param <K>         The type of a key value for a Map.
     * @param <V>         The type of a value to be associated with a key in a Map.
     * @return Builds an object representing a pair of functions, one to retrieve a Map key, and the other to retrieve
     * its associated value.
     */
    public static <T, K, V> KeyValueMapper<T, K, V> keyValueMapper(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return KeyValueMapper.of(keyMapper, valueMapper);
    }
}
