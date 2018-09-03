package org.hringsak.functions;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hringsak.functions.StreamUtils.defaultDoubleStream;
import static org.hringsak.functions.StreamUtils.defaultIntStream;
import static org.hringsak.functions.StreamUtils.defaultLongStream;
import static org.hringsak.functions.StreamUtils.defaultStream;

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
     * @param mapper A method reference to be cast to a Function.
     * @param <T>    The type of the single parameter to the Function.
     * @param <R>    The type of the result of the Function.
     * @return A method reference cast to a Function.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T, R> Function<T, R> mapper(Function<T, R> mapper) {
        return mapper;
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
     * @param mapper       A method reference which takes a single parameter of type &lt;T&gt;, and returns a value of type
     *                     &lt;R&gt;.
     * @param defaultValue A default value of type &lt;R&gt;, to be returned in case the target element, or the result
     *                     of the Function call on the target element is null.
     * @param <T>          The type of the target element on which the mapper Function is to be called.
     * @param <R>          The type of the result to be returned from the mapper Function built by this method.
     * @return A mapper Function that returns a default value if the target element, or the result of the Function call
     * on the target element is null.
     */
    public static <T, R> Function<T, R> mapperDefault(Function<? super T, ? extends R> mapper, R defaultValue) {
        return t -> (t == null || mapper.apply(t) == null) ? defaultValue : mapper.apply(t);
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
     * As in the {@link #mapper(BiFunction, Object)} method, builds a <code>Function</code> from a passed <code>
     * BiFunction</code>, which can be very useful in the common situation where you are streaming through a collection
     * elements, and have a method to call that takes two parameters. In the <code>BiConsumer</code> passed to this
     * method, the parameters are basically the same as in {@link #mapper(BiFunction, Object)}, but in the inverse
     * order. Here, the first parameter is a constant value that will be passed to all invocations of the method, and
     * the second parameter is the target element on which you are streaming. This would typically be called from within
     * a chain of method calls based on a <code>Stream</code>. In the following example, assume the <code>
     * LineItemRequest</code> objects come in from a rest API call, and we want to transform them into a collection of
     * <code>OrderLineItem</code> objects:
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

    @SuppressWarnings("unused")
    public static <R> DoubleFunction<R> doubleMapper(DoubleFunction<R> function) {
        return function;
    }

    public static <U, R> DoubleFunction<R> doubleMapper(BiFunction<Double, ? super U, ? extends R> biFunction, U value) {
        return d -> biFunction.apply(d, value);
    }

    public static <U, R> DoubleFunction<R> inverseDoubleMapper(BiFunction<? super U, Double, ? extends R> biFunction, U value) {
        return d -> biFunction.apply(value, d);
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt;</code> and returns void, to a
     * <code>ToDoubleFunction</code>. Everything said about the {@link #mapper(Function)} method applies here. The
     * difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a primitive <code>
     * double</code> instead. It may be harder to think of a situation where this overload would be useful, but
     * this method is included for sake of completeness.
     *
     * @param function
     * @param <T>
     * @return
     */
    @SuppressWarnings("unused")
    public static <T> ToDoubleFunction<T> toDoubleMapper(ToDoubleFunction<T> function) {
        return function;
    }

    public static <T> ToDoubleFunction<T> toDoubleMapperDefault(ToDoubleFunction<? super T> function, double defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsDouble(t);
    }

    public static <T, U> ToDoubleFunction<T> toDoubleMapper(ToDoubleBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsDouble(t, value);
    }

    public static <T, U> ToDoubleFunction<T> inverseToDoubleMapper(ToDoubleBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsDouble(value, t);
    }

    @SuppressWarnings("unused")
    public static <R> IntFunction<R> intMapper(IntFunction<R> function) {
        return function;
    }

    public static <U, R> IntFunction<R> intMapper(BiFunction<Integer, ? super U, ? extends R> biFunction, U value) {
        return i -> biFunction.apply(i, value);
    }

    public static <U, R> IntFunction<R> inverseIntMapper(BiFunction<? super U, Integer, ? extends R> biFunction, U value) {
        return i -> biFunction.apply(value, i);
    }

    @SuppressWarnings("unused")
    public static <T> ToIntFunction<T> toIntMapper(ToIntFunction<T> function) {
        return function;
    }

    public static <T> ToIntFunction<T> toIntMapperDefault(ToIntFunction<? super T> function, int defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsInt(t);
    }

    public static <T, U> ToIntFunction<T> toIntMapper(ToIntBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsInt(t, value);
    }

    public static <T, U> ToIntFunction<T> inverseToIntMapper(ToIntBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsInt(value, t);
    }

    @SuppressWarnings("unused")
    public static <R> LongFunction<R> longMapper(LongFunction<R> function) {
        return function;
    }

    public static <U, R> LongFunction<R> longMapper(BiFunction<Long, ? super U, ? extends R> biFunction, U value) {
        return l -> biFunction.apply(l, value);
    }

    public static <U, R> LongFunction<R> inverseLongMapper(BiFunction<? super U, Long, ? extends R> biFunction, U value) {
        return l -> biFunction.apply(value, l);
    }

    @SuppressWarnings("unused")
    public static <T> ToLongFunction<T> toLongMapper(ToLongFunction<T> function) {
        return function;
    }

    public static <T> ToLongFunction<T> toLongMapperDefault(ToLongFunction<? super T> function, long defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsLong(t);
    }

    public static <T, U> ToLongFunction<T> toLongMapper(ToLongBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsLong(t, value);
    }

    public static <T, U> ToLongFunction<T> inverseToLongMapper(ToLongBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsLong(value, t);
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

    public static <T> Function<T, DoubleStream> flatDoubleMapper(Function<? super T, ? extends Collection<Double>> doubleMapper) {
        return t -> t == null ? DoubleStream.empty() : defaultDoubleStream(doubleMapper.apply(t));
    }

    public static <T> Function<T, DoubleStream> flatDoubleArrayMapper(Function<? super T, ? extends double[]> doubleMapper) {
        return t -> t == null ? DoubleStream.empty() : defaultDoubleStream(doubleMapper.apply(t));
    }

    public static <T> Function<T, IntStream> flatIntMapper(Function<? super T, ? extends Collection<Integer>> intMapper) {
        return t -> t == null ? IntStream.empty() : defaultIntStream(intMapper.apply(t));
    }

    public static <T> Function<T, IntStream> flatIntArrayMapper(Function<? super T, ? extends int[]> intMapper) {
        return t -> t == null ? IntStream.empty() : defaultIntStream(intMapper.apply(t));
    }

    public static <T> Function<T, LongStream> flatLongMapper(Function<? super T, ? extends Collection<Long>> longMapper) {
        return t -> t == null ? LongStream.empty() : defaultLongStream(longMapper.apply(t));
    }

    public static <T> Function<T, LongStream> flatLongArrayMapper(Function<? super T, ? extends long[]> longMapper) {
        return t -> t == null ? LongStream.empty() : defaultLongStream(longMapper.apply(t));
    }

    public static <T, U, V> Function<T, Pair<U, V>> pairOf(Function<T, U> leftFunction, Function<? super T, ? extends V> rightFunction) {
        return t -> Pair.of(leftFunction.apply(t), rightFunction.apply(t));
    }

    public static <T, R> Function<T, Pair<T, R>> pairWith(List<R> pairedList) {
        return pairWith(Function.identity(), pairedList);
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
        return pairWithIndex(Function.identity());
    }

    public static <T, R> Function<T, Pair<R, Integer>> pairWithIndex(Function<? super T, ? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> Pair.of(function.apply(t), idx.getAndIncrement());
    }

    public static <T, R> Function<T, R> ternary(Predicate<T> predicate, Pair<Function<T, R>, Function<T, R>> extractorPair) {
        return t -> t != null && predicate.test(t) ? extractorPair.getLeft().apply(t) : extractorPair.getRight().apply(t);
    }

    public static <T, R> Pair<Function<T, R>, Function<T, R>> extractors(Function<T, R> trueExtractor, Function<T, R> falseExtractor) {
        return Pair.of(trueExtractor, falseExtractor);
    }
}
