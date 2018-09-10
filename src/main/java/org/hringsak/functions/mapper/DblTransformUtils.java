package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.DblMapperUtils.dblMapper;
import static org.hringsak.functions.mapper.DblMapperUtils.dblPairOf;
import static org.hringsak.functions.stream.DblStreamUtils.defaultDblStream;

public final class DblTransformUtils {

    private DblTransformUtils() {
    }

    public static <R> List<R> dblTransform(double[] doubles, DoubleFunction<R> transformer) {
        return dblTransform(doubles, DblTransformerCollector.of(transformer, toList()));
    }

    public static <R> Set<R> dblTransformToSet(double[] doubles, DoubleFunction<R> transformer) {
        return dblTransform(doubles, DblTransformerCollector.of(transformer, toSet()));
    }

    public static <C extends Collection<Double>> C dblTransform(double[] doubles, Collector<Double, ?, C> collector) {
        return dblTransform(doubles, DblTransformerCollector.of(Double::valueOf, collector));
    }

    public static <R> List<R> dblTransformDistinct(double[] doubles, DoubleFunction<R> transformer) {
        return defaultDblStream(doubles)
                .mapToObj(dblMapper(transformer))
                .distinct()
                .collect(toList());
    }

    public static <U, C extends Collection<U>> C dblTransform(double[] doubles, DblTransformerCollector<U, C> transformerCollector) {
        return defaultDblStream(doubles)
                .mapToObj(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    public static <U, C extends Collection<U>> DblTransformerCollector<U, C> dblTransformAndThen(DoubleFunction<U> transformer, Collector<U, ?, C> collector) {
        return DblTransformerCollector.of(transformer, collector);
    }

    public static <K, V> Map<K, V> dblTransformToMap(double[] doubles, DblKeyValueMapper<K, V> keyValueMapper) {
        return defaultDblStream(doubles)
                .mapToObj(dblPairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }
}
