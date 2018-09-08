package org.hringsak.functions.longs;

import org.hringsak.functions.doubles.DblConsumerUtils;
import org.hringsak.functions.objects.ConsumerUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

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
}
