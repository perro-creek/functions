package org.hringsak.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

/**
 * Methods that build predicates specifically those involving primitive <code>int</code> types.
 */
public final class DoublePredicateUtils {

    private DoublePredicateUtils() {
    }

    public static DoublePredicate doublePredicate(DoublePredicate predicate) {
        return predicate;
    }

    public static <U> DoublePredicate doublePredicate(BiPredicate<Double, ? super U> biPredicate, U value) {
        return i -> biPredicate.test(i, value);
    }

    public static <U> DoublePredicate inverseDoublePredicate(BiPredicate<? super U, Double> biPredicate, U value) {
        return i -> biPredicate.test(value, i);
    }

    public static DoublePredicate doublePredicateConstant(boolean b) {
        return i -> b;
    }

    public static DoublePredicate not(DoublePredicate predicate) {
        return predicate.negate();
    }

    public static <R> DoublePredicate isEqual(R value, DoubleFunction<? extends R> extractor) {
        return i -> Objects.equals(value, extractor.apply(i));
    }

    public static <R> DoublePredicate isNotEqual(R value, DoubleFunction<? extends R> function) {
        return not(isEqual(value, function));
    }

    public static <R> DoublePredicate doubleContains(Collection<? extends R> collection, DoubleFunction<? extends R> function) {
        return i -> collection != null && collection.contains(function.apply(i));
    }

    public static <R> DoublePredicate inverseDoubleContains(DoubleFunction<? extends Collection<R>> collectionExtractor, R value) {
        return i -> {
            Collection<R> collection = collectionExtractor.apply(i);
            return collection != null && collection.contains(value);
        };
    }

    public static <R> DoublePredicate doubleContainsKey(Map<R, ?> map, DoubleFunction<? extends R> function) {
        return i -> map != null && map.containsKey(function.apply(i));
    }

    public static <R> DoublePredicate doubleContainsValue(Map<?, R> map, DoubleFunction<? extends R> function) {
        return i -> map != null && map.containsValue(function.apply(i));
    }

    public static DoublePredicate doubleContainsChar(DoubleFunction<? extends CharSequence> function, int searchChar) {
        return i -> StringUtils.contains(function.apply(i), searchChar);
    }

    public static DoublePredicate doubleContainsSequence(DoubleFunction<? extends CharSequence> function, CharSequence searchSeq) {
        return i -> StringUtils.contains(function.apply(i), searchSeq);
    }

    public static DoublePredicate doubleContainsIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence searchSeq) {
        return i -> StringUtils.containsIgnoreCase(function.apply(i), searchSeq);
    }

    public static DoublePredicate doubleIsAlpha(DoubleFunction<? extends CharSequence> function) {
        return i -> StringUtils.isAlpha(function.apply(i));
    }

    public static DoublePredicate doubleIsAlphanumeric(DoubleFunction<? extends CharSequence> function) {
        return i -> StringUtils.isAlphanumeric(function.apply(i));
    }

    public static DoublePredicate doubleIsNumeric(DoubleFunction<? extends CharSequence> function) {
        return i -> StringUtils.isNumeric(function.apply(i));
    }

    public static DoublePredicate doubleStartsWith(DoubleFunction<? extends CharSequence> function, CharSequence prefix) {
        return i -> StringUtils.startsWith(function.apply(i), prefix);
    }

    public static DoublePredicate doubleStartsWithIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence prefix) {
        return i -> StringUtils.startsWithIgnoreCase(function.apply(i), prefix);
    }

    public static DoublePredicate doubleEndsWith(DoubleFunction<? extends CharSequence> function, CharSequence suffix) {
        return i -> StringUtils.endsWith(function.apply(i), suffix);
    }

    public static DoublePredicate doubleEndsWithIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence suffix) {
        return i -> StringUtils.endsWithIgnoreCase(function.apply(i), suffix);
    }

    public static <R> DoublePredicate isNull(DoubleFunction<? extends R> function) {
        return i -> Objects.isNull(function.apply(i));
    }

    public static <R> DoublePredicate notNull(DoubleFunction<? extends R> function) {
        return not(isNull(function));
    }

    public static <R extends Comparable<R>> DoublePredicate gt(R compareTo, DoubleFunction<? extends R> function) {
        return i -> Objects.compare(compareTo, function.apply(i), nullsLast(naturalOrder())) > 0;
    }

    public static <R extends Comparable<R>> DoublePredicate gte(R compareTo, DoubleFunction<? extends R> function) {
        return i -> Objects.compare(compareTo, function.apply(i), nullsLast(naturalOrder())) >= 0;
    }

    public static <R extends Comparable<R>> DoublePredicate lt(R compareTo, DoubleFunction<? extends R> function) {
        return i -> Objects.compare(compareTo, function.apply(i), nullsLast(naturalOrder())) < 0;
    }

    public static <R extends Comparable<R>> DoublePredicate lte(R compareTo, DoubleFunction<? extends R> function) {
        return i -> Objects.compare(compareTo, function.apply(i), nullsLast(naturalOrder())) <= 0;
    }

    public static <R> DoublePredicate isEmpty(DoubleFunction<? extends Collection<R>> function) {
        return i -> CollectionUtils.isEmpty(function.apply(i));
    }

    public static <R> DoublePredicate isNotEmpty(DoubleFunction<? extends Collection<R>> function) {
        return not(isEmpty(function));
    }
}
