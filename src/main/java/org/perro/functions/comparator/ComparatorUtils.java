package org.perro.functions.comparator;

import org.perro.functions.mapper.MapperUtils;

import java.util.Comparator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

/**
 * Methods that build comparators useful in many different situations, particularly in Java streams.
 */
public final class ComparatorUtils {

    private ComparatorUtils() {
    }

    /**
     * Builds a comparator that can be used to compare and sort instances of any type that extends
     * <code>Comparable</code>. The comparator sorts in a way that null values come <i>before</i> all non-null ones.
     *
     * @param <T> The type of the elements to be compared, extending the Comparable interface.
     * @return A comparator of instances of type &lt;T&gt;.
     */
    public static <T extends Comparable<T>> Comparator<T> comparingNullsFirst() {
        return nullsFirst(naturalOrder());
    }

    /**
     * Builds a comparator that can be used to compare and sort instances of any type, provided that the
     * <code>keyExtractor</code> function retrieves an instance of <code>Comparable</code> from them. The comparator
     * sorts in a way that null values come <i>before</i> all non-null ones. Note that, because this method takes a
     * function as its first parameter, that any of the function building methods from {@link MapperUtils} are fair game
     * here.
     *
     * @param keyExtractor An extractor function that retrieves a key value from a target element, the value being an
     *                     instance of Comparable, which will be compared based on its natural order, with nulls being
     *                     evaluated as less than non-nulls.
     * @param <T>          The type of the elements to be compared.
     * @param <R>          The type of the key value to be extracted, which must extend the Comparable interface.
     * @return A comparator of instances of type &lt;T&gt;.
     */
    public static <T, R extends Comparable<R>> Comparator<T> comparingNullsFirst(Function<? super T, ? extends R> keyExtractor) {
        return comparing(keyExtractor, nullsFirst(naturalOrder()));
    }

    /**
     * Builds a comparator that can be used to compare and sort instances of any type that extends
     * <code>Comparable</code>. The comparator sorts in a way that null values come <i>after</i> all non-null ones.
     *
     * @param <T> The type of the elements to be compared, extending the Comparable interface.
     * @return A comparator of instances of type &lt;T&gt;.
     */
    public static <T extends Comparable<T>> Comparator<T> comparingNullsLast() {
        return nullsLast(naturalOrder());
    }

    /**
     * Builds a comparator that can be used to compare and sort instances of any type, provided that the
     * <code>keyExtractor</code> function retrieves an instance of <code>Comparable</code> from them. The comparator
     * sorts in a way that null values come <i>after</i> all non-null ones. Note that, because this method takes a
     * function as its first parameter, that any of the function building methods from {@link MapperUtils} are fair game
     * here.
     *
     * @param keyExtractor An extractor function that retrieves a key value from a target element, the value being an
     *                     instance of Comparable, which will be compared based on its natural order, with nulls being
     *                     evaluated as greater than non-nulls.
     * @param <T>          The type of the elements to be compared.
     * @param <R>          The type of the key value to be extracted, which must extend the Comparable interface.
     * @return A comparator of instances of type &lt;T&gt;.
     */
    public static <T, R extends Comparable<R>> Comparator<T> comparingNullsLast(Function<? super T, ? extends R> keyExtractor) {
        return comparing(keyExtractor, nullsLast(naturalOrder()));
    }
}
