package org.hringsak.functions.mapper;

import java.util.function.LongFunction;

import static org.hringsak.functions.mapper.LongMapperUtils.longMapper;

class LongKeyValueMapper<K, V> {

    private final LongFunction<K> keyMapper;
    private final LongFunction<V> valueMapper;

    private LongKeyValueMapper(LongFunction<K> keyMapper, LongFunction<V> valueMapper) {
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    static <K, V> LongKeyValueMapper<K, V> of(LongFunction<K> keyMapper, LongFunction<V> valueMapper) {
        return new LongKeyValueMapper<>(keyMapper, valueMapper);
    }

    LongFunction<K> getKeyMapper() {
        return longMapper(keyMapper);
    }

    LongFunction<V> getValueMapper() {
        return longMapper(valueMapper);
    }
}
