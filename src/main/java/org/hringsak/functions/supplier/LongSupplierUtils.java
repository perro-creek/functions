package org.hringsak.functions.supplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

/**
 * Methods that build suppliers, specifically those involving primitive long types.
 */
public final class LongSupplierUtils {

    private LongSupplierUtils() {
    }

    /**
     * Builds a <code>LongSupplier</code> from a passed <code>ToLongFunction</code> which takes a parameter of type
     * &lt;T&gt;. Everything said about the {@link SupplierUtils#supplier(Function, Object)} method applies here. The
     * difference is that instead of the supplier returning a result of type &lt;R&gt;, it returns a primitive
     * <code>long</code> instead. Suppliers are useful in a variety of situations such as lazy loading or evaluation,
     * including in logging as well as in working with the <code>Optional</code> class. This method might also be useful
     * in a situation where, let's say you have a utility method that takes an object <code>Long</code>, and a
     * <code>LongSupplier</code> to return a definite result when the passed <code>Long</code> value is
     * <code>null</code>:
     * <pre>
     *     public final class LongUtils {
     *         ...
     *         public static long getLongWithDefault(Long value, LongSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsLong() : value.longValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary (assume the discount is represented as an integral
     * number of cents):
     * <pre>
     *     public long getCustomerDiscount(String customerId) {
     *         Map&lt;String, Long&gt; discountByCustId = ...
     *         LongSupplier discountSupplier = LongSupplierUtils.longSupplier(this::getCustomerDiscount, customerId);
     *         return LongUtils.getLongWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private long getCustomerDiscount(String customerId) {
     *         ...
     *     }
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes one parameter, then this
     * method can be used to convert a function to a supplier.
     *
     * @param function A method reference which is a ToLongFunction, taking a parameter of type &lt;T&gt;.
     * @param value    A constant value, in that it will be passed to every invocation of the passed function as the
     *                 single parameter to it, and will have the same value for each of them.
     * @param <T>      The type of the constant value to be passed as the single parameter to each invocation of
     *                 function.
     * @return A LongSupplier.
     */
    public static <T> LongSupplier longSupplier(ToLongFunction<T> function, T value) {
        return () -> function.applyAsLong(value);
    }

    /**
     * Builds a <code>LongSupplier</code> from a passed <code>BiFunction</code> which takes parameters of type
     * &lt;T&gt; and &lt;U&gt;, and returns a <code>Long</code>. Everything said about the
     * {@link SupplierUtils#supplier(BiFunction, ConstantValues)} method applies here. The difference is that instead of
     * the supplier returning values of type &lt;R&gt;, it would return a primitive <code>long</code> instead. Suppliers
     * are useful in a variety of situations such as lazy loading or evaluation, including in logging as well as in
     * working with the <code>Optional</code> class. This method might be useful in a situation where, let's say you
     * have a utility method that takes an object <code>Long</code>, and a <code>LongSupplier</code> to return a
     * definite result when the passed <code>Long</code> value is <code>null</code>:
     * <pre>
     *     public final class LongUtils {
     *         ...
     *         public static long getLongWithDefault(Long value, LongSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsLong() : value.longValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary (assume the discount is represented as an integral
     * number of cents):
     * <pre>
     *     public long getCustomerDiscount(String customerId, String purchaseOrderId) {
     *         Map&lt;String, Long&gt; discountByCustId = ...
     *         LongSupplier discountSupplier = LongSupplierUtils.longSupplier(this::getDiscount,
     *                 SupplierUtils.constantValues(customerId, purchaseOrderId));
     *         return LongUtils.getLongWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private long getDiscount(String custId, String purchaseOrderId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     ...
     *         LongSupplier discountSupplier = longSupplier(this::getDiscount, constantValues(customerId, purchaseOrderId));
     *     ...
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes two parameters, then this
     * method can be used to convert a bi-function into a supplier.
     *
     * @param function  A method reference which is a ToLongBiFunction, taking parameters of type &lt;T&gt; and
     *                  &lt;U&gt;, and returning a long.
     * @param constants A pair of constant values, in that they will be passed to every invocation of the passed
     *                  function as the first and second parameters to it, and will have the same value for each of
     *                  them.
     * @param <T>       The type of the first constant value to be passed as the first parameter to each invocation of
     *                  function.
     * @param <U>       The type of the second constant value to be passed as the second parameter to each invocation of
     *                  function.
     * @return A LongSupplier.
     */
    public static <T, U> LongSupplier longSupplier(ToLongBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsLong(constants.getLeft(), constants.getRight());
    }
}
