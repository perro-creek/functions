package org.hringsak.functions.mapper;

import java.util.function.LongFunction;

class LongTernaryMapper<R> {

    private final LongFunction<R> trueMapper;
    private final LongFunction<R> falseMapper;

    private LongTernaryMapper(LongFunction<R> trueMapper, LongFunction<R> falseMapper) {
        this.trueMapper = trueMapper;
        this.falseMapper = falseMapper;
    }

    static <R> LongTernaryMapper<R> of(LongFunction<R> trueMapper, LongFunction<R> falseMapper) {
        return new LongTernaryMapper<>(trueMapper, falseMapper);
    }

    LongFunction<R> getTrueMapper() {
        return trueMapper;
    }

    LongFunction<R> getFalseMapper() {
        return falseMapper;
    }
}
