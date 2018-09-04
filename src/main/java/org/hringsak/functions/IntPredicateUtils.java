package org.hringsak.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

/**
 * Methods that build predicates specifically those involving primitive <code>int</code> types.
 */
public final class IntPredicateUtils {

    private IntPredicateUtils() {
    }

    public static IntPredicate intPredicate(IntPredicate predicate) {
        return predicate;
    }

    public static <U> IntPredicate intPredicate(BiPredicate<Integer, ? super U> biPredicate, U value) {
        return i -> biPredicate.test(i, value);
    }

    public static <U> IntPredicate inverseIntPredicate(BiPredicate<? super U, Integer> biPredicate, U value) {
        return i -> biPredicate.test(value, i);
    }

    public static IntPredicate intPredicateConstant(boolean b) {
        return i -> b;
    }

    public static IntPredicate not(IntPredicate predicate) {
        return predicate.negate();
    }

    public static <R> IntPredicate isEqual(R value, IntFunction<? extends R> extractor) {
        return i -> Objects.equals(value, extractor.apply(i));
    }

    public static <R> IntPredicate isNotEqual(R value, IntFunction<? extends R> function) {
        return not(isEqual(value, function));
    }

    public static <R> IntPredicate intContains(Collection<? extends R> collection, IntFunction<? extends R> extractor) {
        return i -> collection != null && collection.contains(extractor.apply(i));
    }

    public static <R> IntPredicate intInverseContains(IntFunction<? extends Collection<R>> collectionExtractor, R value) {
        return i -> {
            Collection<R> collection = collectionExtractor.apply(i);
            return collection != null && collection.contains(value);
        };
    }

    public static <R> IntPredicate intContainsKey(Map<R, ?> map, IntFunction<? extends R> extractor) {
        return i -> map != null && map.containsKey(extractor.apply(i));
    }

    public static <R> IntPredicate intContainsValue(Map<?, R> map, IntFunction<? extends R> extractor) {
        return i -> map != null && map.containsValue(extractor.apply(i));
    }

    public static IntPredicate intContainsChar(IntFunction<? extends CharSequence> extractor, int searchChar) {
        return i -> StringUtils.contains(extractor.apply(i), searchChar);
    }

    public static IntPredicate intContainsSequence(IntFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return i -> StringUtils.contains(extractor.apply(i), searchSeq);
    }

    public static IntPredicate intContainsIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return i -> StringUtils.containsIgnoreCase(extractor.apply(i), searchSeq);
    }

    public static IntPredicate intIsAlpha(IntFunction<? extends CharSequence> extractor) {
        return i -> StringUtils.isAlpha(extractor.apply(i));
    }

    public static IntPredicate intIsAlphanumeric(IntFunction<? extends CharSequence> extractor) {
        return i -> StringUtils.isAlphanumeric(extractor.apply(i));
    }

    public static IntPredicate intIsNumeric(IntFunction<? extends CharSequence> extractor) {
        return i -> StringUtils.isNumeric(extractor.apply(i));
    }

    public static IntPredicate intStartsWith(IntFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return i -> StringUtils.startsWith(extractor.apply(i), prefix);
    }

    public static IntPredicate intStartsWithIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return i -> StringUtils.startsWithIgnoreCase(extractor.apply(i), prefix);
    }

    public static IntPredicate intEndsWith(IntFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return i -> StringUtils.endsWith(extractor.apply(i), suffix);
    }

    public static IntPredicate intEndsWithIgnoreCase(IntFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return i -> StringUtils.endsWithIgnoreCase(extractor.apply(i), suffix);
    }

    public static <R> IntPredicate isNull(IntFunction<? extends R> function) {
        return i -> Objects.isNull(function.apply(i));
    }

    public static <R> IntPredicate intNotNull(IntFunction<? extends R> function) {
        return not(isNull(function));
    }

    public static <R extends Comparable<R>> IntPredicate gt(R compareTo, IntFunction<? extends R> valueExtractor) {
        return i -> Objects.compare(compareTo, valueExtractor.apply(i), nullsLast(naturalOrder())) > 0;
    }

    public static <R extends Comparable<R>> IntPredicate gte(R compareTo, IntFunction<? extends R> valueExtractor) {
        return i -> Objects.compare(compareTo, valueExtractor.apply(i), nullsLast(naturalOrder())) >= 0;
    }

    public static <R extends Comparable<R>> IntPredicate lt(R compareTo, IntFunction<? extends R> valueExtractor) {
        return i -> Objects.compare(compareTo, valueExtractor.apply(i), nullsLast(naturalOrder())) < 0;
    }

    public static <R extends Comparable<R>> IntPredicate lte(R compareTo, IntFunction<? extends R> valueExtractor) {
        return i -> Objects.compare(compareTo, valueExtractor.apply(i), nullsLast(naturalOrder())) <= 0;
    }

    public static <R> IntPredicate intIsEmpty(IntFunction<? extends Collection<R>> function) {
        return i -> CollectionUtils.isEmpty(function.apply(i));
    }

    public static <R> IntPredicate intIsNotEmpty(IntFunction<? extends Collection<R>> function) {
        return not(intIsNotEmpty(function));
    }

    static <U> IntPredicate extractAndFilter(IntFunction<? extends U> transformer, Predicate<? super U> predicate) {
        return i -> predicate.test(transformer.apply(i));
    }
}
