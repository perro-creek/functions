package org.hringsak.functions.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.CollectorUtils.toMapFromEntry;
import static org.hringsak.functions.mapper.MapperUtils.mapper;
import static org.hringsak.functions.mapper.MapperUtils.pairOf;
import static org.hringsak.functions.stream.StreamUtils.defaultStream;

public final class TransformUtils {

    private TransformUtils() {
    }

    public static <T, R> List<R> transform(Collection<T> objects, Function<T, R> transformer) {
        return transform(objects, TransformerCollector.of(transformer, toList()));
    }

    public static <T, R> Set<R> transformToSet(Collection<T> collection, Function<T, R> transformer) {
        return transform(collection, TransformerCollector.of(transformer, toSet()));
    }

    public static <T, C extends Collection<T>> C transform(Collection<T> objects, Collector<T, ?, C> collector) {
        return transform(objects, TransformerCollector.of(identity(), collector));
    }

    public static <T, R> List<R> transformDistinct(Collection<T> objects, Function<T, R> transformer) {
        return defaultStream(objects)
                .map(mapper(transformer))
                .distinct()
                .collect(toList());
    }

    public static <T, U, C extends Collection<U>> C transform(Collection<T> objects, TransformerCollector<T, U, C> transformerCollector) {
        return defaultStream(objects)
                .map(transformerCollector.getTransformer())
                .collect(transformerCollector.getCollector());
    }

    public static <T, K, V> Map<K, V> transformToMap(Collection<T> objects, KeyValueMapper<T, K, V> keyValueMapper) {
        return defaultStream(objects)
                .map(pairOf(keyValueMapper))
                .collect(toMapFromEntry());
    }

    public static <T, U, C extends Collection<U>> TransformerCollector<T, U, C> transformAndThen(Function<T, U> transformer, Collector<U, ?, C> collector) {
        return TransformerCollector.of(transformer, collector);
    }
}