package org.hringsak.functions;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import static org.hringsak.functions.StreamUtils.defaultStream;

public final class MapperUtils {

    private MapperUtils() {
    }

    @SuppressWarnings("unused")
    public static <T, R> Function<T, R> mapper(Function<T, R> mapper) {
        return mapper;
    }

    public static <T, R> Function<T, R> mapperDefault(Function<? super T, ? extends R> mapper, R defaultValue) {
        return t -> t == null || mapper.apply(t) == null ? defaultValue : mapper.apply(t);
    }

    public static <T, U, R> Function<T, R> mapper(BiFunction<? super T, ? super U, ? extends R> function, U value) {
        return t -> function.apply(t, value);
    }

    public static <T, U, R> Function<T, R> mapper(U value, BiFunction<? super U, ? super T, ? extends R> function) {
        return t -> function.apply(value, t);
    }

    @SuppressWarnings("unused")
    public static <T> ToDoubleFunction<T> doubleMapper(ToDoubleFunction<T> mapper) {
        return mapper;
    }

    public static <T> ToDoubleFunction<T> doubleMapperDefault(ToDoubleFunction<? super T> mapper, double defaultValue) {
        return t -> t == null ? defaultValue : mapper.applyAsDouble(t);
    }

    public static <T, U> ToDoubleFunction<T> doubleMapper(ToDoubleBiFunction<? super T, ? super U> function, U value) {
        return t -> function.applyAsDouble(t, value);
    }

    public static <T, U> ToDoubleFunction<T> doubleMapper(U value, ToDoubleBiFunction<? super U, ? super T> function) {
        return t -> function.applyAsDouble(value, t);
    }

    @SuppressWarnings("unused")
    public static <T> ToIntFunction<T> intMapper(ToIntFunction<T> mapper) {
        return mapper;
    }

    public static <T> ToIntFunction<T> intMapperDefault(ToIntFunction<? super T> mapper, int defaultValue) {
        return t -> t == null ? defaultValue : mapper.applyAsInt(t);
    }

    public static <T, U> ToIntFunction<T> intMapper(ToIntBiFunction<? super T, ? super U> function, U value) {
        return t -> function.applyAsInt(t, value);
    }

    public static <T, U> ToIntFunction<T> intMapper(U value, ToIntBiFunction<? super U, ? super T> function) {
        return t -> function.applyAsInt(value, t);
    }

    @SuppressWarnings("unused")
    public static <T> ToLongFunction<T> longMapper(ToLongFunction<T> mapper) {
        return mapper;
    }

    public static <T> ToLongFunction<T> longMapperDefault(ToLongFunction<? super T> mapper, long defaultValue) {
        return t -> t == null ? defaultValue : mapper.applyAsLong(t);
    }

    public static <T, U> ToLongFunction<T> longMapper(ToLongBiFunction<? super T, ? super U> function, U value) {
        return t -> function.applyAsLong(t, value);
    }

    public static <T, U> ToLongFunction<T> longMapper(U value, ToLongBiFunction<? super U, ? super T> function) {
        return t -> function.applyAsLong(value, t);
    }

    public static <T, U, R> Function<T, R> getValue(Map<U, R> map, Function<T, U> extractor) {
        return t -> (map == null || t == null) ? null : map.get(extractor.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> streamOf(Function<? super T, ? extends R> mapper) {
        return t -> t == null ? Stream.empty() : Stream.of(mapper.apply(t));
    }

    public static <T, R> Function<T, Stream<R>> flatMapper(Function<? super T, ? extends Collection<R>> mapper) {
        return t -> t == null ? Stream.empty() : defaultStream(mapper.apply(t));
    }

    public static <T, U, V> Function<T, Pair<U, V>> pairOf(Function<T, U> leftFunction, Function<? super T, ? extends V> rightFunction) {
        return t -> Pair.of(leftFunction.apply(t), rightFunction.apply(t));
    }

    public static <T, R> Function<T, Pair<T, R>> pairWith(List<R> pairedList) {
        return pairWith(Function.identity(), pairedList);
    }

    public static <T, U, V> Function<T, Pair<U, V>> pairWith(Function<? super T, ? extends U> function, List<V> pairedList) {
        List<V> nonNullList = ListUtils.emptyIfNull(pairedList);
        MutableInt idx = new MutableInt();
        return t -> {
            U extracted = t == null ? null : function.apply(t);
            int i = idx.getAndIncrement();
            return (i < nonNullList.size()) ? Pair.of(extracted, nonNullList.get(i)) : Pair.of(extracted, null);
        };
    }

    public static <T> Function<T, Pair<T, Integer>> pairWithIndex() {
        return pairWithIndex(Function.identity());
    }

    public static <T, R> Function<T, Pair<R, Integer>> pairWithIndex(Function<? super T, ? extends R> function) {
        AtomicInteger idx = new AtomicInteger();
        return t -> Pair.of(function.apply(t), idx.getAndIncrement());
    }

    public static <T, R> Function<T, R> ternary(Predicate<? super T> predicate,
                                                Function<? super T, ? extends R> trueExtractor,
                                                Function<? super T, ? extends R>  falseExtractor) {
        return t -> t != null && predicate.test(t) ? trueExtractor.apply(t) : falseExtractor.apply(t);
    }
}
