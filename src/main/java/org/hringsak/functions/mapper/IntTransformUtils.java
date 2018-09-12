package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.DblMapperUtils.dblFlatMapper;
import static org.hringsak.functions.mapper.DblMapperUtils.flatMapperToDbl;
import static org.hringsak.functions.mapper.IntMapperUtils.flatMapperToInt;
import static org.hringsak.functions.mapper.IntMapperUtils.intFlatMapper;
import static org.hringsak.functions.mapper.IntMapperUtils.intMapper;
import static org.hringsak.functions.mapper.IntMapperUtils.intPairOf;
import static org.hringsak.functions.stream.DblStreamUtils.defaultDblStream;
import static org.hringsak.functions.stream.IntStreamUtils.defaultIntStream;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

public final class IntTransformUtils {

    private IntTransformUtils() {
    }

    public static int[] intUnaryTransform(int[] ints, IntUnaryOperator operator) {
        return defaultIntStream(ints)
                .map(operator)
                .toArray();
    }

    public static int[] intUnaryTransformDistinct(int[] ints, IntUnaryOperator operator) {
        return defaultIntStream(ints)
                .map(operator)
                .distinct()
                .toArray();
    }

    public static <R> List<R> intTransform(int[] ints, IntFunction<R> transformer) {
        return intTransform(ints, IntTransformerCollector.of(transformer, toList()));
    }

    public static <R> Set<R> intTransformToSet(int[] ints, IntFunction<R> transformer) {
        return intTransform(ints, IntTransformerCollector.of(transformer, toSet()));
    }

    public static <C extends Collection<Integer>> C intTransform(int[] ints, Collector<Integer, ?, C> collector) {
        return intTransform(ints, IntTransformerCollector.of(Integer::valueOf, collector));
    }

    public static <R> List<R> intTransformDistinct(int[] ints, IntFunction<R> transformer) {
        return defaultIntStream(ints)
                .mapToObj(intMapper(transformer))
                .distinct()
                .collect(toList());
    }

    public static <U, C extends Collection<U>> C intTransform(int[] ints, IntTransformerCollector<U, C> transformerCollector) {
        return defaultIntStream(ints)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    public static int[] intFlatMap(int[] ints, IntFunction<int[]> function) {
        return defaultIntStream(ints)
                .flatMap(intFlatMapper(function))
                .toArray();
    }

    public static int[] intFlatMapDistinct(int[] ints, IntFunction<int[]> function) {
        return defaultIntStream(ints)
                .flatMap(intFlatMapper(function))
                .distinct()
                .toArray();
    }

    public static <T> int[] flatMapToInt(Collection<T> objects, Function<T, int[]> function) {
        return defaultStream(objects)
                .flatMapToInt(flatMapperToInt(function))
                .toArray();
    }

    public static <T> int[] flatMapToIntDistinct(Collection<T> objects, Function<T, int[]> function) {
        return defaultStream(objects)
                .flatMapToInt(flatMapperToInt(function))
                .distinct()
                .toArray();
    }

    public static <U, C extends Collection<U>> IntTransformerCollector<U, C> intTransformAndThen(IntFunction<U> transformer, Collector<U, ?, C> collector) {
        return IntTransformerCollector.of(transformer, collector);
    }

    public static <K, V> Map<K, V> intTransformToMap(int[] ints, IntKeyValueMapper<K, V> keyValueMapper) {
        return defaultIntStream(ints)
                .mapToObj(intPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
