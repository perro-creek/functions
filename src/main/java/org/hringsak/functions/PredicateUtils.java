package org.hringsak.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

public final class PredicateUtils {

    private PredicateUtils() {
    }

    public static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }

    public static <T, U> Predicate<T> predicate(BiPredicate<? super T, ? super U> biPredicate, U value) {
        return t -> biPredicate.test(t, value);
    }

    public static <T, U> Predicate<T> inversePredicate(BiPredicate<? super U, ? super T> biPredicate, U value) {
        return t -> biPredicate.test(value, t);
    }

    public static <T> Predicate<T> predicateDefault(Predicate<? super T> predicate, boolean defaultIfNull) {
        return t -> t == null ? defaultIfNull : predicate.test(t);
    }

    public static <T> Predicate<T> predicateConstant(boolean b) {
        return t -> b;
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T, R> Predicate<T> isEqual(R target, Function<? super T, ? extends R> extractor) {
        return value -> Objects.equals(target, value == null ? null : extractor.apply(value));
    }

    public static <T, R> Predicate<T> isNotEqual(R target, Function<? super T, ? extends R> function) {
        return not(isEqual(target, function));
    }

    public static <T> Predicate<T> equalsIgnoreCase(CharSequence target, Function<? super T, ? extends CharSequence> extractor) {
        return value -> StringUtils.equalsIgnoreCase(target, value == null ? null : extractor.apply(value));
    }

    public static <T, R> Predicate<T> contains(Collection<? extends R> collection, Function<? super T, ? extends R> extractor) {
        return t -> collection != null && t != null && collection.contains(extractor.apply(t));
    }

    public static <T, R> Predicate<T> inverseContains(Function<? super T, ? extends Collection<R>> collectionExtractor, R value) {
        return t -> {
            if (t != null) {
                Collection<R> collection = collectionExtractor.apply(t);
                return collection != null && collection.contains(value);
            }
            return false;
        };
    }

    public static <T, R> Predicate<T> containsKey(Map<R, ?> map, Function<? super T, ? extends R> extractor) {
        return t -> map != null && t != null && map.containsKey(extractor.apply(t));
    }

    public static <T, R> Predicate<T> containsValue(Map<?, R> map, Function<? super T, ? extends R> extractor) {
        return t -> map != null && t != null && map.containsValue(extractor.apply(t));
    }

    public static <T> Predicate<T> containsChar(Function<? super T, ? extends CharSequence> extractor, int searchChar) {
        return t -> t != null && StringUtils.contains(extractor.apply(t), searchChar);
    }

    public static <T> Predicate<T> containsSequence(Function<? super T, ? extends CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && StringUtils.contains(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> containsIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && StringUtils.containsIgnoreCase(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> isAlpha(Function<? super T, ? extends CharSequence> extractor) {
        return t -> t != null && StringUtils.isAlpha(extractor.apply(t));
    }

    public static <T> Predicate<T> isAlphanumeric(Function<? super T, ? extends CharSequence> extractor) {
        return t -> t != null && StringUtils.isAlphanumeric(extractor.apply(t));
    }

    public static <T> Predicate<T> isNumeric(Function<? super T, ? extends CharSequence> extractor) {
        return t -> t != null && StringUtils.isNumeric(extractor.apply(t));
    }

    public static <T> Predicate<T> startsWith(Function<? super T, ? extends CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && StringUtils.startsWith(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> startsWithIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && StringUtils.startsWithIgnoreCase(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> endsWith(Function<? super T, ? extends CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && StringUtils.endsWith(extractor.apply(t), suffix);
    }

    public static <T> Predicate<T> endsWithIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && StringUtils.endsWithIgnoreCase(extractor.apply(t), suffix);
    }

    public static <T, R> Predicate<T> isNull(Function<? super T, ? extends R> function) {
        return value -> value == null || Objects.isNull(function.apply(value));
    }

    public static <T, R> Predicate<T> notNull(Function<T, R> function) {
        return not(isNull(function));
    }

    public static <T, R extends Comparable<R>> Predicate<T> gt(R compareTo, Function<? super T, ? extends R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) > 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> gte(R compareTo, Function<? super T, ? extends R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) >= 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lt(R compareTo, Function<? super T, ? extends R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) < 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lte(R compareTo, Function<? super T, ? extends R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) <= 0;
    }

    private static <T, R extends Comparable<R>> int getComparisonValue(R compareTo, T target, Function<? super T, ? extends R> valueExtractor) {
        return Objects.compare(compareTo, getValueIfTargetNonNull(target, valueExtractor), nullsLast(naturalOrder()));
    }

    private static <T, R extends Comparable<R>> R getValueIfTargetNonNull(T target, Function<? super T, ? extends R> valueExtractor) {
        return target == null ? null : valueExtractor.apply(target);
    }

    public static <T, R> Predicate<T> isEmpty(Function<? super T, ? extends Collection<R>> function) {
        return t -> t == null || CollectionUtils.isEmpty(function.apply(t));
    }

    public static <T, R> Predicate<T> isNotEmpty(Function<? super T, ? extends Collection<R>> function) {
        return not(isEmpty(function));
    }

    static <T, U> Predicate<T> extractAndFilter(Function<? super T, ? extends U> transformer, Predicate<? super U> predicate) {
        return t -> t != null && predicate.test(transformer.apply(t));
    }
}
