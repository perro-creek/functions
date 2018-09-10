package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongFunction;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.LongMapperUtils.longMapper;
import static org.hringsak.functions.mapper.LongMapperUtils.longPairOf;
import static org.hringsak.functions.stream.LongStreamUtils.defaultLongStream;

public final class LongTransformUtils {

    private LongTransformUtils() {
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

    public static <U, C extends Collection<U>> LongTransformerCollector<U, C> longTransformAndThen(LongFunction<U> transformer, Collector<U, ?, C> collector) {
        return LongTransformerCollector.of(transformer, collector);
    }

    public static <K, V> Map<K, V> longTransformToMap(long[] longs, LongKeyValueMapper<K, V> keyValueMapper) {
        return defaultLongStream(longs)
                .mapToObj(longPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
