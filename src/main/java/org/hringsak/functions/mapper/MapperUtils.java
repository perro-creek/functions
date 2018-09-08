package org.hringsak.functions.mapper;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

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
     * The <code>Function.andThen()</code> method can only be called on the method reference because of the cast.
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
     *     private Collection&lt;OrderLineItem&gt; buildOrderLineItems(Collection&lt;LineItemRequest&gt; requestLineItems, String orderId) {
     *         return Collection&lt;OrderLineItem&gt; orderLineItems = requestLineItems.stream()
     *             .map(MapperUtils.mapper(this::createOrderLineItem, orderId))
     *             .collect(toList());
     *     }
     *
     *     private OrderLineItem createOrderLineItem(RequestLineItem request, String orderId) {
     *         ...
     *     }
     * </pre>
     * By making judicious use of static imports, we can reduce the clutter in the above stream to:
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
     * By making judicious use of static imports, we can reduce the clutter in the above stream to:
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

    public static <T, U, R> Function<T, R> mapper(Function<? super T, ? extends U> left, Function<? super U, ? extends R> right) {
        return t -> right.apply(left.apply(t));
    }

    public static <T, U, R> Function<T, R> getValue(Map<U, R> map, Function<T, U> extractor) {
        return t -> (map == null || t == null) ? null : map.get(extractor.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> streamOf(Function<? super T, ? extends R> mapper) {
        return t -> t == null ? Stream.empty() : Stream.of(mapper.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> flatMapper(Function<? super T, ? extends Collection<R>> mapper) {
        return t -> t == null ? Stream.empty() : defaultStream(mapper.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> flatArrayMapper(Function<? super T, ? extends R[]> mapper) {
        return t -> t == null ? Stream.empty() : defaultStream(mapper.apply(t));
    }

    public static <T, U> Function<T, Pair<T, U>> pairOf(Function<? super T, ? extends U> rightFunction) {
        return pairOf(identity(), rightFunction);
    }

    @SuppressWarnings("WeakerAccess")
    public static <T, U, V> Function<T, Pair<U, V>> pairOf(Function<T, U> leftFunction, Function<? super T, ? extends V> rightFunction) {
        return t -> Pair.of(leftFunction.apply(t), rightFunction.apply(t));
    }

    public static <T, R> Function<T, Pair<T, R>> pairWith(List<R> pairedList) {
        return pairWith(identity(), pairedList);
    }

    public static <T, U, V> Function<T, Pair<U, V>> pairWith(Function<? super T, ? extends U> function, List<V> pairedList) {
        List<V> nonNullList = ListUtils.emptyIfNull(pairedList);
        MutableInt idx = new MutableInt();
        return t -> {
            U extracted = t == null ? null : function.apply(t);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    public static <T> Function<T, Pair<T, Integer>> pairWithIndex() {
        return pairWithIndex(identity());
    }

    public static <T, R> Function<T, Pair<R, Integer>> pairWithIndex(Function<? super T, ? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> Pair.of(function.apply(t), idx.getAndIncrement());
    }

    public static <T, R> Function<T, R> ternary(Predicate<T> predicate, TernaryMapper<T, R> ternaryMapper) {
        return t -> t != null && predicate.test(t) ? ternaryMapper.getTrueMapper().apply(t) : ternaryMapper.getFalseMapper().apply(t);
    }

    public static <T, R> TernaryMapper<T, R> ternaryMapper(Function<T, R> trueExtractor, Function<T, R> falseExtractor) {
        return TernaryMapper.of(trueExtractor, falseExtractor);
    }

    public static <T, K, V> KeyValueMapper<T, K, V> keyValueMapper(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return KeyValueMapper.of(keyMapper, valueMapper);
    }
}
