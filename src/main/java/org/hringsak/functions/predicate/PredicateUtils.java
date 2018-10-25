package org.hringsak.functions.predicate;

import org.hringsak.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;
import static org.hringsak.functions.predicate.CharSequenceUtils.anyCharacterMatches;
import static org.hringsak.functions.predicate.CharSequenceUtils.isCharacterMatch;
import static org.hringsak.functions.predicate.CharSequenceUtils.noCharactersMatch;

/**
 * Methods that build predicates, especially useful in filtering streams.
 */
public final class PredicateUtils {

    private PredicateUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type &lt;T&gt; and returns a
     * <code>boolean</code> (or a <code>Boolean</code>), to a <code>Predicate&lt;T&gt;</code>. This could be useful in a
     * situation where methods of the <code>Predicate</code> interface are to be called on a method reference. In the
     * following example, assume that the <code>Product.isConsumerElectronicType()</code> and
     * <code>isSmartPhone()</code> methods return boolean values:
     * <pre>
     *     Collection&lt;Product&gt; products = ...
     *     Collection&lt;Product&gt; smartPhones = products.stream()
     *         .filter(PredicateUtils.predicate(Product::isConsumerElectronic)
     *             .and(Product::isSmartPhone))
     *         .collect(toList());
     * </pre>
     * The <code>Predicate.and(...)</code> method can only be called on the method reference because of the cast. Note
     * that the second predicate does not need to be cast, because <code>Predicate.and(...)</code> already takes a
     * <code>Predicate&lt;T&gt;</code> just like this method, and so is already doing a cast. An additional benefit of
     * calling this method is the null-checking performed on the target element passed to the resulting function.
     *
     * @param predicate A method reference to be cast to a Predicate.
     * @param <T>       The type of the single parameter to the Predicate.
     * @return A method reference cast to a Predicate.
     */
    public static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }

    /**
     * Builds a <code>Predicate&lt;T&gt;</code> from a passed <code>BiPredicate</code>, which can be very useful in the
     * common situation where you are streaming through a collection of elements, and have a predicate method to call
     * that takes two parameters - the first one being the element on which you are streaming, and the second being some
     * constant value that will be passed to all invocations. This would typically be called from within a chain of
     * method calls based on a <code>Stream</code>. In the following example, assume the <code>OrderLineItem</code>
     * objects passed to the <code>getDiscountedLineItems(...)</code> method are to be filtered based on whether a
     * passed <code>Customer</code> gets a discount on them:
     * <pre>
     *     private Collection&lt;OrderLineItem&gt; getDiscountedLineItems(Collection&lt;OrderLineItem&gt; lineItems, Customer customer) {
     *         return lineItems.stream()
     *             .filter(PredicateUtils.predicate(this::isDiscounted, customer))
     *             .collect(toList());
     *     }
     *
     *     private boolean isDiscounted(OrderLineItem lineItem, Customer customer) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return lineItems.stream()
     *         .filter(predicate(this::isDiscounted, customer))
     *         .collect(toList());
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type &lt;T&gt;,
     *                    and the second of type &lt;U&gt;, either of which can be any type. The method reference will
     *                    be converted by this method to a Predicate, taking a single parameter of type &lt;T&gt;.
     *                    Behind the scenes, this BiPredicate will be called, passing the constant value to each
     *                    invocation as the second parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the second parameter to it, and will have the same value for each of them.
     * @param <T>         The target type of the first parameter to the passed biPredicate.
     * @param <U>         The type of the constant value to be passed as the second parameter to each invocation of
     *                    biPredicate.
     * @return A Predicate taking a single parameter of type &lt;T&gt;.
     */
    public static <T, U> Predicate<T> predicate(BiPredicate<? super T, ? super U> biPredicate, U value) {
        return t -> biPredicate.test(t, value);
    }

    /**
     * As in the {@link #predicate(BiPredicate, Object)} method, builds a <code>Predicate&lt;T&gt;</code> from a passed
     * <code>BiPredicate</code>, which can be very useful in the common situation where you are streaming through a
     * collection of elements, and have a predicate method to call that takes two parameters. In the
     * <code>BiPredicate</code> passed to this method, the parameters are basically the same as in the call to
     * {@link #predicate(BiPredicate, Object)}, but in the inverse order. Here, the first parameter is a constant value
     * that will be passed to all invocations of the method, and the second parameter is the target element on which you
     * are streaming. This would typically be called from within a chain of method calls based on a <code>Stream</code>.
     * In the following example, assume the <code>OrderLineItem</code> objects passed to the
     * <code>getDiscountedLineItems(...)</code> method are to be filtered based on whether a passed
     * <code>Customer</code> gets a discount on them:
     * <pre>
     *     private Collection&lt;OrderLineItem&gt; getDiscountedLineItems(Collection&lt;OrderLineItem&gt; lineItems, Customer customer) {
     *         return lineItems.stream()
     *             .filter(PredicateUtils.inversePredicate(this::isDiscounted, customer))
     *             .collect(toList());
     *     }
     *
     *     private boolean isDiscounted(Customer customer, OrderLineItem lineItem) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return lineItems.stream()
     *         .filter(inversePredicate(this::isDiscounted, customer))
     *         .collect(toList());
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type &lt;U&gt;,
     *                    and the second of type &lt;T&gt;, either of which can be any type. The method reference will
     *                    be converted by this method to a Predicate, taking a single parameter of type &lt;T&gt;.
     *                    Behind the scenes, this BiPredicate will be called, passing the constant value to each
     *                    invocation as the first parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the first parameter to it, and will have the same value for each of them.
     * @param <T>         The target type of the second parameter to the passed biPredicate.
     * @param <U>         The type of the constant value to be passed as the first parameter to each invocation of
     *                    biPredicate.
     * @return A Predicate taking a single parameter of type &lt;T&gt;.
     */
    public static <T, U> Predicate<T> inversePredicate(BiPredicate<? super U, ? super T> biPredicate, U value) {
        return t -> biPredicate.test(value, t);
    }

    /**
     * Builds a <code>Predicate</code> that, if the target element is <code>null</code>, then the passed
     * <code>boolean</code> default value is returned. Otherwise, the passed predicate is evaluated on the element. In
     * the following example, assume that the <code>Product.isConsumerProduct</code> method returns a boolean, and
     * that if the target element is <code>null</code>, then the result should default to <code>false</code>:
     * <pre>
     *     Collection&lt;Product&gt; products = ...
     *     Collection&lt;Product&gt; consumerProducts = products.stream()
     *         .filter(PredicateUtils.predicateDefault(Product::isConsumerProduct, false))
     *         .collect(toList());
     * </pre>
     * This is an admittedly contrived example, because why would an element in a given <code>Collection</code> be
     * <code>null</code>, but it may be quite valuable, since predicates can be useful in a variety of situations, not
     * just in streams.
     *
     * @param predicate     A method reference which takes a single parameter of type &lt;T&gt;, and returns a boolean
     *                      value.
     * @param defaultIfNull A default boolean value, to be returned in case the target element is null.
     * @param <T>           The type of the target element on which the Predicate is to be called.
     * @return A Predicate that returns a default boolean value if the target element is null.
     */
    public static <T> Predicate<T> predicateDefault(Predicate<? super T> predicate, boolean defaultIfNull) {
        return t -> t == null ? defaultIfNull : predicate.test(t);
    }

    /**
     * Builds a predicate based on a passed constant <code>boolean</code> value. The target element of type &lt;T&gt;
     * that is passed to the predicate is ignored, and the constant value is simply returned. This comes in handy when
     * combining one predicate with another using <code>Predicate.and(...)</code> or <code>Predicate.or(...)</code>.
     * Consider the following example:
     * <pre>
     *     private Collection&lt;OrderLineItem&gt; getDiscountedLineItems(Collection&lt;OrderLineItem&gt; lineItems, Customer customer) {
     *         boolean hasBlanketDiscount = customerHasBlanketDiscount(customer);
     *         return lineItems.stream()
     *             .filter(PredicateUtils.constant(hasBlanketDiscount)
     *                 .or(PredicateUtils.predicate(this::isDiscounted, customer)))
     *             .collect(toList());
     *     }
     *
     *     private boolean customerHasBlanketDiscount(Customer customer) {
     *         ...
     *     }
     *
     *     private boolean isDiscounted(OrderLineItem lineItem, Customer customer) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *         return lineItems.stream()
     *             .filter(constant(hasBlanketDiscount)
     *                 .or(predicate(this::isDiscounted, customer)))
     *             .collect(toList());
     * </pre>
     *
     * @param b   A constant boolean value that will be the result of every invocation of the returned Predicate.
     * @param <T> The type of the target element to be passed to the Predicate.
     * @return A Predicate taking an element of type &lt;T&gt;, but returning a constant boolean value.
     */
    public static <T> Predicate<T> constant(boolean b) {
        return t -> b;
    }

    /**
     * Allows conversion of any mapper <code>Function</code> to a <code>Predicate</code>. For example, say you have a
     * <code>Map&lt;String, Boolean&gt;</code>, and you want to use it in a predicate:
     * <pre>
     *     Map&lt;String, Boolean&gt; idToDiscount = ...
     *     Collection&lt;Customer&gt; customers = ...
     *     Collection&lt;Customer&gt; customersWithDiscount = customers.stream
     *         .filter(PredicateUtils.fromMapper(MapperUtils.getValue(idToDiscount, Customer::getId)))
     *         .collect(toList());
     * </pre>
     * Or, with static imports:
     * <pre>
     *     Collection&lt;Customer&gt; customersWithDiscount = customers.stream
     *         .filter(fromMapper(getValue(idToDiscount, Customer::getId)))
     *         .collect(toList());
     * </pre>
     *
     * @param function A Function that takes an element of type &lt;T&gt;, and returns a Boolean value.
     * @param <T>      The type of the element passed to the given function.
     * @return A predicate taking an element of type &lt;T&gt;.
     */
    public static <T> Predicate<T> fromMapper(Function<T, Boolean> function) {
        return t -> t != null && function.apply(t);
    }

    /**
     * A predicate that simply negates the passed predicate. A predicate can always be negated via
     * <code>predicate.negate()</code>, however using this method may improve readability.
     *
     * @param predicate A predicate whose result is to be negated.
     * @param <T>       The type of the element to be passed to the predicate built by this method.
     * @return A predicate taking an element of type &lt;T&gt;.
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * <code>CharSequence</code> returned by that <code>Function</code> is empty.
     *
     * @param function A Function that takes an element and returns a CharSequence.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * CharSequence, and determines whether it is empty.
     */
    public static <T> Predicate<T> isSeqEmpty(Function<? super T, ? extends CharSequence> function) {
        return t -> t == null || function.apply(t) == null || function.apply(t).length() == 0;
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * <code>CharSequence</code> returned by that <code>Function</code> is <i>not</i> empty.
     *
     * @param function A Function that takes an element and returns a CharSequence.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * CharSequence, and determines whether it is <i>not</i> empty.
     */
    public static <T> Predicate<T> isSeqNotEmpty(Function<? super T, ? extends CharSequence> function) {
        return not(isSeqEmpty(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * <code>CharSequence</code> returned by that <code>Function</code> is equal to a passed <code>CharSequence</code>
     * ignoring case.
     *
     * @param function A Function that takes an element and returns a CharSequence.
     * @param value    A CharSequence to be compared to the result of the passed Function, to determine if it is equal
     *                 ignoring case.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * CharSequence, and determines whether it is equal to a passed CharSequence ignoring case.
     */
    public static <T> Predicate<T> equalsIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence value) {
        return t -> CharSequenceUtils.equalsIgnoreCase(t == null ? null : function.apply(t), value);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * <code>CharSequence</code> returned by that <code>Function</code> is <i>not</i> equal to a passed
     * <code>CharSequence</code> ignoring case.
     *
     * @param function A Function that takes an element and returns a CharSequence.
     * @param value    A CharSequence to be compared to the result of the passed Function, to determine if it is
     *                 <i>not</i> equal ignoring case.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * CharSequence, and determines whether it is <i>not</i> equal to a passed CharSequence ignoring case.
     */
    public static <T> Predicate<T> notEqualsIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence value) {
        return not(equalsIgnoreCase(function, value));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a value of type &lt;R&gt;,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * value returned by that <code>Function</code> is equal to the passed <code>value</code>, according to
     * <code>Object.equals(...)</code>.
     *
     * @param function A Function that takes an element and returns a value to be compared with a passed value for
     *                 equality.
     * @param value    A value to be compared to the result of the passed Function for equality.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @param <R>      The type of the value passed, as well as the return value of the passed function.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * value of type &lt;R&gt;, and compares it for equality to a passed value, also of type &lt;R&gt;.
     */
    public static <T, R> Predicate<T> isEqual(Function<? super T, ? extends R> function, R value) {
        return t -> Objects.equals(t == null ? null : function.apply(t), value);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a value of type &lt;R&gt;,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * value returned by that <code>Function</code> is <i>not</i> equal to the passed <code>value</code>, according to
     * <code>Object.equals(...)</code>.
     *
     * @param function A Function that takes an element and returns a value to be compared with a passed value for
     *                 inequality.
     * @param value    A value to be compared to the result of the passed Function for inequality.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @param <R>      The type of the value passed, as well as the return value of the passed function.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * value of type &lt;R&gt;, and compares it for inequality to a passed value, also of type &lt;R&gt;.
     */
    public static <T, R> Predicate<T> isNotEqual(Function<? super T, ? extends R> function, R value) {
        return not(isEqual(function, value));
    }

    /**
     * Given a <code>Collection</code> whose elements are of type &lt;R&gt;, and a <code>Function</code> that takes an
     * element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this method builds a <code>Predicate</code> that
     * takes an element of type &lt;T&gt;, and determines if the given collection contains the value returned by the
     * function. More formally, the <code>Predicate</code> built by this method returns <code>true</code> if and only if
     * the passed collection contains at least one element e such that
     * <code>(o == null ? e == null : o.equals(e))</code>, o being the value returned from the passed function.
     *
     * @param collection A Collection of elements of type &lt;R&gt;, to be checked for whether it contains a value
     *                   returned from a passed function.
     * @param function   A Function taking an element of type &lt;T&gt; and returning a value of type &lt;R&gt; to be
     *                   checked if it is contained in a passed Collection.
     * @param <T>        The type of the element taken by the Predicate built by this method.
     * @param <R>        The type of elements in the passed Collection. Also, the type of the value returned by the
     *                   passed Function.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * value of type &lt;R&gt;. The Predicate checks that the returned value is contained in a passed Collection.
     */
    public static <T, R> Predicate<T> contains(Collection<? extends R> collection, Function<? super T, ? extends R> function) {
        return t -> collection != null && t != null && collection.contains(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection&lt;R&gt;</code>, this method builds a <code>Predicate</code> that takes an element of type
     * &lt;T&gt;, and determines if the collection returned by that function contains the passed <code>value</code> of
     * type &lt;R&gt;. More formally, the <code>Predicate</code> built by this method returns <code>true</code> if and
     * only if the returned collection contains at least one element e such that
     * <code>(o == null ? e == null : o.equals(e))</code>.
     * <p>
     * This method is similar to {@link #contains(Collection, Function)}, but instead of a built predicate checking
     * whether a passed collection contains a value returned by a function, in this method it checks whether a
     * collection returned by a function contains a passed value.
     *
     * @param function A Function that takes an element and returns a Collection of elements of type &lt;R&gt;.
     * @param value    A value of type &lt;R&gt; to be checked if a Collection returned by the above Function contains
     *                 it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @param <R>      The type of elements for collections returned by a passed Function. Also, the type of the passed
     *                 value.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * Collection&lt;R&gt;. The Predicate checks that the returned Collection contains a passed value of type
     * &lt;R&gt;.
     */
    public static <T, R> Predicate<T> inverseContains(Function<? super T, ? extends Collection<R>> function, R value) {
        return t -> {
            if (t != null) {
                Collection<R> collection = function.apply(t);
                return collection != null && collection.contains(value);
            }
            return false;
        };
    }

    /**
     * Given a <code>Map</code> whose keys are of type &lt;R&gt;, and a <code>Function</code> that takes an element of
     * type &lt;T&gt; and returns a value of type &lt;R&gt;, this method builds a <code>Predicate</code> that takes an
     * element of type &lt;T&gt;, and determines if the given map contains a key equal to the value returned by the
     * function. More formally, the <code>Predicate</code> built by this method returns <code>true</code> if and only if
     * this map contains a mapping for a key <code>k</code> such that
     * <code>(key == null ? k == null : key.equals(k))</code>. (There can be at most one such mapping.)
     *
     * @param map      A Map whose keys are of type &lt;R&gt;, to be checked if it contains a key equal to the value
     *                 returned by the passed function.
     * @param function A Function taking an element of type &lt;T&gt; and returning a value of type &lt;R&gt; to be
     *                 checked if it is equal to a key in a passed Map.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @param <R>      The type of keys in the passed Map. Also, the type of the value returned by the passed Function.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies the given function to it resulting in a
     * value of type &lt;R&gt;. The Predicate checks that the passed Map has a key equal to the returned value.
     */
    public static <T, R> Predicate<T> containsKey(Map<R, ?> map, Function<? super T, ? extends R> function) {
        return t -> map != null && t != null && map.containsKey(function.apply(t));
    }

    /**
     * Given a <code>Map</code> whose values are of type &lt;R&gt;, and a <code>Function</code> that takes an element of
     * type &lt;T&gt; and returns a value of type &lt;R&gt;, this method builds a <code>Predicate</code> that takes an
     * element of type &lt;T&gt;, and determines if the given map contains at least one value equal to the value
     * returned by the function. More formally, the <code>Predicate</code> built by this method returns
     * <code>true</code> if and only if this map contains at least one mapping to a value <code>v</code> such that
     * <code>(value == null ? v == null : value.equals(v))</code>.
     *
     * @param map      A Map whose values are of type &lt;R&gt;, to be checked if it contains at least one value equal
     *                 to the value returned by the passed function.
     * @param function A Function taking an element of type &lt;T&gt; and returning a value of type &lt;R&gt; to be
     *                 checked if it is equal to at least one value in a passed Map.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @param <R>      The type of values in the passed Map. Also, the type of the value returned by the passed
     *                 Function.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies a given function to it resulting in a value
     * of type &lt;R&gt;. The Predicate checks that the passed Map has at least one value equal to the returned value.
     */
    public static <T, R> Predicate<T> containsValue(Map<?, R> map, Function<? super T, ? extends R> function) {
        return t -> map != null && t != null && map.containsValue(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function contains at least one character matching the given
     * <code>searchChar</code>.
     *
     * @param function   A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                   contains at least one character matching the given searchChar.
     * @param searchChar A character to be searched in a CharSequence returned from a given function.
     * @param <T>        The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies a given function to it resulting in a
     * CharSequence, and determines whether it contains at least one character matching a given searchChar.
     */
    public static <T> Predicate<T> containsChar(Function<? super T, ? extends CharSequence> function, int searchChar) {
        return t -> t != null && CharSequenceUtils.contains(function.apply(t), searchChar);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function contains at least one character matching the given
     * <code>searchChar</code> ignoring case.
     *
     * @param function   A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                   contains at least one character matching the given searchChar ignoring case.
     * @param searchChar A character to be searched in a CharSequence returned from a given function ignoring case.
     * @param <T>        The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies a given function to it resulting in a
     * CharSequence, and determines whether it contains at least one character matching a given searchChar ignoring
     * case.
     */
    public static <T> Predicate<T> containsCharIgnoreCase(Function<? super T, ? extends CharSequence> function, int searchChar) {
        return t -> t != null && CharSequenceUtils.containsIgnoreCase(function.apply(t), searchChar);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function contains at least one matching instance of the given
     * <code>searchSeq</code>.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                  contains at least one matching instance of the given searchSeq.
     * @param searchSeq A CharSequence substring to be searched in a CharSequence returned from a given function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies a given function to it resulting in a
     * CharSequence, and determines whether it contains at least one matching  instance of a given searchSeq.
     */
    public static <T> Predicate<T> containsSeq(Function<? super T, ? extends CharSequence> function, CharSequence searchSeq) {
        return t -> t != null && CharSequenceUtils.contains(function.apply(t), searchSeq);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function contains at least one matching instance of the given
     * <code>searchSeq</code> ignoring case.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                  contains at least one matching instance of the given searchSeq ignoring case.
     * @param searchSeq A CharSequence substring to be searched in a CharSequence returned from a given function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, applies a given function to it resulting in a
     * CharSequence, and determines whether it contains at least one matching  instance of a given searchSeq ignoring
     * case.
     */
    public static <T> Predicate<T> containsSeqIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence searchSeq) {
        return t -> t != null && CharSequenceUtils.containsIgnoreCase(function.apply(t), searchSeq);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function is made up entirely of alpha characters. More precisely, the
     * returned <code>Predicate</code> returns <code>true</code> if all of the letters in the character sequence return
     * <code>true</code> for <code>Character.isLetter(ch)</code>.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if all
     *                 of its individual characters are letters.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if all of the individual characters of the character sequence returned from
     * the given function are letters, otherwise false. Returns false the sequence is null and true if the it is empty.
     */
    public static <T> Predicate<T> isAlpha(Function<? super T, ? extends CharSequence> function) {
        return t -> CharSequenceUtils.isCharacterMatch(function.apply(t), Character::isLetter);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function is made up entirely of alphanumeric characters. More precisely, the
     * returned <code>Predicate</code> returns <code>true</code> if all of the letters in the character sequence return
     * <code>true</code> for <code>Character.isLetterOrDigit(ch)</code>.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if all
     *                 of its individual characters are letters or digits.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if all of the individual characters of the character sequence returned from
     * the given function are letters or digits, otherwise false. Returns false the sequence is null and true if the it
     * is empty.
     */
    public static <T> Predicate<T> isAlphanumeric(Function<? super T, ? extends CharSequence> function) {
        return t -> CharSequenceUtils.isCharacterMatch(function.apply(t), Character::isLetterOrDigit);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function is made up entirely of alpha characters. More precisely, the
     * returned <code>Predicate</code> returns <code>true</code> if all of the letters in the character sequence return
     * <code>true</code> for <code>Character.isDigit(ch)</code>.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if all
     *                 of its individual characters are digits.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if all of the individual characters of the character sequence returned from
     * the given function are digits, otherwise false. Returns false the sequence is null and true if the it is empty.
     */
    public static <T> Predicate<T> isNumeric(Function<? super T, ? extends CharSequence> function) {
        return t -> CharSequenceUtils.isCharacterMatch(function.apply(t), Character::isDigit);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function starts with a given prefix.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                 starts with a given prefix.
     * @param prefix   A CharSequence prefix to be determined if a sequence returned from a function starts with it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function starts with a
     * given prefix.
     */
    public static <T> Predicate<T> startsWith(Function<? super T, ? extends CharSequence> function, CharSequence prefix) {
        return t -> t != null && CharSequenceUtils.startsWith(function.apply(t), prefix);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function starts with a given prefix ignoring case.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                 starts with a given prefix ignoring case.
     * @param prefix   A CharSequence prefix to be determined if a sequence returned from a function starts with it
     *                 ignoring case.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function starts with a
     * given prefix ignoring case.
     */
    public static <T> Predicate<T> startsWithIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence prefix) {
        return t -> t != null && CharSequenceUtils.startsWithIgnoreCase(function.apply(t), prefix);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function ends with a given suffix.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                 ends with a given suffix.
     * @param suffix   A CharSequence suffix to be determined if a sequence returned from a function ends with it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function ends with a given
     * suffix.
     */
    public static <T> Predicate<T> endsWith(Function<? super T, ? extends CharSequence> function, CharSequence suffix) {
        return t -> t != null && CharSequenceUtils.endsWith(function.apply(t), suffix);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the
     * character sequence returned from the function ends with a given suffix ignoring case.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked if it
     *                 ends with a given suffix ignoring case.
     * @param suffix   A CharSequence suffix to be determined if a sequence returned from a function ends with it
     *                 ignoring case.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function ends with a given
     * suffix ignoring case.
     */
    public static <T> Predicate<T> endsWithIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence suffix) {
        return t -> t != null && CharSequenceUtils.endsWithIgnoreCase(function.apply(t), suffix);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if any of the
     * characters in the character sequence returned from the function match the given <code>IntPredicate</code>.
     *
     * @param function      A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked
     *                      whether any of its characters match the given IntPredicate.
     * @param charPredicate An arbitrary IntPredicate to be tested to see if any characters in a sequence returned by a
     *                      given function return true for it.
     * @param <T>           The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function has any characters
     * in it for which a given IntPredicate returns true, otherwise false. Returns false if the sequence is null or
     * empty.
     */
    public static <T> Predicate<T> anyCharsMatch(Function<? super T, ? extends CharSequence> function, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(function).orElse(null);
            return anyCharacterMatches(sequence, charPredicate);
        };
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if all of the
     * characters in the character sequence returned from the function match the given <code>IntPredicate</code>.
     *
     * @param function      A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked
     *                      whether all of its characters match the given IntPredicate.
     * @param charPredicate An arbitrary IntPredicate to be tested to see if all characters in a sequence returned by a
     *                      given function return true for it.
     * @param <T>           The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function has all of its
     * characters returning true for a given IntPredicate, otherwise false. Returns false if the sequence is null and
     * true if it is empty.
     */
    public static <T> Predicate<T> allCharsMatch(Function<? super T, ? extends CharSequence> function, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(function).orElse(null);
            return isCharacterMatch(sequence, charPredicate);
        };
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>,
     * this method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if none of
     * the characters in the character sequence returned from the function match the given <code>IntPredicate</code>.
     *
     * @param function      A Function taking an element of type &lt;T&gt; and returning a CharSequence to be checked
     *                      whether none of its characters match the given IntPredicate.
     * @param charPredicate An arbitrary IntPredicate to be tested to see if no characters in a sequence returned by a
     *                      given function return true for it.
     * @param <T>           The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the character sequence returned from a given function has no characters
     * in it for which a given IntPredicate returns true, otherwise false. Returns true if the sequence is null or
     * empty.
     */
    public static <T> Predicate<T> noCharsMatch(Function<? super T, ? extends CharSequence> function, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(function).orElse(null);
            return noCharactersMatch(sequence, charPredicate);
        };
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an object of any type, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines whether the
     * value returned from the function is <code>null</code>.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning an object of any type to be checked
     *                 for nullity.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the value returned from a given function is null, otherwise false.
     * Returns true if the element of type &lt;T&gt; passed to the function is null.
     */
    public static <T> Predicate<T> isNull(Function<? super T, ?> function) {
        return t -> t == null || Objects.isNull(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an object of any type, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines whether the
     * value returned from the function is not <code>null</code>.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning an object of any type to be checked
     *                 for non-nullity.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the value returned from a given function is not null, otherwise false.
     * Returns false if the element of type &lt;T&gt; passed to the function is null.
     */
    public static <T> Predicate<T> notNull(Function<T, ?> function) {
        return not(isNull(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the value
     * returned from the function is greater than the <code>compareTo</code> value passed.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a value to be checked whether it is
     *                  greater than a passed compareTo value.
     * @param compareTo A value to which a value returned from a passed function will be compared by the returned
     *                  Predicate.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the compareTo value passed. Also, the return type of the passed function.
     * @return A Predicate that returns true if the value returned from a given function is greater than the passed
     * compareTo value. The comparator used considers null values to be greater than those that are non-null, therefore
     * the predicate will return false if the element passed to the function, or the value returned from it, is null.
     */
    public static <T, R extends Comparable<R>> Predicate<T> gt(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValueNullsLast(target, function, compareTo) > 0;
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the value
     * returned from the function is greater than or equal to the <code>compareTo</code> value passed.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a value to be checked whether it is
     *                  greater than or equal to a passed compareTo value.
     * @param compareTo A value to which a value returned from a passed function will be compared by the returned
     *                  Predicate.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the compareTo value passed. Also, the return type of the passed function.
     * @return A Predicate that returns true if the value returned from a given function is greater than or equal to the
     * passed compareTo value. The comparator used considers null values to be greater than those that are non-null,
     * therefore the predicate will return false if the element passed to the function, or the value returned from it,
     * is null.
     */
    public static <T, R extends Comparable<R>> Predicate<T> gte(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValueNullsLast(target, function, compareTo) >= 0;
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the value
     * returned from the function is less than the <code>compareTo</code> value passed.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a value to be checked whether it is
     *                  less than a passed compareTo value.
     * @param compareTo A value to which a value returned from a passed function will be compared by the returned
     *                  Predicate.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the compareTo value passed. Also, the return type of the passed function.
     * @return A Predicate that returns true if the value returned from a given function is less than the passed
     * compareTo value. The comparator used considers null values to be less than those that are non-null, therefore
     * the predicate will return false if the element passed to the function, or the value returned from it, is null.
     */
    public static <T, R extends Comparable<R>> Predicate<T> lt(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValueNullsFirst(target, function, compareTo) < 0;
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns a value of type &lt;R&gt;, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines if the value
     * returned from the function is less than or equal to the <code>compareTo</code> value passed.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a value to be checked whether it is
     *                  less than or equal to a passed compareTo value.
     * @param compareTo A value to which a value returned from a passed function will be compared by the returned
     *                  Predicate.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the compareTo value passed. Also, the return type of the passed function.
     * @return A Predicate that returns true if the value returned from a given function is less than or equal to the
     * passed compareTo value. The comparator used considers null values to be less than those that are non-null,
     * therefore the predicate will return false if the element passed to the function, or the value returned from it,
     * is null.
     */
    public static <T, R extends Comparable<R>> Predicate<T> lte(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValueNullsFirst(target, function, compareTo) <= 0;
    }

    private static <T, R extends Comparable<R>> int getComparisonValueNullsLast(T target, Function<? super T, ? extends R> valueExtractor, R compareTo) {
        return Objects.compare(getValueIfTargetNonNull(valueExtractor, target), compareTo, nullsLast(naturalOrder()));
    }

    private static <T, R extends Comparable<R>> int getComparisonValueNullsFirst(T target, Function<? super T, ? extends R> valueExtractor, R compareTo) {
        return Objects.compare(getValueIfTargetNonNull(valueExtractor, target), compareTo, nullsFirst(naturalOrder()));
    }

    private static <T, R extends Comparable<R>> R getValueIfTargetNonNull(Function<? super T, ? extends R> valueExtractor, T target) {
        return target == null ? null : valueExtractor.apply(target);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection</code> whose elements may be of any type, this method builds a <code>Predicate</code> that takes
     * an element of type &lt;T&gt;, and determines if the collection returned by that function is empty.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a Collection containing elements of
     *                 any type.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if a collection returned from a given function is empty or null. Returns
     * true if the element passed to the function is null.
     */
    public static <T> Predicate<T> isCollEmpty(Function<? super T, ? extends Collection<?>> function) {
        return t -> t == null || CollectionUtils.isEmpty(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection</code> whose elements may be of any type, this method builds a <code>Predicate</code> that takes
     * an element of type &lt;T&gt;, and determines if the collection returned by that function is <i>not</i> empty.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a Collection containing elements of
     *                 any type.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if a collection returned from a given function is <i>not</i> empty or null.
     * Returns false if the element passed to the function is null.
     */
    public static <T> Predicate<T> isCollNotEmpty(Function<? super T, ? extends Collection<?>> function) {
        return not(isCollEmpty(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection&lt;R&gt;</code>, this method builds a <code>Predicate</code> that takes an element of type
     * &lt;T&gt;, and determines if all of the elements of the collection returned by that function match a given
     * <code>Predicate</code>.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a Collection containing elements of
     *                  type &lt;R&gt;.
     * @param predicate A Predicate taking an element of type &lt;R&gt; to determine if all of the elements in a
     *                  collection returned by a given function return true for it.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the element taken by the predicate passed. Also, the element type of the collection
     *                  returned by the passed function.
     * @return A Predicate that returns true if all of the elements of a collection returned from a given function
     * return true for a passed predicate. Returns false if the passed element, or the collection returned from the
     * given function is null. Returns true if the collection returned from the given function is empty.
     */
    public static <T, R> Predicate<T> allMatch(Function<? super T, ? extends Collection<R>> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.allMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection&lt;R&gt;</code>, this method builds a <code>Predicate</code> that takes an element of type
     * &lt;T&gt;, and determines if any of the elements of the collection returned by that function match a given
     * <code>Predicate</code>.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a Collection containing elements of
     *                  type &lt;R&gt;.
     * @param predicate A Predicate taking an element of type &lt;R&gt; to determine if any of the elements in a
     *                  collection returned by a given function return true for it.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the element taken by the predicate passed. Also, the element type of the collection
     *                  returned by the passed function.
     * @return A Predicate that returns true if any of the elements of a collection returned from a given function
     * return true for a passed predicate. Returns false if the passed element, or the collection returned from the
     * given function is null or empty.
     */
    public static <T, R> Predicate<T> anyMatch(Function<? super T, ? extends Collection<R>> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.anyMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a
     * <code>Collection&lt;R&gt;</code>, this method builds a <code>Predicate</code> that takes an element of type
     * &lt;T&gt;, and determines if none of the elements of the collection returned by that function match a given
     * <code>Predicate</code>.
     *
     * @param function  A Function taking an element of type &lt;T&gt; and returning a Collection containing elements of
     *                  type &lt;R&gt;.
     * @param predicate A Predicate taking an element of type &lt;R&gt; to determine if none of the elements in a
     *                  collection returned by a given function return true for it.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the element taken by the predicate passed. Also, the element type of the collection
     *                  returned by the passed function.
     * @return A Predicate that returns true if none of the elements of a collection returned from a given function
     * return true for a passed predicate. Returns true if the passed element, or the collection returned from the
     * given function is null or empty.
     */
    public static <T, R> Predicate<T> noneMatch(Function<? super T, ? extends Collection<R>> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.noneMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a key value of any type, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines whether no
     * elements that have been encountered so far have the same key returned from the function. Using the predicate
     * built by this method in a <code>Stream.filter(...)</code> will ensure that two elements with the same key value
     * will not be present in the same stream.
     * <p>
     * Note that this violates the edict that stream operations be stateless. This means a couple of things. First, that
     * the predicate returned by this method is not a true function, and second that the result of the predicate may
     * depend on state which might change during the execution of the stream pipeline. This will still return correct
     * results if you are certain that it does not matter which element having the same key ends up in the results of a
     * terminal operation on a stream.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a key value determining the
     *                 uniqueness of elements in the results of a terminal operation on a stream.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the passed element, when applied to the passed function, returns a key
     * value that has not yet been encountered in a stream.
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> function) {
        Set<Object> uniqueKeys = new HashSet<>();
        return t -> uniqueKeys.add(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt;, and returns a key value of any type, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and determines whether no
     * elements that have been encountered so far have the same key returned from the function. Using the predicate
     * built by this method in a <code>Stream.filter(...)</code> will ensure that two elements with the same key value
     * will not be present in the same stream.
     * <p>
     * Note that this violates the edict that stream operations be stateless. This means a couple of things. First, that
     * the predicate returned by this method is not a true function, and second that the result of the predicate may
     * depend on state which might change during the execution of the stream pipeline. This will still return correct
     * results if you are certain that it does not matter which element having the same key ends up in the results of a
     * terminal operation on a stream.
     * <p>
     * The difference between this method and {@link #distinctByKey(Function)} is that this method uses synchronization
     * to ensure correct behavior in parallel streams.
     *
     * @param function A Function taking an element of type &lt;T&gt; and returning a key value determining the
     *                 uniqueness of elements in the results of a terminal operation on a stream.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that returns true if the passed element, when applied to the passed function, returns a key
     * value that has not yet been encountered in a stream.
     */
    public static <T> Predicate<T> distinctByKeyParallel(Function<? super T, ?> function) {
        Set<Object> uniqueKeys = Collections.synchronizedSet(new HashSet<>());
        return t -> uniqueKeys.add(function.apply(t));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; returning a value of type &lt;R&gt;, and a
     * <code>Predicate</code> that takes an element of type &lt;R&gt;, this method builds a <code>Predicate</code> that
     * takes an element of type &lt;T&gt;, and applies the return value of the function to the given predicate. It is a
     * way of adapting a given element to a predicate taking a different element type. For example, the
     * {@link StreamUtils#indexOfFirst(Collection, Predicate)} method uses this predicate in its implementation:
     * <pre>
     * public static &lt;T&gt; int indexOfFirst(Collection&lt;T&gt; objects, Predicate&lt;T&gt; predicate) {
     *     return defaultStream(objects)
     *         .map(pairWithIndex())
     *         .filter(mapAndFilter(ObjectIndexPair::getObject, predicate))
     *         .mapToInt(ObjectIndexPair::getIndex)
     *         .findFirst()
     *         .orElse(-1);
     * }
     * </pre>
     * This map-and-filter predicate is necessary in that case because we have a predicate that operates on the original
     * element type of the stream, but a mapping operation has changed the type to a <code>ObjectIndexPair</code>. The
     * <code>ObjectIndexPair::getObject</code> method reference gets retrieves the original element before the predicate
     * evaluates it.
     *
     * @param function  A Function to transform an element of type &lt;T&gt; to one of type &lt;R&gt; before it is
     *                  passed to a Predicate   .
     * @param predicate A Predicate taking an element of type &lt;R&gt;, whose value will be retrieved from a given
     *                  transformer function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @param <R>       The type of the element taken by the passed predicate. Also, the type of the return value of the
     *                  passed function.
     * @return A Predicate that takes an element of type &lt;T&gt;, and applies the return value of a given function to
     * a given predicate.
     */
    public static <T, R> Predicate<T> mapAndFilter(Function<? super T, ? extends R> function, Predicate<? super R> predicate) {
        return t -> t != null && predicate.test(function.apply(t));
    }
}
