package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;
import org.perro.functions.stream.LongStreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;

import static org.perro.functions.stream.LongStreamUtils.defaultLongStream;

/**
 * Methods that build functions to map a target element into another result. This class deals specifically with mapper
 * functions involving primitive <code>long</code> types.
 */
public final class LongMapperUtils {

    private LongMapperUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>long</code> and returns &lt;R&gt;,
     * to a <code>LongFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)} method applies
     * here. The difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a
     * primitive <code>long</code> instead. This method might be useful in a situation where you have a
     * <code>LongStream</code>, and the <code>LongStream.mapToObj(LongFunction mapper)</code> method is called to
     * convert the primitive to some generic type of object, converting the <code>LongStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #toLongMapper(ToLongFunction)} is that the
     * <code>LongFunction</code> built from this method takes a <code>long</code> and returns a generic type, where the
     * <code>ToLongFunction</code> built from {@link #toLongMapper(ToLongFunction)} takes a generic type and returns a
     * <code>long</code>.
     *
     * @param function A method reference to be cast to an LongFunction.
     * @param <R>      The type of the result of the LongFunction built by this method.
     * @return A method reference cast to an LongFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <R> LongFunction<R> longMapper(LongFunction<R> function) {
        return function;
    }

    /**
     * Builds a <code>LongFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of an element
     * of type &lt;T&gt; being streamed through, it would be a primitive <code>long</code> instead. This method might be
     * useful in a situation where you have a <code>LongStream</code>, and the
     * <code>LongStream.mapToObj(LongFunction mapper)</code> method is called to convert the primitive to some generic
     * type of object, converting the <code>LongStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #toLongMapper(ToLongBiFunction, Object)} is that the
     * <code>LongFunction</code> built from this method takes a <code>long</code> and returns a generic type, where the
     * <code>ToLongFunction</code> built from {@link #toLongMapper(ToLongBiFunction, Object)} takes a generic type and
     * returns a <code>long</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type long, and
     *                   the second of type &lt;U&gt;, which can be any type. The method reference will be converted by
     *                   this method to a LongFunction, taking a single parameter of type long. Behind the scenes, this
     *                   BiFunction will be called, passing the constant value to each invocation as the second
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the LongFunction built by this method.
     * @return A LongFunction taking a single parameter of type long, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> LongFunction<R> longMapper(BiFunction<Long, ? super U, ? extends R> biFunction, U value) {
        return l -> biFunction.apply(l, value);
    }

    /**
     * Builds a <code>LongFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of an
     * element of type &lt;T&gt; being streamed through, it would be a primitive <code>long</code> instead. This method
     * might be useful in a situation where you have a <code>LongStream</code>, and the
     * <code>LongStream.mapToObj(LongFunction mapper)</code> method is called to convert the primitive to some generic
     * type of object, converting the <code>LongStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #inverseToLongMapper(ToLongBiFunction, Object)} is that
     * the <code>LongFunction</code> built from this method takes a <code>long</code> and returns a generic type, where
     * the <code>ToLongFunction</code> built from {@link #inverseToLongMapper(ToLongBiFunction, Object)} takes a generic
     * type and returns a <code>long</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type &lt;U&gt;
     *                   which can be any type, and the second of type long. The method reference will be converted by
     *                   this method to an LongFunction, taking a single parameter of type long. Behind the scenes, this
     *                   biFunction will be called, passing the constant value to each invocation as the first
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the LongFunction built by this method.
     * @return A LongFunction taking a single parameter of type long, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> LongFunction<R> inverseLongMapper(BiFunction<? super U, Long, ? extends R> biFunction, U value) {
        return l -> biFunction.apply(value, l);
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt; and returns <code>long</code>,
     * to a <code>ToLongFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)} method applies
     * here. The difference is that instead of returning a result of a generic object type, it returns a primitive
     * <code>long</code> instead. This method might be useful in a situation where you have a <code>Stream</code> of a
     * generic object type, and the <code>mapToLong(ToLongFunction mapper)</code> method is called to convert the object
     * to a primitive <code>long</code>, converting the stream to a <code>LongStream</code>.
     * <p>
     * Note that the difference between this method and {@link #longMapper(LongFunction)} is that the
     * <code>ToLongFunction</code> built from this method takes a generic type and returns a <code>long</code>, where
     * the <code>LongFunction</code> built from {@link #longMapper(LongFunction)} takes a <code>long</code> and returns
     * a generic type.
     *
     * @param function A method reference to be cast to a ToLongFunction.
     * @param <T>      The type of the single parameter to the ToLongFunction.
     * @return A method reference cast to a ToLongFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T> ToLongFunction<T> toLongMapper(ToLongFunction<T> function) {
        return function;
    }

    /**
     * Builds a mapper <code>ToLongFunction</code> that, if the target element is <code>null</code>, or the result of
     * the <code>Function</code> call on the target element is <code>null</code>, then the passed default value is
     * returned. Everything said about the {@link MapperUtils#mapperDefault(Function, Object)} method applies here. The
     * difference is that instead of returning a result of a generic object type, it returns a primitive
     * <code>long</code> instead. This method might be useful in a situation where you have a <code>Stream</code> of a
     * generic object type, and the <code>mapToLong(ToLongFunction mapper)</code> method is called to convert the object
     * to a primitive <code>long</code>, converting the stream to a <code>LongStream</code>.
     *
     * @param function     A method reference which takes a single parameter of type &lt;T&gt;, and returns a value of
     *                     type long.
     * @param defaultValue A default value of type long, to be returned in case the target element, or the result of
     *                     the ToLongFunction call on the target element is null.
     * @param <T>          The type of the target element on which the mapper ToLongFunction is to be called.
     * @return A ToLongFunction taking a single parameter of type &lt;T&gt;, and returning a result of type long.
     */
    public static <T> ToLongFunction<T> toLongMapperDefault(ToLongFunction<? super T> function, long defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsLong(t);
    }

    /**
     * Builds a <code>ToLongFunction</code> from a passed <code>ToLongBiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of returning a
     * result of a generic object type, it returns a primitive <code>long</code> instead. This method might be useful in
     * a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToLong(ToLongFunction mapper)</code> method is called to convert the object to a primitive
     * <code>long</code>, converting the stream to a <code>LongStream</code>.
     * <p>
     * Note that the difference between this method and {@link #longMapper(BiFunction, Object)} is that the
     * <code>ToLongFunction</code> built from this method takes a generic type and returns a <code>long</code>, where
     * the <code>LongFunction</code> built from {@link #longMapper(BiFunction, Object)} takes a <code>long</code> and
     * returns a generic type.
     *
     * @param biFunction A method reference which is a ToLongBiFunction, taking two parameters - the first of type
     *                   &lt;T&gt;, and the second of type &lt;U&gt;, both of which can be any type. The method
     *                   reference will be converted by this method to a ToLongFunction, taking a single parameter of
     *                   type &lt;T&gt;. Behind the scenes, this ToLongBiFunction will be called, passing the constant
     *                   value to each invocation as the second parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToLongFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @return A ToLongFunction taking a single parameter of type &lt;T&gt;, and returning a result of type long.
     */
    public static <T, U> ToLongFunction<T> toLongMapper(ToLongBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsLong(t, value);
    }

    /**
     * Builds a <code>ToLongFunction</code> from a passed <code>ToLongBiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of
     * returning a result of a generic object type, it returns a primitive <code>long</code> instead. This method might
     * be useful in a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToLong(ToLongFunction mapper)</code> method is called to convert the object to a primitive
     * <code>long</code>, converting the stream to a <code>LongStream</code>.
     * <p>
     * Note that the difference between this method and {@link #inverseLongMapper(BiFunction, Object)} is that the
     * <code>ToLongFunction</code> built from this method takes a generic type and returns a <code>long</code>, where
     * the <code>LongFunction</code> built from {@link #inverseLongMapper(BiFunction, Object)} takes a <code>long</code>
     * and returns a generic type.
     *
     * @param biFunction A method reference which is a ToLongBiFunction, taking two parameters - the first of type
     *                   &lt;U&gt; which can be any type, and the second of type long. The method reference will be
     *                   converted by this method to a ToLongFunction, taking a single parameter of type &lt;T&gt;.
     *                   Behind the scenes, this biFunction will be called, passing the constant value to each
     *                   invocation as the first parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToLongFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @return A ToLongFunction taking a single parameter of type &lt;T&gt;, and returning a result of type long.
     */
    public static <T, U> ToLongFunction<T> inverseToLongMapper(ToLongBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsLong(value, t);
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>long</code> array, this method builds a
     * <code>LongFunction</code> that returns a <code>LongStream</code>. This is useful in the
     * <code>LongStream.flatMap()</code> method. For a very contrived example, let's say you have a method,
     * <code>MathUtils.getFactors(long product)</code>, that takes a long value and returns a <code>long</code> array
     * containing the factors of that number. You have lower and upper bound long values to create a range, and you want
     * to sum the factors of all of the individual long values:
     * <pre>
     *     private long getSumOfAllFactors(long startInclusive, long endExclusive) {
     *         return LongStream.range(startInclusive, endExclusive)
     *             .flatMap(LongMapperUtils.longFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private long getSumOfAllFactors(long startInclusive, long endExclusive) {
     *         return LongStream.range(startInclusive, endExclusive)
     *             .flatMap(longFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     *
     * @param longMapper A LongFunction that returns an array of longs.
     * @return A LongFunction that returns a LongStream. Returns an empty LongStream if the long array returned by the
     * given longMapper is null or empty.
     */
    public static LongFunction<LongStream> longFlatMapper(LongFunction<? extends long[]> longMapper) {
        return l -> defaultLongStream(longMapper.apply(l));
    }

    /**
     * Given a <code>Function</code> that takes an argument of type &lt;T&gt; and returns a <code>long</code> array, this
     * method builds a <code>Function</code> that takes the same argument, but returns a <code>LongStream</code>. This is
     * useful in the <code>Stream.flatMapToLong()</code> method. For example, let's say you have a collection of
     * objects representing all the orders for a particular customer. You want to total the quantities for each of the
     * line items in all of the orders contained in the collection:
     * <pre>
     *     private long getTotalLineItemQuantities(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMapToLong(LongMapperUtils.flatMapperToLong(this::getAllQuantities))
     *             .sum();
     *     }
     *
     *     private long[] getAllQuantities(Order order) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private long getTotalLineItemQuantities(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMapToLong(flatMapperToLong(this::getAllQuantities))
     *             .sum();
     *     }
     * </pre>
     *
     * @param toLongArrayMapper A Function taking an argument of type &lt;T&gt;, that returns an array of longs.
     * @param <T>               The type of the argument to be passed to the given toLongArrayMapper function.
     * @return A Function taking an argument of type &lt;T&gt;, that returns a LongStream. Returns an empty LongStream
     * if the passed argument is null, or the long array returned by the given toLongArrayMapper function is null or
     * empty.
     */
    public static <T> Function<T, LongStream> flatMapperToLong(Function<? super T, ? extends long[]> toLongArrayMapper) {
        return t -> t == null ? LongStream.empty() : defaultLongStream(toLongArrayMapper.apply(t));
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;U&gt;, this method builds a
     * <code>LongFunction</code> that returns a value of type <code>LongObjectPair&lt;U&gt;</code>. This pair will
     * consist of the target long itself, and a value returned by the passed <code>rightFunction</code>.
     *
     * @param rightFunction A LongFunction to extract the right value in the LongObjectPair&lt;U&gt; to be returned
     *                      by the LongFunction built by this method.
     * @param <U>           The type of the right element of the LongObjectPair to be returned by the LongFunction
     *                      built by this method.
     * @return A LongFunction that returns a LongObjectPair of the target long, along with a value returned by the
     * passed rightFunction.
     */
    public static <U> LongFunction<LongObjectPair<U>> longPairOf(LongFunction<? extends U> rightFunction) {
        return l -> LongObjectPair.of(l, rightFunction.apply(l));
    }

    /**
     * Given an object consisting of a pair of long functions, one that returns a value of type &lt;U&gt;, and the other
     * that returns a value of type &lt;V&gt;, this method builds a <code>LongFunction</code> that returns a value of
     * type <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the functions in the
     * passed <code>keyValueMapper</code>. This method does the same thing as the overload that takes a
     * <code>leftFunction</code> and <code>rightFunction</code>, and is included as a convenience when a method already
     * takes a <code>LongKeyValueMapper</code>. For example, the implementation of the
     * {@link LongTransformUtils#longTransformToMap(long[], LongKeyValueMapper)} method is:
     * <pre>
     *     public static &lt;K, V&gt; Map&lt;K, V&gt; longTransformToMap(long[] longs, LongKeyValueMapper&lt;K, V&gt; keyValueMapper) {
     *         return defaultLongStream(longs)
     *             .mapToObj(longPairOf(keyValueMapper))
     *             .collect(toMapFromEntry());
     *     }
     * </pre>
     * This works because the {@link Pair} object implements the Java <code>Map.Entry</code> interface.
     *
     * @param keyValueMapper An object consisting of a pair of long functions that will be used to retrieve a left and
     *                       right value for a Pair that is a result of the LongFunction built by this method.
     * @param <U>            The type of the left element of the Pair to be returned by the LongFunction built by this
     *                       method.
     * @param <V>            The type of the right element of the Pair to be returned by the LongFunction built by this
     *                       method.
     * @return A LongFunction that returns a Pair whose values will be retrieved by a pair of long functions represented
     * by the passed keyValueMapper.
     */
    public static <U, V> LongFunction<Pair<U, V>> longPairOf(LongKeyValueMapper<U, V> keyValueMapper) {
        return longPairOf(keyValueMapper.getKeyMapper(), keyValueMapper.getValueMapper());
    }

    /**
     * Given a pair of long functions, one that returns a value of type &lt;U&gt;, and the other that returns a value of
     * type &lt;V&gt;, this method builds a <code>LongFunction</code> that returns a value of type
     * <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the long functions passed
     * to this method.
     *
     * @param leftFunction  A LongFunction that will be used to retrieve a left value for a Pair that is a result of
     *                      the LongFunction built by this method.
     * @param rightFunction A LongFunction that will be used to retrieve a right value for a Pair that is a result of
     *                      the LongFunction built by this method.
     * @param <U>           The type of the left element of the Pair to be returned by the LongFunction built by this
     *                      method.
     * @param <V>           The type of the right element of the Pair to be returned by the LongFunction built by this
     *                      method.
     * @return A LongFunction that returns a Pair whose values will be retrieved by a pair of long functions passed to
     * this method.
     */
    public static <U, V> LongFunction<Pair<U, V>> longPairOf(LongFunction<? extends U> leftFunction, LongFunction<? extends V> rightFunction) {
        return l -> Pair.of(leftFunction.apply(l), rightFunction.apply(l));
    }

    /**
     * Given a <code>List&lt;R&gt;</code>, this methods builds a <code>LongFunction</code> that returns a
     * <code>LongObjectPair&lt;R&gt;</code>. It is intended to be used in a stream. The
     * <code>LongObjectPair&lt;R&gt;</code> built by this <code>LongFunction</code> will consist of a target long, and
     * an object of type &lt;R&gt; whose element in the passed <code>List</code> is associated with the current long, in
     * encounter order. The function returned from this method is <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the array of longs being streamed, the extra elements are
     * ignored. If it has fewer elements, any target values that do not have associated values in the list, will be
     * paired with a <code>null</code> value.
     *
     * @param pairedList A List whose elements are to be paired with long array elements being streamed, by the
     *                   LongFunction built by this method.
     * @param <R>        The type of the elements in the passed pairedList parameter.
     * @return A LongFunction that will return a LongObjectPair of that long, along with an associated element from the
     * passed pairedList.
     */
    public static <R> LongFunction<LongObjectPair<R>> longPairWith(List<R> pairedList) {
        List<R> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return l -> {
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? LongObjectPair.of(l, nonNullList.get(i)) : LongObjectPair.of(l, null);
        };
    }

    /**
     * Given a <code>LongFunction&lt;U&gt;</code>, and a <code>List&lt;V&gt;</code>, this method builds a
     * <code>LongFunction</code> that returns a <code>Pair&lt;U, V&gt;</code>. It is intended to be used in a stream.
     * The <code>Pair&lt;U, V&gt;</code> built by this <code>LongFunction</code> will consist of an element returned
     * by the passed <code>function</code>, and an object of type &lt;V&gt; whose element in the passed
     * <code>List</code> is associated with the current long, in encounter order. The long function returned from
     * this method is <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the long array being streamed, the extra elements are
     * ignored. If it has fewer elements, any values returned by the passed long <code>function</code>, that do not
     * have associated values in the list, will be paired with a <code>null</code> value.
     *
     * @param function   A LongFunction that will return a value of type &lt;U&gt;, which will become the left element
     *                   in a Pair, returned by the LongFunction built by this method.
     * @param pairedList A List whose elements are to be paired with elements retrieved by the passed long function.
     * @param <U>        The type of the left element, retrieved by the passed function.
     * @param <V>        The type of the right element, retrieved from the passed List.
     * @return A LongFunction that will return a Pair of a value retrieved from the passed function, along with an
     * associated element from the passed pairedList.
     */
    public static <U, V> LongFunction<Pair<U, V>> longPairWith(LongFunction<? extends U> function, List<V> pairedList) {
        List<V> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return l -> {
            U extracted = function.apply(l);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    /**
     * Builds a <code>LongFunction</code> that returns an object that represents a pair of values, one being the long
     * value itself, and the other a primitive zero-based index of the long in encounter order. The
     * <code>LongFunction</code> built by this method is intended to be used in a stream, but is <i>not</i> intended
     * to be used with parallel streams.
     *
     * @return A LongFunction that returns an object representing a pair of the long itself, along with the primitive
     * zero-based int index of the long value.
     */
    public static LongFunction<LongIndexPair> longPairWithIndex() {
        AtomicInteger idx = new AtomicInteger();
        return l -> LongIndexPair.of(l, idx.getAndIncrement());
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;R&gt;, this method builds a
     * <code>LongFunction</code> that returns an object that represents a pair of values, one being a value returned
     * from the passed long <code>function</code>, and the other a primitive zero-based index of the long value in
     * encounter order. The <code>LongFunction</code> built by this method is intended to be used in a stream, but is
     * <i>not</i> intended to be used with parallel streams.
     *
     * @param function A LongFunction that returns a value of type &lt;R&gt;.
     * @param <R>      The type of a value retrieved from the passed long function.
     * @return A LongFunction that returns an object representing a pair of a value returned from the passed long
     * function, along with the primitive zero-based int index of the target long.
     */
    public static <R> LongFunction<Pair<R, Integer>> longPairWithIndex(LongFunction<? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return l -> Pair.of(function.apply(l), idx.getAndIncrement());
    }

    /**
     * Given a LongPredicate and an object consisting of a pair of long functions, each returning a value of type
     * &lt;R&gt;, one to return a value if the predicate is true, the other returning an alternate value if the
     * predicate is false, this method builds a <code>LongFunction</code> that evaluates the predicate and returns a
     * value produced by one or the other of the pair. It may be difficult to think of an example where this may be
     * useful, but this method is included here for the sake of completeness.
     *
     * @param predicate        A long predicate to be evaluated, determining which of the pair of long functions below
     *                         will return a resulting value.
     * @param trueFalseMappers An object consisting of a pair of long functions, one to return a value if the passed
     *                         predicate evaluates to true, and the other to return an alternate value if it evaluates
     *                         to false.
     * @param <R>              The type of the values returned by the pair of long functions in the passed
     *                         trueFalseMappers.
     * @return A value of type &lt;R&gt; returned by one or the other of a pair of functions, depending on whether the
     * passed long predicate evaluates to true or false.
     */
    public static <R> LongFunction<R> longTernary(LongPredicate predicate, LongTernaryMapper<R> trueFalseMappers) {
        return l -> predicate.test(l) ? trueFalseMappers.getTrueMapper().apply(l) : trueFalseMappers.getFalseMapper().apply(l);
    }

    /**
     * Builds an object representing a pair of functions, one to return a value if a long predicate evaluates to true,
     * the other to return an alternate value if it evaluates to false. This method is meant to be used to build the
     * second argument to the {@link #longTernary(LongPredicate, LongTernaryMapper)} method.
     *
     * @param trueExtractor  Retrieves a value to be returned by the
     *                       {@link #longTernary(LongPredicate, LongTernaryMapper)} method when its predicate evaluates
     *                       to true.
     * @param falseExtractor Retrieves a value to be returned by the
     *                       {@link #longTernary(LongPredicate, LongTernaryMapper)} method when its predicate evaluates
     *                       to false.
     * @param <R>            The type of the value to be returned by the extractor methods below.
     * @return An object representing a pair of long functions, one to be called if a predicate evaluates to true, the
     * other to be called if it evaluates to false.
     */
    public static <R> LongTernaryMapper<R> longTrueFalseMappers(LongFunction<R> trueExtractor, LongFunction<R> falseExtractor) {
        return LongTernaryMapper.of(trueExtractor, falseExtractor);
    }

    /**
     * Builds an object representing a pair of long functions, one to return a key in a <code>Map</code>, and the other
     * to return its associated value. This method is meant to be used to build the second parameter to the
     * {@link LongTransformUtils#longTransformToMap(long[], LongKeyValueMapper)} method.
     *
     * @param keyMapper   LongFunction to retrieve a value to be used as a key in a Map.
     * @param valueMapper LongFunction to retrieve a value associated with a key in a Map.
     * @param <K>         The type of a key value for a Map.
     * @param <V>         The type of a value to be associated with a key in a Map.
     * @return Builds an object representing a pair of long functions, one to retrieve a Map key, and the other to
     * retrieve its associated value.
     */
    public static <K, V> LongKeyValueMapper<K, V> longKeyValueMapper(LongFunction<K> keyMapper, LongFunction<V> valueMapper) {
        return LongKeyValueMapper.of(keyMapper, valueMapper);
    }

    /**
     * Builds a <code>LongUnaryOperator</code> that adds a constant value to a <code>long</code> parameter.
     *
     * @param toAdd A constant long value to be added to the parameter of a LongUnaryOperator.
     * @return A LongUnaryOperator to whose parameter a constant long value will be added.
     */
    public static LongUnaryOperator longAdd(long toAdd) {
        return l -> l + toAdd;
    }

    /**
     * Builds a <code>LongUnaryOperator</code> that subtracts a constant value from a <code>long</code> parameter.
     *
     * @param toSubtract A constant long value to be subtracted from the parameter of a LongUnaryOperator.
     * @return A LongUnaryOperator from whose parameter a constant long value will be subtracted.
     */
    public static LongUnaryOperator longSubtract(long toSubtract) {
        return l -> l - toSubtract;
    }

    /**
     * Builds a <code>LongUnaryOperator</code> that multiplies a constant value with a <code>long</code> parameter.
     *
     * @param factor A constant long value to be multiplied with the parameter of a LongUnaryOperator.
     * @return A LongUnaryOperator whose parameter will be multiplied by a constant long value.
     */
    public static LongUnaryOperator longMultiply(long factor) {
        return l -> l * factor;
    }

    /**
     * Builds a <code>LongUnaryOperator</code> that divides its <code>long</code> parameter by a constant value.
     *
     * @param divisor A constant long value to be divided into the parameter of a LongUnaryOperator.
     * @return A LongUnaryOperator whose parameter will be divided by a constant long value.
     */
    public static LongUnaryOperator longDivide(long divisor) {
        return l -> l / divisor;
    }

    /**
     * Builds a <code>LongUnaryOperator</code> that divides its <code>long</code> parameter by a constant value to
     * get a remainder.
     *
     * @param divisor A constant long value to be divided into the parameter of a LongUnaryOperator to get a
     *                remainder.
     * @return A LongUnaryOperator whose parameter will be divided by a constant long value to get a remainder.
     */
    public static LongUnaryOperator longModulo(long divisor) {
        return l -> l % divisor;
    }
}
