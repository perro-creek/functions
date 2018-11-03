package org.hringsak.functions.predicate;

import org.hringsak.functions.mapper.IntMapperUtils;
import org.hringsak.functions.stream.IntStreamUtils;
import org.hringsak.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.hringsak.functions.predicate.PredicateUtils.not;

/**
 * Methods that build predicates specifically those involving primitive <code>int</code> types.
 */
public final class IntPredicateUtils {

    private IntPredicateUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>int</code> or
     * <code>Integer</code>, and returns a <code>boolean</code> or <code>Boolean</code>, to an
     * <code>IntPredicate</code>. This could be useful in a situation where methods of the
     * <code>IntPredicate</code> functional interface are to be called on a method reference. For example:
     * <pre>
     *     private int[] getIntsInRange(int[] ints) {
     *         return Arrays.stream(ints)
     *             .filter(IntPredicateUtils.intPredicate(this::isGreaterThanOrEqualToMin)
     *                 .and(this::isLessThanOrEqualToMax))
     *             .toArray();
     *     }
     *
     *     private boolean isGreaterThanOrEqualToMin(int target) {
     *         ...
     *     }
     *
     *     private boolean isLessThanOrEqualToMax(int target) {
     *         ...
     *     }
     * </pre>
     * The <code>IntPredicate.and(...)</code> method can only be called on the method reference because of the cast.
     * Note that the second predicate does not need to be cast, because <code>IntPredicate.and(...)</code> already
     * takes an <code>IntPredicate</code> just like this method, and so is already doing a cast.
     *
     * @param predicate A method reference to be cast to an IntPredicate.
     * @return A method reference cast to an IntPredicate.
     */
    public static IntPredicate intPredicate(IntPredicate predicate) {
        return predicate;
    }

    /**
     * Builds an <code>IntPredicate</code> from a passed <code>BiPredicate&lt;Integer, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have an integer
     * predicate method to call that takes two parameters - the first one being the <code>int</code> element on which
     * you are streaming, and the second being some constant value that will be passed to all invocations. This would
     * typically be called from within a chain of method calls based on a <code>IntStream</code>. In the following
     * example, assume the <code>Stock</code> objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code>
     * method are to be filtered based on whether their price is less than a limit for the passed <code>Client</code>.
     * Also, assume that the monetary values are being stored as integral values in cents:
     * <pre>
     *     private int[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToInt(Stock::getPrice)
     *             .filter(IntPredicateUtils.intPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1);
     *     }
     *
     *     private boolean isPriceBelowLimit(int price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToInt(Stock::getPrice)
     *         .filter(intPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    Integer, and the second of type &lt;U&gt;. The method reference will be converted by this
     *                    method to an IntPredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the second parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the second parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the second parameter to each invocation of
     *                    biPredicate.
     * @return An IntPredicate adapted from the passed BiPredicate.
     */
    public static <U> IntPredicate intPredicate(BiPredicate<Integer, ? super U> biPredicate, U value) {
        return i -> biPredicate.test(i, value);
    }

    /**
     * Builds an <code>IntPredicate</code> from a passed <code>BiPredicate&lt;Integer, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have an integer
     * predicate method to call that takes two parameters. In the <code>BiPredicate</code> passed to this method, the
     * parameters are basically the same as in the call to {@link #intPredicate(BiPredicate, Object)}, but in the
     * inverse order. Here, the first parameter is a constant value that will be passed to all invocations of the
     * method, and the second parameter is the target <code>int</code> element on which you are streaming. This would
     * typically be called from within a chain of method calls based on an <code>IntStream</code>. In the following
     * example, assume the <code>Stock</code> objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code>
     * method are to be filtered based on whether their price is less than a limit for the passed <code>Client</code>.
     * Also, assume that the monetary values are being stored as integral values in cents:
     * <pre>
     *     private int[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToInt(Stock::getPrice)
     *             .filter(IntPredicateUtils.inverseIntPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1);
     *     }
     *
     *     private boolean isPriceBelowLimit(Client client, int price) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToInt(Stock::getPrice)
     *         .filter(inverseIntPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    &lt;U&gt;, and the second of type Integer. The method reference will be converted by this
     *                    method to an IntPredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the first parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the first parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the first parameter to each invocation of
     *                    biPredicate.
     * @return An IntPredicate adapted from the passed BiPredicate.
     */
    public static <U> IntPredicate inverseIntPredicate(BiPredicate<? super U, Integer> biPredicate, U value) {
        return i -> biPredicate.test(value, i);
    }

    /**
     * Builds an integer predicate based on a passed constant <code>boolean</code> value. The target element of type
     * <code>int</code> that is passed to the predicate is ignored, and the constant value is simply returned. This
     * comes in handy when combining one predicate with another using <code>Predicate.and(...)</code> or
     * <code>Predicate.or(...)</code>. Consider the following example, assuming that the monetary values are being
     * stored as integral values in cents:
     * <pre>
     *     private int[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         boolean hasLimit = client.getLimit() &gt; 0;
     *         return stocks.stream()
     *             .mapToInt(Stock::getPrice)
     *             .filter(IntPredicateUtils.intNot(IntPredicateUtils.intConstant(hasLimit))
     *                 .or(IntPredicateUtils.intPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1.0D);
     *     }
     *
     *     private boolean isPriceBelowLimit(int price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToInt(Stock::getPrice)
     *         .filter(intNot(intConstant(hasLimit))
     *             .or(intPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1.0D);
     * </pre>
     *
     * @param b A constant boolean value that will be the result of every invocation of the returned IntPredicate.
     * @return An IntPredicate returning a constant boolean value.
     */
    public static IntPredicate intConstant(boolean b) {
        return i -> b;
    }

    /**
     * An <code>IntPredicate</code> that simply negates the passed <code>IntPredicate</code>. A predicate can
     * always be negated via <code>predicate.negate()</code>, however using this method may improve readability.
     *
     * @param predicate An IntPredicate whose result is to be negated.
     * @return A negated IntPredicate.
     */
    public static IntPredicate intNot(IntPredicate predicate) {
        return predicate.negate();
    }

    /**
     * This method builds an <code>IntPredicate</code> whose parameter is to be compared for equality to the passed
     * <code>int</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the IntPredicate built by this method.
     * @return An IntPredicate that compares its parameter to a constant int value for equality.
     */
    public static IntPredicate isIntEqual(int value) {
        return i -> i == value;
    }

    /**
     * Given an <code>IntUnaryOperator</code> this method builds an <code>IntPredicate</code> that determines if the
     * value returned by that operator is equal to the passed constant int value. For example:
     * <pre>
     *     int[] ints = IntStream.range(1, 10).toArray();
     *     int[] evens = Arrays.stream(ints)
     *             .filter(IntPredicateUtils.isIntEqual(IntMapperUtils.intModulo(2), 0))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     int[] evens = Arrays.stream(ints)
     *             .filter(isIntEqual(intModulo(2), 0))
     *             .toArray();
     * </pre>
     *
     * @param operator An IntUnaryOperator whose return value is to be compared with a passed constant int value
     *                 for equality.
     * @param value    A value to be compared to the result of the passed operator for equality.
     * @return An IntPredicate whose return value is compared for equality to a passed constant int value.
     */
    public static IntPredicate isIntEqual(IntUnaryOperator operator, int value) {
        return d -> operator.applyAsInt(d) == value;
    }

    /**
     * This method builds an <code>IntPredicate</code> whose parameter is to be compared for inequality to the passed
     * <code>int</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the IntPredicate built by this method.
     * @return An IntPredicate that compares its parameter to a constant int value for inequality.
     */
    public static IntPredicate isIntNotEqual(int value) {
        return intNot(isIntEqual(value));
    }

    /**
     * Given an <code>IntUnaryOperator</code> this method builds an <code>IntPredicate</code> that determines if the
     * value returned by that operator is <i>not</i> equal to the passed constant int value. For example:
     * <pre>
     *     int[] ints = IntStream.range(1, 10).toArray();
     *     int[] odds = Arrays.stream(ints)
     *             .filter(IntPredicateUtils.isIntNotEqual(IntMapperUtils.dblModulo(2), 0))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     int[] odds = Arrays.stream(ints)
     *             .filter(isIntNotEqual(intModulo(2), 0))
     *             .toArray();
     * </pre>
     *
     * @param operator An IntUnaryOperator whose return value is to be compared with a passed constant int value for
     *                 inequality.
     * @param value    A value to be compared to the result of the passed operator for inequality.
     * @return An IntPredicate whose return value is compared for inequality to a passed constant int value.
     */
    public static IntPredicate isIntNotEqual(IntUnaryOperator operator, int value) {
        return intNot(isIntEqual(operator, value));
    }

    /**
     * Given a <code>Collection</code> whose elements are of type &lt;R&gt;, and an <code>IntFunction</code> that
     * returns a value of type &lt;R&gt;, this method builds an <code>IntPredicate</code> that determines if the given
     * collection contains the value returned by the int function. More formally, the <code>IntPredicate</code> built by
     * this method returns <code>true</code> if and only if the passed collection contains at least one element e such
     * that <code>(o == null ? e == null : o.equals(e))</code>, o being the value returned from the passed int function.
     *
     * @param collection A Collection of elements of type &lt;R&gt;, to be checked for whether it contains a value
     *                   returned from a passed IntFunction.
     * @param function   An IntFunction returning a value of type &lt;R&gt; to be checked for whether it is contained in
     *                   a passed Collection.
     * @param <R>        The type of elements in the passed Collection. Also, the type of the value returned by the
     *                   passed IntFunction.
     * @return An IntPredicate that applies the given IntFunction to its parameter, resulting in a value of type
     * &lt;R&gt;. The IntPredicate checks that the returned value is contained in a passed Collection.
     */
    public static <R> IntPredicate intToObjContains(Collection<? extends R> collection, IntFunction<? extends R> function) {
        return i -> collection != null && collection.contains(function.apply(i));
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection&lt;R&gt;</code>, this method builds an
     * <code>IntPredicate</code> that determines if the collection returned by that int function contains the passed
     * <code>value</code> of type &lt;R&gt;. More formally, the <code>IntPredicate</code> built by this method returns
     * <code>true</code> if and only if the returned collection contains at least one element e such that
     * <code>(o == null ? e == null : o.equals(e))</code>, o being the passed constant value of type &lt;R&gt;.
     * <p>
     * This method is similar to {@link #intToObjContains(Collection, IntFunction)}, but instead of a built predicate
     * checking whether a passed collection contains a value returned by a function, in this method it checks whether
     * a collection returned by an int function contains a passed value.
     *
     * @param function An IntFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param value    A value of type &lt;R&gt; to be checked for whether a Collection returned by the above
     *                 IntFunction contains it.
     * @param <R>      The type of elements for collections returned by a passed IntFunction. Also, the type of the
     *                 passed value.
     * @return An IntPredicate that applies the given int function to its parameter resulting in a
     * Collection&lt;R&gt;. The IntPredicate checks that the returned Collection contains a passed constant value of
     * type &lt;R&gt;.
     */
    public static <R> IntPredicate inverseIntToObjContains(IntFunction<? extends Collection<R>> function, R value) {
        return i -> {
            Collection<R> collection = function.apply(i);
            return collection != null && collection.contains(value);
        };
    }

    /**
     * Given an <code>int</code> array, and a <code>ToIntFunction</code> that takes an element of type &lt;T&gt;,
     * this method builds a <code>Predicate</code> that determines if the given array contains the value returned by the
     * <code>ToIntFunction</code>.
     *
     * @param ints     An array of ints, to be checked for whether it contains a value returned from a passed
     *                 ToIntFunction.
     * @param function A ToIntFunction taking a value of type &lt;T&gt;, whose return value is to be checked for
     *                 whether it is contained in a passed array.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given ToIntFunction to its parameter of type &lt;T&gt;. The Predicate
     * checks that the returned value is contained in a passed array of ints.
     */
    public static <T> Predicate<T> objToIntContains(int[] ints, ToIntFunction<? super T> function) {
        return t -> ints != null && IntStreamUtils.intAnyMatch(ints, isIntEqual(function.applyAsInt(t)));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of ints, this method
     * builds a <code>Predicate</code> that determines if the array returned by that function contains the passed
     * constant int value.
     * <p>
     * This method is similar to {@link #objToIntContains(int[], ToIntFunction)}, but instead of a built predicate
     * checking whether a passed array contains a value returned by a function, in this method it checks
     * whether an array returned by a function contains a passed int value.
     *
     * @param function A Function that returns an array of ints.
     * @param value    A constant int value to be checked for whether an array of ints returned by the above Function
     *                 contains it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given function to its parameter resulting in an int array. The Predicate
     * checks that the returned array contains a passed constant int value.
     */
    public static <T> Predicate<T> inverseObjToIntContains(Function<T, int[]> function, int value) {
        return t -> {
            int[] ints = function.apply(t);
            return IntStreamUtils.intAnyMatch(ints, isIntEqual(value));
        };
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of an arbitrary type, this method builds an
     * <code>IntPredicate</code> that determines whether that returned value is <code>null</code>.
     *
     * @param function An IntFunction that returns a value of an arbitrary type.
     * @return An IntPredicate that applies an IntFunction to its parameter, which returns a value of an arbitrary type.
     * The predicate determines whether that value is null.
     */
    public static IntPredicate intIsNull(IntFunction<?> function) {
        return i -> Objects.isNull(function.apply(i));
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of an arbitrary type, this method builds an
     * <code>IntPredicate</code> that determines whether that returned value is <i>not</i> <code>null</code>.
     *
     * @param function An IntFunction that returns a value of an arbitrary type.
     * @return An IntPredicate that applies an IntFunction to its parameter, which returns a value of an arbitrary type.
     * The predicate determines whether that value is not null.
     */
    public static IntPredicate intIsNotNull(IntFunction<?> function) {
        return intNot(intIsNull(function));
    }

    /**
     * Builds an <code>IntPredicate</code> that determines whether a value is greater than a passed constant int value.
     *
     * @param compareTo A constant int value to be compared to the target value of an IntPredicate built by this method.
     * @return An IntPredicate that compares its target value to a passed constant int value and determines whether it
     * is greater than that value.
     */
    public static IntPredicate intGt(int compareTo) {
        return i -> i > compareTo;
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds an <code>IntPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is greater than a passed constant value of type &lt;R&gt;
     * (also a <code>Comparable</code>).
     *
     * @param function  An IntFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of an IntFunction.
     * @param <R>       The return type of the passed IntFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return An IntPredicate that applies an IntFunction to its target value, and compares its Comparable return value
     * to a passed constant value to determine whether the return value is greater.
     */
    public static <R extends Comparable<R>> IntPredicate intGt(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) > 0;
    }

    /**
     * Given a <code>ToIntFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * a passed constant int value.
     *
     * @param function  A ToIntFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant int value to see whether it is
     *                  greater.
     * @param compareTo A constant int value to be compared with a value returned by a passed ToIntFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToIntFunction to its target element, and compares its int return value to a
     * passed constant value to determine whether the return value is greater.
     */
    public static <T> Predicate<T> toIntGt(ToIntFunction<? super T> function, int compareTo) {
        return t -> t != null && function.applyAsInt(t) > compareTo;
    }

    /**
     * Builds an <code>IntPredicate</code> that determines whether a value is greater than or equal to a passed constant
     * int value.
     *
     * @param compareTo A constant int value to be compared to the target value of an IntPredicate built by this method.
     * @return An IntPredicate that compares its target value to a passed constant int value and determines whether it
     * is greater than or equal to that value.
     */
    public static IntPredicate intGte(int compareTo) {
        return i -> i >= compareTo;
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds an <code>IntPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is greater than or equal to a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  An IntFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of an IntFunction.
     * @param <R>       The return type of the passed IntFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return An IntPredicate that applies an IntFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is greater or equal to it.
     */
    public static <R extends Comparable<R>> IntPredicate intGte(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    /**
     * Given a <code>ToIntFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * or equal to a passed constant int value.
     *
     * @param function  A ToIntFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant int value to see whether it is
     *                  greater than or equal to it.
     * @param compareTo A constant int value to be compared with a value returned by a passed ToIntFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToIntFunction to its target element, and compares its int return value to a
     * passed constant value to determine whether the return value is greater than or equal to it.
     */
    public static <T> Predicate<T> toIntGte(ToIntFunction<? super T> function, int compareTo) {
        return t -> t != null && function.applyAsInt(t) >= compareTo;
    }

    /**
     * Builds an <code>IntPredicate</code> that determines whether a value is less than a passed constant int value.
     *
     * @param compareTo A constant int value to be compared to the target value of an IntPredicate built by this method.
     * @return An IntPredicate that compares its target value to a passed constant int value and determines whether it
     * is less than that value.
     */
    public static IntPredicate intLt(int compareTo) {
        return i -> i < compareTo;
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds an <code>IntPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is less than a passed constant value of type &lt;R&gt; (also a
     * <code>Comparable</code>).
     *
     * @param function  An IntFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of an IntFunction.
     * @param <R>       The return type of the passed IntFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return An IntPredicate that applies an IntFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is less than it.
     */
    public static <R extends Comparable<R>> IntPredicate intLt(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) < 0;
    }

    /**
     * Given a <code>ToIntFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than a
     * passed constant int value.
     *
     * @param function  A ToIntFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant int value to see whether it is
     *                  less than it.
     * @param compareTo A constant int value to be compared with a value returned by a passed ToIntFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToIntFunction to its target element, and compares its int return value to a
     * passed constant value to determine whether the return value is less than it.
     */
    public static <T> Predicate<T> toIntLt(ToIntFunction<? super T> function, int compareTo) {
        return t -> t != null && function.applyAsInt(t) < compareTo;
    }

    /**
     * Builds an <code>IntPredicate</code> that determines whether a value is less than or equal to a passed constant
     * int value.
     *
     * @param compareTo A constant int value to be compared to the target value of an IntPredicate built by this method.
     * @return An IntPredicate that compares its target value to a passed constant int value and determines whether it
     * is less than or equal to that value.
     */
    public static IntPredicate intLte(int compareTo) {
        return i -> i <= compareTo;
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type &lt;R&gt;,
     * this method builds an <code>IntPredicate</code> that applies that function to its target value, and determines
     * whether the returned <code>Comparable</code> value is less than or equal to a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  An IntFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of an IntFunction.
     * @param <R>       The return type of the passed IntFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return An IntPredicate that applies an IntFunction to its target value, and compares its Comparable return value
     * to a passed constant value to determine whether the return value is less or equal to it.
     */
    public static <R extends Comparable<R>> IntPredicate intLte(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    /**
     * Given a <code>ToIntFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than or
     * equal to a passed constant int value.
     *
     * @param function  A ToIntFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant int value to see whether it is
     *                  less than or equal to it.
     * @param compareTo A constant int value to be compared with a value returned by a passed ToIntFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToIntFunction to its target element, and compares its int return value to a
     * passed constant value to determine whether the return value is less than or equal to it.
     */
    public static <T> Predicate<T> toIntLte(ToIntFunction<? super T> function, int compareTo) {
        return t -> t != null && function.applyAsInt(t) <= compareTo;
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds an <code>IntPredicate</code> that determines whether the returned <code>Collection</code> is empty.
     *
     * @param function An IntFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return An IntPredicate that applies its parameter to an IntFunction that returns a Collection of elements of an
     * arbitrary type. The IntPredicate determines whether the returned Collection is empty.
     */
    public static IntPredicate isIntCollEmpty(IntFunction<? extends Collection<?>> function) {
        return i -> CollectionUtils.isEmpty(function.apply(i));
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds an <code>IntPredicate</code> that determines whether the returned <code>Collection</code> is
     * <i>not</i> empty.
     *
     * @param function An IntFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return An IntPredicate that applies its parameter to an IntFunction that returns a Collection of elements of an
     * arbitrary type. The IntPredicate determines whether the returned Collection is <i>not</i> empty.
     */
    public static IntPredicate isIntCollNotEmpty(IntFunction<? extends Collection<?>> function) {
        return intNot(isIntCollEmpty(function));
    }

    /**
     * Given a <code>Function</code> that returns an array of int, this method builds a <code>Predicate</code> that
     * determines whether the returned array is empty.
     *
     * @param function A Function that returns an array of ints.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of ints. The Predicate
     * determines whether the returned array is empty.
     */
    public static <T> Predicate<T> isIntArrayEmpty(Function<? super T, int[]> function) {
        return t -> {
            int[] ints = t == null ? null : function.apply(t);
            return ints == null || ints.length == 0;
        };
    }

    /**
     * Given a <code>Function</code> that returns an array of ints, this method builds a <code>Predicate</code> that
     * determines whether the returned array is <i>not</i> empty.
     *
     * @param function A Function that returns an array of ints.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of ints. The Predicate
     * determines whether the returned array is <i>not</i> empty.
     */
    public static <T> Predicate<T> isIntArrayNotEmpty(Function<? super T, int[]> function) {
        return not(isIntArrayEmpty(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of ints, and an
     * <code>IntPredicate</code>, this method builds a <code>Predicate</code> that determines whether all ints in the
     * returned array match the <code>IntPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of ints.
     * @param predicate An IntPredicate that will be applied to all elements of an array of ints returned by the passed
     *                  Function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * ints. The Predicate determines whether all elements in that array match a passed IntPredicate.
     */
    public static <T> Predicate<T> objToIntsAllMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intAllMatch(function.apply(t), predicate);
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds an <code>IntPredicate</code> that determines whether all elements in
     * the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  An IntFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  IntFunction.
     * @param <R>       The type of the elements of a Collection returned by a passed IntFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return An IntPredicate that applies its parameter to an IntFunction that returns a Collection of elements of
     * type &lt;R&gt;. The IntPredicate determines whether all elements in that Collection match a passed Predicate.
     */
    public static <R> IntPredicate intToObjectsAllMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.allMatch(function.apply(i), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of ints, and an
     * <code>IntPredicate</code>, this method builds a <code>Predicate</code> that determines whether any of the ints in
     * the returned array match the <code>IntPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of ints.
     * @param predicate An IntPredicate that will be applied to all elements of an array of ints returned by the passed
     *                  Function to determine whether any match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * ints. The Predicate determines whether any of the elements in that array match a passed IntPredicate.
     */
    public static <T> Predicate<T> objToIntsAnyMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intAnyMatch(function.apply(t), predicate);
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds an <code>IntPredicate</code> that determines whether any of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  An IntFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  IntFunction to determine whether any match.
     * @param <R>       The type of the elements of a Collection returned by a passed IntFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return An IntPredicate that applies its parameter to an IntFunction that returns a Collection of elements of
     * type &lt;R&gt;. The IntPredicate determines whether any of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> IntPredicate intToObjectsAnyMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.anyMatch(function.apply(i), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of ints, and an
     * <code>IntPredicate</code>, this method builds a <code>Predicate</code> that determines whether none of the ints
     * in the returned array match the <code>IntPredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of ints.
     * @param predicate An IntPredicate that will be applied to all elements of an array of ints returned by the passed
     *                  Function to determine whether none match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * ints. The Predicate determines whether none of the elements in that array match a passed IntPredicate.
     */
    public static <T> Predicate<T> objToIntsNoneMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intNoneMatch(function.apply(t), predicate);
    }

    /**
     * Given an <code>IntFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds an <code>IntPredicate</code> that determines whether none of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  An IntFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  IntFunction to determine whether none match.
     * @param <R>       The type of the elements of a Collection returned by a passed IntFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return An IntPredicate that applies its parameter to an IntFunction that returns a Collection of elements of
     * type &lt;R&gt;. The IntPredicate determines whether none of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> IntPredicate intToObjectsNoneMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.noneMatch(function.apply(i), predicate);
    }

    /**
     * Given a <code>ToIntFunction</code> taking a value of type &lt;T&gt;, and an <code>IntPredicate</code>, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and applies the return value of
     * the <code>ToIntFunction</code> to the given predicate. It is a way of adapting an <code>IntPredicate</code> to a
     * <code>Stream</code> of a different type. For example, the
     * {@link IntStreamUtils#indexOfFirstInt(int[], IntPredicate)} method uses this predicate in its implementation
     * (Note that the <code>intPairWithIndex()</code> below refers to {@link IntMapperUtils#intPairWithIndex()}):
     * <pre>
     * public static int indexOfFirstInt(int[] ints, IntPredicate intPredicate) {
     *     return defaultIntStream(ints)
     *         .mapToObj(intPairWithIndex())
     *         .filter(mapToIntAndFilter(IntIndexPair::getIntValue, intPredicate))
     *         .mapToInt(IntIndexPair::getIndex)
     *         .findFirst()
     *         .orElse(-1);
     * }
     * </pre>
     * The map-and-filter predicate is necessary in this case because we have a predicate that operates on the original
     * <code>int</code> type of the stream, but a mapping operation has changed the type to an
     * <code>IntIndexPair</code>. The <code>IntIndexPair::getIntValue</code> method reference, passed as the
     * <code>ToIntFunction</code> argument to this method, retrieves the original int value before the
     * <code>IntPredicate</code> evaluates it.
     * <p>
     * As a side note, the pairing of an object with another can be very useful in streaming operations. In this case,
     * we need to have both the int value and its index available at the same point in the stream, so we temporarily
     * pair the two together, before mapping to just the index.
     *
     * @param function  A ToIntFunction to transform an element of type &lt;T&gt; to an int before it is passed to an
     *                  IntPredicate.
     * @param predicate An IntPredicate whose value will be retrieved from a given transformer function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, and applies the return value of a passed
     * ToIntFunction to a passed IntPredicate.
     */
    public static <T> Predicate<T> mapToIntAndFilter(ToIntFunction<? super T> function, IntPredicate predicate) {
        return i -> predicate.test(function.applyAsInt(i));
    }

    /**
     * Given an <code>IntFunction</code> that returns a value of type &lt;T&gt;, and a <code>Predicate</code>, this
     * method builds an <code>IntPredicate</code> that applies the return value of the <code>IntFunction</code> to the
     * given predicate. It is a way of adapting a int value to a <code>Predicate</code>. This method is the inverse of
     * {@link #mapToIntAndFilter(ToIntFunction, IntPredicate)}. In that method, we map from a value of type &lt;T&gt; to
     * an <code>int</code>, and then apply an <code>IntPredicate</code>. In this method we map from an <code>int</code>
     * to a value of type &lt;T&gt;, and then apply a <code>Predicate&lt;T&gt;</code>.
     *
     * @param function  An IntFunction to transform an int value to an element of type &lt;T&gt; before it is passed to
     *                  a Predicate.
     * @param predicate A Predicate whose value will be retrieved from a given IntFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return An IntPredicate that applies the return value of a passed IntFunction to a passed Predicate.
     */
    public static <T> IntPredicate intMapAndFilter(IntFunction<? extends T> function, Predicate<? super T> predicate) {
        return i -> predicate.test(function.apply(i));
    }
}
