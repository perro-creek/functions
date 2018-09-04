package org.hringsak.functions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

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
     * @return A LongFunction taking a single parameter of type int, and returning a result of type &lt;R&gt;.
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
}
