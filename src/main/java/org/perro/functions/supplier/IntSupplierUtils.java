package org.perro.functions.supplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

import static org.perro.functions.supplier.LazyIntegerSupplier.newLazyIntegerSupplier;

/**
 * Methods that build suppliers, specifically those involving primitive int types.
 */
public final class IntSupplierUtils {

    private IntSupplierUtils() {
    }

    /**
     * Builds an <code>IntSupplier</code> from a passed <code>ToIntFunction</code> which takes a parameter of type
     * &lt;T&gt;. Everything said about the {@link SupplierUtils#supplier(Function, Object)} method applies here. The
     * difference is that instead of the supplier returning a result of type &lt;R&gt;, it returns a primitive
     * <code>int</code> instead. Suppliers are useful in a variety of situations such as lazy loading or evaluation,
     * including in logging as well as in working with the <code>Optional</code> class. This method might also be useful
     * in a situation where, let's say you have a utility method that takes an object <code>Integer</code>, and an
     * <code>IntSupplier</code> to return a definite result when the passed <code>Integer</code> value is
     * <code>null</code>:
     * <pre>
     *     public final class IntegerUtils {
     *         ...
     *         public static int getIntWithDefault(Integer value, IntSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsInt() : value.intValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary (assume the discount is represented as an integral
     * number of cents):
     * <pre>
     *     public int getCustomerDiscount(String customerId) {
     *         Map&lt;String, Integer&gt; discountByCustId = ...
     *         IntSupplier discountSupplier = IntSupplierUtils.intSupplier(this::getCustomerDiscount, customerId);
     *         return IntegerUtils.getIntWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private int getCustomerDiscount(String customerId) {
     *         ...
     *     }
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes one parameter, then this
     * method can be used to convert a function to a supplier.
     *
     * @param function A method reference which is a ToIntFunction, taking a parameter of type &lt;T&gt;.
     * @param value    A constant value, in that it will be passed to every invocation of the passed function as the
     *                 single parameter to it, and will have the same value for each of them.
     * @param <T>      The type of the constant value to be passed as the single parameter to each invocation of
     *                 function.
     * @return An IntSupplier.
     */
    public static <T> IntSupplier intSupplier(ToIntFunction<T> function, T value) {
        return () -> function.applyAsInt(value);
    }

    /**
     * Builds an <code>IntSupplier</code> from a passed <code>BiFunction</code> which takes parameters of type
     * &lt;T&gt; and &lt;U&gt;, and returns an <code>Integer</code>. Everything said about the
     * {@link SupplierUtils#supplier(BiFunction, ConstantValues)} method applies here. The difference is that instead of
     * the supplier returning values of type &lt;R&gt;, it would return a primitive <code>int</code> instead. Suppliers
     * are useful in a variety of situations such as lazy loading or evaluation, including in logging as well as in
     * working with the <code>Optional</code> class. This method might be useful in a situation where, let's say you
     * have a utility method that takes an object <code>Double</code>, and an <code>IntSupplier</code> to return a
     * definite result when the passed <code>Integer</code> value is <code>null</code> (assume the discount is
     * represented as an integral number of cents):
     * <pre>
     *     public final class IntegerUtils {
     *         ...
     *         public static int getIntWithDefault(Integer value, IntSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsInt() : value.intValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary (assume the discount is represented as an integral
     * number of cents):
     * <pre>
     *     public int getCustomerDiscount(String customerId, String purchaseOrderId) {
     *         Map&lt;String, Integer&gt; discountByCustId = ...
     *         IntSupplier discountSupplier = IntSupplierUtils.intSupplier(this::getDiscount,
     *                 SupplierUtils.constantValues(customerId, purchaseOrderId));
     *         return IntegerUtils.getIntWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private int getDiscount(String custId, String purchaseOrderId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     ...
     *         IntSupplier discountSupplier = intSupplier(this::getDiscount, constantValues(customerId, purchaseOrderId));
     *     ...
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes two parameters, then this
     * method can be used to convert a bi-function into a supplier.
     *
     * @param function  A method reference which is a ToIntBiFunction, taking parameters of type &lt;T&gt; and
     *                  &lt;U&gt;, and returning an int.
     * @param constants A pair of constant values, in that they will be passed to every invocation of the passed
     *                  function as the first and second parameters to it, and will have the same value for each of
     *                  them.
     * @param <T>       The type of the first constant value to be passed as the first parameter to each invocation of
     *                  function.
     * @param <U>       The type of the second constant value to be passed as the second parameter to each invocation of
     *                  function.
     * @return An IntSupplier.
     */
    public static <T, U> IntSupplier intSupplier(ToIntBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsInt(constants.getLeft(), constants.getRight());
    }

    /**
     * Takes an <code>IntSupplier</code> and turns it into a lazy supplier. The behavior is that the passed supplier
     * is called only the first time a value is retrieved, and caches its result for subsequent invocations. Useful in
     * many situations where retrieving a value is an expensive operation, such as retrieving an object from a database,
     * or calling a micro-service.
     *
     * @param supplier A supplier to be called only once, and whose result will be cached.
     * @return A lazy IntSupplier of a value.
     */
    public static IntSupplier lazyIntSupplier(IntSupplier supplier) {
        return newLazyIntegerSupplier(supplier);
    }

    /**
     * First creates a supplier using the {@link #intSupplier(ToIntFunction, Object)} method, and turns it into a lazy
     * supplier. The behavior is that the passed supplier is called only the first time a value is retrieved, and caches
     * its result for subsequent invocations. Useful in many situations where retrieving a value is an expensive
     * operation, such as retrieving an object from a database, or calling a micro-service.
     *
     * @param function A ToIntFunction to be used to build a supplier of a int value.
     * @param value    A constant value of type &lt;T&gt; to be passed to the single invocation of the above
     *                 ToIntFunction.
     * @param <T>      The type of the parameter to the passed ToIntFunction. Also the type of the constant value
     *                 passed to this method.
     * @return A lazy IntSupplier built from the passed ToIntFunction and constant value of type &lt;T&gt;.
     */
    public static <T> IntSupplier lazyIntSupplier(ToIntFunction<T> function, T value) {
        return newLazyIntegerSupplier(intSupplier(function, value));
    }

    /**
     * First creates a supplier using the {@link #intSupplier(ToIntBiFunction, ConstantValues)} method, and turns it
     * into a lazy supplier. The behavior is that the passed supplier is called only the first time a value is
     * retrieved, and caches its result for subsequent invocations. Useful in many situations where retrieving a value
     * is an expensive operation, such as retrieving an object from a database, or calling a micro-service.
     *
     * @param function  A ToIntBiFunction taking arguments of type &lt;T&gt; and &lt;U&gt;, to be used to build a
     *                  lazy supplier of an int value.
     * @param constants An object representing a pair of constant values of type &lt;T&gt; and &lt;U&gt;, to be passed
     *                  to the single invocation of the above ToIntBiFunction. This value should be supplied via a call
     *                  to the {@link SupplierUtils#constantValues(Object, Object)} method.
     * @param <T>       The type of the first parameter to the passed ToIntBiFunction. Also the type of the first
     *                  constant value in the constants pair passed to this method.
     * @param <U>       The type of the second parameter to the passed ToIntBiFunction. Also the type of the second
     *                  constant value in the constants pair passed to this method.
     * @return A lazy IntSupplier built from the passed ToIntBiFunction and object representing a pair of constant
     * values of type &lt;T&gt; and &lt;U&gt;.
     */
    public static <T, U> IntSupplier lazyIntSupplier(ToIntBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return newLazyIntegerSupplier(intSupplier(function, constants));
    }
}
