package org.hringsak.functions;

import java.util.Comparator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

public final class ComparatorUtils {

    private ComparatorUtils() {
    }

    public static <T, U extends Comparable<U>> Comparator<T> comparingNullsFirst(Function<T, U> keyExtractor) {
        return comparing(keyExtractor, nullsFirst(naturalOrder()));
    }

    public static <T, U extends Comparable<U>> Comparator<T> comparingNullsLast(Function<T, U> keyExtractor) {
        return comparing(keyExtractor, nullsLast(naturalOrder()));
    }

}
