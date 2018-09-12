package org.hringsak.functions.predicate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.predicate.DblPredicateUtils.dblPredicate;
import static org.hringsak.functions.stream.DblStreamUtils.defaultDblStream;

/**
 * Convenience methods for filtering arrays of <code>double</code> primitives, without having to spell out the entire
 * stream-&gt;filter-&gt;collect process.
 */
public final class DblFilterUtils {

    private DblFilterUtils() {
    }

    /**
     * Filters an array of doubles, based a predicate, into another double array.
     *
     * @param doubles   An array of doubles to be filtered.
     * @param predicate A predicate with which to filter the doubles array.
     * @return An array of doubles filtered from an array of double.
     */
    public static double[] dblFilter(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(predicate)
                .toArray();
    }

    /**
     * Filters an array of doubles, based a predicate, into a set.
     *
     * @param doubles   An array of doubles to be filtered.
     * @param predicate A predicate with which to filter the doubles array.
     * @return A Set of Double objects filtered from an array of double.
     */
    public static Set<Double> dblFilterToSet(double[] doubles, DoublePredicate predicate) {
        return dblFilter(doubles, DoubleFilterCollector.of(predicate, toSet()));
    }

    /**
     * Filters an array of doubles, based a predicate, into a list of distinct Double instances (according to
     * <code>Double.compare(double, double)</code>).
     *
     * @param doubles   An array of doubles to be filtered.
     * @param predicate A predicate with which to filter the doubles array.
     * @return A List of Double objects filtered from an array of double.
     */
    public static List<Double> dblFilterDistinct(double[] doubles, DoublePredicate predicate) {
        return defaultDblStream(doubles)
                .filter(dblPredicate(predicate))
                .distinct()
                .boxed()
                .collect(toList());
    }

    /**
     * Filters an array of doubles, based a predicate, into a collection. This overload takes an object that is built
     * using the {@link #dblFilterAndThen(DoublePredicate, Collector)} method, which allows you to specify both a
     * <code>predicate</code> and a <code>Collector</code> to build any type of <code>Collection</code> as a result. For
     * example:
     * <pre>
     *     double[] doubles = ...
     *     List&lt;Double&gt; = DblFilterUtils.dblFilter(doubles, FilterUtils.dblFilterAndThen(DblPredicateUtils.dblGt(1.0D), Collectors.toCollection(LinkedList::new)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     dblFilter(doubles, dblFilterAndThen(dblGt(1.0D), toCollection(LinkedList::new)));
     * </pre>
     *
     * @param doubles         An array of doubles to be filtered.
     * @param filterCollector An object containing a predicate with which to filter the doubles array, and a Collector
     *                        to accumulate results into a Collection.
     * @param <C>             The type of the resulting Collection.
     * @return A Collection of Double objects filtered from an array of double.
     */
    public static <C extends Collection<Double>> C dblFilter(double[] doubles, DoubleFilterCollector<C> filterCollector) {
        return defaultDblStream(doubles)
                .filter(filterCollector.getFilter())
                .boxed()
                .collect(filterCollector.getCollector());
    }

    /**
     * Builds an object combining a <code>DoublePredicate</code> and a <code>Collector</code> for use in the {@link
     * #dblFilter(double[], DoubleFilterCollector)} method.
     *
     * @param filter    A Predicate for filtering elements from an array of doubles.
     * @param collector A <code>Collector</code> to accumulate elements into a Collection of Double objects.
     * @param <C>       The type of the resulting collection.
     * @return A Collection of Double containing objects filtered from an array of double.
     */
    public static <C extends Collection<Double>> DoubleFilterCollector<C> dblFilterAndThen(DoublePredicate filter, Collector<Double, ?, C> collector) {
        return DoubleFilterCollector.of(filter, collector);
    }
}
