package org.hringsak.functions;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.hringsak.functions.StreamUtils.defaultStream;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static <T, R> Function<T, R> mapper(Function<T, R> mapper) {
        return mapper;
    }

    public static <T, U, R> Function<T, R> mapper(BiFunction<T, U, R> function, U value) {
        return t -> function.apply(t, value);
    }

    public static <T, U, R> Function<T, R> mapper(U value, BiFunction<U, T, R> function) {
        return t -> function.apply(value, t);
    }

    public static <T, R> Function<T, Stream<R>> streamOf(Function<T, R> mapper) {
        return t -> t == null ? Stream.empty() : Stream.of(mapper.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> flatMapper(Function<T, Collection<R>> mapper) {
        return t -> t == null ? Stream.empty() : defaultStream(mapper.apply(t));
    }

    public static <T, U, R> Function<T, Pair<U, R>> pairOf(Function<T, U> leftFunction, Function<T, R> rightFunction) {
        return t -> Pair.of(leftFunction.apply(t), rightFunction.apply(t));
    }

    public static <T, R> Function<T, Pair<T, R>> pairWith(List<R> pairedList) {
        return pairWith(Function.identity(), pairedList);
    }

    public static <T, U, R> Function<T, Pair<U, R>> pairWith(Function<T, U> function, List<R> pairedList) {
        List<R> nonNullList = pairedList == null ? Collections.emptyList() : pairedList;
        AtomicInteger idx = new AtomicInteger();
        return t -> idx.get() < nonNullList.size() ? Pair.of(function.apply(t), nonNullList.get(idx.getAndIncrement())) :
                Pair.of(function.apply(t), null);
    }

    public static <T> Function<T, Pair<T, Integer>> pairWithIndex() {
        return pairWithIndex(Function.identity());
    }

    public static <T, R> Function<T, Pair<R, Integer>> pairWithIndex(Function<T, R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> Pair.of(function.apply(t), idx.getAndIncrement());
    }

    public static <T, R> Function<T, R> ternary(Predicate<T> predicate, Function<T, R> trueAnswer, Function<T, R> falseAnswer) {
        return t -> {
            if (t == null) {
                return null;
            }
            return predicate.test(t) ? trueAnswer.apply(t) : falseAnswer.apply(t);
        };
    }
}
