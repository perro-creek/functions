package org.perro.functions.mapper;

import java.util.function.Function;

import static org.perro.functions.mapper.MapperUtils.mapper;

class KeyValueMapper <T, K, V> {

    private final Function<T, K> keyMapper;
    private final Function<T, V> valueMapper;

    private KeyValueMapper(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        this.keyMapper = keyMapper;
        this.valueMapper = valueMapper;
    }

    static <T, K, V> KeyValueMapper<T, K, V> of(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return new KeyValueMapper<>(keyMapper, valueMapper);
    }

    Function<T, K> getKeyMapper() {
        return mapper(keyMapper);
    }

    Function<T, V> getValueMapper() {
        return mapper(valueMapper);
    }
}
