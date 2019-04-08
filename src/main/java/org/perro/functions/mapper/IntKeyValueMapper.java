package org.perro.functions.mapper;

import java.util.function.IntFunction;

import static org.perro.functions.mapper.IntMapperUtils.intMapper;

class IntKeyValueMapper<K, V> {

    private final IntFunction<K> keyMapper;
    private final IntFunction<V> valueMapper;

    private IntKeyValueMapper(IntFunction<K> keyMapper, IntFunction<V> valueMapper) {
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    static <K, V> IntKeyValueMapper<K, V> of(IntFunction<K> keyMapper, IntFunction<V> valueMapper) {
        return new IntKeyValueMapper<>(keyMapper, valueMapper);
    }

    IntFunction<K> getKeyMapper() {
        return intMapper(keyMapper);
    }

    IntFunction<V> getValueMapper() {
        return intMapper(valueMapper);
    }
}
