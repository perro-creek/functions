package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.LongMapperUtils.flatMapperToLong;
import static org.hringsak.functions.mapper.LongMapperUtils.longFlatMapper;
import static org.hringsak.functions.mapper.LongMapperUtils.longMapper;
import static org.hringsak.functions.mapper.LongMapperUtils.longPairOf;
import static org.hringsak.functions.stream.LongStreamUtils.defaultLongStream;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

public final class LongTransformUtils {

    private LongTransformUtils() {
    }

    public static long[] longUnaryTransform(long[] longs, LongUnaryOperator operator) {
        return defaultLongStream(longs)
                .map(operator)
                .toArray();
    }

    public static long[] longUnaryTransformDistinct(long[] longs, LongUnaryOperator operator) {
        return defaultLongStream(longs)
                .map(operator)
                .distinct()
                .toArray();
    }

    public static <R> List<R> longTransform(long[] longs, LongFunction<R> transformer) {
        return longTransform(longs, LongTransformerCollector.of(transformer, toList()));
    }

    public static <R> Set<R> longTransformToSet(long[] longs, LongFunction<R> transformer) {
        return longTransform(longs, LongTransformerCollector.of(transformer, toSet()));
    }

    public static <C extends Collection<Long>> C longTransform(long[] longs, Collector<Long, ?, C> collector) {
        return longTransform(longs, LongTransformerCollector.of(Long::valueOf, collector));
    }

    public static <R> List<R> longTransformDistinct(long[] longs, LongFunction<R> transformer) {
        return defaultLongStream(longs)
                .mapToObj(longMapper(transformer))
                .distinct()
                .collect(toList());
    }

    public static <U, C extends Collection<U>> C longTransform(long[] longs, LongTransformerCollector<U, C> transformerCollector) {
        return defaultLongStream(longs)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    public static long[] longFlatMap(long[] longs, LongFunction<long[]> function) {
        return defaultLongStream(longs)
                .flatMap(longFlatMapper(function))
                .toArray();
    }

    public static long[] longFlatMapDistinct(long[] longs, LongFunction<long[]> function) {
        return defaultLongStream(longs)
                .flatMap(longFlatMapper(function))
                .distinct()
                .toArray();
    }

    public static <T> long[] flatMapToLong(Collection<T> objects, Function<T, long[]> function) {
        return defaultStream(objects)
                .flatMapToLong(flatMapperToLong(function))
                .toArray();
    }

    public static <T> long[] flatMapToLongDistinct(Collection<T> objects, Function<T, long[]> function) {
        return defaultStream(objects)
                .flatMapToLong(flatMapperToLong(function))
                .distinct()
                .toArray();
    }

    public static <U, C extends Collection<U>> LongTransformerCollector<U, C> longTransformAndThen(LongFunction<U> transformer, Collector<U, ?, C> collector) {
        return LongTransformerCollector.of(transformer, collector);
    }

    public static <K, V> Map<K, V> longTransformToMap(long[] longs, LongKeyValueMapper<K, V> keyValueMapper) {
        return defaultLongStream(longs)
                .mapToObj(longPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
