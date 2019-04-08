package org.perro.functions.mapper;

import java.util.function.IntFunction;

class IntTernaryMapper<R> {

    private final IntFunction<R> trueMapper;
    private final IntFunction<R> falseMapper;

    private IntTernaryMapper(IntFunction<R> trueMapper, IntFunction<R> falseMapper) {
        this.trueMapper = trueMapper;
        this.falseMapper = falseMapper;
    }

    static <R> IntTernaryMapper<R> of(IntFunction<R> trueMapper, IntFunction<R> falseMapper) {
        return new IntTernaryMapper<>(trueMapper, falseMapper);
    }

    IntFunction<R> getTrueMapper() {
        return trueMapper;
    }

    IntFunction<R> getFalseMapper() {
        return falseMapper;
    }
}
