package org.hringsak.functions.mapper;

import org.hringsak.functions.internal.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

import static org.hringsak.functions.stream.DblStreamUtils.defaultDblStream;

/**
 * Methods that build functions to map a target element into another result. This class deals specifically with mapper
 * functions involving primitive <code>double</code> types.
 */
public final class DblMapperUtils {

    private DblMapperUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>double</code> and returns
     * &lt;R&gt;, to a <code>DoubleFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)}
     * method applies here. The difference is that instead of an element of type &lt;T&gt; being streamed through, it
     * would be a primitive <code>double</code> instead. This method might be useful in a situation where you have a
     * <code>DoubleStream</code>, and the <code>DoubleStream.mapToObj(DoubleFunction mapper)</code> method is called to
     * convert the primitive to some generic type of object, converting the <code>DoubleStream</code> to an object
     * stream.
     * <p>
     * Note that the difference between this method and {@link #toDblMapper(ToDoubleFunction)} is that the
     * <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic type, where
     * the <code>ToDoubleFunction</code> built from {@link #toDblMapper(ToDoubleFunction)} takes a generic type and
     * returns a <code>double</code>.
     *
     * @param function A method reference to be cast to a DoubleFunction.
     * @param <R>      The type of the result of the DoubleFunction built by this method.
     * @return A method reference cast to a DoubleFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <R> DoubleFunction<R> dblMapper(DoubleFunction<R> function) {
        return function;
    }

    /**
     * Builds a <code>DoubleFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of an element
     * of type &lt;T&gt; being streamed through, it would be a primitive <code>double</code> instead. This method might
     * be useful in a situation where you have a <code>DoubleStream</code>, and the
     * <code>DoubleStream.mapToObj(DoubleFunction mapper)</code> method is called to convert the primitive to some
     * generic type of object, converting the <code>DoubleStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #toDblMapper(ToDoubleBiFunction, Object)} is that the
     * <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic type, where
     * the <code>ToDoubleFunction</code> built from {@link #toDblMapper(ToDoubleBiFunction, Object)} takes a generic
     * type and returns a <code>double</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type double, and
     *                   the second of type &lt;U&gt;, which can be any type. The method reference will be converted by
     *                   this method to a DoubleFunction, taking a single parameter of type double. Behind the scenes,
     *                   this BiFunction will be called, passing the constant value to each invocation as the second
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the DoubleFunction built by this method.
     * @return A DoubleFunction taking a single parameter of type double, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> DoubleFunction<R> dblMapper(BiFunction<Double, ? super U, ? extends R> biFunction, U value) {
        return d -> biFunction.apply(d, value);
    }

    /**
     * Builds a <code>DoubleFunction</code> from a passed <code>BiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of an
     * element of type &lt;T&gt; being streamed through, it would be a primitive <code>double</code> instead. This
     * method might be useful in a situation where you have a <code>DoubleStream</code>, and the
     * <code>DoubleStream.mapToObj(DoubleFunction mapper)</code> method is called to convert the primitive to some
     * generic type of object, converting the <code>DoubleStream</code> to an object stream.
     * <p>
     * Note that the difference between this method and {@link #inverseToDblMapper(ToDoubleBiFunction, Object)} is
     * that the <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic
     * type, where the <code>ToDoubleFunction</code> built from {@link #inverseToDblMapper(ToDoubleBiFunction, Object)}
     * takes a generic type and returns a <code>double</code>.
     *
     * @param biFunction A method reference which is a BiFunction, taking two parameters - the first of type &lt;U&gt;
     *                   which can be any type, and the second of type double. The method reference will be converted by
     *                   this method to a DoubleFunction, taking a single parameter of type double. Behind the scenes,
     *                   this biFunction will be called, passing the constant value to each invocation as the first
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @param <R>        The type of the result of the DoubleFunction built by this method.
     * @return A DoubleFunction taking a single parameter of type double, and returning a result of type &lt;R&gt;.
     */
    public static <U, R> DoubleFunction<R> inverseDblMapper(BiFunction<? super U, Double, ? extends R> biFunction, U value) {
        return d -> biFunction.apply(value, d);
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt; and returns <code>double</code>,
     * to a <code>ToDoubleFunction</code>. Everything said about the {@link MapperUtils#mapper(Function)} method applies
     * here. The difference is that instead of returning a result of a generic object type, it returns a primitive
     * <code>double</code> instead. This method might be useful in a situation where you have a <code>Stream</code> of a
     * generic object type, and the <code>mapToDouble(ToDoubleFunction mapper)</code> method is called to convert the
     * object to a primitive <code>double</code>, converting the stream to a <code>DoubleStream</code>.
     * <p>
     * Note that the difference between this method and {@link #dblMapper(DoubleFunction)} is that the
     * <code>ToDoubleFunction</code> built from this method takes a generic type and returns a <code>double</code>,
     * where the <code>DoubleFunction</code> built from {@link #dblMapper(DoubleFunction)} takes a
     * <code>double</code> and returns a generic type.
     *
     * @param function A method reference to be cast to a ToDoubleFunction.
     * @param <T>      The type of the single parameter to the ToDoubleFunction.
     * @return A method reference cast to a ToDoubleFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T> ToDoubleFunction<T> toDblMapper(ToDoubleFunction<T> function) {
        return function;
    }

    /**
     * Builds a mapper <code>ToDoubleFunction</code> that, if the target element is <code>null</code>, or the result of
     * the <code>Function</code> call on the target element is <code>null</code>, then the passed default value is
     * returned. Everything said about the {@link MapperUtils#mapperDefault(Function, Object)} method applies here. The
     * difference is that instead of returning a result of a generic object type, it returns a primitive
     * <code>double</code> instead. This method might be useful in a situation where you have a <code>Stream</code> of a
     * generic object type, and the <code>mapToDouble(ToDoubleFunction mapper)</code> method is called to convert the
     * object to a primitive <code>double</code>, converting the stream to a <code>DoubleStream</code>.
     *
     * @param function     A method reference which takes a single parameter of type &lt;T&gt;, and returns a value of
     *                     type double.
     * @param defaultValue A default value of type double, to be returned in case the target element, or the result of
     *                     the ToDoubleFunction call on the target element is null.
     * @param <T>          The type of the target element on which the mapper ToDoubleFunction is to be called.
     * @return A ToDoubleFunction taking a single parameter of type &lt;T&gt;, and returning a result of type double.
     */
    public static <T> ToDoubleFunction<T> toDblMapperDefault(ToDoubleFunction<? super T> function, double defaultValue) {
        return t -> t == null ? defaultValue : function.applyAsDouble(t);
    }

    /**
     * Builds a <code>ToDoubleFunction</code> from a passed <code>ToDoubleBiFunction</code>. Everything said about the
     * {@link MapperUtils#mapper(BiFunction, Object)} method applies here. The difference is that instead of returning a
     * result of a generic object type, it returns a primitive <code>double</code> instead. This method might be useful
     * in a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToDouble(ToDoubleFunction mapper)</code> method is called to convert the object to a primitive
     * <code>double</code>, converting the stream to a <code>DoubleStream</code>.
     * <p>
     * Note that the difference between this method and {@link #dblMapper(BiFunction, Object)} is that the
     * <code>ToDoubleFunction</code> built from this method takes a generic type and returns a <code>double</code>,
     * where the <code>DoubleFunction</code> built from {@link #dblMapper(BiFunction, Object)} takes a
     * <code>double</code> and returns a generic type.
     *
     * @param biFunction A method reference which is a ToDoubleBiFunction, taking two parameters - the first of type
     *                   &lt;T&gt;, and the second of type &lt;U&gt;, both of which can be any type. The method
     *                   reference will be converted by this method to a ToDoubleFunction, taking a single parameter of
     *                   type &lt;T&gt;. Behind the scenes, this ToDoubleBiFunction will be called, passing the constant
     *                   value to each invocation as the second parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToDoubleFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biFunction.
     * @return A ToDoubleFunction taking a single parameter of type &lt;T&gt;, and returning a result of type double.
     */
    public static <T, U> ToDoubleFunction<T> toDblMapper(ToDoubleBiFunction<? super T, ? super U> biFunction, U value) {
        return t -> biFunction.applyAsDouble(t, value);
    }

    /**
     * Builds a <code>ToDoubleFunction</code> from a passed <code>ToDoubleBiFunction</code>. Everything said about the
     * {@link MapperUtils#inverseMapper(BiFunction, Object)} method applies here. The difference is that instead of
     * returning a result of a generic object type, it returns a primitive <code>double</code> instead. This method
     * might be useful in a situation where you have a <code>Stream</code> of a generic object type, and the
     * <code>mapToDouble(ToDoubleFunction mapper)</code> method is called to convert the object to a primitive
     * <code>double</code>, converting the stream to a <code>DoubleStream</code>.
     * <p>
     * Note that the difference between this method and {@link #inverseDblMapper(BiFunction, Object)} is
     * that the <code>ToDoubleFunction</code> built from this method takes a generic type and returns a
     * <code>double</code>, where the <code>DoubleFunction</code> built from
     * {@link #inverseDblMapper(BiFunction, Object)} takes a <code>double</code> and returns a generic type.
     *
     * @param biFunction A method reference which is a ToDoubleBiFunction, taking two parameters - the first of type
     *                   &lt;U&gt; which can be any type, and the second of type double. The method reference will be
     *                   converted by this method to a ToDoubleFunction, taking a single parameter of type &lt;T&gt;.
     *                   Behind the scenes, this biFunction will be called, passing the constant value to each
     *                   invocation as the first parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biFunction as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <T>        The type of the target element on which the mapper ToDoubleFunction is to be called.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @return A ToDoubleFunction taking a single parameter of type &lt;T&gt;, and returning a result of type double.
     */
    public static <T, U> ToDoubleFunction<T> inverseToDblMapper(ToDoubleBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsDouble(value, t);
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>double</code> array, this method builds a
     * <code>DoubleFunction</code> that returns a <code>DoubleStream</code>. This is useful in the
     * <code>DoubleStream.flatMap()</code> method. For a very contrived example, let's say you have a method,
     * <code>MathUtils.getFactors(double product)</code>, that takes a double value, truncating the decimal portion,
     * and returns a <code>double</code> array containing the factors of that number. You have lower and upper bound int
     * values to create a range, which is converted to a <code>DoubleStream</code>, and you want to sum the factors of
     * all of the individual double values:
     * <pre>
     *     private double getSumOfAllFactors(int startInclusive, int endExclusive) {
     *         return IntStream.range(startInclusive, endExclusive).asDoubleStream()
     *             .flatMap(DblMapperUtils.dblFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private double getSumOfAllFactors(int startInclusive, int endExclusive) {
     *         return IntStream.range(startInclusive, endExclusive).asDoubleStream()
     *             .flatMap(dblFlatMapper(MathUtils::getFactors))
     *             .sum();
     *     }
     * </pre>
     *
     * @param doubleMapper A DoubleFunction that returns an array of doubles.
     * @return A DoubleFunction that returns a DoubleStream. Returns an empty DoubleStream if the double array returned
     * by the given doubleMapper is null or empty.
     */
    public static DoubleFunction<DoubleStream> dblFlatMapper(DoubleFunction<? extends double[]> doubleMapper) {
        return d -> defaultDblStream(doubleMapper.apply(d));
    }

    /**
     * Given a <code>Function</code> that takes an argument of type &lt;T&gt; and returns a <code>double</code> array,
     * this method builds a <code>Function</code> that takes the same argument, but returns a <code>DoubleStream</code>.
     * This is useful in the <code>Stream.flatMapToDouble()</code> method. For example, let's say you have a collection
     * of objects representing the product lines of your company. You want to total the prices of all products in those
     * product lines contained in the collection:
     * <pre>
     *     private double getTotalProductLinePrices(Collection&lt;ProductLine&gt; productLines) {
     *         return productLines.stream()
     *             .flatMapToDouble(DblMapperUtils.flatMapperToDbl(this::getAllPrices))
     *             .sum();
     *     }
     *
     *     private double[] getAllPrices(ProductLine productLine) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     private double getTotalProductLinePrices(Collection&lt;ProductLine&gt; productLines) {
     *         return productLines.stream()
     *             .flatMapToDouble(flatMapperToDbl(this::getAllPrices))
     *             .sum();
     *     }
     * </pre>
     *
     * @param toDoubleArrayMapper A Function taking an argument of type &lt;T&gt;, that returns an array of doubles.
     * @param <T>                 The type of the argument to be passed to the given toDoubleArrayMapper function.
     * @return A Function taking an argument of type &lt;T&gt;, that returns a DoubleStream. Returns an empty
     * DoubleStream if the passed argument is null, or the double array returned by the given toDoubleArrayMapper
     * function is null or empty.
     */
    public static <T> Function<T, DoubleStream> flatMapperToDbl(Function<? super T, ? extends double[]> toDoubleArrayMapper) {
        return t -> t == null ? DoubleStream.empty() : defaultDblStream(toDoubleArrayMapper.apply(t));
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;U&gt;, this method builds a
     * <code>DoubleFunction</code> that returns a value of type <code>DoubleObjectPair&lt;U&gt;</code>. This pair will
     * consist of the target double itself, and a value returned by the passed <code>rightFunction</code>.
     *
     * @param rightFunction A DoubleFunction to extract the right value in the DoubleObjectPair&lt;U&gt; to be returned
     *                      by the DoubleFunction built by this method.
     * @param <U>           The type of the right element of the DoubleObjectPair to be returned by the DoubleFunction
     *                      built by this method.
     * @return A DoubleFunction that returns a DoubleObjectPair of the target double, along with a value returned by the
     * passed rightFunction.
     */
    public static <U> DoubleFunction<DoubleObjectPair<U>> dblPairOf(DoubleFunction<? extends U> rightFunction) {
        return d -> DoubleObjectPair.of(d, rightFunction.apply(d));
    }

    /**
     * Given an object consisting of a pair of double functions, one that returns a value of type &lt;U&gt;, and the
     * other that returns a value of type &lt;V&gt;, this method builds a <code>DoubleFunction</code> that returns a
     * value of type <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the
     * functions in the passed <code>keyValueMapper</code>. This method does the same thing as the overload that takes a
     * <code>leftFunction</code> and <code>rightFunction</code>, and is included as a convenience when a method already
     * takes a <code>DblKeyValueMapper</code>. For example, the implementation of the
     * {@link DblTransformUtils#dblTransformToMap(double[], DblKeyValueMapper)} method is:
     * <pre>
     *     public static &lt;K, V&gt; Map&lt;K, V&gt; dblTransformToMap(double[] doubles, DblKeyValueMapper&lt;K, V&gt; keyValueMapper) {
     *         return defaultDblStream(doubles)
     *             .mapToObj(dblPairOf(keyValueMapper))
     *             .collect(toMapFromEntry());
     *     }
     * </pre>
     * This works because the {@link Pair} object implements the Java <code>Map.Entry</code> interface.
     *
     * @param keyValueMapper An object consisting of a pair of double functions that will be used to retrieve a left and
     *                       right value for a Pair that is a result of the DoubleFunction built by this method.
     * @param <U>            The type of the left element of the Pair to be returned by the DoubleFunction built by this
     *                       method.
     * @param <V>            The type of the right element of the Pair to be returned by the DoubleFunction built by
     *                       this method.
     * @return A DoubleFunction that returns a Pair whose values will be retrieved by a pair of double functions
     * represented by the passed keyValueMapper.
     */
    public static <U, V> DoubleFunction<Pair<U, V>> dblPairOf(DblKeyValueMapper<U, V> keyValueMapper) {
        return dblPairOf(keyValueMapper.getKeyMapper(), keyValueMapper.getValueMapper());
    }

    /**
     * Given a pair of double functions, one that returns a value of type &lt;U&gt;, and the other that returns a value
     * of type &lt;V&gt;, this method builds a <code>DoubleFunction</code> that returns a value of type
     * <code>Pair&lt;U, V&gt;</code>. This pair will consist of the values returned by each of the double functions
     * passed to this method.
     *
     * @param leftFunction  A DoubleFunction that will be used to retrieve a left value for a Pair that is a result of
     *                      the DoubleFunction built by this method.
     * @param rightFunction A DoubleFunction that will be used to retrieve a right value for a Pair that is a result of
     *                      the DoubleFunction built by this method.
     * @param <U>           The type of the left element of the Pair to be returned by the DoubleFunction built by this
     *                      method.
     * @param <V>           The type of the right element of the Pair to be returned by the DoubleFunction built by this
     *                      method.
     * @return A DoubleFunction that returns a Pair whose values will be retrieved by a pair of double functions passed
     * to this method.
     */
    public static <U, V> DoubleFunction<Pair<U, V>> dblPairOf(DoubleFunction<? extends U> leftFunction, DoubleFunction<? extends V> rightFunction) {
        return d -> Pair.of(leftFunction.apply(d), rightFunction.apply(d));
    }

    /**
     * Given a <code>List&lt;R&gt;</code>, this methods builds a <code>DoubleFunction</code> that returns a
     * <code>DoubleObjectPair&lt;R&gt;</code>. It is intended to be used in a stream. The
     * <code>DoubleObjectPair&lt;R&gt;</code> built by this <code>DoubleFunction</code> will consist of a target
     * double, and an object of type &lt;R&gt; whose element in the passed <code>List</code> is associated with the
     * current double, in encounter order. The function returned from this method is <i>not</i> intended to be used with
     * parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the array of doubles being streamed, the extra elements
     * are ignored. If it has fewer elements, any target values that do not have associated values in the list, will be
     * paired with a <code>null</code> value.
     *
     * @param pairedList A List whose elements are to be paired with double array elements being streamed, by the
     *                   DoubleFunction built by this method.
     * @param <R>        The type of the elements in the passed pairedList parameter.
     * @return A DoubleFunction that will return a DoubleObjectPair of that double, along with an associated element
     * from the passed pairedList.
     */
    public static <R> DoubleFunction<DoubleObjectPair<R>> dblPairWith(List<R> pairedList) {
        List<R> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return d -> {
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? DoubleObjectPair.of(d, nonNullList.get(i)) : DoubleObjectPair.of(d, null);
        };
    }

    /**
     * Given a <code>DoubleFunction&lt;U&gt;</code>, and a <code>List&lt;V&gt;</code>, this method builds a
     * <code>DoubleFunction</code> that returns a <code>Pair&lt;U, V&gt;</code>. It is intended to be used in a stream.
     * The <code>Pair&lt;U, V&gt;</code> built by this <code>DoubleFunction</code> will consist of an element returned
     * by the passed <code>function</code>, and an object of type &lt;V&gt; whose element in the passed
     * <code>List</code> is associated with the current double, in encounter order. The double function returned from
     * this method is <i>not</i> intended to be used with parallel streams.
     * <p>
     * If the passed <code>List</code> has more elements than the double array being streamed, the extra elements are
     * ignored. If it has fewer elements, any values returned by the passed double <code>function</code>, that do not
     * have associated values in the list, will be paired with a <code>null</code> value.
     *
     * @param function   A DoubleFunction that will return a value of type &lt;U&gt;, which will become the left element
     *                   in a Pair, returned by the DoubleFunction built by this method.
     * @param pairedList A List whose elements are to be paired with elements retrieved by the passed double function.
     * @param <U>        The type of the left element, retrieved by the passed function.
     * @param <V>        The type of the right element, retrieved from the passed List.
     * @return A DoubleFunction that will return a Pair of a value retrieved from the passed function, along with an
     * associated element from the passed pairedList.
     */
    public static <U, V> DoubleFunction<Pair<U, V>> dblPairWith(DoubleFunction<? extends U> function, List<V> pairedList) {
        List<V> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return d -> {
            U extracted = function.apply(d);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    /**
     * Builds a <code>DoubleFunction</code> that returns an object that represents a pair of values, one being the
     * double value itself, and the other a primitive zero-based index of the double in encounter order. The
     * <code>DoubleFunction</code> built by this method is intended to be used in a stream, but is <i>not</i> intended
     * to be used with parallel streams.
     *
     * @return A DoubleFunction that returns an object representing a pair of the double itself, along with the
     * primitive zero-based int index of the double value.
     */
    public static DoubleFunction<DoubleIndexPair> dblPairWithIndex() {
        AtomicInteger idx = new AtomicInteger();
        return d -> DoubleIndexPair.of(d, idx.getAndIncrement());
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;R&gt;, this method builds a
     * <code>DoubleFunction</code> that returns an object that represents a pair of values, one being a value returned
     * from the passed double <code>function</code>, and the other a primitive zero-based index of the double value in
     * encounter order. The <code>DoubleFunction</code> built by this method is intended to be used in a stream, but is
     * <i>not</i> intended to be used with parallel streams.
     *
     * @param function A DoubleFunction that returns a value of type &lt;R&gt;.
     * @param <R>      The type of a value retrieved from the passed double function.
     * @return A DoubleFunction that returns an object representing a pair of a value returned from the passed double
     * function, along with the primitive zero-based int index of the target double.
     */
    public static <R> DoubleFunction<Pair<R, Integer>> dblPairWithIndex(DoubleFunction<? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return d -> Pair.of(function.apply(d), idx.getAndIncrement());
    }

    /**
     * Given a DoublePredicate and an object consisting of a pair of double functions, each returning a value of type
     * &lt;R&gt;, one to return a value if the predicate is true, the other returning an alternate value if the
     * predicate is false, this method builds a <code>DoubleFunction</code> that evaluates the predicate and returns a
     * value produced by one or the other of the pair. It may be difficult to think of an example where this may be
     * useful, but this method is included here for the sake of completeness.
     *
     * @param predicate        A double predicate to be evaluated, determining which of the pair of double functions
     *                         below will return a resulting value.
     * @param trueFalseMappers An object consisting of a pair of double functions, one to return a value if the passed
     *                         predicate evaluates to true, and the other to return an alternate value if it evaluates
     *                         to false.
     * @param <R>              The type of the values returned by the pair of double functions in the passed
     *                         trueFalseMappers.
     * @return A value of type &lt;R&gt; returned by one or the other of a pair of functions, depending on whether the
     * passed double predicate evaluates to true or false.
     */
    public static <R> DoubleFunction<R> dblTernary(DoublePredicate predicate, DoubleTernaryMapper<R> trueFalseMappers) {
        return d -> predicate.test(d) ? trueFalseMappers.getTrueMapper().apply(d) : trueFalseMappers.getFalseMapper().apply(d);
    }

    /**
     * Builds an object representing a pair of functions, one to return a value if a double predicate evaluates to true,
     * the other to return an alternate value if it evaluates to false. This method is meant to be used to build the
     * second argument to the {@link #dblTernary(DoublePredicate, DoubleTernaryMapper)} method.
     *
     * @param trueExtractor  Retrieves a value to be returned by the
     *                       {@link #dblTernary(DoublePredicate, DoubleTernaryMapper)} method when its predicate
     *                       evaluates to true.
     * @param falseExtractor Retrieves a value to be returned by the
     *                       {@link #dblTernary(DoublePredicate, DoubleTernaryMapper)} method when its predicate
     *                       evaluates to false.
     * @param <R>            The type of the value to be returned by the extractor methods below.
     * @return An object representing a pair of double functions, one to be called if a predicate evaluates to true, the
     * other to be called if it evaluates to false.
     */
    public static <R> DoubleTernaryMapper<R> dblTrueFalseMappers(DoubleFunction<R> trueExtractor, DoubleFunction<R> falseExtractor) {
        return DoubleTernaryMapper.of(trueExtractor, falseExtractor);
    }

    /**
     * Builds an object representing a pair of double functions, one to return a key in a <code>Map</code>, and the
     * other to return its associated value. This method is meant to be used to build the second parameter to the
     * {@link DblTransformUtils#dblTransformToMap(double[], DblKeyValueMapper)} method.
     *
     * @param keyMapper   DoubleFunction to retrieve a value to be used as a key in a Map.
     * @param valueMapper DoubleFunction to retrieve a value associated with a key in a Map.
     * @param <K>         The type of a key value for a Map.
     * @param <V>         The type of a value to be associated with a key in a Map.
     * @return Builds an object representing a pair of double functions, one to retrieve a Map key, and the other to
     * retrieve its associated value.
     */
    public static <K, V> DblKeyValueMapper<K, V> dblKeyValueMapper(DoubleFunction<K> keyMapper, DoubleFunction<V> valueMapper) {
        return DblKeyValueMapper.of(keyMapper, valueMapper);
    }
}
