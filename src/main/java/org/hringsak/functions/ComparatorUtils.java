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

    public static <T, R extends Comparable<R>> Comparator<T> comparingNullsFirst(Function<? super T, ? extends R> keyExtractor) {
        return comparing(keyExtractor, nullsFirst(naturalOrder()));
    }

    public static <T, R extends Comparable<R>> Comparator<T> comparingNullsLast(Function<? super T, ? extends R> keyExtractor) {
        return comparing(keyExtractor, nullsLast(naturalOrder()));
    }
}
