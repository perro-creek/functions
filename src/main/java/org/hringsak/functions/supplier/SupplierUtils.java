package org.hringsak.functions.supplier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Methods that build suppliers, specifically those involving arbitrary object types.
 */
public final class SupplierUtils {

    private SupplierUtils() {
    }

    /**
     * Builds a <code>Supplier</code> of objects of type &lt;R&gt; from a passed <code>Function</code> taking a
     * parameter of type &lt;T&gt;. This can be very useful in the situation where you have a reference to a method that
     * takes an object of type &lt;T&gt;, and an object to be passed to that method reference, which will be used for
     * every invocation of it. In the following example we want to find the first order line item in an order, with a
     * quantity greater than zero (assume there are static imports for the classes in this library):
     * <pre>
     *     private OrderLineItem getFirstItemWithQuantity(Order order) {
     *         String errorMessage = "Order must contain a line item with non-zero quantity";
     *         return defaultStream(order.getLineItems())
     *             .filter(intGt(OrderLineItem::getQuantity))
     *             .findFirst()
     *             .orElseThrow(supplier(IllegalArgumentException::new, errorMessage));
     *     }
     * </pre>
     *
     * @param function A Function to be used to build a supplier of values of type &lt;R&gt;.
     * @param value    A constant value of type &lt;T&gt; to be passed to every invocation of the above Function.
     * @param <T>      The type of the parameter to the passed Function. Also the type of the constant value passed to
     *                 this method.
     * @param <R>      The return type of the passed Function, and the Supplier built by this method.
     * @return A Supplier built from the passed Function and constant value of type &lt;T&gt;.
     */
    public static <T, R> Supplier<R> supplier(Function<T, R> function, T value) {
        return () -> function.apply(value);
    }

    /**
     * Builds a <code>Supplier</code> of objects of type &lt;R&gt; from a passed <code>BiFunction</code> taking
     * parameters of type &lt;T&gt; and &lt;U&gt;. This can be very useful in the situation where you have a reference
     * to a method that takes parameters of type &lt;T&gt; and &lt;U&gt;, and an object representing a pair of constant
     * values to be passed to every invocation of that method reference. In the following example we want to find the
     * first order line item in an order, with a quantity greater than zero (assume there are static imports for the
     * classes in this library):
     * <pre>
     *     private OrderLineItem getFirstItemWithQuantity(Order order, String customerId) {
     *         return defaultStream(order.getLineItems())
     *             .filter(intGt(OrderLineItem::getQuantity))
     *             .findFirst()
     *             .orElseThrow(supplier(this::buildNonZeroQuantityException, order.getId(), customerId));
     *     }
     *
     *     private RuntimeException buildNonZeroQuantityException(String orderId, String customerId) {
     *         String errorMessage = "Order must contain a line item with non-zero quantity - orderId: %s, customerId: %s";
     *         return new IllegalArgumentException(String.format(errorMessage, orderId, customerId));
     *     }
     * </pre>
     *
     * @param function  A BiFunction taking arguments of type &lt;T&gt; and &lt;U&gt;, to be used to build a supplier of
     *                  values of type &lt;R&gt;.
     * @param constants An object representing a pair of constant values of type &lt;T&gt; and &lt;U&gt;, to be passed
     *                  to every invocation of the above BiFunction. This value should be supplied via a call to the
     *                  {@link #constantValues(Object, Object)} method.
     * @param <T>       The type of the first parameter to the passed BiFunction. Also the type of the first constant
     *                  value in the constants pair passed to this method.
     * @param <U>       The type of the second parameter to the passed BiFunction. Also the type of the second constant
     *                  value in the constants pair passed to this method.
     * @param <R>       The return type of the passed Function, and the Supplier built by this method.
     * @return A Supplier built from the passed BiFunction and object representing a pair of constant values of type
     * &lt;T&gt; and &lt;U&gt;.
     */
    public static <T, U, R> Supplier<R> supplier(BiFunction<T, U, R> function, ConstantValues<T, U> constants) {
        return () -> function.apply(constants.getLeft(), constants.getRight());
    }

    /**
     * Builds an object representing a pair of constant values of type &lt;T&gt; and &lt;U&gt; to be used as the second
     * parameter to the above {@link #supplier(BiFunction, ConstantValues)} method.
     *
     * @param left  A value of type &lt;T&gt; representing the first constant in the pair built by this method.
     * @param right A value of type &lt;U&gt; representing the second constant in the pair built by this method.
     * @param <T>   The type of the first constant in the pair built by this method.
     * @param <U>   The type of the second constant in the pair built by this method.
     * @return An object representing a pair of constant values.
     */
    public static <T, U> ConstantValues<T, U> constantValues(T left, U right) {
        return ConstantValues.of(left, right);
    }
}
