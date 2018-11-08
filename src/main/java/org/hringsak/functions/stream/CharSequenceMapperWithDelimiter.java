package org.hringsak.functions.stream;

import java.util.function.Function;

class CharSequenceMapperWithDelimiter<T> {

    private final Function<T, CharSequence> charSequenceMapper;
    private final CharSequence delimiter;

    private CharSequenceMapperWithDelimiter(Function<T, CharSequence> charSequenceMapper, CharSequence delimiter) {
        this.charSequenceMapper = charSequenceMapper;
        this.delimiter = delimiter;
    }

    public static <T> CharSequenceMapperWithDelimiter<T> of(Function<T, CharSequence> mapper, CharSequence delimiter) {
        return new CharSequenceMapperWithDelimiter<>(mapper, delimiter);
    }

    public Function<T, CharSequence> getCharSequenceMapper() {
        return charSequenceMapper;
    }

    public CharSequence getDelimiter() {
        return delimiter;
    }
}
