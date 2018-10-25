package org.hringsak.functions.predicate;

import org.hringsak.functions.mapper.DblMapperUtils;
import org.hringsak.functions.stream.DblStreamUtils;
import org.hringsak.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.hringsak.functions.predicate.PredicateUtils.not;

/**
 * Methods that build predicates specifically those involving primitive <code>double</code> types.
 */
public final class DblPredicateUtils {

    private DblPredicateUtils() {
    }

    /**
     * Simply casts a method reference, which takes a single parameter of type <code>double</code> or
     * <code>Double</code> and returns a <code>boolean</code> or <code>Boolean</code>), to a
     * <code>DoublePredicate</code>. This could be useful in a situation where methods of the
     * <code>DoublePredicate</code> functional interface are to be called on a method reference. For example:
     * <pre>
     *     private double[] getDoublesInRange(double[] doubles) {
     *         return Arrays.stream(doubles)
     *             .filter(DblPredicateUtils.dblPredicate(this::isGreaterThanOrEqualToMin)
     *                 .and(this::isLessThanOrEqualToMax))
     *             .toArray();
     *     }
     *
     *     private boolean isGreaterThanOrEqualToMin() {
     *         ...
     *     }
     *
     *     private boolean isLessThanOrEqualToMax() {
     *         ...
     *     }
     * </pre>
     * The <code>DoublePredicate.and(...)</code> method can only be called on the method reference because of the cast.
     * Note that the second predicate does not need to be cast, because <code>DoublePredicate.and(...)</code> already
     * takes a <code>DoublePredicate</code> just like this method, and so is already doing a cast.
     *
     * @param predicate A method reference to be cast to a DoublePredicate.
     * @return A method reference cast to a DoublePredicate.
     */
    public static DoublePredicate dblPredicate(DoublePredicate predicate) {
        return predicate;
    }

    /**
     * Builds a <code>DoublePredicate</code> from a passed <code>BiPredicate&lt;Double, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have a predicate
     * method to call that takes two parameters - the first one being the <code>double</code> element on which you are
     * streaming, and the second being some constant value that will be passed to all invocations. This would typically
     * be called from within a chain of method calls based on a <code>DoubleStream</code>. In the following example,
     * assume the <code>Stock</code> objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code> method are
     * to be filtered based on whether their price is less than a limit for the passed <code>Client</code>:
     * <pre>
     *     private double[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToDouble(Stock::getPrice)
     *             .filter(DblPredicateUtils.dblPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1.0D);
     *     }
     *
     *     private boolean isPriceBelowLimit(double price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToDouble(Stock::getPrice)
     *         .filter(dblPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1.0D);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    Double, and the second of type &lt;U&gt;. The method reference will be converted by this
     *                    method to a DoublePredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the second parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the second parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the second parameter to each invocation of
     *                    biPredicate.
     * @return A DoublePredicate adapted from the passed BiPredicate.
     */
    public static <U> DoublePredicate dblPredicate(BiPredicate<Double, ? super U> biPredicate, U value) {
        return d -> biPredicate.test(d, value);
    }

    /**
     * Builds a <code>DoublePredicate</code> from a passed <code>BiPredicate&lt;Double, U&gt;</code>, which can be very
     * useful in the common situation where you are streaming through a collection of elements, and have a predicate
     * method to call that takes two parameters. In the
     * <code>BiPredicate</code> passed to this method, the parameters are basically the same as in the call to
     * {@link #dblPredicate(BiPredicate, Object)}, but in the inverse order. Here, the first parameter is a constant
     * value that will be passed to all invocations of the method, and the second parameter is the target
     * <code>double</code> element on which you are streaming. This would typically be called from within a chain of
     * method calls based on a <code>DoubleStream</code>. In the following example, assume the <code>Stock</code>
     * objects passed to the <code>getHighestPriceOfStockBelowLimit(...)</code> method are to be filtered based on
     * whether their price is less than a limit for the passed <code>Client</code>:
     * <pre>
     *     private double[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         return stocks.stream()
     *             .mapToDouble(Stock::getPrice)
     *             .filter(DblPredicateUtils.inverseDblPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1.0D);
     *     }
     *
     *     private boolean isPriceBelowLimit(Client client, double price) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToDouble(Stock::getPrice)
     *         .filter(inverseDblPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1.0D);
     * </pre>
     *
     * @param biPredicate A method reference (a BiPredicate) which takes two parameters - the first of type
     *                    &lt;U&gt;, and the second of type Double. The method reference will be converted by this
     *                    method to a DoublePredicate. Behind the scenes, this BiPredicate will be called, passing the
     *                    constant value to each invocation as the first parameter.
     * @param value       A constant value, in that it will be passed to every invocation of the passed biPredicate as
     *                    the first parameter to it, and will have the same value for each of them.
     * @param <U>         The type of the constant value to be passed as the first parameter to each invocation of
     *                    biPredicate.
     * @return A DoublePredicate adapted from the passed BiPredicate.
     */
    public static <U> DoublePredicate inverseDblPredicate(BiPredicate<? super U, Double> biPredicate, U value) {
        return d -> biPredicate.test(value, d);
    }

    /**
     * Builds a predicate based on a passed constant <code>boolean</code> value. The target element of type
     * <code>double</code> that is passed to the predicate is ignored, and the constant value is simply returned. This
     * comes in handy when combining one predicate with another using <code>Predicate.and(...)</code> or
     * <code>Predicate.or(...)</code>. Consider the following example:
     * <pre>
     *     private double[] getHighestPriceOfStockBelowLimit(Collection&lt;Stock&gt; stocks, Client client) {
     *         boolean hasLimit = client.getLimit() &gt; 0.0D;
     *         return stocks.stream()
     *             .mapToDouble(Stock::getPrice)
     *             .filter(DblPredicateUtils.dblNot(DblPredicateUtils.dblConstant(hasLimit))
     *                 .or(DblPredicateUtils.dblPredicate(this::isPriceBelowLimit, client))
     *             .max()
     *             .orElse(-1.0D);
     *     }
     *
     *     private boolean isPriceBelowLimit(double price, Client client) {
     *         ...
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     return stocks.stream()
     *         .mapToDouble(Stock::getPrice)
     *         .filter(dblNot(dblConstant(hasLimit))
     *             .or(dblPredicate(this::isPriceBelowLimit, client))
     *         .max()
     *         .orElse(-1.0D);
     * </pre>
     *
     * @param b A constant boolean value that will be the result of every invocation of the returned DoublePredicate.
     * @return A DoublePredicate returning a constant boolean value.
     */
    public static DoublePredicate dblConstant(boolean b) {
        return d -> b;
    }

    /**
     * A <code>DoublePredicate</code> that simply negates the passed <code>DoublePredicate</code>. A predicate can
     * always be negated via <code>predicate.negate()</code>, however using this method may improve readability.
     *
     * @param predicate A DoublePredicate whose result is to be negated.
     * @return A DoublePredicate to be negated.
     */
    public static DoublePredicate dblNot(DoublePredicate predicate) {
        return predicate.negate();
    }

    /**
     * This method builds a <code>DoublePredicate</code> whose parameter is to be compared for equality to the passed
     * <code>double</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the DoublePredicate built by this method.
     * @return A DoublePredicate that compares its parameter to a constant double value for equality.
     */
    public static DoublePredicate isDblEqual(double value) {
        return d -> d == value;
    }

    /**
     * This method builds a <code>DoublePredicate</code> whose parameter is to be compared for equality to the passed
     * <code>double</code> constant value. If the absolute value of the difference between the two numbers is less than
     * or equal to a passed delta, the values will be considered equal.
     *
     * @param value A constant value to be compared to the parameter of the DoublePredicate built by this method.
     * @param delta A delta value meaning that if the absolute value of the difference between the two numbers to be
     *              compared is less than or equal to the delta, the values will be considered equal.
     * @return A DoublePredicate that compares its parameter to a constant double value for equality within a delta
     * value.
     */
    public static DoublePredicate isDblEqual(double value, double delta) {
        return d -> DoubleWithDelta.of(value, delta).isEqualWithinDelta(d);
    }

    /**
     * Given a <code>DoubleUnaryOperator</code> this method builds a <code>DoublePredicate</code> that determines if the
     * value returned by that operator is equal to the passed constant double value. For example:
     * <pre>
     *     double[] doubles = IntStream.range(1, 10).asDoubleStream().toArray();
     *     double[] evens = Arrays.stream(doubles)
     *             .filter(DblPredicateUtils.isDblEqual(DblMapperUtils.dblModulo(2.0D), 0.0D))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     double[] evens = Arrays.stream(doubles)
     *             .filter(isDblEqual(dblModulo(2.0D), 0.0D))
     *             .toArray();
     * </pre>
     *
     * @param operator A DoubleUnaryOperator whose return value is to be compared with a passed constant double value
     *                 for equality.
     * @param value    A value to be compared to the result of the passed operator for equality.
     * @return A DoublePredicate whose return value is compared for equality to a passed constant double value.
     */
    public static DoublePredicate isDblEqual(DoubleUnaryOperator operator, double value) {
        return d -> operator.applyAsDouble(d) == value;
    }

    /**
     * Given a <code>DoubleUnaryOperator</code> this method builds a <code>DoublePredicate</code> that determines if a
     * value returned by that operator is equal to a constant double value, within a delta parameter.
     * <pre>
     *     double[] doubles = IntStream.range(1, 10).asDoubleStream().toArray();
     *     double[] evens = Arrays.stream(doubles)
     *             .filter(DblPredicateUtils.isDblEqual(DblMapperUtils.dblModulo(2.0D), DblPredicateUtils.dblWithDelta(0.0D, 0.000001D)))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     double[] evens = Arrays.stream(doubles)
     *             .filter(isDblEqual(dblModulo(2.0D), dblWithDelta(0.0D, 0.000001D)))
     *             .toArray();
     * </pre>
     *
     * @param operator        A DoubleUnaryOperator whose return value is to be compared with a passed constant double
     *                        value for equality.
     * @param doubleWithDelta An object containing a constant double value for comparison, and a double delta.
     * @return A DoublePredicate whose parameter is to be applied to the passed operator, and the result is compared for
     * equality to a constant double value. If the difference is less than or equal to a delta value, then the values
     * are considered equal.
     */
    public static DoublePredicate isDblEqual(DoubleUnaryOperator operator, DoubleWithDelta doubleWithDelta) {
        return d -> doubleWithDelta.isEqualWithinDelta(operator.applyAsDouble(d));
    }

    /**
     * This method builds a <code>DoublePredicate</code> whose parameter is to be compared for inequality to the passed
     * <code>double</code> constant value.
     *
     * @param value A constant value to be compared to the parameter of the DoublePredicate built by this method.
     * @return A DoublePredicate that compares its parameter to a constant double value for inequality.
     */
    public static DoublePredicate isDblNotEqual(double value) {
        return dblNot(isDblEqual(value));
    }

    /**
     * This method builds a <code>DoublePredicate</code> whose parameter is to be compared for inequality to the passed
     * <code>double</code> constant value. If the absolute value of the difference between the two numbers is greater
     * than a passed delta, the values will be considered <i>not</i> equal.
     *
     * @param value A constant value to be compared to the parameter of the DoublePredicate built by this method.
     * @param delta A delta value meaning that if the absolute value of the difference between the two numbers to be
     *              compared is greater than the delta, the values will be considered <i>not</i> equal.
     * @return A DoublePredicate that compares its parameter to a constant double value for inequality outside of a
     * delta value.
     */
    public static DoublePredicate isDblNotEqual(double value, double delta) {
        return d -> !DoubleWithDelta.of(value, delta).isEqualWithinDelta(d);
    }

    /**
     * Given a <code>DoubleUnaryOperator</code> this method builds a <code>DoublePredicate</code> that determines if the
     * value returned by that operator is <i>not</i> equal to the passed constant double value. For example:
     * <pre>
     *     double[] doubles = IntStream.range(1, 10).asDoubleStream().toArray();
     *     double[] odds = Arrays.stream(doubles)
     *             .filter(DblPredicateUtils.isDblNotEqual(DblMapperUtils.dblModulo(2.0D), 0.0D))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     double[] odds = Arrays.stream(doubles)
     *             .filter(isDblNotEqual(dblModulo(2.0D), 0.0D))
     *             .toArray();
     * </pre>
     *
     * @param operator A DoubleUnaryOperator whose return value is to be compared with a passed constant double value
     *                 for inequality.
     * @param value    A value to be compared to the result of the passed operator for inequality.
     * @return A DoublePredicate whose return value is compared for inequality to a passed constant double value.
     */
    public static DoublePredicate isDblNotEqual(DoubleUnaryOperator operator, double value) {
        return dblNot(isDblEqual(operator, value));
    }

    /**
     * Given a <code>DoubleUnaryOperator</code> this method builds a <code>DoublePredicate</code> that determines if a
     * value returned by that operator is equal to a constant double value, within a delta parameter.
     * <pre>
     *     double[] doubles = IntStream.range(1, 10).asDoubleStream().toArray();
     *     double[] odds = Arrays.stream(doubles)
     *             .filter(DblPredicateUtils.isDblNotEqual(DblMapperUtils.dblModulo(2.0D), DblPredicateUtils.dblWithDelta(0.0D, 0.000001D)))
     *             .toArray();
     * </pre>
     * Or, with static imports:
     * <pre>
     *     double[] odds = Arrays.stream(doubles)
     *             .filter(isDblNotEqual(dblModulo(2.0D), dblWithDelta(0.0D, 0.000001D)))
     *             .toArray();
     * </pre>
     *
     * @param operator        A DoubleUnaryOperator whose return value is to be compared with a constant double value
     *                        for inequality, within a delta.
     * @param doubleWithDelta An object containing a constant double value for comparison, and a double delta.
     * @return A DoublePredicate whose parameter is to be applied to the passed operator, and the result is compared for
     * equality to a constant double value. If the difference is less than or equal to a delta value, then the values
     * are considered equal.
     */
    public static DoublePredicate isDblNotEqual(DoubleUnaryOperator operator, DoubleWithDelta doubleWithDelta) {
        return d -> !doubleWithDelta.isEqualWithinDelta(operator.applyAsDouble(d));
    }

    /**
     * Given a <code>Collection</code> whose elements are of type &lt;R&gt;, and a <code>DoubleFunction</code> that
     * returns a value of type &lt;R&gt;, this method builds a <code>DoublePredicate</code> that determines if the given
     * collection contains the value returned by the double function. More formally, the <code>DoublePredicate</code>
     * built by this method returns <code>true</code> if and only if the passed collection contains at least one element
     * e such that <code>(o == null ? e == null : o.equals(e))</code>, o being the value returned from the passed
     * double function.
     *
     * @param collection A Collection of elements of type &lt;R&gt;, to be checked for whether it contains a value
     *                   returned from a passed DoubleFunction.
     * @param function   A DoubleFunction returning a value of type &lt;R&gt; to be checked for whether it is contained
     *                   in a passed Collection.
     * @param <R>        The type of elements in the passed Collection. Also, the type of the value returned by the
     *                   passed DoubleFunction.
     * @return A DoublePredicate that applies the given DoubleFunction to its parameter, resulting in a value of type
     * &lt;R&gt;. The DoublePredicate checks that the returned value is contained in a passed Collection.
     */
    public static <R> DoublePredicate dblToObjContains(Collection<? extends R> collection, DoubleFunction<? extends R> function) {
        return d -> collection != null && collection.contains(function.apply(d));
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection&lt;R&gt;</code>, this method builds a
     * <code>DoublePredicate</code> that determines if the collection returned by that double function contains the
     * passed <code>value</code> of type &lt;R&gt;. More formally, the <code>DoublePredicate</code> built by this method
     * returns <code>true</code> if and only if the returned collection contains at least one element e such that
     * <code>(o == null ? e == null : o.equals(e))</code>, o being the passed constant value of type &lt;R&gt;.
     * <p>
     * This method is similar to {@link #dblToObjContains(Collection, DoubleFunction)}, but instead of a built
     * predicate checking whether a passed collection contains a value returned by a function, in this method it checks
     * whether a collection returned by a double function contains a passed value.
     *
     * @param function A DoubleFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param value    A value of type &lt;R&gt; to be checked for whether a Collection returned by the above Function
     *                 contains it.
     * @param <R>      The type of elements for collections returned by a passed DoubleFunction. Also, the type of the
     *                 passed value.
     * @return A DoublePredicate that applies the given double function to its parameter resulting in a
     * Collection&lt;R&gt;. The DoublePredicate checks that the returned Collection contains a passed constant value of
     * type &lt;R&gt;.
     */
    public static <R> DoublePredicate inverseDblToObjContains(DoubleFunction<? extends Collection<R>> function, R value) {
        return d -> {
            Collection<R> collection = function.apply(d);
            return collection != null && collection.contains(value);
        };
    }

    /**
     * Given a <code>double</code> array, and a <code>ToDoubleFunction</code> that takes an element of type &lt;T&gt;,
     * this method builds a <code>Predicate</code> that determines if the given array contains the value returned by the
     * <code>ToDoubleFunction</code>.
     *
     * @param doubles  An array of doubles, to be checked for whether it contains a value returned from a passed
     *                 ToDoubleFunction.
     * @param function A ToDoubleFunction taking a value of type &lt;T&gt;, whose return value is to be checked for
     *                 whether it is contained in a passed array.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given ToDoubleFunction to its parameter of type &lt;T&gt;. The Predicate
     * checks that the returned value is contained in a passed array of doubles.
     */
    public static <T> Predicate<T> objToDblContains(double[] doubles, ToDoubleFunction<? super T> function) {
        return t -> doubles != null && DblStreamUtils.dblAnyMatch(doubles, isDblEqual(function.applyAsDouble(t)));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of doubles, this
     * method builds a <code>Predicate</code> that determines if the array returned by that function contains the passed
     * constant double value.
     * <p>
     * This method is similar to {@link #objToDblContains(double[], ToDoubleFunction)}, but instead of a built
     * predicate checking whether a passed array contains a value returned by a function, in this method it checks
     * whether an array returned by a function contains a passed double value.
     *
     * @param function A Function that returns an array of doubles.
     * @param value    A constant double value to be checked for whether an array of doubles returned by the above
     *                 Function contains it.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies the given function to its parameter resulting in a double array. The Predicate
     * checks that the returned array contains a passed constant double value.
     */
    public static <T> Predicate<T> inverseObjToDblContains(Function<T, double[]> function, double value) {
        return t -> t != null && DblStreamUtils.dblAnyMatch(function.apply(t), isDblEqual(value));
    }

    /**
     * Given a DoubleFunction that returns a value of type &lt;R&gt;, this method builds a <code>DoublePredicate</code>
     * that determines whether that returned value is <code>null</code>.
     *
     * @param function A DoubleFunction that returns a value of an arbitrary type.
     * @return A DoublePredicate that applies a DoubleFunction to its parameter, which returns a value of an arbitrary
     * type. The predicate determines whether that value is null.
     */
    public static DoublePredicate dblIsNull(DoubleFunction<?> function) {
        return d -> Objects.isNull(function.apply(d));
    }

    /**
     * Given a DoubleFunction that returns a value of type &lt;R&gt;, this method builds a <code>DoublePredicate</code>
     * that determines whether that returned value is <i>not</i> <code>null</code>.
     *
     * @param function A DoubleFunction that returns a value of an arbitrary type.
     * @return A DoublePredicate that applies a DoubleFunction to its parameter, which returns a value of an arbitrary
     * type. The predicate determines whether that value is not null.
     */
    public static DoublePredicate dblIsNotNull(DoubleFunction<?> function) {
        return dblNot(dblIsNull(function));
    }

    /**
     * Builds a <code>DoublePredicate</code> that determines whether a value is greater than a passed constant double
     * value.
     *
     * @param compareTo A constant double value to be compared to the target value of a DoublePredicate built by this
     *                  method.
     * @return A DoublePredicate that compares its target value to a passed constant double value and determines
     * whether it is greater than that value.
     */
    public static DoublePredicate dblGt(double compareTo) {
        return d -> d > compareTo;
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type
     * &lt;R&gt;, this method builds a <code>DoublePredicate</code> that applies that function to its target value, and
     * determines whether the returned <code>Comparable</code> value is greater than a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A DoubleFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a DoubleFunction.
     * @param <R>       The return type of the passed DoubleFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A DoublePredicate that applies a DoubleFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is greater.
     */
    public static <R extends Comparable<R>> DoublePredicate dblGt(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) > 0;
    }

    /**
     * Given a <code>ToDoubleFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * a passed constant double value.
     *
     * @param function  A ToDoubleFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant double value to see whether it is
     *                  greater.
     * @param compareTo A constant double value to be compared with a value returned by a passed ToDoubleFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToDoubleFunction to its target element, and compares its double return value
     * to a passed constant value to determine whether the return value is greater.
     */
    public static <T> Predicate<T> toDblGt(ToDoubleFunction<? super T> function, double compareTo) {
        return t -> t != null && function.applyAsDouble(t) > compareTo;
    }

    /**
     * Builds a <code>DoublePredicate</code> that determines whether a value is greater than or equal to a passed
     * constant double value.
     *
     * @param compareTo A constant double value to be compared to the target value of a DoublePredicate built by this
     *                  method.
     * @return A DoublePredicate that compares its target value to a passed constant double value and determines
     * whether it is greater than or equal to that value.
     */
    public static DoublePredicate dblGte(double compareTo) {
        return d -> d >= compareTo;
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type
     * &lt;R&gt;, this method builds a <code>DoublePredicate</code> that applies that function to its target value, and
     * determines whether the returned <code>Comparable</code> value is greater than or equal to a passed constant value
     * of type &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A DoubleFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a DoubleFunction.
     * @param <R>       The return type of the passed DoubleFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A DoublePredicate that applies a DoubleFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is greater or equal to it.
     */
    public static <R extends Comparable<R>> DoublePredicate dblGte(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    /**
     * Given a <code>ToDoubleFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is greater than
     * or equal to a passed constant double value.
     *
     * @param function  A ToDoubleFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant double value to see whether it is
     *                  greater than or equal to it.
     * @param compareTo A constant double value to be compared with a value returned by a passed ToDoubleFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToDoubleFunction to its target element, and compares its double return value
     * to a passed constant value to determine whether the return value is greater than or equal to it.
     */
    public static <T> Predicate<T> toDblGte(ToDoubleFunction<? super T> function, double compareTo) {
        return t -> t != null && function.applyAsDouble(t) >= compareTo;
    }

    /**
     * Builds a <code>DoublePredicate</code> that determines whether a value is less than a passed constant double
     * value.
     *
     * @param compareTo A constant double value to be compared to the target value of a DoublePredicate built by this
     *                  method.
     * @return A DoublePredicate that compares its target value to a passed constant double value and determines
     * whether it is less than that value.
     */
    public static DoublePredicate dblLt(double compareTo) {
        return d -> d < compareTo;
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type
     * &lt;R&gt;, this method builds a <code>DoublePredicate</code> that applies that function to its target value, and
     * determines whether the returned <code>Comparable</code> value is less than a passed constant value of type
     * &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A DoubleFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a DoubleFunction.
     * @param <R>       The return type of the passed DoubleFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A DoublePredicate that applies a DoubleFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is less than it.
     */
    public static <R extends Comparable<R>> DoublePredicate dblLt(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) < 0;
    }

    /**
     * Given a <code>ToDoubleFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than a
     * passed constant double value.
     *
     * @param function  A ToDoubleFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant double value to see whether it is
     *                  less than it.
     * @param compareTo A constant double value to be compared with a value returned by a passed ToDoubleFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToDoubleFunction to its target element, and compares its double return value
     * to a passed constant value to determine whether the return value is less than it.
     */
    public static <T> Predicate<T> toDblLt(ToDoubleFunction<? super T> function, double compareTo) {
        return t -> t != null && function.applyAsDouble(t) < compareTo;
    }

    /**
     * Builds a <code>DoublePredicate</code> that determines whether a value is less than or equal to a passed constant
     * double value.
     *
     * @param compareTo A constant double value to be compared to the target value of a DoublePredicate built by this
     *                  method.
     * @return A DoublePredicate that compares its target value to a passed constant double value and determines
     * whether it is less than or equal to that value.
     */
    public static DoublePredicate dblLte(double compareTo) {
        return d -> d <= compareTo;
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;R&gt;, and a constant value of type
     * &lt;R&gt;, this method builds a <code>DoublePredicate</code> that applies that function to its target value, and
     * determines whether the returned <code>Comparable</code> value is less than or equal to a passed constant value of
     * type &lt;R&gt; (also a <code>Comparable</code>).
     *
     * @param function  A DoubleFunction whose return value of type &lt;R&gt; is to be compared to the passed Comparable
     *                  value of type &lt;R&gt;.
     * @param compareTo A constant value of type &lt;R&gt; to be compared to the return value of a DoubleFunction.
     * @param <R>       The return type of the passed DoubleFunction parameter. Also, the type of the passed constant
     *                  value.
     * @return A DoublePredicate that applies a DoubleFunction to its target value, and compares its Comparable return
     * value to a passed constant value to determine whether the return value is less or equal to it.
     */
    public static <R extends Comparable<R>> DoublePredicate dblLte(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    /**
     * Given a <code>ToDoubleFunction</code> that takes an element of type &lt;T&gt;, this method builds a
     * <code>Predicate</code> that compares the return value of that function, and determines whether it is less than or
     * equal to a passed constant double value.
     *
     * @param function  A ToDoubleFunction that takes an element of type &lt;T&gt;, whose return value is to be compared
     *                  by the Predicate built by this method, with a passed constant double value to see whether it is
     *                  less than or equal to it.
     * @param compareTo A constant double value to be compared with a value returned by a passed ToDoubleFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies a ToDoubleFunction to its target element, and compares its double return value
     * to a passed constant value to determine whether the return value is less than or equal to it.
     */
    public static <T> Predicate<T> toDblLte(ToDoubleFunction<? super T> function, double compareTo) {
        return t -> t != null && function.applyAsDouble(t) <= compareTo;
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds a <code>DoublePredicate</code> that determines whether the returned <code>Collection</code> is
     * empty.
     *
     * @param function A DoubleFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return A DoublePredicate that applies its parameter to a DoubleFunction that returns a Collection of elements of
     * an arbitrary type. The DoublePredicate determines whether the returned Collection is empty.
     */
    public static DoublePredicate isDblCollEmpty(DoubleFunction<? extends Collection<?>> function) {
        return d -> CollectionUtils.isEmpty(function.apply(d));
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection</code> of elements of an arbitrary type, this
     * method builds a <code>DoublePredicate</code> that determines whether the returned <code>Collection</code> is
     * <i>not</i> empty.
     *
     * @param function A DoubleFunction that returns a <code>Collection</code> of elements of an arbitrary type.
     * @return A DoublePredicate that applies its parameter to a DoubleFunction that returns a Collection of elements of
     * an arbitrary type. The DoublePredicate determines whether the returned Collection is <i>not</i> empty.
     */
    public static DoublePredicate isDblCollNotEmpty(DoubleFunction<? extends Collection<?>> function) {
        return dblNot(isDblCollEmpty(function));
    }

    /**
     * Given a <code>Function</code> that returns an array of doubles, this method builds a <code>Predicate</code> that
     * determines whether the returned array is empty.
     *
     * @param function A Function that returns an array of doubles.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of doubles. The Predicate
     * determines whether the returned array is empty.
     */
    public static <T> Predicate<T> isDblArrayEmpty(Function<? super T, double[]> function) {
        return t -> {
            double[] doubles = t == null ? null : function.apply(t);
            return doubles == null || doubles.length == 0;
        };
    }

    /**
     * Given a <code>Function</code> that returns an array of doubles, this method builds a <code>Predicate</code> that
     * determines whether the returned array is <i>not</i> empty.
     *
     * @param function A Function that returns an array of doubles.
     * @param <T>      The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its parameter to a Function that returns an array of doubles. The Predicate
     * determines whether the returned array is <i>not</i> empty.
     */
    public static <T> Predicate<T> isDblArrayNotEmpty(Function<? super T, double[]> function) {
        return not(isDblArrayEmpty(function));
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of doubles, and a
     * <code>DoublePredicate</code>, this method builds a <code>Predicate</code> that determines whether all doubles
     * in the returned array match the <code>DoublePredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of doubles.
     * @param predicate A DoublePredicate that will be applied to all elements of an array of doubles returned by the
     *                  passed Function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * doubles. The Predicate determines whether all elements in that array match a passed DoublePredicate.
     */
    public static <T> Predicate<T> objToDblsAllMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblAllMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>DoublePredicate</code> that determines whether all
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A DoubleFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  DoubleFunction.
     * @param <R>       The type of the elements of a Collection returned by a passed DoubleFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A DoublePredicate that applies its parameter to a DoubleFunction that returns a Collection of elements of
     * type &lt;R&gt;. The DoublePredicate determines whether all elements in that Collection match a passed Predicate.
     */
    public static <R> DoublePredicate dblToObjsAllMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.allMatch(function.apply(d), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of doubles, and a
     * <code>DoublePredicate</code>, this method builds a <code>Predicate</code> that determines whether any of the
     * doubles in the returned array match the <code>DoublePredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of doubles.
     * @param predicate A DoublePredicate that will be applied to all elements of an array of doubles returned by the
     *                  passed Function to determine whether any match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * doubles. The Predicate determines whether any of the elements in that array match a passed DoublePredicate.
     */
    public static <T> Predicate<T> objToDblsAnyMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblAnyMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>DoublePredicate</code> that determines whether any of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A DoubleFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  DoubleFunction to determine whether any match.
     * @param <R>       The type of the elements of a Collection returned by a passed DoubleFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A DoublePredicate that applies its parameter to a DoubleFunction that returns a Collection of elements of
     * type &lt;R&gt;. The DoublePredicate determines whether any of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> DoublePredicate dblToObjsAnyMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.anyMatch(function.apply(d), predicate);
    }

    /**
     * Given a <code>Function</code> that takes an element of type &lt;T&gt; and returns an array of doubles, and a
     * <code>DoublePredicate</code>, this method builds a <code>Predicate</code> that determines whether none of the
     * doubles in the returned array match the <code>DoublePredicate</code>.
     *
     * @param function  A Function that takes en element of type &lt;T&gt; and returns an array of doubles.
     * @param predicate A DoublePredicate that will be applied to all elements of an array of doubles returned by the
     *                  passed Function to determine whether none match.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that applies its target element of type &lt;T&gt; to a Function that returns an array of
     * doubles. The Predicate determines whether none of the elements in that array match a passed DoublePredicate.
     */
    public static <T> Predicate<T> objToDblsNoneMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblNoneMatch(function.apply(t), predicate);
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a <code>Collection</code> of type &lt;R&gt;, and a
     * <code>Predicate</code>, this method builds a <code>DoublePredicate</code> that determines whether none of the
     * elements in the returned <code>Collection</code> match the <code>Predicate</code>.
     *
     * @param function  A DoubleFunction that returns a Collection of elements of type &lt;R&gt;.
     * @param predicate A Predicate that will be applied to all elements of a Collection returned by the passed
     *                  DoubleFunction to determine whether none match.
     * @param <R>       The type of the elements of a Collection returned by a passed DoubleFunction. Also the type of
     *                  elements taken by a passed Predicate that will be applied to all elements of that Collection.
     * @return A DoublePredicate that applies its parameter to a DoubleFunction that returns a Collection of elements of
     * type &lt;R&gt;. The DoublePredicate determines whether none of the elements in that Collection match a passed
     * Predicate.
     */
    public static <R> DoublePredicate dblToObjsNoneMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.noneMatch(function.apply(d), predicate);
    }

    /**
     * Given a <code>ToDoubleFunction</code> taking a value of type &lt;T&gt;, and a <code>DoublePredicate</code>, this
     * method builds a <code>Predicate</code> that takes an element of type &lt;T&gt;, and applies the return value of
     * the <code>ToDoubleFunction</code> to the given predicate. It is a way of adapting a <code>DoublePredicate</code>
     * to a <code>Stream</code> of a different type. For example, the
     * {@link DblStreamUtils#indexOfFirstDbl(double[], DoublePredicate)} method uses this predicate in its
     * implementation (Note that the <code>dblPairWithIndex()</code> below refers to
     * {@link DblMapperUtils#dblPairWithIndex()}):
     * <pre>
     * public static int indexOfFirstDbl(double[] doubles, DoublePredicate doublePredicate) {
     *     return defaultDblStream(doubles)
     *         .mapToObj(dblPairWithIndex())
     *         .filter(mapToDblAndFilter(DoubleIndexPair::getDoubleValue, doublePredicate))
     *         .mapToInt(DoubleIndexPair::getIndex)
     *         .findFirst()
     *         .orElse(-1);
     * }
     * </pre>
     * The map-and-filter predicate is necessary in this case because we have a predicate that operates on the original
     * <code>double</code> type of the stream, but a mapping operation has changed the type to a
     * <code>DoubleIndexPair</code>. The <code>DoubleIndexPair::getDoubleValue</code> method reference, passed as the
     * <code>ToDoubleFunction</code> argument to this method, retrieves the original double value before the
     * <code>DoublePredicate</code> evaluates it.
     * <p>
     * As a side note, the pairing of an object with another can be very useful in streaming operations. In this case,
     * we need to have both the double value and its index available at the same point in the stream, so we temporarily
     * pair the two together, before mapping to just the index.
     *
     * @param function  A ToDoubleFunction to transform an element of type &lt;T&gt; to a double before it is passed to
     *                  a DoublePredicate.
     * @param predicate A DoublePredicate whose value will be retrieved from a given transformer function.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A Predicate that takes an element of type &lt;T&gt;, and applies the return value of a passed
     * ToDoubleFunction to a passed DoublePredicate.
     */
    public static <T> Predicate<T> mapToDblAndFilter(ToDoubleFunction<? super T> function, DoublePredicate predicate) {
        return t -> predicate.test(function.applyAsDouble(t));
    }

    /**
     * Given a <code>DoubleFunction</code> that returns a value of type &lt;T&gt;, and a <code>Predicate</code>, this
     * method builds a <code>DoublePredicate</code> that applies the return value of the <code>DoubleFunction</code> to
     * the given predicate. It is a way of adapting a double value to a <code>Predicate</code>. This method is the
     * inverse of {@link #mapToDblAndFilter(ToDoubleFunction, DoublePredicate)}. In that method, we map from a value of
     * type &lt;T&gt; to a <code>double</code>, and then apply a <code>DoublePredicate</code>. In this method we map
     * from a <code>double</code> to a value of type &lt;T&gt;, and then apply a predicate.
     *
     * @param function  A DoubleFunction to transform a double value to an element of type &lt;T&gt; before it is passed
     *                  to a Predicate.
     * @param predicate A Predicate whose value will be retrieved from a given DoubleFunction.
     * @param <T>       The type of the element taken by the Predicate built by this method.
     * @return A DoublePredicate that applies the return value of a passed ToDoubleFunction to a passed Predicate.
     */
    public static <T> DoublePredicate dblMapAndFilter(DoubleFunction<? extends T> function, Predicate<? super T> predicate) {
        return d -> predicate.test(function.apply(d));
    }

    /**
     * Combines a <code>double</code> value for use in a comparison, with a delta. When an equality comparison is made,
     * if the difference in values is smaller than the delta, then the values are considered equal. This method is used
     * to build the second parameter to the {@link #isDblEqual(DoubleUnaryOperator, DoubleWithDelta)} and
     * {@link #isDblNotEqual(DoubleUnaryOperator, DoubleWithDelta)} methods.
     *
     * @param value A double value to be compared with another value.
     * @param delta A delta value to be used - if the difference of two values is within it, they are still considered
     *              <i>equal</i>.
     * @return An object encapsulating a double value for comparison, and a delta value.
     */
    public static DoubleWithDelta dblWithDelta(double value, double delta) {
        return DoubleWithDelta.of(value, delta);
    }
}
