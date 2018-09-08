package org.hringsak.functions.doubles;

import org.hringsak.functions.objects.MapperUtils;
import org.hringsak.functions.objects.ConsumerUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ToDoubleFunction;

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
     * By making judicious use of static imports, we can reduce the clutter in the <code>forEach(...)</code> invocation
     * to:
     * <pre>
     *         widgets.forEach(setter(Widget::setPrice, mapper(this::calculatePrice, discount)));
     * </pre>
     *
     * @param consumer  A setter method reference from the class of the target element. It is a BiConsumer because the
     *                  first parameter will be the element itself, and the second parameter is the value to be set on
     *                  it.
     * @param extractor An extractor function that, given the target element, returns the double value to be set.
     * @param <T>       The type of the target input element.
     * @return A Consumer representing the invocation of a setter method on a target element.
     */
    public static <T> Consumer<T> dblSetter(BiConsumer<? super T, Double> consumer, ToDoubleFunction<T> extractor) {
        return t -> {
            if (t != null) {
                consumer.accept(t, extractor.applyAsDouble(t));
            }
        };
    }
}
