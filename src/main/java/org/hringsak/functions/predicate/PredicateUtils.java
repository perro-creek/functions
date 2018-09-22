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
import static java.util.Comparator.nullsLast;
import static org.hringsak.functions.predicate.CharSequenceUtils.isCharacterMatch;

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

    public static <T> Predicate<T> constant(boolean b) {
        return t -> b;
    }

    public static <T> Predicate<T> fromMapper(Function<T, Boolean> function) {
        return t -> t != null && function.apply(t);
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T, R extends CharSequence> Predicate<T> isSeqEmpty(Function<? super T, ? extends R> function) {
        return t -> t == null  || function.apply(t) == null || function.apply(t).length() == 0;
    }

    public static <T, R extends CharSequence> Predicate<T> isSeqNotEmpty(Function<? super T, ? extends R> function) {
        return not(isSeqEmpty(function));
    }

    public static <T> Predicate<T> equalsIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence value) {
        return t -> CharSequenceUtils.equalsIgnoreCase(t == null ? null : function.apply(t), value);
    }

    public static <T> Predicate<T> notEqualsIgnoreCase(Function<? super T, ? extends CharSequence> function, CharSequence value) {
        return not(equalsIgnoreCase(function, value));
    }

    public static <T, R> Predicate<T> isEqual(Function<? super T, ? extends R> extractor, R value) {
        return t -> Objects.equals(t == null ? null : extractor.apply(t), value);
    }

    public static <T, R> Predicate<T> isNotEqual(Function<? super T, ? extends R> function, R value) {
        return not(isEqual(function, value));
    }

    public static <T, R> Predicate<T> contains(Function<? super T, ? extends Collection<R>> collectionExtractor, R value) {
        return t -> {
            if (t != null) {
                Collection<R> collection = collectionExtractor.apply(t);
                return collection != null && collection.contains(value);
            }
            return false;
        };
    }

    public static <T, R> Predicate<T> inverseContains(Collection<? extends R> collection, Function<? super T, ? extends R> function) {
        return t -> collection != null && t != null && collection.contains(function.apply(t));
    }

    public static <T, R> Predicate<T> containsKey(Map<R, ?> map, Function<? super T, ? extends R> extractor) {
        return t -> map != null && t != null && map.containsKey(extractor.apply(t));
    }

    public static <T, R> Predicate<T> containsValue(Map<?, R> map, Function<? super T, ? extends R> extractor) {
        return t -> map != null && t != null && map.containsValue(extractor.apply(t));
    }

    public static <T> Predicate<T> containsChar(Function<? super T, ? extends CharSequence> extractor, int searchChar) {
        return t -> t != null && CharSequenceUtils.contains(extractor.apply(t), searchChar);
    }

    public static <T> Predicate<T> containsSequence(Function<? super T, ? extends CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && CharSequenceUtils.contains(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> containsIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence searchSeq) {
        return t -> t != null && CharSequenceUtils.containsIgnoreCase(extractor.apply(t), searchSeq);
    }

    public static <T> Predicate<T> isAlpha(Function<? super T, ? extends CharSequence> extractor) {
        return t -> CharSequenceUtils.isCharacterMatch(extractor.apply(t), Character::isLetter);
    }

    public static <T> Predicate<T> isAlphanumeric(Function<? super T, ? extends CharSequence> extractor) {
        return t -> CharSequenceUtils.isCharacterMatch(extractor.apply(t), Character::isLetterOrDigit);
    }

    public static <T> Predicate<T> isNumeric(Function<? super T, ? extends CharSequence> extractor) {
        return t -> CharSequenceUtils.isCharacterMatch(extractor.apply(t), Character::isDigit);
    }

    public static <T> Predicate<T> startsWith(Function<? super T, ? extends CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && CharSequenceUtils.startsWith(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> startsWithIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence prefix) {
        return t -> t != null && CharSequenceUtils.startsWithIgnoreCase(extractor.apply(t), prefix);
    }

    public static <T> Predicate<T> endsWith(Function<? super T, ? extends CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && CharSequenceUtils.endsWith(extractor.apply(t), suffix);
    }

    public static <T> Predicate<T> endsWithIgnoreCase(Function<? super T, ? extends CharSequence> extractor, CharSequence suffix) {
        return t -> t != null && CharSequenceUtils.endsWithIgnoreCase(extractor.apply(t), suffix);
    }

    public static <T> Predicate<T> anyCharsMatch(Function<? super T, ? extends CharSequence> extractor, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(extractor).orElse(null);
            return sequence != null && sequence.codePoints().anyMatch(charPredicate);
        };
    }

    public static <T> Predicate<T> allCharsMatch(Function<? super T, ? extends CharSequence> extractor, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(extractor).orElse(null);
            return isCharacterMatch(sequence, charPredicate);
        };
    }

    public static <T> Predicate<T> noCharsMatch(Function<? super T, ? extends CharSequence> extractor, IntPredicate charPredicate) {
        return t -> {
            CharSequence sequence = Optional.ofNullable(t).map(extractor).orElse(null);
            return sequence != null && sequence.codePoints().noneMatch(charPredicate);
        };
    }

    public static <T, R> Predicate<T> isNull(Function<? super T, ? extends R> function) {
        return value -> value == null || Objects.isNull(function.apply(value));
    }

    public static <T, R> Predicate<T> notNull(Function<T, R> function) {
        return not(isNull(function));
    }

    public static <T, R extends Comparable<R>> Predicate<T> gt(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValue(target, function, compareTo) > 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> gte(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValue(target, function, compareTo) >= 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lt(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValue(target, function, compareTo) < 0;
    }

    public static <T, R extends Comparable<R>> Predicate<T> lte(Function<? super T, ? extends R> function, R compareTo) {
        return target -> getComparisonValue(target, function, compareTo) <= 0;
    }

    private static <T, R extends Comparable<R>> int getComparisonValue(T target, Function<? super T, ? extends R> valueExtractor, R compareTo) {
        return Objects.compare(getValueIfTargetNonNull(valueExtractor, target), compareTo, nullsLast(naturalOrder()));
    }

    private static <T, R extends Comparable<R>> R getValueIfTargetNonNull(Function<? super T, ? extends R> valueExtractor, T target) {
        return target == null ? null : valueExtractor.apply(target);
    }

    public static <T, R> Predicate<T> isCollEmpty(Function<? super T, ? extends Collection<R>> function) {
        return t -> t == null || CollectionUtils.isEmpty(function.apply(t));
    }

    public static <T, R> Predicate<T> isCollNotEmpty(Function<? super T, ? extends Collection<R>> function) {
        return not(isCollEmpty(function));
    }

    public static <T, R> Predicate<T> allMatch(Function<? super T, ? extends Collection<R>> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.allMatch(function.apply(t), predicate);
    }

    public static <T, R, C extends Collection<R>> Predicate<T> anyMatch(Function<? super T, ? extends C> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.anyMatch(function.apply(t), predicate);
    }

    public static <T, R, C extends Collection<R>> Predicate<T> noneMatch(Function<? super T, ? extends C> function, Predicate<R> predicate) {
        return t -> t != null && StreamUtils.noneMatch(function.apply(t), predicate);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> uniqueKeys = new HashSet<>();
        return t -> uniqueKeys.add(keyExtractor.apply(t));
    }

    public static <T> Predicate<T> distinctByKeyParallel(Function<? super T, ?> keyExtractor) {
        Set<Object> uniqueKeys = Collections.synchronizedSet(new HashSet<>());
        return t -> uniqueKeys.add(keyExtractor.apply(t));
    }

    public static <T, U> Predicate<T> mapAndFilter(Function<? super T, ? extends U> transformer, Predicate<? super U> predicate) {
        return t -> t != null && predicate.test(transformer.apply(t));
    }
}
