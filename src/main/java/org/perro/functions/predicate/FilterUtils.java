package org.perro.functions.predicate;

import org.perro.functions.stream.StreamUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.perro.functions.predicate.PredicateUtils.predicate;
import static org.perro.functions.stream.StreamUtils.defaultStream;

/**
 * Convenience methods for filtering collections of objects, without having to spell out the entire
 * stream-&gt;filter-&gt;collect process.
 */
public final class FilterUtils {

    private FilterUtils() {
    }

    /**
     * Filters a <code>Collection</code> of objects, based a predicate, into a list.
     *
     * @param objects   A collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects Collection.
     * @return A List of objects filtered from the objects Collection.
     */
    public static <T> List<T> filter(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, FilterCollector.of(predicate, toList()));
    }

    /**
     * Filters a <code>Collection</code> of objects, based a predicate, into a set.
     *
     * @param objects   A collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects Collection.
     * @return A Set of objects filtered from the objects Collection.
     */
    public static <T> Set<T> filterToSet(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, FilterCollector.of(predicate, toSet()));
    }

    /**
     * Filters a <code>Collection</code> of objects, based a predicate, into a list of distinct instances (according to
     * <code>Object.equals(Object)</code>).
     *
     * @param objects   A Collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects Collection.
     * @return A List of objects filtered from the objects Collection.
     */
    public static <T> List<T> filterDistinct(Collection<T> objects, Predicate<T> predicate) {
        return StreamUtils.defaultStream(objects)
                .filter(PredicateUtils.predicate(predicate))
                .distinct()
                .collect(toList());
    }

    /**
     * Filters a collection of objects, based a predicate, into a <code>Collection</code>. This overload takes an object
     * that is built using the {@link #filterAndThen(Predicate, Collector)} method, which allows you to specify both a
     * <code>predicate</code> and a <code>Collector</code> to build any type of <code>Collection</code> as a result. For
     * example:
     * <pre>
     *     Collection&lt;Widget&gt; widgets = ...
     *     List&lt;Widget&gt; = FilterUtils.filter(widgets, FilterUtils.filterAndThen(Widget::isConsumerProduct, Collectors.toCollection(LinkedList::new)));
     * </pre>
     * Or, with static imports:
     * <pre>
     *     filter(widgets, filterAndThen(Widget::isConsumerProduct, toCollection(LinkedList::new)));
     * </pre>
     *
     * @param objects         A Collection of objects of type &lt;T&gt; to be filtered.
     * @param filterCollector An object containing a predicate with which to filter the objects Collection, and a
     *                        Collector to accumulate results into a Collection.
     * @param <T>             The target element type for the objects Collection.
     * @param <C>             The type of the resulting Collection.
     * @return A Collection of objects filtered from the objects Collection.
     */
    public static <T, C extends Collection<T>> C filter(Collection<T> objects, FilterCollector<T, C> filterCollector) {
        return StreamUtils.defaultStream(objects)
                .filter(filterCollector.getFilter())
                .collect(filterCollector.getCollector());
    }

    /**
     * Builds an object combining a <code>Predicate</code> and a <code>Collector</code> for use in the {@link
     * #filter(Collection, FilterCollector)} method.
     *
     * @param filter    A Predicate for filtering elements from a Collection of objects. The provided predicate will be
     *                  made null-safe, and if the target is null, then the predicate will return false.
     * @param collector A Collector to accumulating elements into a Collection of any type.
     * @param <T>       The target element type for a collection to be filtered.
     * @param <C>       The type of the resulting Collection.
     * @return A Collection of any type containing objects filtered from an input Collection.
     */
    public static <T, C extends Collection<T>> FilterCollector<T, C> filterAndThen(Predicate<T> filter, Collector<T, ?, C> collector) {
        return FilterCollector.of(filter, collector);
    }
}
