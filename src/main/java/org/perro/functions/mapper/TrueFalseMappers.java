package org.perro.functions.mapper;

import java.util.function.Function;

class TrueFalseMappers<T, R> {

    private final Function<T, R> trueMapper;
    private final Function<T, R> falseMapper;

    private TrueFalseMappers(Function<T, R> trueMapper, Function<T, R> falseMapper) {
        this.trueMapper = trueMapper;
        this.falseMapper = falseMapper;
    }

    static <T, R> TrueFalseMappers<T, R> of(Function<T, R> trueMapper, Function<T, R> falseMapper) {
        return new TrueFalseMappers<>(trueMapper, falseMapper);
    }

    Function<T, R> getTrueMapper() {
        return trueMapper;
    }

    Function<T, R> getFalseMapper() {
        return falseMapper;
    }
}
