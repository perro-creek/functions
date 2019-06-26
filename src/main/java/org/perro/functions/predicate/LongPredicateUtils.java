package org.perro.functions.predicate;

import org.perro.functions.internal.CollectionUtils;
import org.perro.functions.mapper.LongMapperUtils;
import org.perro.functions.stream.LongStreamUtils;
import org.perro.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.perro.functions.predicate.PredicateUtils.not;

/**
 * Methods that build predicates specifically those involving primitive <code>long</code> types.
 */
public final class LongPredicateUtils {

    private LongPredicateUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>long</code> or
     * <code>Long</code>, and returns a <code>boolean</code> or <code>Boolean</code>, to a
     * <code>LongPredicate</code>. This could be useful in a situation where methods of the
     * <code>LongPredicate</code> functional interface are to be called on a method reference. For example:
     * <pre>
     *     private long[] getLongsInRange(long[] longs) {
     *         return Arrays.stream(longs)
     *             .filter(LongPredicateUtils.longPredicate(this::isGreaterThanOrEqualToMin)
     *                 .and(this::isLessThanOrEqualToMax))
     *             .toArray();
     *     }
     *
     *     private boolean isGreaterThanOrEqualToMin(long target) {
     *         ...
     *     }
     *
     *     private boolean isLessThanOrEqualToMax(long target) {
     *         ...
     *     }
     * </pre>
     * The <code>LongPredicate.and(...)</code> method can only be called on the method reference because of the cast.
     * Note that the second predicate does not need to be cast, because <code>LongPredicate.and(...)</code> already
     * takes a <code>LongPredicate</code> just like this method, and so is already doing a cast.
     *
     * @param predicate A method reference to be cast to a LongPredicate.
     * @return A method reference cast to a LongPredicate.
     */
    public static LongPredicate longPredicate(LongPredicate predicate) {
        return predicate;
    }

    /**
     * Builds a <code>LongPredicate</code> from a passed <code>BiPredicate&lt;Long, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have a long
     * predicate method to call that takes two parameters - the first one being the <code>long</code> element on which
     * you are streaming, and the second being some constant value that will be passed to all invocations. This would
     * typically be called from within a chain of method calls based on a <code>LongStream</code>. In the following
     * example, assume the <code>Stock</code> objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code>
     * method are to be filtered based on whether their price is less than a limit for the passed <code>Client</code>.
     * Also, assume that the monetary values are being stored as integral values in cents:
     * <pre>
     *     private long[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToLong(Stock::getPrice)
     *             .filter(LongPredicateUtils.longPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1L);
     *     }
     *
     *     private boolean isPriceBelowLimit(long price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToLong(Stock::getPrice)
     *         .filter(longPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1L);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    Long, and the second of type &lt;U&gt;. The method reference will be converted by this
     *                    method to a LongPredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the second parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the second parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the second parameter to each invocation of
     *                    biPredicate.
     * @return A LongPredicate adapted from the passed BiPredicate.
     */
    public static <U> LongPredicate longPredicate(BiPredicate<Long, ? super U> biPredicate, U value) {
        return l -> biPredicate.test(l, value);
    }

    /**
     * Builds a <code>LongPredicate</code> from a passed <code>BiPredicate&lt;Long, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have a long
     * predicate method to call that takes two parameters. In the <code>BiPredicate</code> passed to this method, the
     * parameters are basically the same as in the call to {@link #longPredicate(BiPredicate, Object)}, but in the
     * inverse order. Here, the first parameter is a constant value that will be passed to all invocations of the
     * method, and the second parameter is the target <code>long</code> element on which you are streaming. This would
     * typically be called from within a chain of method calls based on a <code>LongStream</code>. In the following
     * example, assume the <code>Stock</code> objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code>
     * method are to be filtered based on whether their price is less than a limit for the passed <code>Client</code>.
     * Also, assume that the monetary values are being stored as integral values in cents:
     * <pre>
     *     private long[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToLong(Stock::getPrice)
     *             .filter(LongPredicateUtils.inverseLongPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1L);
     *     }
     *
     *     private boolean isPriceBelowLimit(Client client, long price) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToLong(Stock::getPrice)
     *         .filter(inverseLongPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1L);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    &lt;U&gt;, and the second of type Long. The method reference will be converted by this
     *                    method to a LongPredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the first parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the first parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the first parameter to each invocation of
     *                    biPredicate.
     * @return A LongPredicate adapted from the passed BiPredicate.
     */
    public static <U> LongPredicate inverseLongPredicate(BiPredicate<? super U, Long> biPredicate, U value) {
        return l -> biPredicate.test(value, l);
    }

    /**
     * Builds a long predicate based on a passed constant <code>boolean</code> value. The target element of type
     * <code>long</code> that is passed to the predicate is ignored, and the constant value is simply returned. This
     * comes in handy when combining one predicate with another using <code>Predicate.and(...)</code> or
     * <code>Predicate.or(...)</code>. Consider the following example, assuming that the monetary values are being
     * stored as integral values in cents:
     * <pre>
     *     private long[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         boolean hasLimit = client.getLimit() &gt; 0L;
     *         return stocks.stream()
     *             .mapToLong(Stock::getPrice)
     *             .filter(LongPredicateUtils.longNot(LongPredicateUtils.longConstant(hasLimit))
     *                 .or(LongPredicateUtils.longPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1L);
     *     }
     *
     *     private boolean isPriceBelowLimit(long price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToLong(Stock::getPrice)
     *         .filter(longNot(longConstant(hasLimit))
     *             .or(longPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1L);
     * </pre>
     *
     * @param b A constant boolean value that will be the result of every invocation of the returned LongPredicate.
     * @return A LongPredicate returning a constant boolean value.
     */
    public static LongPredicate longConstant(boolean b) {
        return l -> b;
    }

    /**
     * A <code>LongPredicate</code> that simply negates the passed <code>LongPredicate</code>. A predicate can
     * always be negated via <code>predicate.negate()</code>, however using this method may improve readability.
     *
     * @param predicate A LongPredicate whose result is to be negated.
     * @return A negated LongPredicate.
     */
    public static LongPredicate longNot(LongPredicate predicate) {
        return predicate.negate();
    }

    /**
     * This method builds a <code>LongPredicate</code> whose parameter is to be compared for equality to the passed
     * <code>long</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the LongPredicate built by this method.
     * @return A LongPredicate that compares its parameter to a constant long value for equality.
     */
    public static LongPredicate isLongEqual(long value) {
        return l -> l == value;
    }

    /**
     * Given a <code>LongUnaryOperator</code> this method builds a <code>LongPredicate</code> that determines if the
     * value returned by that operator is equal to the passed constant long value. For example:
     * <pre>
     *     long[] longs = LongStream.range(1L, 10L).toArray();
     *     long[] evens = Arrays.stream(longs)
     *             .filter(LongPredicateUtils.isLongEqual(LongMapperUtils.longModulo(2L), 0L))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     long[] evens = Arrays.stream(longs)
     *             .filter(isLongEqual(longModulo(2L), 0L))
     *             .toArray();
     * </pre>
     *
     * @param operator A LongUnaryOperator whose return value is to be compared with a passed constant long value for
     *                 equality.
     * @param value    A value to be compared to the result of the passed operator for equality.
     * @return A LongPredicate whose return value is compared for equality to a passed constant long value.
     */
    public static LongPredicate isLongEqual(LongUnaryOperator operator, long value) {
        return d -> operator.applyAsLong(d) == value;
    }

    /**
     * This method builds a <code>LongPredicate</code> whose parameter is to be compared for inequality to the passed
     * <code>long</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the LongPredicate built by this method.
     * @return A LongPredicate that compares its parameter to a constant long value for inequality.
     */
    public static LongPredicate isLongNotEqual(long value) {
        return longNot(isLongEqual(value));
    }

    /**
     * Given a <code>LongUnaryOperator</code> this method builds a <code>LongPredicate</code> that determines if the
     * value returned by that operator is <i>not</i> equal to the passed constant long value. For example:
     * <pre>
     *     long[] longs = LongStream.range(1L, 10L).toArray();
     *     long[] odds = Arrays.stream(longs)
     *             .filter(LongPredicateUtils.isLongNotEqual(LongMapperUtils.longModulo(2L), 0L))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     long[] odds = Arrays.stream(longs)
     *             .filter(isLongNotEqual(longModulo(2L), 0L))
     *             .toArray();
     * </pre>
     *
     * @param operator A LongUnaryOperator whose return value is to be compared with a passed constant long value for
     *                 inequality.
     * @param value    A value to be compared to the result of the passed operator for inequality.
     * @return A LongPredicate whose return value is compared for inequality to a passed constant long value.
     */
    public static LongPredicate isLongNotEqual(LongUnaryOperator operator, long value) {
        return longNot(isLongEqual(operator, value));
    }

    /**
     * Given a <code>Collection</code> whose elements are of type &lt;R&gt;, and a <code>LongFunction</code> that
     * returns a value of type &lt;R&gt;, this method builds a <code>LongPredicate</code> that determines if the given
     * collection contains the value returned by the long function. More formally, the <code>LongPredicate</code> built
     * by this method returns <code>true</code> if and only if the passed collection contains at least one element e
     * such that <code>(o == null ? e == null : o.equals(e))</code>, o being the value returned from the passed long
     * function.
     *
     * @param collection A Collection of elements of type &lt;R&gt;, to be checked for whether it contains a value
     *                   returned from a passed LongFunction.
     * @param function   A LongFunction returning a value of type &lt;R&gt; to be checked for whether it is contained in
     *                   a passed Collection.
     * @param <R>        The type of elements in the passed Collection. Also, the type of the value returned by the
     *                   passed LongFunction.
     * @return A LongPredicate that applies the given LongFunction to its parameter, resulting in a value of type
     * &lt;R&gt;. The LongPredicate checks that the returned value is contained in a passed Collection.
     */
    public static <R> LongPredicate longToObjContains(Collection<? extends R> collection, LongFunction<? extends R> function) {
        return l -> collection != null && collection.contains(function.apply(l));
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection&lt;R&gt;</code>, this method builds a
     * <code>LongPredicate</code> that determines if the collection returned by that long function contains the passed
     * <code>value</code> of type &lt;R&gt;. More formally, the <code>LongPredicate</code> built by this method returns
     * <code>true</code> if and only if the returned collection contains at least one element e such that
     * <code>(o == null ? e == null : o.equals(e))</code>, o being the passed constant value of type &lt;R&gt;.
     * <p>
     * This method is similar to {@link #longToObjContains(Collection, LongFunction)}, but instead of a built predicate
     * checking whether a passed collection contains a value returned by a function, in this method it checks whether a
     * collection returned by a long function contains a passed value.
     *
     * @param function A LongFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param value    A value of type &lt;R&gt; to be checked for whether a Collection returned by the above
     *                 LongFunction contains it.
     * @param <R>      The type of elements for collections returned by a passed LongFunction. Also, the type of the
     *                 passed value.
     * @return A LongPredicate that applies the given long function to its parameter resulting in a Collection&lt;R&gt;.
     * The LongPredicate checks that the returned Collection contains a passed constant value of type &lt;R&gt;.
     */
    public static <R> LongPredicate inverseLongToObjContains(LongFunction<? extends Collection<R>> function, R value) {
        return l -> {
            Collection<R> collection = function.apply(l);
            return collection != null && collection.contains(value);
        };
    }

    /**
     * Given a <code>long</code> array, and a <code>ToLongFunction</code> that takes an element of type &lt;T&gt;, this
     * method builds a <code>Predicate</code> that determines if the given array contains the value returned by the
     * <code>ToLongFunction</code>.
     *
     * @param longs  An array of longs, to be checked for whether it contains a value returned from a passed
     *                 ToLongFunction.
     * @param function A ToLongFunction taking a value of type &lt;T&gt;, whose return value is to be checked for
     *                 whether it is contained in a passed array.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given ToLongFunction to its parameter of type &lt;T&gt;. The Predicate
     * checks that the returned value is contained in a passed array of longs.
     */
    public static <T> Predicate<T> objToLongContains(long[] longs, ToLongFunction<? super T> function) {
        return t -> longs != null && LongStreamUtils.longAnyMatch(longs, isLongEqual(function.applyAsLong(t)));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of longs, this method
     * builds a <code>Predicate</code> that determines if the array returned by that function contains the passed
     * constant long value.
     * <p>
     * This method is similar to {@link #objToLongContains(long[], ToLongFunction)}, but instead of a built
     * predicate checking whether a passed array contains a value returned by a function, in this method it checks
     * whether an array returned by a function contains a passed long value.
     *
     * @param function A Function that returns an array of longs.
     * @param value    A constant long value to be checked for whether an array of longs returned by the above Function
     *                 contains it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given function to its parameter resulting in a long array. The Predicate
     * checks that the returned array contains a passed constant long value.
     */
    public static <T> Predicate<T> inverseObjToLongContains(Function<T, ? extends long[]> function, long value) {
        return t -> {
            long[] longs = function.apply(t);
            return LongStreamUtils.longAnyMatch(longs, isLongEqual(value));
        };
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of an arbitrary type, this method builds a
     * <code>LongPredicate</code> that determines whether that returned value is <code>null</code>.
     *
     * @param function A LongFunction that returns a value of an arbitrary type.
     * @return A LongPredicate that applies a LongFunction to its parameter, which returns a value of an arbitrary type.
     * The predicate determines whether that value is null.
     */
    public static LongPredicate longIsNull(LongFunction<?> function) {
        return l -> Objects.isNull(function.apply(l));
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of an arbitrary type, this method builds a
     * <code>LongPredicate</code> that determines whether that returned value is <i>not</i> <code>null</code>.
     *
     * @param function A LongFunction that returns a value of an arbitrary type.
     * @return A LongPredicate that applies a LongFunction to its parameter, which returns a value of an arbitrary type.
     * The predicate determines whether that value is not null.
     */
    public static LongPredicate longIsNotNull(LongFunction<?> function) {
        return longNot(longIsNull(function));
    }

    /**
     * Builds a <code>LongPredicate</code> that determines whether a value is greater than a passed constant long value.
     *
     * @param compareTo A constant long value to be compared to the target value of a LongPredicate built by this
     *                  method.
     * @return A LongPredicate that compares its target value to a passed constant long value and determines whether it
     * is greater than that value.
     */
    public static LongPredicate longGt(long compareTo) {
        return l -> l > compareTo;
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds a <code>LongPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is greater than a passed constant value of type &lt;R&gt;
     * (also a <code>Comparable</code>).
     *
     * @param function  A LongFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a LongFunction.
     * @param <R>       The return type of the passed LongFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A LongPredicate that applies a LongFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is greater.
     */
    public static <R extends Comparable<R>> LongPredicate longGt(LongFunction<? extends R> function, R compareTo) {
        return l -> Objects.compare(function.apply(l), compareTo, nullsLast(naturalOrder())) > 0;
    }

    /**
     * Given a <code>ToLongFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * a passed constant long value.
     *
     * @param function  A ToLongFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant long value to see whether it is
     *                  greater.
     * @param compareTo A constant long value to be compared with a value returned by a passed ToLongFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToLongFunction to its target element, and compares its long return value to a
     * passed constant value to determine whether the return value is greater.
     */
    public static <T> Predicate<T> toLongGt(ToLongFunction<? super T> function, long compareTo) {
        return t -> t != null && function.applyAsLong(t) > compareTo;
    }

    /**
     * Builds a <code>LongPredicate</code> that determines whether a value is greater than or equal to a passed constant
     * long value.
     *
     * @param compareTo A constant long value to be compared to the target value of a LongPredicate built by this
     *                  method.
     * @return A LongPredicate that compares its target value to a passed constant long value and determines whether it
     * is greater than or equal to that value.
     */
    public static LongPredicate longGte(long compareTo) {
        return l -> l >= compareTo;
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds a <code>LongPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is greater than or equal to a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A LongFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a LongFunction.
     * @param <R>       The return type of the passed LongFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A LongPredicate that applies a LongFunction to its target value, and compares its Comparable return value
     * to a passed constant value to determine whether the return value is greater or equal to it.
     */
    public static <R extends Comparable<R>> LongPredicate longGte(LongFunction<? extends R> function, R compareTo) {
        return l -> Objects.compare(function.apply(l), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    /**
     * Given a <code>ToLongFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * or equal to a passed constant long value.
     *
     * @param function  A ToLongFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant long value to see whether it is
     *                  greater than or equal to it.
     * @param compareTo A constant long value to be compared with a value returned by a passed ToLongFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToLongFunction to its target element, and compares its long return value to a
     * passed constant value to determine whether the return value is greater than or equal to it.
     */
    public static <T> Predicate<T> toLongGte(ToLongFunction<? super T> function, long compareTo) {
        return t -> t != null && function.applyAsLong(t) >= compareTo;
    }

    /**
     * Builds a <code>LongPredicate</code> that determines whether a value is less than a passed constant long value.
     *
     * @param compareTo A constant long value to be compared to the target value of a LongPredicate built by this
     *                  method.
     * @return A LongPredicate that compares its target value to a passed constant long value and determines whether it
     * is less than that value.
     */
    public static LongPredicate longLt(long compareTo) {
        return l -> l < compareTo;
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds a <code>LongPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is less than a passed constant value of type &lt;R&gt; (also a
     * <code>Comparable</code>).
     *
     * @param function  A LongFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a LongFunction.
     * @param <R>       The return type of the passed LongFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A LongPredicate that applies a LongFunction to its target value, and compares its Comparable return value
     * to a passed constant value to determine whether the return value is less than it.
     */
    public static <R extends Comparable<R>> LongPredicate longLt(LongFunction<? extends R> function, R compareTo) {
        return l -> Objects.compare(function.apply(l), compareTo, nullsLast(naturalOrder())) < 0;
    }

    /**
     * Given a <code>ToLongFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than a
     * passed constant long value.
     *
     * @param function  A ToLongFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant long value to see whether it is
     *                  less than it.
     * @param compareTo A constant long value to be compared with a value returned by a passed ToLongFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToLongFunction to its target element, and compares its long return value to a
     * passed constant value to determine whether the return value is less than it.
     */
    public static <T> Predicate<T> toLongLt(ToLongFunction<? super T> function, long compareTo) {
        return t -> t != null && function.applyAsLong(t) < compareTo;
    }

    /**
     * Builds a <code>LongPredicate</code> that determines whether a value is less than or equal to a passed constant
     * long value.
     *
     * @param compareTo A constant long value to be compared to the target value of a LongPredicate built by this
     *                  method.
     * @return A LongPredicate that compares its target value to a passed constant long value and determines whether it
     * is less than or equal to that value.
     */
    public static LongPredicate longLte(long compareTo) {
        return l -> l <= compareTo;
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds a <code>LongPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is less than or equal to a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A LongFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a LongFunction.
     * @param <R>       The return type of the passed LongFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A LongPredicate that applies a LongFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is less or equal to it.
     */
    public static <R extends Comparable<R>> LongPredicate longLte(LongFunction<? extends R> function, R compareTo) {
        return l -> Objects.compare(function.apply(l), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    /**
     * Given a <code>ToLongFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than or
     * equal to a passed constant long value.
     *
     * @param function  A ToLongFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant long value to see whether it is
     *                  less than or equal to it.
     * @param compareTo A constant long value to be compared with a value returned by a passed ToLongFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToLongFunction to its target element, and compares its long return value
     * to a passed constant value to determine whether the return value is less than or equal to it.
     */
    public static <T> Predicate<T> toLongLte(ToLongFunction<? super T> function, long compareTo) {
        return t -> t != null && function.applyAsLong(t) <= compareTo;
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds a <code>LongPredicate</code> that determines whether the returned <code>Collection</code> is empty.
     *
     * @param function A LongFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return A LongPredicate that applies its parameter to a LongFunction that returns a Collection of elements of an
     * arbitrary type. The LongPredicate determines whether the returned Collection is empty.
     */
    public static LongPredicate isLongCollEmpty(LongFunction<? extends Collection<?>> function) {
        return l -> CollectionUtils.isEmpty(function.apply(l));
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds a <code>LongPredicate</code> that determines whether the returned <code>Collection</code> is
     * <i>not</i> empty.
     *
     * @param function A LongFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return A LongPredicate that applies its parameter to a LongFunction that returns a Collection of elements of
     * an arbitrary type. The LongPredicate determines whether the returned Collection is <i>not</i> empty.
     */
    public static LongPredicate isLongCollNotEmpty(LongFunction<? extends Collection<?>> function) {
        return longNot(isLongCollEmpty(function));
    }

    /**
     * Given a <code>Function</code> that returns an array of longs, this method builds a <code>Predicate</code> that
     * determines whether the returned array is empty.
     *
     * @param function A Function that returns an array of longs.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of longs. The Predicate
     * determines whether the returned array is empty.
     */
    public static <T> Predicate<T> isLongArrayEmpty(Function<? super T, long[]> function) {
        return t -> {
            long[] longs = t == null ? null : function.apply(t);
            return longs == null || longs.length == 0;
        };
    }

    /**
     * Given a <code>Function</code> that returns an array of longs, this method builds a <code>Predicate</code> that
     * determines whether the returned array is <i>not</i> empty.
     *
     * @param function A Function that returns an array of longs.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of longs. The Predicate
     * determines whether the returned array is <i>not</i> empty.
     */
    public static <T> Predicate<T> isLongArrayNotEmpty(Function<? super T, long[]> function) {
        return not(isLongArrayEmpty(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of longs, and a
     * <code>LongPredicate</code>, this method builds a <code>Predicate</code> that determines whether all longs in the
     * returned array match the <code>LongPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of longs.
     * @param predicate A LongPredicate that will be applied to all elements of an array of longs returned by the passed
     *                  Function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * longs. The Predicate determines whether all elements in that array match a passed LongPredicate.
     */
    public static <T> Predicate<T> objToLongsAllMatch(Function<T, ? extends long[]> function, LongPredicate predicate) {
        return t -> t != null && LongStreamUtils.longAllMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>LongPredicate</code> that determines whether all elements in
     * the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A LongFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  LongFunction.
     * @param <R>       The type of the elements of a Collection returned by a passed LongFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A LongPredicate that applies its parameter to a LongFunction that returns a Collection of elements of
     * type &lt;R&gt;. The LongPredicate determines whether all elements in that Collection match a passed Predicate.
     */
    public static <R> LongPredicate longToObjectsAllMatch(LongFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return l -> StreamUtils.allMatch(function.apply(l), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of longs, and a
     * <code>LongPredicate</code>, this method builds a <code>Predicate</code> that determines whether any of the longs
     * in the returned array match the <code>LongPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of longs.
     * @param predicate A LongPredicate that will be applied to all elements of an array of longs returned by the passed
     *                  Function to determine whether any match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * longs. The Predicate determines whether any of the elements in that array match a passed LongPredicate.
     */
    public static <T> Predicate<T> objToLongsAnyMatch(Function<T, ? extends long[]> function, LongPredicate predicate) {
        return t -> t != null && LongStreamUtils.longAnyMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>LongPredicate</code> that determines whether any of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A LongFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  LongFunction to determine whether any match.
     * @param <R>       The type of the elements of a Collection returned by a passed LongFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A LongPredicate that applies its parameter to a LongFunction that returns a Collection of elements of
     * type &lt;R&gt;. The LongPredicate determines whether any of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> LongPredicate longToObjectsAnyMatch(LongFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return l -> StreamUtils.anyMatch(function.apply(l), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of longs, and a
     * <code>LongPredicate</code>, this method builds a <code>Predicate</code> that determines whether none of the
     * longs in the returned array match the <code>LongPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of longs.
     * @param predicate A LongPredicate that will be applied to all elements of an array of longs returned by the passed
     *                  Function to determine whether none match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * longs. The Predicate determines whether none of the elements in that array match a passed LongPredicate.
     */
    public static <T> Predicate<T> objToLongsNoneMatch(Function<T, ? extends long[]> function, LongPredicate predicate) {
        return t -> t != null && LongStreamUtils.longNoneMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>LongFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>LongPredicate</code> that determines whether none of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A LongFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  LongFunction to determine whether none match.
     * @param <R>       The type of the elements of a Collection returned by a passed LongFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A LongPredicate that applies its parameter to a LongFunction that returns a Collection of elements of
     * type &lt;R&gt;. The LongPredicate determines whether none of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> LongPredicate longToObjectsNoneMatch(LongFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return l -> StreamUtils.noneMatch(function.apply(l), predicate);
    }

    /**
     * Given a <code>ToLongFunction</code> taking a value of type &lt;T&gt;, and a <code>LongPredicate</code>, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and applies the return value of
     * the <code>ToLongFunction</code> to the given predicate. It is a way of adapting a <code>LongPredicate</code> to a
     * <code>Stream</code> of a different type. For example, the
     * {@link LongStreamUtils#indexOfFirstLong(long[], LongPredicate)} method uses this predicate in its implementation
     * (Note that the <code>longPairWithIndex()</code> below refers to {@link LongMapperUtils#longPairWithIndex()}):
     * <pre>
     * public static int indexOfFirstLong(long[] longs, LongPredicate longPredicate) {
     *     return defaultLongStream(longs)
     *         .mapToObj(longPairWithIndex())
     *         .filter(mapToLongAndFilter(LongIndexPair::getLongValue, longPredicate))
     *         .mapToInt(LongIndexPair::getIndex)
     *         .findFirst()
     *         .orElse(-1);
     * }
     * </pre>
     * The map-and-filter predicate is necessary in this case because we have a predicate that operates on the original
     * <code>long</code> type of the stream, but a mapping operation has changed the type to a
     * <code>LongIndexPair</code>. The <code>LongIndexPair::getLongValue</code> method reference, passed as the
     * <code>ToLongFunction</code> argument to this method, retrieves the original long value before the
     * <code>LongPredicate</code> evaluates it.
     * <p>
     * As a side note, the pairing of an object with another can be very useful in streaming operations. In this case,
     * we need to have both the long value and its index available at the same point in the stream, so we temporarily
     * pair the two together, before mapping to just the index.
     *
     * @param function  A ToLongFunction to transform an element of type &lt;T&gt; to a long before it is passed to a
     *                  LongPredicate.
     * @param predicate A LongPredicate whose value will be retrieved from a given transformer function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, and applies the return value of a passed
     * ToLongFunction to a passed LongPredicate.
     */
    public static <T> Predicate<T> mapToLongAndFilter(ToLongFunction<? super T> function, LongPredicate predicate) {
        return l -> predicate.test(function.applyAsLong(l));
    }

    /**
     * Given a <code>LongFunction</code> that returns a value of type &lt;T&gt;, and a <code>Predicate</code>, this
     * method builds a <code>LongPredicate</code> that applies the return value of the <code>LongFunction</code> to
     * the given predicate. It is a way of adapting a long value to a <code>Predicate</code>. This method is the
     * inverse of {@link #mapToLongAndFilter(ToLongFunction, LongPredicate)}. In that method, we map from a value of
     * type &lt;T&gt; to a <code>long</code>, and then apply a <code>LongPredicate</code>. In this method we map from a
     * <code>long</code> to a value of type &lt;T&gt;, and then apply a <code>Predicate&lt;T&gt;</code>.
     *
     * @param function  A LongFunction to transform a long value to an element of type &lt;T&gt; before it is passed
     *                  to a Predicate.
     * @param predicate A Predicate whose value will be retrieved from a given LongFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A LongPredicate that applies the return value of a passed LongFunction to a passed Predicate.
     */
    public static <T> LongPredicate longMapAndFilter(LongFunction<? extends T> function, Predicate<? super T> predicate) {
        return l -> predicate.test(function.apply(l));
    }
}
