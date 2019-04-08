package org.perro.functions.stream;

import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.joining;

class CharSequenceMapperWithCollector<T> {

    private final Function<T, CharSequence> charSequenceMapper;
    private final Collector<CharSequence, ?, String> joiner;

    private CharSequenceMapperWithCollector(Function<T, CharSequence> charSequenceMapper, Collector<CharSequence, ?, String> joiner) {
        this.charSequenceMapper = charSequenceMapper;
        this.joiner = joiner;
    }

    public static <T> CharSequenceMapperWithCollector<T> of(Function<T, CharSequence> mapper, Collector<CharSequence, ?, String> joiner) {
        return new CharSequenceMapperWithCollector<>(mapper, joiner);
    }

    static <T> CharSequenceMapperWithCollector<T> of(CharSequenceMapperWithDelimiter<T> mapperWithDelimiter) {
        return of(mapperWithDelimiter.getCharSequenceMapper(), joining(mapperWithDelimiter.getDelimiter()));
    }

    public Function<T, CharSequence> getCharSequenceMapper() {
        return charSequenceMapper;
    }

    public Collector<CharSequence, ?, String> getJoiner() {
        return joiner;
    }
}
