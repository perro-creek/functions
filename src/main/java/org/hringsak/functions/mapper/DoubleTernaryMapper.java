package org.hringsak.functions.mapper;

import java.util.function.DoubleFunction;

class DoubleTernaryMapper<R> {

    private final DoubleFunction<R> trueMapper;
    private final DoubleFunction<R> falseMapper;

    private DoubleTernaryMapper(DoubleFunction<R> trueMapper, DoubleFunction<R> falseMapper) {
        this.trueMapper = trueMapper;
        this.falseMapper = falseMapper;
    }

    public static <R> DoubleTernaryMapper<R> of(DoubleFunction<R> trueMapper, DoubleFunction<R> falseMapper) {
        return new DoubleTernaryMapper<>(trueMapper, falseMapper);
    }

    DoubleFunction<R> getTrueMapper() {
        return trueMapper;
    }

    DoubleFunction<R> getFalseMapper() {
        return falseMapper;
    }
}
