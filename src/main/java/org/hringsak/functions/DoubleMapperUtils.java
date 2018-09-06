package org.hringsak.functions;

import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * Methods that build functions to map a target element into another result. This class deals specifically with mapper
 * functions involving primitive <code>double</code> types.
 */
public final class DoubleMapperUtils {

    private DoubleMapperUtils() {
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
     * Note that the difference between this method and {@link #toDoubleMapper(ToDoubleFunction)} is that the
     * <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic type, where
     * the <code>ToDoubleFunction</code> built from {@link #toDoubleMapper(ToDoubleFunction)} takes a generic type and
     * returns a <code>double</code>.
     *
     * @param function A method reference to be cast to a DoubleFunction.
     * @param <R>      The type of the result of the DoubleFunction built by this method.
     * @return A method reference cast to a DoubleFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <R> DoubleFunction<R> doubleMapper(DoubleFunction<R> function) {
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
     * Note that the difference between this method and {@link #toDoubleMapper(ToDoubleBiFunction, Object)} is that the
     * <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic type, where
     * the <code>ToDoubleFunction</code> built from {@link #toDoubleMapper(ToDoubleBiFunction, Object)} takes a generic
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
    public static <U, R> DoubleFunction<R> doubleMapper(BiFunction<Double, ? super U, ? extends R> biFunction, U value) {
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
     * Note that the difference between this method and {@link #inverseToDoubleMapper(ToDoubleBiFunction, Object)} is
     * that the <code>DoubleFunction</code> built from this method takes a <code>double</code> and returns a generic
     * type, where the <code>ToDoubleFunction</code> built from {@link #inverseToDoubleMapper(ToDoubleBiFunction, Object)}
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
    public static <U, R> DoubleFunction<R> inverseDoubleMapper(BiFunction<? super U, Double, ? extends R> biFunction, U value) {
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
     * Note that the difference between this method and {@link #doubleMapper(DoubleFunction)} is that the
     * <code>ToDoubleFunction</code> built from this method takes a generic type and returns a <code>double</code>,
     * where the <code>DoubleFunction</code> built from {@link #doubleMapper(DoubleFunction)} takes a
     * <code>double</code> and returns a generic type.
     *
     * @param function A method reference to be cast to a ToDoubleFunction.
     * @param <T>      The type of the single parameter to the ToDoubleFunction.
     * @return A method reference cast to a ToDoubleFunction.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static <T> ToDoubleFunction<T> toDoubleMapper(ToDoubleFunction<T> function) {
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
    public static <T> ToDoubleFunction<T> toDoubleMapperDefault(ToDoubleFunction<? super T> function, double defaultValue) {
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
     * Note that the difference between this method and {@link #doubleMapper(BiFunction, Object)} is that the
     * <code>ToDoubleFunction</code> built from this method takes a generic type and returns a <code>double</code>,
     * where the <code>DoubleFunction</code> built from {@link #doubleMapper(BiFunction, Object)} takes a
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
    public static <T, U> ToDoubleFunction<T> toDoubleMapper(ToDoubleBiFunction<? super T, ? super U> biFunction, U value) {
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
     * Note that the difference between this method and {@link #inverseDoubleMapper(BiFunction, Object)} is
     * that the <code>ToDoubleFunction</code> built from this method takes a generic type and returns a
     * <code>double</code>, where the <code>DoubleFunction</code> built from
     * {@link #inverseDoubleMapper(BiFunction, Object)} takes a <code>double</code> and returns a generic type.
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
    public static <T, U> ToDoubleFunction<T> inverseToDoubleMapper(ToDoubleBiFunction<? super U, ? super T> biFunction, U value) {
        return t -> biFunction.applyAsDouble(value, t);
    }

    public static DoubleFunction<Pair<Double, Integer>> pairDoubleWithIndex() {
        return pairDoubleWithIndex(Double::valueOf);
    }

    public static <R> DoubleFunction<Pair<R, Integer>> pairDoubleWithIndex(DoubleFunction<? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> Pair.of(function.apply(t), idx.getAndIncrement());
    }
}
