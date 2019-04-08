package org.perro.functions.mapper;

import java.util.function.DoubleFunction;

import static org.perro.functions.mapper.DblMapperUtils.dblMapper;

class DblKeyValueMapper<K, V> {

    private final DoubleFunction<K> keyMapper;
    private final DoubleFunction<V> valueMapper;

    private DblKeyValueMapper(DoubleFunction<K> keyMapper, DoubleFunction<V> valueMapper) {
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    static <K, V> DblKeyValueMapper<K, V> of(DoubleFunction<K> keyMapper, DoubleFunction<V> valueMapper) {
        return new DblKeyValueMapper<>(keyMapper, valueMapper);
    }

    DoubleFunction<K> getKeyMapper() {
        return DblMapperUtils.dblMapper(keyMapper);
    }

    DoubleFunction<V> getValueMapper() {
        return DblMapperUtils.dblMapper(valueMapper);
    }
}
