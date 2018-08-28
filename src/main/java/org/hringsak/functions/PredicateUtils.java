package org.hringsak.functions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
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

    public static <T> Predicate<T> predicate(Predicate<T> predicate, boolean defaultIfNull) {
        return t -> t == null ? defaultIfNull : predicate.test(t);
    }

    public static <T> Predicate<T> predicate(boolean b) {
        return t -> b;
    }

    public static <T, U> Predicate<T> predicate(BiFunction<T, U, Boolean> biFunction, U value) {
        return t -> biFunction.apply(t, value);
    }

    public static <T, U> Predicate<T> predicate(U value, BiFunction<U, T, Boolean> biFunction) {
        return t -> biFunction.apply(value, t);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T> Predicate<T> not(Predicate<T> predicate, boolean defaultIfNull) {
        return t -> t == null ? defaultIfNull : predicate.negate().test(t);
    }

    public static <T, R> Predicate<T> isEqual(R target, Function<T, R> extractor) {
        return value -> Objects.equals(target, value == null ? null : extractor.apply(value));
    }

    public static <T, R> Predicate<T> isNotEqual(R target, Function<T, R> function) {
        return not(isEqual(target, function));
    }

    public static <T> Predicate<T> equalsIgnoreCase(CharSequence target, Function<T, CharSequence> extractor) {
        return value -> StringUtils.equalsIgnoreCase(target, value == null ? null : extractor.apply(value));
    }

    public static <T, R> Predicate<T> contains(Collection<? super R> collection, Function<T, R> extractor) {
        return t -> collection != null && t != null && collection.contains(extractor.apply(t));
    }

    public static <T, R> Predicate<T> contains(Function<T, Collection<? super R>> collectionExtractor, R value) {
        return t -> {
            if (t != null) {
                Collection<? super R> collection = collectionExtractor.apply(t);
                return collection != null && collection.contains(value);
            }
            return false;
        };
    }

    public static <T> Predicate<T> containsChar(Function<T, CharSequence> extractor, int searchChar) {
        return t -> t != null && StringUtils.contains(extractor.apply(t), searchChar);
    }

    public static <T> Predicate<T> containsSequence(Function<T, CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && StringUtils.contains(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> containsIgnoreCase(Function<T, CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && StringUtils.containsIgnoreCase(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> isAlpha(Function<T, CharSequence> extractor) {
        return t -> t != null && StringUtils.isAlpha(extractor.apply(t));
    }

    public static <T> Predicate<T> isAlphanumeric(Function<T, CharSequence> extractor) {
        return t -> t != null && StringUtils.isAlphanumeric(extractor.apply(t));
    }

    public static <T> Predicate<T> isNumeric(Function<T, CharSequence> extractor) {
        return t -> t != null && StringUtils.isNumeric(extractor.apply(t));
    }

    public static <T> Predicate<T> startsWith(Function<T, CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && StringUtils.startsWith(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> startsWithIgnoreCase(Function<T, CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && StringUtils.startsWithIgnoreCase(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> endsWith(Function<T, CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && StringUtils.endsWith(extractor.apply(t), suffix);
    }

    public static <T> Predicate<T> endsWithIgnoreCase(Function<T, CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && StringUtils.endsWithIgnoreCase(extractor.apply(t), suffix);
    }

    public static <T, R> Predicate<T> isNull(Function<T, R> function) {
        return value -> value == null || Objects.isNull(function.apply(value));
    }

    public static <T, R> Predicate<T> notNull(Function<T, R> function) {
        return not(isNull(function));
    }

    public static <T, R extends Comparable<R>> Predicate<T> gt(R compareTo, Function<T, R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) > 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> gte(R compareTo, Function<T, R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) >= 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lt(R compareTo, Function<T, R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) < 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lte(R compareTo, Function<T, R> valueExtractor) {
        return target -> getComparisonValue(compareTo, target, valueExtractor) <= 0;
    }

    private static <T, R extends Comparable<R>> int getComparisonValue(R compareTo, T target, Function<T, R> valueExtractor) {
        return Objects.compare(compareTo, getValueIfTargetNonNull(target, valueExtractor), nullsLast(naturalOrder()));
    }

    private static <T, R extends Comparable<R>> R getValueIfTargetNonNull(T target, Function<T, R> valueExtractor) {
        return target == null ? null : valueExtractor.apply(target);
    }

    public static <T, R> Predicate<T> isEmpty(Function<T, Collection<R>> function) {
        return t -> t == null || CollectionUtils.isEmpty(function.apply(t));
    }

    public static <T, R> Predicate<T> isNotEmpty(Function<T, Collection<R>> function) {
        return not(isEmpty(function));
    }
}
