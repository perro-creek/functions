package org.perro.functions.consumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

/**
 * Methods that build consumers useful in many different situations, particularly in Java streams.  This class deals
 * specifically with consumers involving primitive <code>long</code> types.
 */
public final class LongConsumerUtils {

    private LongConsumerUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>long</code> and returns void, to
     * a <code>LongConsumer</code>. Everything said about the {@link ConsumerUtils#consumer(Consumer)} method applies
     * here. The difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a
     * primitive <code>long</code> instead. It may be harder to think of a situation where this overload would be
     * useful, but this method is included for sake of completeness.
     *
     * @param consumer A method reference to be cast to a LongConsumer.
     * @return A method reference cast to a LongConsumer.
     */
    @SuppressWarnings("unused")
    public static LongConsumer longConsumer(LongConsumer consumer) {
        return consumer;
    }

    /**
     * Simply casts a method reference, which takes no parameters and returns void, to a <code>LongConsumer</code>. This
     * could be useful in a situation where you have a method that takes no parameters, and has no return value,
     * which you would like to call in a stream, for example, in the <code>LongStream.forEach(...)</code> method. In the
     * following example, assume that <code>processLong()</code> takes a single <code>long</code> parameter and has no
     * return value, and <code>logCurrentState()</code> takes no parameter and has no return value:
     * <pre>
     *     long[] longs = ...
     *     Arrays.stream(longs).forEach(LongConsumerUtils.longConsumer(this::processLong)
     *             .andThen(LongConsumerUtils.longConsumer(this::logCurrentState)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     Arrays.stream(longs).forEach(longConsumer(this::processLong)
     *             .andThen(longConsumer(this::logCurrentState)));
     * </pre>
     * Admittedly, the fact that we are using <code>forEach(...)</code> here, using object state for the logging, and
     * not returning any values, makes this code imperative, and not functional. However, casting a
     * <code>Runnable</code> to a <code>LongConsumer</code> does come in handy at times.
     * <p>
     * Note that this method can also be used to cast a <code>Supplier</code> method reference to a
     * <code>LongConsumer</code>, that is a reference to a method that takes no parameters, and returns an object of any
     * type.
     *
     * @param runnable A method reference taking no parameters and having a return value of any type, including no
     *                 return value, to be cast to a LongConsumer.
     * @return A Runnable or Supplier method reference cast to a LongConsumer.
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static LongConsumer longConsumer(Runnable runnable) {
        return l -> runnable.run();
    }

    /**
     * Builds a <code>LongConsumer</code> from a passed <code>BiConsumer</code>. Everything said about the
     * {@link ConsumerUtils#consumer(BiConsumer, Object)} method applies here. The difference is that instead of an
     * element of type &lt;T&gt; being streamed through, it would be a primitive <code>long</code> instead. It may be
     * harder to think of a situation where this overload would be useful, but this method is included for sake of
     * completeness.
     * <p>
     * One note about using the Java <code>LongConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, LongConsumer is expected to operate via side-effects."
     *
     * @param biConsumer A method reference which is a BiConsumer, taking two parameters - the first of type long, and
     *                   the second of type &lt;U&gt;, which can be any type. The method reference will be converted by
     *                   this method to a LongConsumer, taking a single parameter of type long. Behind the scenes, this
     *                   BiConsumer will be called, passing the constant value to each invocation as the second
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biConsumer as the
     *                   second parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the second parameter to each invocation of
     *                   biConsumer.
     * @return A LongConsumer taking a single parameter of type long.
     */
    public static <U> LongConsumer longConsumer(BiConsumer<Long, ? super U> biConsumer, U value) {
        return t -> biConsumer.accept(t, value);
    }

    /**
     * Builds a <code>LongConsumer</code> from a passed <code>BiConsumer</code>. Everything said about the
     * {@link ConsumerUtils#inverseConsumer(BiConsumer, Object)} method applies here. The difference is that instead of
     * an element of type &lt;T&gt; being streamed through, it would be a primitive <code>long</code> instead. It may be
     * harder to think of a situation where this overload would be useful, but this method is included for sake of
     * completeness.
     * <p>
     * One note about using the Java <code>LongConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, LongConsumer is expected to operate via side-effects."
     *
     * @param biConsumer A method reference which is a BiConsumer, taking two parameters - the first of type &lt;U&gt;
     *                   which can be any type, and the second of type long. The method reference will be converted by
     *                   this method to a LongConsumer, taking a single parameter of type long. Behind the scenes, this
     *                   BiConsumer will be called, passing the constant value to each invocation as the first
     *                   parameter.
     * @param value      A constant value, in that it will be passed to every invocation of the passed biConsumer as the
     *                   first parameter to it, and will have the same value for each of them.
     * @param <U>        The type of the constant value to be passed as the first parameter to each invocation of
     *                   biConsumer.
     * @return A LongConsumer taking a single parameter of type long.
     */
    public static <U> LongConsumer inverseLongConsumer(ObjLongConsumer<? super U> biConsumer, U value) {
        return t -> biConsumer.accept(value, t);
    }

    /**
     * Builds a <code>Consumer</code> from a passed <code>BiConsumer</code>. Everything said about the
     * {@link DblConsumerUtils#dblSetter(BiConsumer, ToDoubleFunction)} applies here. The difference is simply that the
     * <code>extractor</code> function parameter is of type <code>ToLongFunction</code> rather than
     * <code>ToDoubleFunction</code>.
     *
     * @param consumer  A setter method reference from the class of the target element. It is a BiConsumer because the
     *                  first parameter will be the element itself, and the second parameter is the value to be set on
     *                  it.
     * @param extractor An extractor function that, given the target element, returns the long value to be set.
     * @param <T>       The type of the target input element.
     * @return A Consumer representing the invocation of a setter method on a target element.
     */
    public static <T> Consumer<T> longSetter(BiConsumer<? super T, Long> consumer, ToLongFunction<T> extractor) {
        return t -> {
            if (t != null) {
                consumer.accept(t, extractor.applyAsLong(t));
            }
        };
    }

    /**
     * Applies a <code>ToLongFunction</code> to a target element, before passing its result to a
     * <code>LongConsumer</code>. For example, let's say that we have a collection of order line items, and we want to
     * call a validation method to make sure that the quantity for the current line item is appropriate for a given
     * customer (the OrderLineItem.getQuantity() method returns a long):
     * <pre>
     *     private void validateLineItems(Collection&lt;OrderLineItem&gt; lineItems, String customerId) {
     *         lineItems.forEach(LongConsumerUtils.mapToLongAndConsume(OrderLineItem::getQuantity, LongConsumerUtils.longConsumer(this::validateQuantity, customerId)));
     *     }
     *
     *     private String validateQuantity(long quantity, String customerId) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         lineItems.forEach(mapToLongAndConsume(OrderLineItem::getQuantity, longConsumer(this::validateQuantity, customerId)));
     * </pre>
     * Note that the same thing could be done like this:
     * <pre>
     *         lineItems.stream()
     *             .map(OrderLineItem::getQuantity)
     *             .forEach(longConsumer(this::validateQuantity, customerId));
     * </pre>
     * Which of the above is more concise and readable is up to the individual developer, but this method provides an
     * alternative way of accomplishing the above validation.
     *
     * @param function A ToLongFunction to be applied to a target element.
     * @param consumer A LongConsumer to be applied to the result of a ToLongFunction.
     * @param <T>      The type of the target input element.
     * @return A Consumer taking a single parameter of type &lt;T&gt;.
     */
    public static <T> Consumer<T> mapToLongAndConsume(ToLongFunction<? super T> function, LongConsumer consumer) {
        return t -> {
            if (t != null) {
                long value = function.applyAsLong(t);
                consumer.accept(value);
            }
        };
    }

    /**
     * Builds a <code>LongConsumer</code> from a passed <code>LongFunction</code> and a <code>Consumer</code>.
     * Everything said about the {@link ConsumerUtils#mapAndConsume(Function, Consumer)} method applies here. The
     * difference is that instead of an element of type &lt;T&gt; being streamed through, it would be a primitive
     * <code>long</code> instead. Also, this method takes a <code>LongFunction</code> rather than a generic
     * <code>Function</code>. It may be harder to think of a situation where this method would be useful, but it is
     * included for sake of completeness.
     * <p>
     * One note about using the Java <code>LongConsumer</code> interface, as it says in the Javadoc documentation for
     * it, "Unlike most other functional interfaces, LongConsumer is expected to operate via side-effects."
     *
     * @param function A method reference which is a LongFunction, taking a single long parameter, and returning a value
     *                 of type &lt;U&gt;.
     * @param consumer A Consumer&lt;U&gt;, which will be passed the result of a LongFunction.
     * @param <U>      The type of the result of an LongFunction.
     * @return A LongConsumer taking a single parameter of type long.
     */
    public static <U> LongConsumer longMapAndConsume(LongFunction<? extends U> function, Consumer<U> consumer) {
        return l -> consumer.accept(function.apply(l));
    }
}
