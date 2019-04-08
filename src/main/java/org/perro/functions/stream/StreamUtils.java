package org.perro.functions.stream;

import org.perro.functions.collector.CollectorUtils;
import org.perro.functions.mapper.ObjectIndexPair;
import org.perro.functions.mapper.MapperUtils;
import org.perro.functions.predicate.PredicateUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.Comparator.naturalOrder;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toSet;
import static org.perro.functions.mapper.MapperUtils.pairWithIndex;

/**
 * Methods that are shortcuts to creating streams, specifically methods involving object instances.
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * Given a <code>Collection</code> of objects of type &lt;T&gt; and a <code>Predicate</code>, returns a
     * <code>boolean</code> value indicating whether <i>all</i> of the values in the collection match the predicate.
     *
     * @param objects   A Collection of objects of type &lt;T&gt;, to be checked whether the given Predicate matches all
     *                  of its elements.
     * @param predicate A Predicate to match against the elements in the input Collection.
     * @param <T>       The type of elements in the objects Collection. Extends Comparable.
     * @return A boolean indication of whether all of the elements in a given Collection match a given predicate.
     */
    public static <T> boolean allMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().allMatch(predicate);
    }

    /**
     * Given a <code>Collection</code> of objects of type &lt;T&gt; and a <code>Predicate</code>, returns a
     * <code>boolean</code> value indicating whether <i>any</i> of the values in the collection match the predicate.
     *
     * @param objects   A Collection of objects of type &lt;T&gt;, to be checked whether the given Predicate matches any
     *                  of its elements.
     * @param predicate A Predicate to match against the elements in the input Collection.
     * @param <T>       The type of elements in the objects Collection. Extends Comparable.
     * @return A boolean indication of whether any of the elements in a given Collection match a given predicate.
     */
    public static <T> boolean anyMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().anyMatch(predicate);
    }

    /**
     * Given a <code>Collection</code> of objects of type &lt;T&gt; and a <code>Predicate</code>, returns a
     * <code>boolean</code> value indicating whether <i>none</i> of the values in the collection match the predicate.
     *
     * @param objects   A Collection of objects of type &lt;T&gt;, to be checked whether the given Predicate matches
     *                  none of its elements.
     * @param predicate A Predicate to match against the elements in the input Collection.
     * @param <T>       The type of elements in the objects Collection. Extends Comparable.
     * @return A boolean indication of whether none of the elements in a given Collection match a given predicate.
     */
    public static <T> boolean noneMatch(Collection<T> objects, Predicate<T> predicate) {
        return objects != null && objects.stream().noneMatch(predicate);
    }

    /**
     * Given a <code>Collection</code> of objects of type &lt;T&gt;, and a <code>Predicate</code>, returns a
     * <code>long</code> value indicating the number of elements in the collection that match the predicate.
     *
     * @param objects   A Collection of objects of type &lt;T&gt;, to be counted for the number of them that match the
     *                  given Predicate.
     * @param predicate A Predicate to match against the values in the input array.
     * @param <T>       The type of elements in the objects Collection.
     * @return A long value indicating the number of elements in a given Collection that match a given predicate.
     */
    public static <T> long count(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(predicate)
                .count();
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt; and a <code>Predicate</code>, this method returns
     * the maximum element in the collection that matches the predicate, returning <code>null</code> if none are found.
     * Note that &lt;T&gt; must extend <code>Comparable</code>.
     *
     * @param objects   A Collection of elements of type &lt;T&gt; from which to get the maximum element.
     * @param predicate A Predicate to match against the elements in the input Collection.
     * @param <T>       The type of elements in the objects Collection. Extends Comparable.
     * @return The maximum element of type &lt;T&gt; in the input Collection that matches the given predicate, or null
     * if the Collection is null or empty, or if no elements in it match the predicate.
     */
    public static <T extends Comparable<T>> T maxDefaultNull(Collection<T> objects, Predicate<T> predicate) {
        return maxDefault(objects, findWithDefault(predicate));
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a
     * <code>Predicate</code> along with a default value of type &lt;T&gt;, this method returns the maximum element in
     * the Collection that matches the predicate, or the default value if no matching element is found. Note that
     * &lt;T&gt; must extend <code>Comparable</code>.
     *
     * @param objects         A Collection of elements of type &lt;T&gt; from which to get the maximum element or the
     *                        default value in the given findWithDefault object.
     * @param findWithDefault An object representing a Predicate along with a default value of type &lt;T&gt;.
     * @param <T>             The type of elements in the objects Collection. Extends Comparable.
     * @return The maximum element of type &lt;T&gt; in the input Collection that matches the given predicate, or the
     * default value if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T extends Comparable<T>> T maxDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .max(naturalOrder())
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a
     * <code>Predicate</code> along with a <code>Supplier</code> for a default value, this method returns the maximum
     * element in the Collection that matches the predicate, or a default value from the supplier if no matching element
     * is found.
     *
     * @param objects                 A Collection of elements of type &lt;T&gt; from which to get the maximum element
     *                                or the default value from the supplier in the given findWithDefaultSupplier
     *                                object.
     * @param findWithDefaultSupplier An object representing a Predicate along with a Supplier of a default value.
     * @param <T>                     The type of elements in the objects Collection. Extends Comparable.
     * @return The maximum element of type &lt;T&gt; in the input Collection that matches the predicate, or the default
     * value from the supplier if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T extends Comparable<T>> T maxDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefaultSupplier.getPredicate())
                .max(naturalOrder())
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt; and a <code>Predicate</code>, this method returns
     * the minimum element in the collection that matches the predicate, returning <code>null</code> if none are found.
     * Note that &lt;T&gt; must extend <code>Comparable</code>.
     *
     * @param objects   A Collection of elements of type &lt;T&gt; from which to get the minimum element.
     * @param predicate A Predicate to match against the elements in the input Collection.
     * @param <T>       The type of elements in the objects Collection. Extends Comparable.
     * @return The minimum element of type &lt;T&gt; in the input Collection that matches the given predicate, or null
     * if the Collection is null or empty, or if no elements in it match the predicate.
     */
    public static <T extends Comparable<T>> T minDefaultNull(Collection<T> objects, Predicate<T> predicate) {
        return minDefault(objects, findWithDefault(predicate));
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a
     * <code>Predicate</code> along with a default value of type &lt;T&gt;, this method returns the minimum element in
     * the Collection that matches the predicate, or the default value if no matching element is found. Note that
     * &lt;T&gt; must extend <code>Comparable</code>.
     *
     * @param objects         A Collection of elements of type &lt;T&gt; from which to get the minimum element or the
     *                        default value in the given findWithDefault object.
     * @param findWithDefault An object representing a Predicate along with a default value of type &lt;T&gt;.
     * @param <T>             The type of elements in the objects Collection. Extends Comparable.
     * @return The minimum element of type &lt;T&gt; in the input Collection that matches the given predicate, or the
     * default value if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T extends Comparable<T>> T minDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .min(naturalOrder())
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a
     * <code>Predicate</code> along with a <code>Supplier</code> for a default value, this method returns the minimum
     * element in the Collection that matches the predicate, or a default value from the supplier if no matching element
     * is found.
     *
     * @param objects                 A Collection of elements of type &lt;T&gt; from which to get the minimum element
     *                                or the default value from the supplier in the given findWithDefaultSupplier
     *                                object.
     * @param findWithDefaultSupplier An object representing a Predicate along with a Supplier of a default value.
     * @param <T>                     The type of elements in the objects Collection. Extends Comparable.
     * @return The minimum element of type &lt;T&gt; in the input Collection that matches the predicate, or the default
     * value from the supplier if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T extends Comparable<T>> T minDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefaultSupplier.getPredicate())
                .min(naturalOrder())
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find any matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning <code>null</code> if one is not found. Here is a contrived example of how this method would
     * be called - assume <code>WidgetType</code> is an enum representing the type of <code>Widget</code>:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return StreamUtils.findAnyDefaultNull(widgets, PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return findAnyDefaultNull(widgets, isEqual(WidgetType.PREMIUM, Widget::getType));
     *     }
     * </pre>
     *
     * @param objects   A Collection of elements of type &lt;T&gt;.
     * @param predicate A predicate for finding an element of type &lt;T&gt;.
     * @param <T>       The type of elements in the objects Collection.
     * @return An element of type &lt;T&gt; if one is found, otherwise null if the Collection is null or empty, or if no
     * values in it match the predicate.
     */
    public static <T> T findAnyDefaultNull(Collection<T> objects, Predicate<T> predicate) {
        return findAnyWithDefault(objects, findWithDefault(predicate));
    }

    /**
     * Attempt to find any matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning a default element if one is not found. Here is a contrived example of how this method would
     * be called:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         Widget defaultWidget = ...
     *         return StreamUtils.findAnyWithDefault(widgets, StreamUtils.findWithDefault(PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType), defaultWidget));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         Widget defaultWidget = ...
     *         return findAnyWithDefault(widgets, findWithDefault(isEqual(WidgetType.PREMIUM, Widget::getType), defaultWidget));
     *     }
     * </pre>
     *
     * @param objects         A Collection of elements of type &lt;T&gt;.
     * @param findWithDefault An object representing a predicate for finding a value, and a default element one is not
     *                        found. Use the {@link #findWithDefault(Predicate, Supplier)} to provide this parameter.
     * @param <T>             The type of elements in the objects Collection.
     * @return An element of type &lt;T&gt; if one is found, otherwise the default from findWithDefault if the
     * Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .findAny()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find any matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning a default value retrieved using a <code>Supplier</code> if one is not found. Here is a
     * contrived example of how this method would be called:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return StreamUtils.findAnyWithDefault(widgets, StreamUtils.findWithDefault(PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType), Widget::createDefault));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return findAnyWithDefault(widgets, findWithDefault(isEqual(WidgetType.PREMIUM, Widget::getType), Widget::createDefault));
     *     }
     * </pre>
     *
     * @param objects                 A Collection of elements of type &lt;T&gt;.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default Supplier if
     *                                one is not found. Use the {@link #findWithDefault(Predicate, Supplier)} method to
     *                                provide this parameter.
     * @param <T>                     The type of elements in the objects Collection.
     * @return An element of type &lt;T&gt; if one is found, otherwise a default from the Supplier in
     * findWithDefaultSupplier if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T> T findAnyWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefaultSupplier.getPredicate())
                .findAny()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    /**
     * Attempt to find the first matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning <code>null</code> if one is not found. Here is a contrived example of how this method would
     * be called - assume <code>WidgetType</code> is an enum representing the type of <code>Widget</code>:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return StreamUtils.findFirstDefaultNull(widgets, PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return findFirstDefaultNull(widgets, isEqual(WidgetType.PREMIUM, Widget::getType));
     *     }
     * </pre>
     *
     * @param objects   A Collection of elements of type &lt;T&gt;.
     * @param predicate A predicate for finding an element of type &lt;T&gt;.
     * @param <T>       The type of elements in the objects Collection.
     * @return The first matching element of type &lt;T&gt; if one is found, otherwise null if the Collection is null or
     * empty, or if no values in it match the predicate.
     */
    public static <T> T findFirstDefaultNull(Collection<T> objects, Predicate<T> predicate) {
        return findFirstWithDefault(objects, findWithDefault(predicate));
    }

    /**
     * Attempt to find the first matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning a default element if one is not found. Here is a contrived example of how this method would
     * be called:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         Widget defaultWidget = ...
     *         return StreamUtils.findFirstWithDefault(widgets, StreamUtils.findWithDefault(PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType), defaultWidget));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         Widget defaultWidget = ...
     *         return findFirstWithDefault(widgets, findWithDefault(isEqual(WidgetType.PREMIUM, Widget::getType), defaultWidget));
     *     }
     * </pre>
     *
     * @param objects         A Collection of elements of type &lt;T&gt;.
     * @param findWithDefault An object representing a predicate for finding a value, and a default element one is not
     *                        found. Use the {@link #findWithDefault(Predicate, Supplier)} to provide this parameter.
     * @param <T>             The type of elements in the objects Collection.
     * @return The first matching element of type &lt;T&gt; if one is found, otherwise the default from findWithDefault
     * if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefault<T> findWithDefault) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefault.getPredicate())
                .findFirst()
                .orElse(findWithDefault.getDefaultValue());
    }

    /**
     * Attempt to find the first matching element in a <code>Collection</code> of elements of type &lt;T&gt; using a
     * predicate, returning a default value retrieved using a <code>Supplier</code> if one is not found. Here is a
     * contrived example of how this method would be called:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return StreamUtils.findFirstWithDefault(widgets, StreamUtils.findWithDefault(PredicateUtils.isEqual(WidgetType.PREMIUM, Widget::getType), Widget::createDefault));
     *     }
     * </pre>
     * Or, with static imports:
     * <pre>
     *     {
     *         Collection&lt;Widget&gt; widgets = ...
     *         return findFirstWithDefault(widgets, findWithDefault(isEqual(WidgetType.PREMIUM, Widget::getType), Widget::createDefault));
     *     }
     * </pre>
     *
     * @param objects                 A Collection of elements of type &lt;T&gt;.
     * @param findWithDefaultSupplier An object representing a predicate for finding a value, and a default Supplier if
     *                                one is not found. Use the {@link #findWithDefault(Predicate, Supplier)} method to
     *                                provide this parameter.
     * @param <T>                     The type of elements in the objects Collection.
     * @return The first matching element of type &lt;T&gt; if one is found, otherwise a default from the Supplier in
     * findWithDefaultSupplier if the Collection is null or empty, or if no values in it match the predicate.
     */
    public static <T> T findFirstWithDefault(Collection<T> objects, FindWithDefaultSupplier<T> findWithDefaultSupplier) {
        return defaultStream(objects)
                .filter(Objects::nonNull)
                .filter(findWithDefaultSupplier.getPredicate())
                .findFirst()
                .orElseGet(findWithDefaultSupplier.getDefaultSupplier());
    }

    private static <T> FindWithDefault<T> findWithDefault(Predicate<T> predicate) {
        return FindWithDefault.of(predicate, null);
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default element of type &lt;T&gt; if one
     * is not found. See {@link #findAnyWithDefault(Collection, FindWithDefault)} or
     * {@link #findFirstWithDefault(Collection, FindWithDefault)} for usage examples.
     *
     * @param predicate    A predicate used in finding a particular element of type &lt;T&gt;.
     * @param defaultValue A default element of type &lt;T&gt; to return if one could not be found using the above
     *                     predicate.
     * @param <T>          The type of elements taken by the given Predicate. Also, the type of the defaultValue.
     * @return An object containing a predicate and default element of type &lt;T&gt;.
     */
    public static <T> FindWithDefault<T> findWithDefault(Predicate<T> predicate, T defaultValue) {
        return FindWithDefault.of(predicate, defaultValue);
    }

    /**
     * Retrieves an object representing a predicate for finding a value, and a default <code>Supplier</code> if one is
     * not found. See {@link #findAnyWithDefault(Collection, FindWithDefaultSupplier)} or
     * {@link #findFirstWithDefault(Collection, FindWithDefaultSupplier)} for usage examples.
     *
     * @param predicate       A predicate used in finding a particular double value.
     * @param defaultSupplier A default DoubleSupplier used to return if one could not be found using the above
     *                        predicate.
     * @param <T>             The type of elements taken by the given Predicate. Also, the return type of the
     *                        defaultSupplier.
     * @return An object containing a predicate and default Supplier returning an object of type &lt;T&gt;.
     */
    public static <T> FindWithDefaultSupplier<T> findWithDefault(Predicate<T> predicate, Supplier<T> defaultSupplier) {
        return FindWithDefaultSupplier.of(predicate, defaultSupplier);
    }

    /**
     * Finds the index of the first element in a <code>Collection</code> of type &lt;T&gt;, that matches a
     * <code>Predicate</code>.
     *
     * @param objects   A Collection of elements of type &lt;T&gt;.
     * @param predicate A Predicate used to find a matching value.
     * @param <T>       The type of elements in the objects Collection.
     * @return An index of the first value in the given objects Collection matching the predicate. Returns -1 if no
     * matches are found.
     */
    public static <T> int indexOfFirst(Collection<T> objects, Predicate<T> predicate) {
        return defaultStream(objects)
                .map(MapperUtils.pairWithIndex())
                .filter(PredicateUtils.mapAndFilter(ObjectIndexPair::getObject, predicate))
                .mapToInt(ObjectIndexPair::getIndex)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and a <code>Function</code> that takes an element
     * of type &lt;T&gt; and returns a <code>CharSequence</code>, this method builds a <code>String</code> of those
     * char sequences joined together using <code>","</code> as a delimiter.
     *
     * @param objects A Collection of elements of type &lt;T&gt;, each of which is to be mapped to a CharSequence. A
     *                String will be built using each individual CharSequence, delimited by ",".
     * @param mapper  A Function to map each element of type &lt;T&gt; to a CharSequence.
     * @param <T>     The type of elements in the objects Collection.
     * @return A String built by mapping each element of type &lt;T&gt; to a CharSequence, and joining them together
     * using "," as a delimiter.
     */
    public static <T> String join(Collection<T> objects, Function<T, CharSequence> mapper) {
        return join(objects, mapperWithDelimiter(mapper, ","));
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a <code>Function</code>
     * that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>, and a delimiter, this method
     * builds a <code>String</code> of those char sequences joined together using the delimiter.
     *
     * @param objects             A Collection of elements of type &lt;T&gt;, each of which is to be mapped to a
     *                            CharSequence. A String will be built using each individual CharSequence.
     * @param mapperWithDelimiter An object representing a <code>Function</code> that takes an element of type &lt;T&gt;
     *                            and returns a <code>CharSequence</code>, and a delimiter.
     * @param <T>                 The type of elements in the objects Collection.
     * @return A String built by mapping each element of type &lt;T&gt; to a CharSequence, and joining them together
     * using the delimiter from mapperWithDelimiter.
     */
    public static <T> String join(Collection<T> objects, CharSequenceMapperWithDelimiter<T> mapperWithDelimiter) {
        return join(objects, CharSequenceMapperWithCollector.of(mapperWithDelimiter));
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and an object representing a <code>Function</code>
     * that takes an element of type &lt;T&gt; and returns a <code>CharSequence</code>, and a joining Collector, this
     * method builds a <code>String</code> of those char sequences joined together using the collector. The
     * {@link #mapperWithCollector(Function, Collector)} should be used to build the second parameter to this method.
     * Because that method takes a <code>Collector&lt;CharSequence, ?, String&gt;</code>, the overloads of the
     * Java <code>Collector.joining(...)</code> method may be used, including the one that takes a delimiter, prefix and
     * suffix.
     *
     * @param objects             A Collection of elements of type &lt;T&gt;, each of which is to be mapped to a
     *                            CharSequence. A String will be built using each individual CharSequence.
     * @param mapperWithCollector An object representing a <code>Function</code> that takes an element of type &lt;T&gt;
     *                            and returns a <code>CharSequence</code>, and a Collector used to join the individual
     *                            char sequences together.
     * @param <T>                 The type of elements in the objects Collection.
     * @return A String built by mapping each element of type &lt;T&gt; to a CharSequence, and joining them together
     * using the Collector from mapperWithCollector.
     */
    public static <T> String join(Collection<T> objects, CharSequenceMapperWithCollector<T> mapperWithCollector) {
        return defaultStream(objects)
                .map(mapperWithCollector.getCharSequenceMapper())
                .collect(mapperWithCollector.getJoiner());
    }

    /**
     * Builds an object representing a <code>Function</code> that takes an element of type &lt;T&gt; and returns a
     * <code>CharSequence</code>, and a delimiter. Used to build the second parameter to the
     * {@link #join(Collection, CharSequenceMapperWithDelimiter)} method.
     *
     * @param charSequenceMapper A Function that takes an element of type &lt;T&gt; and returns a CharSequence.
     * @param delimiter          A delimiter used to join individual char sequences together.
     * @param <T>                The type of elements taken by the given Function.
     * @return An object representing a Function that takes an element of type &lt;T&gt; and returns a CharSequence,
     * along with a delimiter for joining the char sequences together.
     */
    public static <T> CharSequenceMapperWithDelimiter<T> mapperWithDelimiter(Function<T, CharSequence> charSequenceMapper, CharSequence delimiter) {
        return CharSequenceMapperWithDelimiter.of(charSequenceMapper, delimiter);
    }

    /**
     * Builds an object representing a <code>Function</code> that takes an element of type &lt;T&gt; and returns a
     * <code>CharSequence</code>, and a joining Collector. Used to build the second parameter to the
     * {@link #join(Collection, CharSequenceMapperWithCollector)} method.
     *
     * @param charSequenceMapper A Function that takes an element of type &lt;T&gt; and returns a CharSequence.
     * @param joiner             A joining Collector used to join individual char sequences together.
     * @param <T>                The type of elements taken by the given Function.
     * @return An object representing a Function that takes an element of type &lt;T&gt; and returns a CharSequence,
     * along with a joining Collector used to join individual char sequences together.
     */
    public static <T> CharSequenceMapperWithCollector<T> mapperWithCollector(Function<T, CharSequence> charSequenceMapper, Collector<CharSequence, ?, String> joiner) {
        return CharSequenceMapperWithCollector.of(charSequenceMapper, joiner);
    }

    /**
     * Given a <code>Set</code> from which to subtract the elements of another one, returns a <code>Set</code> of the
     * remaining elements. More formally, elements <code>o</code> in the <code>toSubtract</code> set are removed from
     * the <code>from</code> set if <code>(o==null ? e==null : o.equals(e))</code> where <code>e</code> is an element in
     * <code>from</code>.
     *
     * @param from       A source set from which to subtract elements.
     * @param toSubtract A set of elements to be subtracted from a source set.
     * @param <T>        The type of elements in the from and toSubtract sets.
     * @return A set of elements remaining, after subtracting the elements of toSubtract that are contained in from.
     */
    public static <T> Set<T> subtract(Set<T> from, Set<T> toSubtract) {
        Stream<T> keyStream = defaultStream(from);
        Set<T> nonNullToSubtract = toSubtract == null ? emptySet() : toSubtract;
        return keyStream.filter(PredicateUtils.not(PredicateUtils.contains(nonNullToSubtract, identity())))
                .collect(toSet());
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and a partition size, this method divides the
     * collection into a <code>List</code> of lists, each of whose length is at most <code>partitionSize</code>.
     *
     * @param objects       A Collection of elements of type &lt;T&gt; to be partitioned up into a list of lists.
     * @param partitionSize The maximum length of the individual lists in the returned list.
     * @param <T>           The type of the elements in the objects parameter.
     * @return A List of lists of elements of type &lt;T&gt;, each of whose length is at most partitionSize. The last
     * list may have a length that is less than partitionSize.
     */
    public static <T> List<List<T>> toPartitionedList(Collection<T> objects, int partitionSize) {
        return defaultStream(objects).collect(CollectorUtils.toPartitionedList(partitionSize));
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, and a partition size, this method divides the
     * collection into a <code>Stream</code> of lists, each of whose length is at most <code>partitionSize</code>.
     *
     * @param objects       A Collection of elements of type &lt;T&gt; to be partitioned up into a stream of lists.
     * @param partitionSize The maximum length of the individual lists in the returned stream.
     * @param <T>           The type of the elements in the objects parameter.
     * @return A stream of lists of elements of type &lt;T&gt;, each of whose length is at most partitionSize. The last
     * list may have a length that is less than partitionSize.
     */
    public static <T> Stream<List<T>> toPartitionedStream(Collection<T> objects, int partitionSize) {
        return defaultStream(objects).collect(CollectorUtils.toPartitionedStream(partitionSize));
    }

    /**
     * Given an <code>Iterator</code> of elements of type &lt;T&gt;, returns a <code>Stream</code> of those elements.
     *
     * @param iterator An Iterator of elements of type &lt;T&gt;.
     * @param <T>      The type of elements iterated by the above iterator.
     * @return A Stream of elements of type &lt;T&gt;.
     */
    public static <T> Stream<T> fromIterator(Iterator<T> iterator) {
        if (iterator != null) {
            Iterable<T> iterable = () -> iterator;
            return StreamSupport.stream(iterable.spliterator(), false);
        }
        return Stream.empty();
    }

    /**
     * Given a <code>Collection</code> of elements of type &lt;T&gt;, returns a <code>Stream</code> of those elements.
     *
     * @param objects A Collection of elements of type &lt;T&gt; from which to return a Stream.
     * @param <T>     The type of the elements in the objects parameter. Also, the type of the elements in the returned
     *                Stream.
     * @return A Stream from the given Collection of elements of type &lt;T&gt;. Returns an empty Stream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(Collection<T> objects) {
        return objects == null ? Stream.empty() : objects.stream();
    }

    /**
     * Given a <code>Stream</code> of elements of type &lt;T&gt;, returns that stream if it is not <code>null</code>,
     * otherwise returns an empty <code>Stream</code>.
     *
     * @param stream An input Stream.
     * @param <T>    The type of the elements in the objects parameter. Also, the type of the elements in the returned
     *               Stream.
     * @return The given Stream if it is not null, otherwise an empty Stream.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(Stream<T> stream) {
        return stream == null ? Stream.empty() : stream;
    }

    /**
     * Given an array of elements of type &lt;T&gt;, returns a <code>Stream</code> of them.
     *
     * @param array An array of elements of type &lt;T&gt;.
     * @param <T>   The type of the elements in the array parameter. Also, the type of the elements in the returned
     *              Stream.
     * @return A Stream from the given array. Returns an empty Stream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(T[] array) {
        return array == null ? Stream.empty() : Arrays.stream(array);
    }

    /**
     * Given a single element of type &lt;T&gt;, returns a <code>Stream</code> containing only that element.
     *
     * @param target A single element of type &lt;T&gt;.
     * @param <T>    The type of the target element.
     * @return A Stream containing the given target element. Returns an empty Stream if it is null.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> Stream<T> defaultStream(T target) {
        return target == null ? Stream.empty() : Stream.of(target);
    }
}
