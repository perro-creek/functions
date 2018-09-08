package org.hringsak.functions.mapper;

import java.util.function.Function;

class TernaryMapper <T, R> {

    private final Function<T, R> trueMapper;
    private final Function<T, R> falseMapper;

    private TernaryMapper(Function<T, R> trueMapper, Function<T, R> falseMapper) {
        this.trueMapper = trueMapper;
        this.falseMapper = falseMapper;
    }

    static <T, R> TernaryMapper<T, R> of(Function<T, R> trueMapper, Function<T, R> falseMapper) {
        return new TernaryMapper<>(trueMapper, falseMapper);
    }

    Function<T, R> getTrueMapper() {
        return trueMapper;
    }

    Function<T, R> getFalseMapper() {
        return falseMapper;
    }
}
