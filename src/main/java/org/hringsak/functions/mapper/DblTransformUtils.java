package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.DblMapperUtils.dblFlatMapper;
import static org.hringsak.functions.mapper.DblMapperUtils.dblMapper;
import static org.hringsak.functions.mapper.DblMapperUtils.dblPairOf;
import static org.hringsak.functions.mapper.DblMapperUtils.flatMapperToDbl;
import static org.hringsak.functions.mapper.MapperUtils.flatMapper;
import static org.hringsak.functions.stream.DblStreamUtils.defaultDblStream;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

public final class DblTransformUtils {

    private DblTransformUtils() {
    }

    public static double[] dblUnaryTransform(double[] doubles, DoubleUnaryOperator operator) {
        return defaultDblStream(doubles)
                .map(operator)
                .toArray();
    }

    public static double[] dblUnaryTransformDistinct(double[] doubles, DoubleUnaryOperator operator) {
        return defaultDblStream(doubles)
                .map(operator)
                .distinct()
                .toArray();
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

    public static double[] dblFlatMap(double[] doubles, DoubleFunction<double[]> function) {
        return defaultDblStream(doubles)
                .flatMap(dblFlatMapper(function))
                .toArray();
    }

    public static double[] dblFlatMapDistinct(double[] doubles, DoubleFunction<double[]> function) {
        return defaultDblStream(doubles)
                .flatMap(dblFlatMapper(function))
                .distinct()
                .toArray();
    }

    public static <T> double[] flatMapToDbl(Collection<T> objects, Function<T, double[]> function) {
        return defaultStream(objects)
                .flatMapToDouble(flatMapperToDbl(function))
                .toArray();
    }

    public static <T> double[] flatMapToDblDistinct(Collection<T> objects, Function<T, double[]> function) {
        return defaultStream(objects)
                .flatMapToDouble(flatMapperToDbl(function))
                .distinct()
                .toArray();
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
