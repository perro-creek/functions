package org.hringsak.functions.supplier;

import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * Methods that build suppliers, specifically those involving primitive double types.
 */
public final class DblSupplierUtils {

    private DblSupplierUtils() {
    }

    /**
     * Builds a <code>DoubleSupplier</code> from a passed <code>ToDoubleFunction</code> which takes a parameter of type
     * &lt;T&gt;. Everything said about the {@link SupplierUtils#supplier(Function, Object)} method applies here. The
     * difference is that instead of the supplier returning a result of type &lt;R&gt;, it returns a primitive
     * <code>double</code> instead. Suppliers are useful in a variety of situations such as lazy loading or evaluation,
     * including in logging as well as in working with the <code>Optional</code> class. This method might also be useful
     * in a situation where, let's say you have a utility method that takes an object <code>Double</code>, and a
     * <code>DoubleSupplier</code> to return a definite result when the passed <code>Double</code> value is
     * <code>null</code>:
     * <pre>
     *     public final class DoubleUtils {
     *         ...
     *         public static double getDoubleWithDefault(Double value, DoubleSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsDouble() : value.doubleValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary:
     * <pre>
     *     public double getCustomerDiscount(String customerId) {
     *         Map&lt;String, Double&gt; discountByCustId = ...
     *         DoubleSupplier discountSupplier = DblSupplierUtils.dblSupplier(this::getCustomerDiscount, customerId);
     *         return DoubleUtils.getDoubleWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private double getCustomerDiscount(String customerId) {
     *         ...
     *     }
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes one parameter, then this
     * method can be used to convert a function to a supplier.
     *
     * @param function A method reference which is a ToDoubleFunction, taking a parameter of type &lt;T&gt;.
     * @param value    A constant value, in that it will be passed to every invocation of the passed function as the
     *                 single parameter to it, and will have the same value for each of them.
     * @param <T>      The type of the constant value to be passed as the single parameter to each invocation of
     *                 function.
     * @return A DoubleSupplier.
     */
    public static <T> DoubleSupplier dblSupplier(ToDoubleFunction<T> function, T value) {
        return () -> function.applyAsDouble(value);
    }

    /**
     * Builds a <code>DoubleSupplier</code> from a passed <code>BiFunction</code> which takes parameters of type
     * &lt;T&gt; and &lt;U&gt;, and returns a <code>Double</code>. Everything said about the
     * {@link SupplierUtils#supplier(BiFunction, ConstantValues)} method applies here. The difference is that instead of
     * the supplier returning values of type &lt;R&gt;, it would return a primitive <code>double</code> instead.
     * Suppliers are useful in a variety of situations such as lazy loading or evaluation, including in logging as well
     * as in working with the <code>Optional</code> class. This method might be useful in a situation where, let's say
     * you have a utility method that takes an object <code>Double</code>, and a <code>DoubleSupplier</code> to return
     * a definite result when the passed <code>Double</code> value is <code>null</code>:
     * <pre>
     *     public final class DoubleUtils {
     *         ...
     *         public static double getDoubleWithDefault(Double value, DoubleSupplier defaultSupplier) {
     *             return value == null ? defaultSupplier.getAsDouble() : value.doubleValue();
     *         }
     *         ...
     *     }
     * </pre>
     * Let's assume that, in order to get the default, you have to call a separate micro-service, or retrieve the value
     * from the database. In that case, you would want to do that only if the value was actually <code>null</code>, so
     * you pass a supplier which only gets invoked when necessary:
     * <pre>
     *     public double getCustomerDiscount(String customerId, String purchaseOrderId) {
     *         Map&lt;String, Double&gt; discountByCustId = ...
     *         DoubleSupplier discountSupplier = DoubleSupplierUtils.dblSupplier(this::getDiscount,
     *                 SupplierUtils.constantValues(customerId, purchaseOrderId));
     *         return DoubleUtils.getDoubleWithDefault(discountByCustId.get(custId), discountSupplier);
     *     }
     *
     *     private double getDiscount(String custId, String purchaseOrderId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     ...
     *         DoubleSupplier discountSupplier = dblSupplier(this::getDiscount, constantValues(customerId, purchaseOrderId));
     *     ...
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might
     * take, whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If
     * the method reference takes zero parameters, then there's no problem, but if it takes two parameters, then this
     * method can be used to convert a bi-function into a supplier.
     *
     * @param function  A method reference which is a ToDoubleBiFunction, taking parameters of type &lt;T&gt; and
     *                  &lt;U&gt;, and returning a double.
     * @param constants A pair of constant values, in that they will be passed to every invocation of the passed
     *                  function as the first and second parameters to it, and will have the same value for each of
     *                  them.
     * @param <T>       The type of the first constant value to be passed as the first parameter to each invocation of
     *                  function.
     * @param <U>       The type of the second constant value to be passed as the second parameter to each invocation of
     *                  function.
     * @return A DoubleSupplier.
     */
    public static <T, U> DoubleSupplier dblSupplier(ToDoubleBiFunction<T, U> function, ConstantValues<T, U> constants) {
        return () -> function.applyAsDouble(constants.getLeft(), constants.getRight());
    }
}
