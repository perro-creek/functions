package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.IntStream;

import static org.perro.functions.stream.IntStreamUtils.defaultIntStream;

/**
 * Methods that build functions to map a target element into another result. This class deals specifically with mapper
 * functions involving primitive <code>int</code> types.
 */
public final class IntMapperUtils {

    private IntMapperUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>int</code> and returns &lt;R&gt;,
     * to an <code>IntFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)} method applies
     * here. The difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a
     * primitive <code>int</code> instead. This method might be useful in a situation where you have an
     * <code>IntStream</code>, and the <code>IntStream.mapToObj(IntFunction mapper)</code> method is called to convert
     * the primitive to some generic type of object, converting the <code>IntStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #toIntMapper(ToIntFunction)} is that the
     * <code>IntFunction</code> built from this method takes an <code>int</code> and returns a generic type, where the
     * <code>ToIntFunction</code> built from {@link #toIntMapper(ToIntFunction)} takes a generic type and returns an
     * <code>int</code>.
     *
     * @param function A method reference to be cast to an IntFunction.
     * @param <R>      The type of the result of the IntFunction built by this method.
     * @return A method reference cast to an IntFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <R> IntFunction<R> intMapper(IntFunction<R> function) {
        return function;
    }

    /**
     * Builds an <code>IntFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of an element
     * of type &lt;T&gt; being streamed through, it would be a primitive <code>int</code> instead. This method might be
     * useful in a situation where you have an <code>IntStream</code>, and the
     * <code>IntStream.mapToObj(IntFunction mapper)</code> method is called to convert the primitive to some generic
     * type of object, converting the <code>IntStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #toIntMapper(ToIntBiFunction, Object)} is that the
     * <code>IntFunction</code> built from this method takes an <code>int</code> and returns a generic type, where the
     * <code>ToIntFunction</code> built from {@link #toIntMapper(ToIntBiFunction, Object)} takes a generic type and
     * returns an <code>int</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type int, and
     *                   the second of type &lt;U&gt;, which can be any type. The method reference will be converted by
     *                   this method to an IntFunction, taking a single parameter of type int. Behind the scenes, this
     *                   BiFunction will be called, passing the constant value to each invocation as the second
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the IntFunction built by this method.
     * @return An IntFunction taking a single parameter of type int, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> IntFunction<R> intMapper(BiFunction<Integer, ? super U, ? extends R> biFunction, U value) {
        return i -> biFunction.apply(i, value);
    }

    /**
     * Builds an <code>IntFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of an
     * element of type &lt;T&gt; being streamed through, it would be a primitive <code>int</code> instead. This method
     * might be useful in a situation where you have an <code>IntStream</code>, and the
     * <code>IntStream.mapToObj(IntFunction mapper)</code> method is called to convert the primitive to some generic
     * type of object, converting the <code>IntStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #inverseToIntMapper(ToIntBiFunction, Object)} is that the
     * <code>IntFunction</code> built from this method takes an <code>int</code> and returns a generic type, where the
     * <code>ToIntFunction</code> built from {@link #inverseToIntMapper(ToIntBiFunction, Object)} takes a generic type
     * and returns an <code>int</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type &lt;U&gt;
     *                   which can be any type, and the second of type int. The method reference will be converted by
     *                   this method to an IntFunction, taking a single parameter of type int. Behind the scenes, this
     *                   biFunction will be called, passing the constant value to each invocation as the first
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the IntFunction built by this method.
     * @return An IntFunction taking a single parameter of type int, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> IntFunction<R> inverseIntMapper(BiFunction<? super U, Integer, ? extends R> biFunction, U value) {
        return i -> biFunction.apply(value, i);
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt; and returns <code>int</code>,
     * to a <code>ToIntFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)} method applies
     * here. The difference is that instead of returning a result of a generic object type, it returns a primitive
     * <code>int</code> instead. This method might be useful in a situation where you have a <code>Stream</code> of a
     * generic object type, and the <code>mapToInt(ToIntFunction mapper)</code> method is called to convert the object
     * to a primitive <code>int</code>, converting the stream to an <code>IntStream</code>.
     * <p>
     * Note that the difference between this method and {@link #intMapper(IntFunction)} is that the
     * <code>ToIntFunction</code> built from this method takes a generic type and returns an <code>int</code>, where the
     * <code>IntFunction</code> built from {@link #intMapper(IntFunction)} takes an <code>int</code> and returns a
     * generic type.
     *
     * @param function A method reference to be cast to a ToIntFunction.
     * @param <T>      The type of the single parameter to the ToIntFunction.
     * @return A method reference cast to a ToIntFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T> ToIntFunction<T> toIntMapper(ToIntFunction<T> function) {
        return function;
    }

    /**
     * Builds a mapper <code>ToIntFunction</code> that, if the target element is <code>null</code>, or the result of the
     * <code>Function</code> call on the target element is <code>null</code>, then the passed default value is returned.
     * Everything said about the {@link MapperUtils#mapperDefault(Function, Object)} method applies here. The difference
     * is that instead of returning a result of a generic object type, it returns a primitive <code>int</code> instead.
     * This method might be useful in a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToInt(ToIntFunction mapper)</code> method is called to convert the object to a primitive
     * <code>int</code>, converting the stream to an <code>IntStream</code>.
     *
     * @param function     A method reference which takes a single parameter of type &lt;T&gt;, and returns a value of
     *                     type int.
     * @param defaultValue A default value of type int, to be returned in case the target element, or the result of
     *                     the ToIntFunction call on the target element is null.
     * @param <T>          The type of the target element on which the mapper ToIntFunction is to be called.
     * @return A ToIntFunction taking a single parameter of type &lt;T&gt;, and returning a result of type int.
     */
    public static <T> ToIntFunction<T> toIntMapperDefault(ToIntFunction<? super T> function, int defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsInt(t);
    }

    /**
     * Builds a <code>ToIntFunction</code> from a passed <code>ToIntBiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of returning a
     * result of a generic object type, it returns a primitive <code>int</code> instead. This method might be useful in
     * a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToInt(ToIntFunction mapper)</code> method is called to convert the object to a primitive
     * <code>int</code>, converting the stream to an <code>IntStream</code>.
     * <p>
     * Note that the difference between this method and {@link #intMapper(BiFunction, Object)} is that the
     * <code>ToIntFunction</code> built from this method takes a generic type and returns an <code>int</code>,
     * where the <code>IntFunction</code> built from {@link #intMapper(BiFunction, Object)} takes an <code>int</code>
     * and returns a generic type.
     *
     * @param biFunction A method reference which is a ToIntBiFunction, taking two parameters - the first of type
     *                   &lt;T&gt;, and the second of type &lt;U&gt;, both of which can be any type. The method
     *                   reference will be converted by this method to a ToIntFunction, taking a single parameter of
     *                   type &lt;T&gt;. Behind the scenes, this ToIntBiFunction will be called, passing the constant
     *                   value to each invocation as the second parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToIntFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @return A ToIntFunction taking a single parameter of type &lt;T&gt;, and returning a result of type int.
     */
    public static <T, U> ToIntFunction<T> toIntMapper(ToIntBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsInt(t, value);
    }

    /**
     * Builds a <code>ToIntFunction</code> from a passed <code>ToIntBiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of
     * returning a result of a generic object type, it returns a primitive <code>int</code> instead. This method might
     * be useful in a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToInt(ToIntFunction mapper)</code> method is called to convert the object to a primitive
     * <code>int</code>, converting the stream to an <code>IntStream</code>.
     * <p>
     * Note that the difference between this method and {@link #inverseIntMapper(BiFunction, Object)} is that the
     * <code>ToIntFunction</code> built from this method takes a generic type and returns an <code>int</code>, where the
     * <code>IntFunction</code> built from {@link #inverseIntMapper(BiFunction, Object)} takes an <code>int</code> and
     * returns a generic type.
     *
     * @param biFunction A method reference which is a ToIntBiFunction, taking two parameters - the first of type
     *                   &lt;U&gt; which can be any type, and the second of type int. The method reference will be
     *                   converted by this method to a ToIntFunction, taking a single parameter of type &lt;T&gt;.
     *                   Behind the scenes, this biFunction will be called, passing the constant value to each
     *                   invocation as the first parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToIntFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @return A ToIntFunction taking a single parameter of type &lt;T&gt;, and returning a result of type int.
     */
    public static <T, U> ToIntFunction<T> inverseToIntMapper(ToIntBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsInt(value, t);
    }

    /**
     * Given an <code>IntFunction</code> that returns an <code>int</code> array, this method builds an
     * <code>IntFunction</code> that returns an <code>IntStream</code>. This is useful in the
     * <code>IntStream.flatMap()</code> method. For a very contrived example, let's say you have a method,
     * <code>MathUtils.getFactors(int product)</code>, that takes an int value and returns an <code>int</code> array
     * containing the factors of that number. You have lower and upper bound int values to create a range, and you want
     * to sum the factors of all of the individual int values:
     * <pre>
     *     private int getSumOfAllFactors(int startInclusive, int endExclusive) {
     *         return IntStream.range(startInclusive, endExclusive)
     *             .flatMap(IntMapperUtils.intFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private int getSumOfAllFactors(int startInclusive, int endExclusive) {
     *         return IntStream.range(startInclusive, endExclusive)
     *             .flatMap(intFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     *
     * @param intMapper An IntFunction that returns an array of ints.
     * @return An IntFunction that returns an IntStream. Returns an empty IntStream if the int array returned by the
     * given intMapper is null or empty.
     */
    public static IntFunction<IntStream> intFlatMapper(IntFunction<? extends int[]> intMapper) {
        return i -> defaultIntStream(intMapper.apply(i));
    }

    /**
     * Given a <code>Function</code> that takes an argument of type &lt;T&gt; and returns an <code>int</code> array,
     * this method builds a <code>Function</code> that takes the same argument, but returns an <code>IntStream</code>.
     * This is useful in the <code>Stream.flatMapToInt()</code> method. For example, let's say you have a collection of
     * objects representing all the orders for a particular customer. You want to total the quantities for each of the
     * line items in all of the orders contained in the collection:
     * <pre>
     *     private int getTotalLineItemQuantities(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMapToInt(IntMapperUtils.flatMapperToInt(this::getAllQuantities))
     *             .sum();
     *     }
     *
     *     private int[] getAllQuantities(Order order) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private int getTotalLineItemQuantities(Collection&lt;Order&gt; customerOrders) {
     *         return customerOrders.stream()
     *             .flatMapToInt(flatMapperToInt(this::getAllQuantities))
     *             .sum();
     *     }
     * </pre>
     *
     * @param toIntArrayMapper A Function taking an argument of type &lt;T&gt;, that returns an array of ints.
     * @param <T>              The type of the argument to be passed to the given toIntArrayMapper function.
     * @return A Function taking an argument of type &lt;T&gt;, that returns an IntStream. Returns an empty IntStream if
     * the passed argument is null, or the int array returned by the given toIntArrayMapper function is null or empty.
     */
    public static <T> Function<T, IntStream> flatMapperToInt(Function<? super T, ? extends int[]> toIntArrayMapper) {
        return t -> t == null ? IntStream.empty() : defaultIntStream(toIntArrayMapper.apply(t));
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;U&gt;, this method builds an
     * <code>IntFunction</code> that returns a value of type <code>IntObjectPair&lt;U&gt;</code>. This pair will consist
     * of the target int itself, and a value returned by the passed <code>rightFunction</code>.
     *
     * @param rightFunction An IntFunction to extract the right value in the IntObjectPair&lt;U&gt; to be returned by
     *                      the IntFunction built by this method.
     * @param <U>           The type of the right element of the IntObjectPair to be returned by the IntFunction built
     *                      by this method.
     * @return An IntFunction that returns an IntObjectPair of the target int, along with a value returned by the passed
     * rightFunction.
     */
    public static <U> IntFunction<IntObjectPair<U>> intPairOf(IntFunction<? extends U> rightFunction) {
        return i -> IntObjectPair.of(i, rightFunction.apply(i));
    }

    /**
     * Given an object consisting of a pair of int functions, one that returns a value of type &lt;U&gt;, and the
     * other that returns a value of type &lt;V&gt;, this method builds an <code>IntFunction</code> that returns a value
     * of type <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the functions in
     * the passed <code>keyValueMapper</code>. This method does the same thing as the overload that takes a
     * <code>leftFunction</code> and <code>rightFunction</code>, and is included as a convenience when a method already
     * takes an <code>IntKeyValueMapper</code>. For example, the implementation of the
     * {@link IntTransformUtils#intTransformToMap(int[], IntKeyValueMapper)} method is:
     * <pre>
     *     public static &lt;K, V&gt; Map&lt;K, V&gt; intTransformToMap(int[] ints, IntKeyValueMapper&lt;K, V&gt; keyValueMapper) {
     *         return defaultIntStream(ints)
     *             .mapToObj(intPairOf(keyValueMapper))
     *             .collect(toMapFromEntry());
     *     }
     * </pre>
     * This works because the {@link Pair} object implements the Java <code>Map.Entry</code> interface.
     *
     * @param keyValueMapper An object consisting of a pair of int functions that will be used to retrieve a left and
     *                       right value for a Pair that is a result of the IntFunction built by this method.
     * @param <U>            The type of the left element of the Pair to be returned by the IntFunction built by this
     *                       method.
     * @param <V>            The type of the right element of the Pair to be returned by the IntFunction built by this
     *                       method.
     * @return An IntFunction that returns a Pair whose values will be retrieved by a pair of int functions represented
     * by the passed keyValueMapper.
     */
    public static <U, V> IntFunction<Pair<U, V>> intPairOf(IntKeyValueMapper<U, V> keyValueMapper) {
        return intPairOf(keyValueMapper.getKeyMapper(), keyValueMapper.getValueMapper());
    }

    /**
     * Given a pair of int functions, one that returns a value of type &lt;U&gt;, and the other that returns a value of
     * type &lt;V&gt;, this method builds a <code>DoubleFunction</code> that returns a value of type
     * <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the int functions passed
     * to this method.
     *
     * @param leftFunction  An IntFunction that will be used to retrieve a left value for a Pair that is a result of
     *                      the IntFunction built by this method.
     * @param rightFunction A IntFunction that will be used to retrieve a right value for a Pair that is a result of
     *                      the IntFunction built by this method.
     * @param <U>           The type of the left element of the Pair to be returned by the IntFunction built by this
     *                      method.
     * @param <V>           The type of the right element of the Pair to be returned by the IntFunction built by this
     *                      method.
     * @return An IntFunction that returns a Pair whose values will be retrieved by a pair of int functions passed to
     * this method.
     */
    public static <U, V> IntFunction<Pair<U, V>> intPairOf(IntFunction<? extends U> leftFunction, IntFunction<? extends V> rightFunction) {
        return i -> Pair.of(leftFunction.apply(i), rightFunction.apply(i));
    }

    /**
     * Given a <code>List&lt;R&gt;</code>, this methods builds an <code>IntFunction</code> that returns an
     * <code>IntObjectPair&lt;R&gt;</code>. It is intended to be used in a stream. The
     * <code>IntObjectPair&lt;R&gt;</code> built by this <code>IntFunction</code> will consist of a target int, and an
     * object of type &lt;R&gt; whose element in the passed <code>List</code> is associated with the current int, in
     * encounter order. The function returned from this method is <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the array of ints being streamed, the extra elements are
     * ignored. If it has fewer elements, any target values that do not have associated values in the list, will be
     * paired with a <code>null</code> value.
     *
     * @param pairedList A List whose elements are to be paired with int array elements being streamed, by the
     *                   IntFunction built by this method.
     * @param <R>        The type of the elements in the passed pairedList parameter.
     * @return An IntFunction that will return a IntObjectPair of that int, along with an associated element from the
     * passed pairedList.
     */
    public static <R> IntFunction<IntObjectPair<R>> intPairWith(List<R> pairedList) {
        List<R> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return i -> {
            int j = idx.getAndIncrement();
            return (j < nonNullList.size()) ? IntObjectPair.of(i, nonNullList.get(j)) : IntObjectPair.of(i, null);
        };
    }

    /**
     * Given an <code>IntFunction&lt;U&gt;</code>, and a <code>List&lt;V&gt;</code>, this method builds an
     * <code>IntFunction</code> that returns a <code>Pair&lt;U, V&gt;</code>. It is intended to be used in a stream.
     * The <code>Pair&lt;U, V&gt;</code> built by this <code>IntFunction</code> will consist of an element returned
     * by the passed <code>function</code>, and an object of type &lt;V&gt; whose element in the passed
     * <code>List</code> is associated with the current int, in encounter order. The int function returned from this
     * method is <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the int array being streamed, the extra elements are
     * ignored. If it has fewer elements, any values returned by the passed int <code>function</code>, that do not
     * have associated values in the list, will be paired with a <code>null</code> value.
     *
     * @param function   An IntFunction that will return a value of type &lt;U&gt;, which will become the left element
     *                   in a Pair, returned by the IntFunction built by this method.
     * @param pairedList A List whose elements are to be paired with elements retrieved by the passed int function.
     * @param <U>        The type of the left element, retrieved by the passed function.
     * @param <V>        The type of the right element, retrieved from the passed List.
     * @return An IntFunction that will return a Pair of a value retrieved from the passed function, along with an
     * associated element from the passed pairedList.
     */
    public static <U, V> IntFunction<Pair<U, V>> intPairWith(IntFunction<? extends U> function, List<V> pairedList) {
        PairWithBuilder<U, V> builder = new PairWithBuilder<>(pairedList);
        return i -> builder.buildPair(function.apply(i));
    }

    /**
     * Builds an <code>IntFunction</code> that returns an object that represents a pair of values, one being the int
     * value itself, and the other a primitive zero-based index of the int in encounter order. The
     * <code>IntFunction</code> built by this method is intended to be used in a stream, but is <i>not</i> intended to
     * be used with parallel streams.
     *
     * @return An IntFunction that returns an object representing a pair of the int itself, along with the primitive
     * zero-based int index of the int value.
     */
    public static IntFunction<IntIndexPair> intPairWithIndex() {
        AtomicInteger idx = new AtomicInteger();
        return i -> IntIndexPair.of(i, idx.getAndIncrement());
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;R&gt;, this method builds an
     * <code>IntFunction</code> that returns an object that represents a pair of values, one being a value returned
     * from the passed int <code>function</code>, and the other a primitive zero-based index of the int value in
     * encounter order. The <code>IntFunction</code> built by this method is intended to be used in a stream, but is
     * <i>not</i> intended to be used with parallel streams.
     *
     * @param function An IntFunction that returns a value of type &lt;R&gt;.
     * @param <R>      The type of a value retrieved from the passed int function.
     * @return An IntFunction that returns an object representing a pair of a value returned from the passed int
     * function, along with the primitive zero-based int index of the target int.
     */
    public static <R> IntFunction<Pair<R, Integer>> intPairWithIndex(IntFunction<? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return i -> Pair.of(function.apply(i), idx.getAndIncrement());
    }

    /**
     * Given an IntPredicate and an object consisting of a pair of int functions, each returning a value of type
     * &lt;R&gt;, one to return a value if the predicate is true, the other returning an alternate value if the
     * predicate is false, this method builds an <code>IntFunction</code> that evaluates the predicate and returns a
     * value produced by one or the other of the pair. It may be difficult to think of an example where this may be
     * useful, but this method is included here for the sake of completeness.
     *
     * @param predicate        An int predicate to be evaluated, determining which of the pair of double functions below
     *                         will return a resulting value.
     * @param trueFalseMappers An object consisting of a pair of int functions, one to return a value if the passed
     *                         predicate evaluates to true, and the other to return an alternate value if it evaluates
     *                         to false.
     * @param <R>              The type of the values returned by the pair of int functions in the passed
     *                         trueFalseMappers.
     * @return A value of type &lt;R&gt; returned by one or the other of a pair of functions, depending on whether the
     * passed int predicate evaluates to true or false.
     */
    public static <R> IntFunction<R> intTernary(IntPredicate predicate, IntTernaryMapper<R> trueFalseMappers) {
        return i -> predicate.test(i) ? trueFalseMappers.getTrueMapper().apply(i) : trueFalseMappers.getFalseMapper().apply(i);
    }

    /**
     * Builds an object representing a pair of functions, one to return a value if an int predicate evaluates to true,
     * the other to return an alternate value if it evaluates to false. This method is meant to be used to build the
     * second argument to the {@link #intTernary(IntPredicate, IntTernaryMapper)} method.
     *
     * @param trueExtractor  Retrieves a value to be returned by the
     *                       {@link #intTernary(IntPredicate, IntTernaryMapper)} method when its predicate evaluates to
     *                       true.
     * @param falseExtractor Retrieves a value to be returned by the
     *                       {@link #intTernary(IntPredicate, IntTernaryMapper)} method when its predicate evaluates to
     *                       false.
     * @param <R>            The type of the value to be returned by the extractor methods below.
     * @return An object representing a pair of int functions, one to be called if a predicate evaluates to true, the
     * other to be called if it evaluates to false.
     */
    public static <R> IntTernaryMapper<R> intTrueFalseMappers(IntFunction<R> trueExtractor, IntFunction<R> falseExtractor) {
        return IntTernaryMapper.of(trueExtractor, falseExtractor);
    }

    /**
     * Builds an object representing a pair of int functions, one to return a key in a <code>Map</code>, and the other
     * to return its associated value. This method is meant to be used to build the second parameter to the
     * {@link IntTransformUtils#intTransformToMap(int[], IntKeyValueMapper)} method.
     *
     * @param keyMapper   IntFunction to retrieve a value to be used as a key in a Map.
     * @param valueMapper IntFunction to retrieve a value associated with a key in a Map.
     * @param <K>         The type of a key value for a Map.
     * @param <V>         The type of a value to be associated with a key in a Map.
     * @return Builds an object representing a pair of int functions, one to retrieve a Map key, and the other to
     * retrieve its associated value.
     */
    public static <K, V> IntKeyValueMapper<K, V> intKeyValueMapper(IntFunction<K> keyMapper, IntFunction<V> valueMapper) {
        return IntKeyValueMapper.of(keyMapper, valueMapper);
    }

    /**
     * Builds an <code>IntUnaryOperator</code> that adds a constant value to an <code>int</code> parameter.
     *
     * @param toAdd A constant int value to be added to the parameter of an IntUnaryOperator.
     * @return An IntUnaryOperator to whose parameter a constant int value will be added.
     */
    public static IntUnaryOperator intAdd(int toAdd) {
        return i -> i + toAdd;
    }

    /**
     * Builds an <code>IntUnaryOperator</code> that subtracts a constant value from an <code>int</code> parameter.
     *
     * @param toSubtract A constant int value to be subtracted from the parameter of an IntUnaryOperator.
     * @return An IntUnaryOperator from whose parameter a constant int value will be subtracted.
     */
    public static IntUnaryOperator intSubtract(int toSubtract) {
        return i -> i - toSubtract;
    }

    /**
     * Builds an <code>IntUnaryOperator</code> that multiplies a constant value with an <code>int</code> parameter.
     *
     * @param factor A constant int value to be multiplied with the parameter of an IntUnaryOperator.
     * @return An IntUnaryOperator whose parameter will be multiplied by a constant int value.
     */
    public static IntUnaryOperator intMultiply(int factor) {
        return i -> i * factor;
    }

    /**
     * Builds an <code>IntUnaryOperator</code> that divides its <code>int</code> parameter by a constant value.
     *
     * @param divisor A constant int value to be divided into the parameter of an IntUnaryOperator.
     * @return An IntUnaryOperator whose parameter will be divided by a constant int value.
     */
    public static IntUnaryOperator intDivide(int divisor) {
        return i -> i / divisor;
    }

    /**
     * Builds an <code>IntUnaryOperator</code> that divides its <code>int</code> parameter by a constant value to
     * get a remainder.
     *
     * @param divisor A constant int value to be divided into the parameter of an IntUnaryOperator to get a
     *                remainder.
     * @return An IntUnaryOperator whose parameter will be divided by a constant int value to get a remainder.
     */
    public static IntUnaryOperator intModulo(int divisor) {
        return i -> i % divisor;
    }
}
