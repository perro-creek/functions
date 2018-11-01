package org.hringsak.functions.predicate;

import org.hringsak.functions.stream.IntStreamUtils;
import org.hringsak.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.hringsak.functions.predicate.CharSequenceUtils.isCharacterMatch;

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
     * method are to be filtered based on whether their price is less than a limit for the passed <code>Client</code>:
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

    public static <R> IntPredicate inverseIntToObjContains(IntFunction<? extends Collection<R>> collectionExtractor, R value) {
        return i -> {
            Collection<R> collection = collectionExtractor.apply(i);
            return collection != null && collection.contains(value);
        };
    }

    public static <T> Predicate<T> objToIntsContains(Function<T, int[]> collectionExtractor, int value) {
        return t -> {
            int[] ints = collectionExtractor.apply(t);
            return IntStreamUtils.intAnyMatch(ints, isIntEqual(value));
        };
    }

    public static <T> Predicate<T> inverseObjToIntContains(int[] ints, ToIntFunction<? super T> function) {
        return t -> ints != null && IntStreamUtils.intAnyMatch(ints, isIntEqual(function.applyAsInt(t)));
    }

    public static <R> IntPredicate intContainsKey(Map<R, ?> map, IntFunction<? extends R> extractor) {
        return i -> map != null && map.containsKey(extractor.apply(i));
    }

    public static <R> IntPredicate intContainsValue(Map<?, R> map, IntFunction<? extends R> extractor) {
        return i -> map != null && map.containsValue(extractor.apply(i));
    }

    public static IntPredicate intContainsChar(IntFunction<? extends CharSequence> extractor, int searchChar) {
        return i -> CharSequenceUtils.contains(extractor.apply(i), searchChar);
    }

    public static IntPredicate intContainsSequence(IntFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return i -> CharSequenceUtils.contains(extractor.apply(i), searchSeq);
    }

    public static IntPredicate intContainsIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return i -> CharSequenceUtils.containsIgnoreCase(extractor.apply(i), searchSeq);
    }

    public static IntPredicate intIsAlpha(IntFunction<? extends CharSequence> extractor) {
        return i -> CharSequenceUtils.isCharacterMatch(extractor.apply(i), Character::isLetter);
    }

    public static IntPredicate intIsAlphanumeric(IntFunction<? extends CharSequence> extractor) {
        return i -> CharSequenceUtils.isCharacterMatch(extractor.apply(i), Character::isLetterOrDigit);
    }

    public static IntPredicate intIsNumeric(IntFunction<? extends CharSequence> extractor) {
        return i -> CharSequenceUtils.isCharacterMatch(extractor.apply(i), Character::isDigit);
    }

    public static IntPredicate intStartsWith(IntFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return i -> CharSequenceUtils.startsWith(extractor.apply(i), prefix);
    }

    public static IntPredicate intStartsWithIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return i -> CharSequenceUtils.startsWithIgnoreCase(extractor.apply(i), prefix);
    }

    public static IntPredicate intEndsWith(IntFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return i -> CharSequenceUtils.endsWith(extractor.apply(i), suffix);
    }

    public static IntPredicate intEndsWithIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return i -> CharSequenceUtils.endsWithIgnoreCase(extractor.apply(i), suffix);
    }

    public static IntPredicate intAnyCharsMatch(IntFunction<? extends CharSequence> function, IntPredicate charPredicate) {
        return i -> {
            CharSequence sequence = function.apply(i);
            return sequence != null && sequence.codePoints().anyMatch(charPredicate);
        };
    }

    public static IntPredicate intAllCharsMatch(IntFunction<? extends CharSequence> function, IntPredicate charPredicate) {
        return i -> isCharacterMatch(function.apply(i), charPredicate);
    }

    public static IntPredicate intNoCharsMatch(IntFunction<? extends CharSequence> function, IntPredicate charPredicate) {
        return i -> {
            CharSequence sequence = function.apply(i);
            return sequence != null && sequence.codePoints().noneMatch(charPredicate);
        };
    }

    public static <R> IntPredicate isIntNull(IntFunction<? extends R> function) {
        return i -> Objects.isNull(function.apply(i));
    }

    public static <R> IntPredicate isIntNotNull(IntFunction<? extends R> function) {
        return intNot(isIntNull(function));
    }

    public static IntPredicate intGt(int compareTo) {
        return i -> i > compareTo;
    }

    public static <R extends Comparable<R>> IntPredicate intGt(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) > 0;
    }

    public static IntPredicate intGte(int compareTo) {
        return i -> i >= compareTo;
    }

    public static <R extends Comparable<R>> IntPredicate intGte(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    public static IntPredicate intLt(int compareTo) {
        return i -> i < compareTo;
    }

    public static <R extends Comparable<R>> IntPredicate intLt(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) < 0;
    }

    public static IntPredicate intLte(int compareTo) {
        return i -> i <= compareTo;
    }

    public static <R extends Comparable<R>> IntPredicate intLte(IntFunction<? extends R> function, R compareTo) {
        return i -> Objects.compare(function.apply(i), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    public static <R> IntPredicate isIntCollEmpty(IntFunction<? extends Collection<R>> function) {
        return i -> CollectionUtils.isEmpty(function.apply(i));
    }

    public static <R> IntPredicate isIntCollNotEmpty(IntFunction<? extends Collection<R>> function) {
        return intNot(isIntCollEmpty(function));
    }

    public static <T> Predicate<T> objToIntsAllMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intAllMatch(function.apply(t), predicate);
    }

    public static <R> IntPredicate intToObjectsAllMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.allMatch(function.apply(i), predicate);
    }

    public static <T> Predicate<T> objToIntsAnyMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intAnyMatch(function.apply(t), predicate);
    }

    public static <R> IntPredicate intToObjectsAnyMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.anyMatch(function.apply(i), predicate);
    }

    public static <T> Predicate<T> objToIntsNoneMatch(Function<T, ? extends int[]> function, IntPredicate predicate) {
        return t -> t != null && IntStreamUtils.intNoneMatch(function.apply(t), predicate);
    }

    public static <R> IntPredicate intToObjectsNoneMatch(IntFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return i -> StreamUtils.noneMatch(function.apply(i), predicate);
    }

    public static IntPredicate intDistinctByKey(IntFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return i -> uniqueKeys.add(keyExtractor.apply(i));
    }

    public static IntPredicate intDistinctByKeyParallel(IntFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Collections.synchronizedSet(new HashSet<>());
        return i -> uniqueKeys.add(keyExtractor.apply(i));
    }

    public static <T> Predicate<T> mapToIntAndFilter(ToIntFunction<? super T> transformer, IntPredicate predicate) {
        return i -> predicate.test(transformer.applyAsInt(i));
    }

    public static <U> IntPredicate intMapAndFilter(IntFunction<? extends U> transformer, Predicate<? super U> predicate) {
        return i -> predicate.test(transformer.apply(i));
    }
}
