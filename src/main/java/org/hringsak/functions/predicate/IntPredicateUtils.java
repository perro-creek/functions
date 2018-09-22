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

    public static IntPredicate intPredicate(IntPredicate predicate) {
        return predicate;
    }

    public static <U> IntPredicate intPredicate(BiPredicate<Integer, ? super U> biPredicate, U value) {
        return i -> biPredicate.test(i, value);
    }

    public static <U> IntPredicate inverseIntPredicate(BiPredicate<? super U, Integer> biPredicate, U value) {
        return i -> biPredicate.test(value, i);
    }

    public static IntPredicate intConstant(boolean b) {
        return i -> b;
    }

    public static IntPredicate fromIntMapper(IntFunction<Boolean> function) {
        return function::apply;
    }

    public static IntPredicate intNot(IntPredicate predicate) {
        return predicate.negate();
    }

    public static <R extends CharSequence> IntPredicate isIntSeqEmpty(IntFunction<? extends R> function) {
        return i -> { CharSequence seq = function.apply(i); return seq == null || seq.length() == 0; };
    }

    public static <R extends CharSequence> IntPredicate isIntSeqNotEmpty(IntFunction<? extends R> function) {
        return intNot(isIntSeqEmpty(function));
    }

    public static <R extends CharSequence> IntPredicate intEqualsIgnoreCase(IntFunction<? extends R> function, R value) {
        return i -> CharSequenceUtils.equalsIgnoreCase(function.apply(i), value);
    }

    public static <R extends CharSequence> IntPredicate intNotEqualsIgnoreCase(IntFunction<? extends R> function, R value) {
        return intNot(intEqualsIgnoreCase(function, value));
    }

    public static IntPredicate isIntEqual(int value) {
        return i -> i == value;
    }

    public static IntPredicate isIntEqual(IntUnaryOperator operator, int value) {
        return d -> operator.applyAsInt(d) == value;
    }

    public static IntPredicate isIntNotEqual(int value) {
        return intNot(isIntEqual(value));
    }

    public static IntPredicate isIntNotEqual(IntUnaryOperator operator, int value) {
        return intNot(isIntEqual(operator, value));
    }

    public static <R> IntPredicate isIntMapperEqual(IntFunction<? extends R> function, R value) {
        return i -> Objects.equals(function.apply(i), value);
    }

    public static <R> IntPredicate isIntMapperNotEqual(IntFunction<? extends R> function, R value) {
        return intNot(isIntMapperEqual(function, value));
    }

    public static <R> IntPredicate intToObjsContains(IntFunction<? extends Collection<R>> collectionExtractor, R value) {
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

    public static <R> IntPredicate inverseIntToObjContains(Collection<? extends R> collection, IntFunction<? extends R> extractor) {
        return i -> collection != null && collection.contains(extractor.apply(i));
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
