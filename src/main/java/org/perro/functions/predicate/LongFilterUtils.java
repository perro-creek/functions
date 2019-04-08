package org.perro.functions.predicate;

import java.util.Collection;
import java.util.Set;
import java.util.function.LongPredicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toSet;
import static org.perro.functions.stream.LongStreamUtils.defaultLongStream;

/**
 * Convenience methods for filtering arrays of <code>long</code> primitives, without having to spell out the entire
 * stream-&gt;filter-&gt;collect process.
 */
public final class LongFilterUtils {

    private LongFilterUtils() {
    }

    /**
     * Filters an array of <code>long</code> primitives, based a predicate, into another <code>long</code> array.
     *
     * @param longs      An array of longs to be filtered.
     * @param predicate A predicate with which to filter the longs array.
     * @return An array of longs filtered from an array of long.
     */
    public static long[] longFilter(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .toArray();
    }

    /**
     * Filters an array of <code>long</code> primitives, based a predicate, into a <code>Set</code>.
     *
     * @param longs      An array of longs to be filtered.
     * @param predicate A predicate with which to filter the longs array.
     * @return A Set of Long objects filtered from an array of long.
     */
    public static Set<Long> longFilterToSet(long[] longs, LongPredicate predicate) {
        return longFilter(longs, LongFilterCollector.of(predicate, toSet()));
    }

    /**
     * Filters an array of longs, based a predicate, into an array of distinct <code>long</code> values (according
     * to <code>Long.compare(long, long)</code>).
     *
     * @param longs   An array of longs to be filtered.
     * @param predicate A predicate with which to filter the longs array.
     * @return An array of distinct long values filtered, using the passed predicate, from the input array.
     */
    public static long[] longFilterDistinct(long[] longs, LongPredicate predicate) {
        return defaultLongStream(longs)
                .filter(predicate)
                .distinct()
                .toArray();
    }

    /**
     * Filters an array of <code>long</code> primitives, based a predicate, into a Collection. This overload takes an
     * object that is built using the {@link #longFilterAndThen(LongPredicate, Collector)} method, which allows you to
     * specify both a <code>predicate</code> and a <code>Collector</code> to build any type of <code>Collection</code>
     * as a result. For example:
     * <pre>
     *     long[] longs = ...
     *     List&lt;Long&gt; = LongFilterUtils.longFilter(longs, FilterUtils.longFilterAndThen(LongPredicateUtils.longGt(1.0D), Collectors.toCollection(LinkedList::new)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     longFilter(longs, longFilterAndThen(longGt(1.0D), toCollection(LinkedList::new)));
     * </pre>
     *
     * @param longs            An array of longs to be filtered.
     * @param filterCollector An object containing a predicate with which to filter the longs array, and a Collector to
     *                        accumulate results into a Collection.
     * @param <C>             The type of the resulting Collection.
     * @return A Collection of Long objects filtered from an array of long.
     */
    public static <C extends Collection<Long>> C longFilter(long[] longs, LongFilterCollector<C> filterCollector) {
        return defaultLongStream(longs)
                .filter(filterCollector.getFilter())
                .boxed()
                .collect(filterCollector.getCollector());
    }

    /**
     * Filters an array of longs, based a predicate, into an array of distinct <code>long</code> values. This overload
     * takes an object that is built using the {@link #longFilterAndThen(LongPredicate, Collector)} method, which allows
     * you to specify both a <code>LongPredicate</code> and a <code>Collector</code> to build any type of
     * <code>Collection</code> as a result. For example:
     * <pre>
     *     long[] longs = ...
     *     List&lt;Long&gt; = LongFilterUtils.longFilter(longs, LongFilterUtils.longFilterAndThen(LongPredicateUtils.longGt(1L), Collectors.toCollection(LinkedList::new)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     List&lt;Long&gt; = longFilter(longs, longFilterAndThen(longGt(1L), toCollection(LinkedList::new)));
     * </pre>
     *
     * @param longs An array of longs to be filtered.
     * @param filterCollector An object containing a predicate with which to filter the longs array, and a Collector to
     *                        accumulate results into a Collection.
     * @param <C>             The type of the resulting Collection.
     * @return An array of distinct long values filtered, using a predicate from the passed filterCollector, from the
     * input array.
     */
    public static <C extends Collection<Long>> C longFilterDistinct(long[] longs, LongFilterCollector<C> filterCollector) {
        return defaultLongStream(longs)
                .filter(filterCollector.getFilter())
                .distinct()
                .boxed()
                .collect(filterCollector.getCollector());
    }

    /**
     * Builds an object combining a <code>LongPredicate</code> and a <code>Collector</code> for use in the {@link
     * #longFilter(long[], LongFilterCollector)} method.
     *
     * @param filter    A Predicate for filtering elements from an array of longs.
     * @param collector A <code>Collector</code> to accumulate elements into a Collection of Long objects.
     * @param <C>       The type of the resulting collection.
     * @return A Collection of Long containing objects filtered from an array of long.
     */
    public static <C extends Collection<Long>> LongFilterCollector<C> longFilterAndThen(LongPredicate filter, Collector<Long, ?, C> collector) {
        return LongFilterCollector.of(filter, collector);
    }
}
