package org.hringsak.functions.predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

/**
 * Methods that build predicates specifically those involving primitive <code>long</code> types.
 */
public final class LongPredicateUtils {

    private LongPredicateUtils() {
    }

    public static LongPredicate longPredicate(LongPredicate predicate) {
        return predicate;
    }

    public static <U> LongPredicate longPredicate(BiPredicate<Long, ? super U> biPredicate, U value) {
        return l -> biPredicate.test(l, value);
    }

    public static <U> LongPredicate inverseLongPredicate(BiPredicate<? super U, Long> biPredicate, U value) {
        return l -> biPredicate.test(value, l);
    }

    public static LongPredicate longConstant(boolean b) {
        return l -> b;
    }

    public static LongPredicate fromDblMapper(LongFunction<Boolean> function) {
        return function::apply;
    }

    public static LongPredicate notLong(LongPredicate predicate) {
        return predicate.negate();
    }

    public static <R extends CharSequence> LongPredicate isLongStrEmpty(LongFunction<? extends R> function) {
        return l -> { CharSequence seq = function.apply(l); return seq == null || seq.length() == 0; };
    }

    public static <R extends CharSequence> LongPredicate isLongStrNotEmpty(LongFunction<? extends R> function) {
        return notLong(isLongStrEmpty(function));
    }

    public static LongPredicate isLongEqual(long value) {
        return l -> l == value;
    }

    public static <R> LongPredicate isLongEqual(R value, LongFunction<? extends R> extractor) {
        return l -> Objects.equals(value, extractor.apply(l));
    }

    public static LongPredicate isLongNotEqual(long value) {
        return notLong(isLongEqual(value));
    }

    public static <R> LongPredicate isLongNotEqual(R value, LongFunction<? extends R> function) {
        return notLong(isLongEqual(value, function));
    }

    public static <R> LongPredicate longContains(Collection<? extends R> collection, LongFunction<? extends R> extractor) {
        return l -> collection != null && collection.contains(extractor.apply(l));
    }

    public static <R> LongPredicate longInverseContains(LongFunction<? extends Collection<R>> collectionExtractor, R value) {
        return l -> {
            Collection<R> collection = collectionExtractor.apply(l);
            return collection != null && collection.contains(value);
        };
    }

    public static <R> LongPredicate longContainsKey(Map<R, ?> map, LongFunction<? extends R> extractor) {
        return l -> map != null && map.containsKey(extractor.apply(l));
    }

    public static <R> LongPredicate longContainsValue(Map<?, R> map, LongFunction<? extends R> extractor) {
        return l -> map != null && map.containsValue(extractor.apply(l));
    }

    public static LongPredicate longContainsChar(LongFunction<? extends CharSequence> extractor, int searchChar) {
        return l -> StringUtils.contains(extractor.apply(l), searchChar);
    }

    public static LongPredicate longContainsSequence(LongFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return l -> StringUtils.contains(extractor.apply(l), searchSeq);
    }

    public static LongPredicate longContainsIgnoreCase(LongFunction<? extends CharSequence> extractor, CharSequence searchSeq) {
        return l -> StringUtils.containsIgnoreCase(extractor.apply(l), searchSeq);
    }

    public static LongPredicate longIsAlpha(LongFunction<? extends CharSequence> extractor) {
        return l -> StringUtils.isAlpha(extractor.apply(l));
    }

    public static LongPredicate longIsAlphanumeric(LongFunction<? extends CharSequence> extractor) {
        return l -> StringUtils.isAlphanumeric(extractor.apply(l));
    }

    public static LongPredicate longIsNumeric(LongFunction<? extends CharSequence> extractor) {
        return l -> StringUtils.isNumeric(extractor.apply(l));
    }

    public static LongPredicate longStartsWith(LongFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return l -> StringUtils.startsWith(extractor.apply(l), prefix);
    }

    public static LongPredicate longStartsWithIgnoreCase(LongFunction<? extends CharSequence> extractor, CharSequence prefix) {
        return l -> StringUtils.startsWithIgnoreCase(extractor.apply(l), prefix);
    }

    public static LongPredicate longEndsWith(LongFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return l -> StringUtils.endsWith(extractor.apply(l), suffix);
    }

    public static LongPredicate longEndsWithIgnoreCase(LongFunction<? extends CharSequence> extractor, CharSequence suffix) {
        return l -> StringUtils.endsWithIgnoreCase(extractor.apply(l), suffix);
    }

    public static <R> LongPredicate isLongNull(LongFunction<? extends R> function) {
        return l -> Objects.isNull(function.apply(l));
    }

    public static <R> LongPredicate longNotNull(LongFunction<? extends R> function) {
        return notLong(isLongNull(function));
    }

    public static LongPredicate longGt(long compareTo) {
        return l -> l > compareTo;
    }

    public static <R extends Comparable<R>> LongPredicate longGt(R compareTo, LongFunction<? extends R> valueExtractor) {
        return l -> Objects.compare(valueExtractor.apply(l), compareTo, nullsLast(naturalOrder())) > 0;
    }

    public static LongPredicate longGte(long compareTo) {
        return l -> l >= compareTo;
    }

    public static <R extends Comparable<R>> LongPredicate longGte(R compareTo, LongFunction<? extends R> valueExtractor) {
        return l -> Objects.compare(valueExtractor.apply(l), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    public static LongPredicate longLt(long compareTo) {
        return l -> l < compareTo;
    }

    public static <R extends Comparable<R>> LongPredicate longLt(R compareTo, LongFunction<? extends R> valueExtractor) {
        return l -> Objects.compare(valueExtractor.apply(l), compareTo, nullsLast(naturalOrder())) < 0;
    }

    public static LongPredicate longLte(long compareTo) {
        return l -> l <= compareTo;
    }

    public static <R extends Comparable<R>> LongPredicate longLte(R compareTo, LongFunction<? extends R> valueExtractor) {
        return l -> Objects.compare(valueExtractor.apply(l), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    public static <R> LongPredicate isLongCollEmpty(LongFunction<? extends Collection<R>> function) {
        return l -> CollectionUtils.isEmpty(function.apply(l));
    }

    public static <R> LongPredicate isLongCollNotEmpty(LongFunction<? extends Collection<R>> function) {
        return notLong(isLongCollEmpty(function));
    }

    public static <T> Predicate<T> mapToLongAndFilter(ToLongFunction<? super T> transformer, LongPredicate predicate) {
        return l -> predicate.test(transformer.applyAsLong(l));
    }

    public static <U> LongPredicate longMapAndFilter(LongFunction<? extends U> transformer, Predicate<? super U> predicate) {
        return l -> predicate.test(transformer.apply(l));
    }
}
