package org.hringsak.functions.consumer;

import org.hringsak.functions.mapper.MapperUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ToDoubleFunction;

/**
 * Methods that build consumers useful in many different situations, particularly in Java streams.  This class deals
 * specifically with consumers involving primitive <code>double</code> types.
 */
public final class DblConsumerUtils {

    private DblConsumerUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>double</code> and returns void, to
     * a <code>DoubleConsumer</code>. Everything said about the {@link ConsumerUtils#consumer(Consumer)} method applies
     * here. The difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a
     * primitive <code>double</code> instead. It may be harder to think of a situation where this overload would be
     * useful, but this method is included for sake of completeness.
     *
     * @param consumer A method reference to be cast to a DoubleConsumer.
     * @return A method reference cast to a DoubleConsumer.
     */
    @SuppressWarnings("unused")
    public static DoubleConsumer dblConsumer(DoubleConsumer consumer) {
        return consumer;
    }

    /**
     * Simply casts a method reference, which takes no parameters and returns void, to a <code>DoubleConsumer</code>.
     * This could be useful in a situation where you have a method that takes no parameters, and has no return value,
     * which you would like to call in a stream, for example, in the <code>DoubleStream.forEach(...)</code> method. In
     * the following example, assume that <code>processDouble()</code> takes a single <code>double</code> parameter and
     * has no return value, and <code>logCurrentState()</code> takes no parameter and has no return value:
     * <pre>
     *     double[] doubles = ...
     *     Arrays.stream(doubles).forEach(DblConsumerUtils.dblConsumer(this::processDouble)
     *             .andThen(DblConsumerUtils.dblConsumer(this::logCurrentState)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     Arrays.stream(doubles).forEach(dblConsumer(this::processDouble)
     *             .andThen(dblConsumer(this::logCurrentState)));
     * </pre>
     * Admittedly, the fact that we are using <code>forEach(...)</code> here, using object state for the logging, and
     * not returning any values, makes this code imperative, and not functional. However, casting a
     * <code>Runnable</code> to a <code>DoubleConsumer</code> does come in handy at times.
     * <p>
     * Note that this method can also be used to cast a <code>Supplier</code> method reference to a
     * <code>DoubleConsumer</code>, that is a reference to a method that takes no parameters, and returns an object of
     * any type.
     *
     * @param runnable A method reference taking no parameters and having a return value of any type, including no
     *                 return value, to be cast to a DoubleConsumer.
     * @return A Runnable or Supplier method reference cast to a DoubleConsumer.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static DoubleConsumer dblConsumer(Runnable runnable) {
        return d -> runnable.run();
    }

    /**
     * Builds a <code>DoubleConsumer</code> from a passed <code>BiConsumer</code>. Everything said about the
     * {@link ConsumerUtils#consumer(BiConsumer, Object)} method applies here. The difference is that instead of an
     * element of type &lt;T&gt; being streamed through, it would be a primitive <code>double</code> instead. It may be
     * harder to think of a situation where this overload would be useful, but this method is included for sake of
     * completeness.
     * <p>
     * One note about using the Java <code>DoubleConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, DoubleConsumer is expected to operate via side-effects."
     *
     * @param biConsumer A method reference which is a BiConsumer, taking two parameters - the first of type double, and
     *                   the second of type &lt;U&gt;, which can be any type. The method reference will be converted by
     *                   this method to a DoubleConsumer, taking a single parameter of type double. Behind the scenes,
     *                   this BiConsumer will be called, passing the constant value to each invocation as the second
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biConsumer as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biConsumer.
     * @return A DoubleConsumer taking a single parameter of type double.
     */
    public static <U> DoubleConsumer dblConsumer(BiConsumer<Double, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    /**
     * Builds a <code>DoubleConsumer</code> from a passed <code>BiConsumer</code>. Everything said about the
     * {@link ConsumerUtils#inverseConsumer(BiConsumer, Object)} method applies here. The difference is that instead of
     * an element of type &lt;T&gt; being streamed through, it would be a primitive <code>double</code> instead. It may
     * be harder to think of a situation where this overload would be useful, but this method is included for sake of
     * completeness.
     * <p>
     * One note about using the Java <code>DoubleConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, DoubleConsumer is expected to operate via side-effects."
     *
     * @param biConsumer A method reference which is a BiConsumer, taking two parameters - the first of type &lt;U&gt;
     *                   which can be any type, and the second of type double. The method reference will be converted by
     *                   this method to a DoubleConsumer, taking a single parameter of type double. Behind the scenes,
     *                   this BiConsumer will be called, passing the constant value to each invocation as the first
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biConsumer as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biConsumer.
     * @return A DoubleConsumer taking a single parameter of type double.
     */
    public static <U> DoubleConsumer inverseDblConsumer(ObjDoubleConsumer<? super U> biConsumer, U value) {
        return t -> biConsumer.accept(value, t);
    }

    /**
     * This method is a variation of the <code>consumer(...)</code> methods that has some special properties. First, it
     * is assumed that a setter method is being called on the target element, so there is a null check to make sure that
     * it is an object instance. If not, the <code>Consumer</code> built by this method simply returns. Second, it takes
     * a function that, given the target element, it returns the value to be passed to the setter. This  means that any
     * of the methods of {@link MapperUtils} are fair game to be used to build that function. For example, suppose we
     * want to write a method that, given a collection of <code>Widget</code> instances, it will calculate and set a
     * price for each of them. We could use this method to help accomplish that in the following contrived example
     * (also note that is generally not a good idea to use a <code>double</code> for currency amounts):
     * <pre>
     *     private void buildWidgetPrices(Collection&lt;Widget&gt; widgets, String customerId) {
     *         double discount = getCustomerDiscount(customerId);
     *         widgets.forEach(ConsumerUtils.setter(Widget::setPrice, MapperUtils.mapper(this::calculatePrice, discount)));
     *     }
     *
     *     private double calculatePrice(Widget widget, double discount) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         widgets.forEach(setter(Widget::setPrice, mapper(this::calculatePrice, discount)));
     * </pre>
     *
     * @param consumer A setter method reference from the class of the target element. It is a BiConsumer because the
     *                 first parameter will be the element itself, and the second parameter is the value to be set on
     *                 it.
     * @param function A function that, given the target element, returns the double value to be set.
     * @param <T>      The type of the target input element.
     * @return A Consumer representing the invocation of a setter method on a target element.
     */
    public static <T> Consumer<T> dblSetter(BiConsumer<? super T, Double> consumer, ToDoubleFunction<T> function) {
        return t -> {
            if (t != null) {
                consumer.accept(t, function.applyAsDouble(t));
            }
        };
    }

    /**
     * Applies a <code>ToDoubleFunction</code> to a target element, before passing its result to a
     * <code>DoubleConsumer</code>. For example, let's say that we have a collection of order line items, and we want to
     * call a validation method to make sure that the discount for the current line item is appropriate for a given
     * customer (the OrderLineItem.getDiscount() method returns a double):
     * <pre>
     *     private void validateLineItems(Collection&lt;OrderLineItem&gt; lineItems, String customerId) {
     *         lineItems.forEach(DblConsumerUtils.mapToDblAndConsume(OrderLineItem::getDiscount, DblConsumerUtils.dblConsumer(this::validateDiscount, customerId)));
     *     }
     *
     *     private String validateDiscount(double discount, String customerId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         lineItems.forEach(mapToDblAndConsume(OrderLineItem::getDiscount, dblConsumer(this::validateDiscount, customerId)));
     * </pre>
     * Note that the same thing could be done like this:
     * <pre>
     *         lineItems.stream()
     *             .map(OrderLineItem::getDiscount)
     *             .forEach(dblConsumer(this::validateDiscount, customerId));
     * </pre>
     * Which of the above is more concise and readable is up to the individual developer, but this method provides an
     * alternative way of accomplishing the above validation.
     *
     * @param function A ToDoubleFunction to be applied to a target element.
     * @param consumer A DoubleConsumer to be applied to the result of a ToDoubleFunction.
     * @param <T>      The type of the target input element.
     * @return A Consumer taking a single parameter of type &lt;T&gt;.
     */
    public static <T> Consumer<T> mapToDblAndConsume(ToDoubleFunction<? super T> function, DoubleConsumer consumer) {
        return t -> {
            if (t != null) {
                double value = function.applyAsDouble(t);
                consumer.accept(value);
            }
        };
    }

    /**
     * Builds a <code>DoubleConsumer</code> from a passed <code>DoubleFunction</code> and a <code>Consumer</code>.
     * Everything said about the {@link ConsumerUtils#mapAndConsume(Function, Consumer)} method applies here. The
     * difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a primitive
     * <code>double</code> instead. Also, this method takes a <code>DoubleFunction</code> rather than a generic
     * <code>Function</code>. It may be harder to think of a situation where this method would be useful, but it is
     * included for sake of completeness.
     * <p>
     * One note about using the Java <code>DoubleConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, DoubleConsumer is expected to operate via side-effects."
     *
     * @param function A method reference which is a DoubleFunction, taking a single double parameter, and returning a
     *                 value of type &lt;U&gt;.
     * @param consumer A Consumer&lt;U&gt;, which will be passed the result of a DoubleFunction.
     * @param <U>      The type of the result of a DoubleFunction.
     * @return A DoubleConsumer taking a single parameter of type double.
     */
    public static <U> DoubleConsumer dblMapAndConsume(DoubleFunction<? extends U> function, Consumer<U> consumer) {
        return d -> consumer.accept(function.apply(d));
    }
}
