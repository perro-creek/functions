package org.hringsak.functions;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Methods that build suppliers, specifically those involving primitive boolean types.
 */
public final class BooleanSupplierUtils {

    private BooleanSupplierUtils() {
    }

    /**
     * Builds a <code>BooleanSupplier</code> from a passed <code>Function</code> which takes a parameter of type
     * &lt;T&gt;, and returns a <code>Boolean</code>. Everything said about the
     * {@link SupplierUtils#supplier(Function, Object)} method applies here. The difference is that instead of returning
     * a result of type &lt;T&gt;, it would return a primitive <code>boolean</code> instead. Suppliers are useful in a
     * variety of situations such as lazy loading or evaluation, including in logging as well as in working with the
     * <code>Optional</code> class. This method might be useful in a situation where, let's say you have a utility
     * method that takes an object <code>Boolean</code>, and a <code>BooleanSupplier</code> to return a definite result
     * when the passed <code>Boolean</code> value is null:
     * <pre>
     *     public final class BooleanUtils {
     *         ...
     *         public static boolean getFlagWithDefault(Boolean flag, BooleanSupplier defaultSupplier) {
     *             return flag == null ? defaultSupplier.getAsBoolean() : flag.booleanValue();
     *         }
     *         ...
     *     }
     * </pre>
     * This method might come in handy when, in order to get the default, you have to call a separate micro-service, or
     * retrieve the flag from the database. In that case, you would want to do that only if the value was actually null,
     * so you pass a supplier which only gets invoked when necessary:
     * <pre>
     *     public boolean getCustomerDiscount() {
     *         Map&lt;String, Boolean&gt; discountByCustId = ...
     *         String customerId = ...
     *         BooleanSupplier flagSupplier = BooleanSupplierUtils.booleanSupplier(this::getDiscountFlag, customerId);
     *         return BooleanUtils.getFlagWithDefault(discountByCustId.get(custId), flagSupplier);
     *     }
     *
     *     private boolean getDiscountFlag(String custId) {
     *         ...
     *     }
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might take,
     * whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If the
     * method reference takes zero parameters, then there's no problem, but if it takes one parameter, then this method
     * can be used to convert a function to a supplier.
     *
     * @param function A method reference which is a Function, taking a parameter of type &lt;T&gt;, and returning a
     *                 Boolean.
     * @param value    A constant value, in that it will be passed to every invocation of the passed function as the
     *                 single parameter to it, and will have the same value for each of them.
     * @param <T>      The type of the constant value to be passed as the single parameter to each invocation of
     *                 function.
     * @return A BooleanSupplier taking a single parameter of type double, and returning a result of type &lt;R&gt;.
     */
    public static <T> BooleanSupplier booleanSupplier(Function<T, Boolean> function, T value) {
        return () -> function.apply(value);
    }

    /**
     * Builds a <code>BooleanSupplier</code> from a passed <code>BiFunction</code> which takes parameters of type
     * &lt;T&gt; and &lt;U&gt;, and returns a <code>Boolean</code>. Everything said about the
     * {@link SupplierUtils#supplier(BiFunction, ConstantValues)} method applies here. The difference is that instead of
     * returning a result of type &lt;R&gt;, it would return a primitive <code>boolean</code> instead. Suppliers are
     * useful in a variety of situations such as lazy loading or evaluation, including in logging as well as in working
     * with the <code>Optional</code> class. This method might be useful in a situation where, let's say you have a
     * utility method that takes an object <code>Boolean</code>, and a <code>BooleanSupplier</code> to return a definite
     * result when the passed <code>Boolean</code> value is null:
     * <pre>
     *     public final class BooleanUtils {
     *         ...
     *         public static boolean getFlagWithDefault(Boolean flag, BooleanSupplier defaultSupplier) {
     *             return flag == null ? defaultSupplier.getAsBoolean() : flag.booleanValue();
     *         }
     *         ...
     *     }
     * </pre>
     * This method might come in handy when, in order to get the default, you have to call a separate micro-service, or
     * retrieve the flag from the database. In that case, you would want to do that only if the value was actually null,
     * so you pass a supplier which only gets invoked when necessary:
     * <pre>
     *     public boolean getCustomerDiscount() {
     *         Map&lt;String, Boolean&gt; discountByCustId = ...
     *         String customerId = ...
     *         String purchaseOrderId = ...
     *         BooleanSupplier flagSupplier = BooleanSupplierUtils.booleanSupplier(this::getDiscountFlag,
     *                 SupplierUtils.constantValues(customerId, purchaseOrderId));
     *         return BooleanUtils.getFlagWithDefault(discountByCustId.get(custId), flagSupplier);
     *     }
     *
     *     private boolean getDiscountFlag(String custId, String purchaseOrderId) {
     *         ...
     *     }
     * </pre>
     * By making judicious use of static imports, we could reduce the clutter of the call to create the supplier to:
     * <pre>
     *     ...
     *     BooleanSupplier flagSupplier = booleanSupplier(this::getDiscountFlag, constantValues(customerId, purchaseOrderId));
     *     ...
     * </pre>
     * A number of generic utility methods take suppliers, because of the lazy loading/evaluation features they provide.
     * An issue with their use is, that because they don't know how many parameters a passed method reference might take,
     * whether zero, one, or more, they just end up taking a supplier with some kind of generic return value. If the
     * method reference takes zero parameters, then there's no problem, but if it takes two parameters, then this method
     * can be used to convert a bi-function to a supplier.
     *
     * @param biFunction A method reference which is a Function, taking a parameter of type &lt;T&gt;, and returning a
     *                   Boolean.
     * @param constants  A pair of constant values, in that they will be passed to every invocation of the passed
     *                   function as the first and second parameters to it, and will have the same value for each of
     *                   them.
     * @param <T>        The type of the first constant value to be passed as the first parameter to each invocation of
     *                   biFunction.
     * @param <U>        The type of the second constant value to be passed as the second parameter to each invocation
     *                   of biFunction.
     * @return A BooleanSupplier taking two parameters of type &lt;T&gt; and &lt;U&gt;, and returning a result of type
     * Boolean.
     */
    public static <T, U> BooleanSupplier booleanSupplier(BiFunction<T, U, Boolean> biFunction, ConstantValues<T, U> constants) {
        return () -> biFunction.apply(constants.getLeft(), constants.getRight());
    }
}
