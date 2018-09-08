package org.hringsak.functions.objects;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.objects.PredicateUtils.predicate;
import static org.hringsak.functions.objects.StreamUtils.defaultStream;

/**
 * Convenience methods for filtering collections of objects, without having to spell out the entire
 * stream-&gt;filter-&gt;collect process.
 */
public final class FilterUtils {

    private FilterUtils() {
    }

    /**
     * Filters a collection of objects, based a predicate, into a list.
     *
     * @param objects   A collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects collection.
     * @return A list of objects filtered from the objects collection.
     */
    public static <T> List<T> filter(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, FilterCollector.of(predicate, toList()));
    }

    /**
     * Filters a collection of objects, based a predicate, into a set.
     *
     * @param objects   A collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects collection.
     * @return A set of objects filtered from the objects collection.
     */
    public static <T> Set<T> filterToSet(Collection<T> objects, Predicate<T> predicate) {
        return filter(objects, FilterCollector.of(predicate, toSet()));
    }

    /**
     * Filters a collection of objects, based a predicate, into a list of distinct instances (according to
     * <code>Object.equals(Object)</code>).
     *
     * @param objects   A collection of objects of type &lt;T&gt; to be filtered.
     * @param predicate A predicate with which to filter the objects collection. The provided predicate will be made
     *                  null-safe, and if the target is null, then the predicate will return false.
     * @param <T>       The target element type for the objects collection.
     * @return A list of objects filtered from the objects collection.
     */
    public static <T> List<T> filterDistinct(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .filter(predicate(predicate))
                .distinct()
                .collect(toList());
    }

    /**
     * Filters a collection of objects, based a predicate, into a list. This overload takes an object that is built
     * using the {@link #filterAndThen(Predicate, Collector)} method, which allows you to specify both a <code>predicate
     * </code> and a <code>Collector</code> to build any type of <code>Collection</code> as a result. For example:
     * <pre>
     *     Collection&lt;Widget&gt; widgets = ...
     *     List&lt;Widget&gt; = FilterUtils.filter(widgets, FilterUtils.filterAndThen(Widget::isConsumerProduct, Collectors.toCollection(LinkedList::new)));
     * </pre>
     * By making judicious use of static imports, we can reduce the clutter in the <code>filter(...)</code> invocation
     * to:
     * <pre>
     *     filter(widgets, filterAndThen(Widget::isConsumerProduct, toCollection(LinkedList::new)));
     * </pre>
     *
     * @param objects         A collection of objects of type &lt;T&gt; to be filtered.
     * @param filterCollector A predicate with which to filter the objects collection.
     * @param <T>             The target element type for the objects collection.
     * @param <C>             The type of the resulting collection.
     * @return A list of objects filtered from the objects collection.
     */
    public static <T, C extends Collection<T>> C filter(Collection<T> objects, FilterCollector<T, C> filterCollector) {
        return defaultStream(objects)
                .filter(filterCollector.getFilter())
                .collect(filterCollector.getCollector());
    }

    /**
     * Builds an object combining a <code>Predicate</code> and a <code>Collector</code> for use in the {@link
     * #filter(Collection, FilterCollector)} method.
     *
     * @param filter    A Predicate for filtering elements from a collection of objects. The provided predicate will be
     *                  made null-safe, and if the target is null, then the predicate will return false.
     * @param collector A <code>Collector</code> to accumulating elements into a <code>Collection</code> of any type.
     * @param <T>       The target element type for a collection to be filtered.
     * @param <C>       The type of the resulting collection.
     * @return A collection of any type containing objects filtered from an input collection.
     */
    public static <T, C extends Collection<T>> FilterCollector<T, C> filterAndThen(Predicate<T> filter, Collector<T, ?, C> collector) {
        return FilterCollector.of(filter, collector);
    }
}
