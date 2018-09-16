package org.hringsak.functions.predicate;

import org.hringsak.functions.stream.DblStreamUtils;
import org.hringsak.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

/**
 * Methods that build predicates specifically those involving primitive <code>int</code> types.
 */
public final class DblPredicateUtils {

    private DblPredicateUtils() {
    }

    public static DoublePredicate dblPredicate(DoublePredicate predicate) {
        return predicate;
    }

    public static <U> DoublePredicate dblPredicate(BiPredicate<Double, ? super U> biPredicate, U value) {
        return d -> biPredicate.test(d, value);
    }

    public static <U> DoublePredicate inverseDblPredicate(BiPredicate<? super U, Double> biPredicate, U value) {
        return d -> biPredicate.test(value, d);
    }

    public static DoublePredicate dblConstant(boolean b) {
        return d -> b;
    }

    public static DoublePredicate fromDblMapper(DoubleFunction<Boolean> function) {
        return function::apply;
    }

    public static DoublePredicate dblNot(DoublePredicate predicate) {
        return predicate.negate();
    }

    public static <R extends CharSequence> DoublePredicate isDblSeqEmpty(DoubleFunction<? extends R> function) {
        return d -> {
            CharSequence seq = function.apply(d);
            return seq == null || seq.length() == 0;
        };
    }

    public static <R extends CharSequence> DoublePredicate isDblSeqNotEmpty(DoubleFunction<? extends R> function) {
        return dblNot(isDblSeqEmpty(function));
    }

    public static <R extends CharSequence> DoublePredicate dblEqualsIgnoreCase(DoubleFunction<? extends R> function, R value) {
        return d -> CharSequenceUtils.equalsIgnoreCase(function.apply(d), value);
    }

    public static <R extends CharSequence> DoublePredicate dblNotEqualsIgnoreCase(DoubleFunction<? extends R> function, R value) {
        return dblNot(dblEqualsIgnoreCase(function, value));
    }

    public static DoublePredicate isDblEqual(double value) {
        return d -> d == value;
    }

    public static DoublePredicate isDblEqual(DoubleUnaryOperator operator, double value) {
        return d -> operator.applyAsDouble(d) == value;
    }

    public static <R> DoublePredicate isDblMapperEqual(DoubleFunction<? extends R> function, R value) {
        return d -> Objects.equals(function.apply(d), value);
    }

    public static DoublePredicate isDblNotEqual(double value) {
        return dblNot(isDblEqual(value));
    }

    public static DoublePredicate isDblNotEqual(DoubleUnaryOperator operator, double value) {
        return dblNot(isDblEqual(operator, value));
    }

    public static <R> DoublePredicate isDblMapperNotEqual(DoubleFunction<? extends R> function, R value) {
        return dblNot(isDblMapperEqual(function, value));
    }

    public static <R> DoublePredicate dblToObjsContains(DoubleFunction<? extends Collection<R>> collectionExtractor, R value) {
        return d -> {
            Collection<R> collection = collectionExtractor.apply(d);
            return collection != null && collection.contains(value);
        };
    }

    public static <T> Predicate<T> objToDblsContains(Function<T, double[]> collectionExtractor, double value) {
        return t -> {
            double[] doubles = collectionExtractor.apply(t);
            return DblStreamUtils.dblAnyMatch(doubles, isDblEqual(value));
        };
    }

    public static <R> DoublePredicate inverseDblToObjContains(Collection<? extends R> collection, DoubleFunction<? extends R> function) {
        return d -> collection != null && collection.contains(function.apply(d));
    }

    public static <T> Predicate<T> inverseObjToDblContains(double[] doubles, ToDoubleFunction<? super T> function) {
        return t -> doubles != null && DblStreamUtils.dblAnyMatch(doubles, isDblEqual(function.applyAsDouble(t)));
    }

    public static <R> DoublePredicate dblContainsKey(Map<R, ?> map, DoubleFunction<? extends R> function) {
        return d -> map != null && map.containsKey(function.apply(d));
    }

    public static <R> DoublePredicate dblContainsValue(Map<?, R> map, DoubleFunction<? extends R> function) {
        return d -> map != null && map.containsValue(function.apply(d));
    }

    public static DoublePredicate dblContainsChar(DoubleFunction<? extends CharSequence> function, int searchChar) {
        return d -> CharSequenceUtils.contains(function.apply(d), searchChar);
    }

    public static DoublePredicate dblContainsSequence(DoubleFunction<? extends CharSequence> function, CharSequence searchSeq) {
        return d -> CharSequenceUtils.contains(function.apply(d), searchSeq);
    }

    public static DoublePredicate dblContainsIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence searchSeq) {
        return d -> CharSequenceUtils.containsIgnoreCase(function.apply(d), searchSeq);
    }

    public static DoublePredicate dblIsAlpha(DoubleFunction<? extends CharSequence> function) {
        return d -> CharSequenceUtils.isAlpha(function.apply(d));
    }

    public static DoublePredicate dblIsAlphanumeric(DoubleFunction<? extends CharSequence> function) {
        return d -> CharSequenceUtils.isAlphaNumeric(function.apply(d));
    }

    public static DoublePredicate dblIsNumeric(DoubleFunction<? extends CharSequence> function) {
        return d -> CharSequenceUtils.isNumeric(function.apply(d));
    }

    public static DoublePredicate dblStartsWith(DoubleFunction<? extends CharSequence> function, CharSequence prefix) {
        return d -> CharSequenceUtils.startsWith(function.apply(d), prefix);
    }

    public static DoublePredicate dblStartsWithIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence prefix) {
        return d -> CharSequenceUtils.startsWithIgnoreCase(function.apply(d), prefix);
    }

    public static DoublePredicate dblEndsWith(DoubleFunction<? extends CharSequence> function, CharSequence suffix) {
        return d -> CharSequenceUtils.endsWith(function.apply(d), suffix);
    }

    public static DoublePredicate dblEndsWithIgnoreCase(DoubleFunction<? extends CharSequence> function, CharSequence suffix) {
        return d -> CharSequenceUtils.endsWithIgnoreCase(function.apply(d), suffix);
    }

    public static <R> DoublePredicate isDblNull(DoubleFunction<? extends R> function) {
        return d -> Objects.isNull(function.apply(d));
    }

    public static <R> DoublePredicate isDblNotNull(DoubleFunction<? extends R> function) {
        return dblNot(isDblNull(function));
    }

    public static DoublePredicate dblGt(double compareTo) {
        return d -> d > compareTo;
    }

    public static <R extends Comparable<R>> DoublePredicate dblGt(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) > 0;
    }

    public static DoublePredicate dblGte(double compareTo) {
        return d -> d >= compareTo;
    }

    public static <R extends Comparable<R>> DoublePredicate dblGte(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) >= 0;
    }

    public static DoublePredicate dblLt(double compareTo) {
        return d -> d < compareTo;
    }

    public static <R extends Comparable<R>> DoublePredicate dblLt(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) < 0;
    }

    public static DoublePredicate dblLte(double compareTo) {
        return d -> d <= compareTo;
    }

    public static <R extends Comparable<R>> DoublePredicate dblLte(DoubleFunction<? extends R> function, R compareTo) {
        return d -> Objects.compare(function.apply(d), compareTo, nullsLast(naturalOrder())) <= 0;
    }

    public static <R> DoublePredicate isDblCollEmpty(DoubleFunction<? extends Collection<R>> function) {
        return d -> CollectionUtils.isEmpty(function.apply(d));
    }

    public static <R> DoublePredicate isDblCollNotEmpty(DoubleFunction<? extends Collection<R>> function) {
        return dblNot(isDblCollEmpty(function));
    }

    public static <T> Predicate<T> objToDblsAllMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblAllMatch(function.apply(t), predicate);
    }

    public static <R> DoublePredicate dblToObjsAllMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.allMatch(function.apply(d), predicate);
    }

    public static <T> Predicate<T> objToDblsAnyMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblAnyMatch(function.apply(t), predicate);
    }

    public static <R> DoublePredicate dblToObjsAnyMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.anyMatch(function.apply(d), predicate);
    }

    public static <T> Predicate<T> objToDblsNoneMatch(Function<T, ? extends double[]> function, DoublePredicate predicate) {
        return t -> t != null && DblStreamUtils.dblNoneMatch(function.apply(t), predicate);
    }

    public static <R> DoublePredicate dblToObjsNoneMatch(DoubleFunction<? extends Collection<R>> function, Predicate<R> predicate) {
        return d -> StreamUtils.noneMatch(function.apply(d), predicate);
    }

    public static DoublePredicate dblDistinctByKey(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = new HashSet<>();
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static DoublePredicate dblDistinctByKeyParallel(DoubleFunction<?> keyExtractor) {
        Set<? super Object> uniqueKeys = Collections.synchronizedSet(new HashSet<>());
        return d -> uniqueKeys.add(keyExtractor.apply(d));
    }

    public static <T> Predicate<T> mapToDblAndFilter(ToDoubleFunction<? super T> function, DoublePredicate predicate) {
        return t -> predicate.test(function.applyAsDouble(t));
    }

    public static <U> DoublePredicate dblMapAndFilter(DoubleFunction<? extends U> function, Predicate<? super U> predicate) {
        return d -> predicate.test(function.apply(d));
    }
}
