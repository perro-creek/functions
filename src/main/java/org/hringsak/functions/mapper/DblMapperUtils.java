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
     * @param doubleMapper A function taking a double value, that returns an array of doubles.
     * @return A DoubleFunction that, given an argument of type &lt;double&gt;, returns a Stream of double values.
     * Returns an empty Stream if the double array returned by the given function is null or empty.
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
     * @return A Function taking an argument of type &lt;T&gt;, that returns a Stream of double values. Returns an empty
     * Stream if the passed argument is null, or the double array returned by the given function is null or empty.
     */
    public static <T> Function<T, DoubleStream> flatMapperToDbl(Function<? super T, ? extends double[]> toDoubleArrayMapper) {
        return t -> t == null ? DoubleStream.empty() : defaultDblStream(toDoubleArrayMapper.apply(t));
    }

    public static <U> DoubleFunction<DoubleObjectPair<U>> dblPairOf(DoubleFunction<? extends U> rightFunction) {
        return d -> DoubleObjectPair.of(d, rightFunction.apply(d));
    }

    public static <U, V> DoubleFunction<Pair<U, V>> dblPairOf(DblKeyValueMapper<U, V> keyValueMapper) {
        return dblPairOf(keyValueMapper.getKeyMapper(), keyValueMapper.getValueMapper());
    }

    public static <U, V> DoubleFunction<Pair<U, V>> dblPairOf(DoubleFunction<? extends U> leftFunction, DoubleFunction<? extends V> rightFunction) {
        return d -> Pair.of(leftFunction.apply(d), rightFunction.apply(d));
    }

    public static <R> DoubleFunction<DoubleObjectPair<R>> dblPairWith(List<R> pairedList) {
        List<R> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return d -> {
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? DoubleObjectPair.of(d, nonNullList.get(i)) : DoubleObjectPair.of(d, null);
        };
    }

    public static <U, V> DoubleFunction<Pair<U, V>> dblPairWith(DoubleFunction<? extends U> function, List<V> pairedList) {
        List<V> nonNullList = pairedList == null ? new ArrayList<>() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return d -> {
            U extracted = function.apply(d);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    public static DoubleFunction<DoubleIndexPair> dblPairWithIndex() {
        AtomicInteger idx = new AtomicInteger();
        return d -> DoubleIndexPair.of(d, idx.getAndIncrement());
    }

    public static <R> DoubleFunction<Pair<R, Integer>> dblPairWithIndex(DoubleFunction<? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return d -> Pair.of(function.apply(d), idx.getAndIncrement());
    }

    public static <R> DoubleFunction<R> dblTernary(DoublePredicate predicate, DoubleTernaryMapper<R> ternaryMapper) {
        return d -> predicate.test(d) ? ternaryMapper.getTrueMapper().apply(d) : ternaryMapper.getFalseMapper().apply(d);
    }

    public static <R> DoubleTernaryMapper<R> dblTernaryMapper(DoubleFunction<R> trueExtractor, DoubleFunction<R> falseExtractor) {
        return DoubleTernaryMapper.of(trueExtractor, falseExtractor);
    }

    public static <K, V> DblKeyValueMapper<K, V> dblKeyValueMapper(DoubleFunction<K> keyMapper, DoubleFunction<V> valueMapper) {
        return DblKeyValueMapper.of(keyMapper, valueMapper);
    }
}
