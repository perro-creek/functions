package org.hringsak.functions.collector;

import org.hringsak.functions.internal.Invariants;
import org.hringsak.functions.stream.DblStreamUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hringsak.functions.supplier.SupplierUtils.supplier;

/**
 * Methods that build different kinds of <code>Collector</code> instances useful in conjunction with Java streams.
 */
public final class CollectorUtils {

    private CollectorUtils() {
    }

    /**
     * When using the Java <code>Collectors.groupingBy(...)</code> methods, you might come across a situation where you
     * want to include a grouping with a key in the resulting map, but don't want to include particular elements in the
     * collection it maps to. An example of this might be that you have a collection of customer order objects you need
     * to report on. The report should include all orders for a particular customer, but only those line items whose
     * price is over a certain amount. Here is an example of how this might work:
     * <pre>
     *     Collection&lt;OrderLineItem&gt; customerLineItems = ...
     *     Predicate&lt;OrderLineItem&gt; priceLevelPredicate = PredicateUtils.gt(100, OrderLineItem::getPrice);
     *     Map&lt;String, List&lt;OrderLineItem&gt;&gt; itemsByOrderId = customerLineItems.stream()
     *         .collect(groupingBy(OrderLineItem::getOrderId, CollectorUtils.conditionalCollector(priceLevelPredicate)));
     * </pre>
     * This conditional collector can be used with any of the <code>Collectors.groupingBy(...)</code> overloads that
     * take a downstream <code>Collector</code>. It can also be used along with the Java <code>Collectors.mapping(...)</code>
     * method, which would apply a transformation to the element before applying the conditional collector.
     * <p>
     * This method is equivalent to calling {@link #conditionalCollector(Predicate, Supplier)} passing <code>ArrayList::new</code>
     * as the supplier.
     *
     * @param shouldAddToGroup A predicate to filter the elements from the collection value associated with each key in
     *                         map returned by the Java Collectors.groupingBy(...) collector.
     * @param <T>              The type of the input elements being streamed.
     * @return A Collector that filters elements being grouped into a list.
     */
    public static <T> Collector<T, ?, List<T>> conditionalCollector(Predicate<T> shouldAddToGroup) {
        return conditionalCollector(shouldAddToGroup, ArrayList::new);
    }

    /**
     * When using the Java <code>Collectors.groupingBy(...)</code> methods, you might come across a situation where you
     * want to include a grouping with a key in the resulting map, but don't want to include particular elements in the
     * collection it maps to. An example of this might be that you have a collection of customer order objects you need
     * to report on. The report should include all orders for a particular customer, but only those line items whose
     * price is over a certain amount. Here is an example of how this might work:
     * <pre>
     *     Collection&lt;OrderLineItem&gt; customerLineItems = ...
     *     Predicate&lt;OrderLineItem&gt; priceLevelPredicate = PredicateUtils.gt(100, OrderLineItem::getPrice);
     *     Map&lt;String, Set&lt;OrderLineItem&gt;&gt; itemsByOrderId = customerLineItems.stream()
     *         .collect(groupingBy(OrderLineItem::getOrderId, CollectorUtils.conditionalCollector(priceLevelPredicate, HashSet::new)));
     * </pre>
     * This conditional collector can be used with any of the <code>Collectors.groupingBy(...)</code> overloads that
     * take a downstream <code>Collector</code>. It can also be used along with the Java <code>Collectors.mapping(...)</code>
     * method, which would apply a transformation to the element before applying the conditional collector.
     *
     * @param shouldAddToGroup A predicate to filter the elements from the collection value associated with each key in
     *                         map returned by the Java Collectors.groupingBy(...) collector.
     * @param supplier         A supplier of the collection, into which elements are to be grouped.
     * @param <T>              The type of the input elements being streamed.
     * @param <R>              The type of collection to group into, as specified by the passed supplier.
     * @return A Collector that filters elements being grouped into a list.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T, R extends Collection<T>> Collector<T, R, R> conditionalCollector(Predicate<T> shouldAddToGroup, Supplier<R> supplier) {
        return Collector.of(supplier, accumulator(shouldAddToGroup), combiner(Collection::addAll));
    }

    private static <T, C extends Collection<T>> BiConsumer<C, T> accumulator(Predicate<T> shouldAddToGroup) {
        return (groupedResults, target) -> {
            if (shouldAddToGroup.test(target)) {
                groupedResults.add(target);
            }
        };
    }

    private static <T> BinaryOperator<T> combiner(BiConsumer<? super T, ? super T> biConsumer) {
        return (left, right) -> {
            biConsumer.accept(left, right);
            return left;
        };
    }

    /**
     * Sometimes it is convenient to stream through the entries of a map. In the process of doing so, you may map the
     * entry so that the key or value changes from one type to another. At the end, it would often be useful to convert
     * the entry back into a map. For example, given this map of enum names to enum values:
     * <pre>
     *     Map&lt;String, TestEnum&gt; enumMap = Arrays.stream(TestEnum.values())
     *         .collect(toMap(TestEnum::name, Function.identity());
     * </pre>
     * Then, if we stream over the entries of this map:
     * <pre>
     *     enumMap.entrySet().stream()
     *         .map(MapperUtils.pairOf(Map.Entry::getValue, Map.Entry::getKey))
     *         .collect(CollectorUtils.toMapFromEntry());
     * </pre>
     * We can collect the transformed entries of the map back into a different one. The <code>MapperUtils.pairOf(...)</code>
     * method creates an apache commons lang <code>Pair</code> object, which implements the <code>Map.Entry</code>
     * interface. This allows us to call this method, switch the key and value around, and create a new, reversed
     * map where the key has now become the value, and vice versa. This is just a contrived example, but dealing with
     * <code>Map.Entry</code> and/or <code>Pair</code> instances in streams, and the ability to convert them back into
     * transformed maps can be very useful.
     *
     * @param <K> The Map.Entry key type.
     * @param <V> The Map.Entry value type.
     * @return A Collector that accumulates Map.Entry and/or Pair instances into into a Map.
     */
    public static <K, V> Collector<Entry<K, V>, ?, Map<K, V>> toMapFromEntry() {
        return Collector.of(HashMap::new, mapEntryAccumulator(), combiner(Map::putAll));
    }

    private static <K, V> BiConsumer<Map<K, V>, Entry<K, V>> mapEntryAccumulator() {
        return (map, entry) -> map.put(entry.getKey(), entry.getValue());
    }

    /**
     * Builds a <code>Collector</code> that accumulates a stream of elements into a stream of lists of those elements,
     * each limited to the passed <code>partitionSize</code>. For example, given the following code:
     * <pre>
     *     IntStream.range(0, 100)
     *         .boxed()
     *         .collect(CollectorUtils.toPartitionedStream(10))
     *         .forEach(System.out::println);
     * </pre>
     * After execution of this code, the output would be:
     * <pre>
     *     [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     *     [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
     *     [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
     *     [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
     *     [40, 41, 42, 43, 44, 45, 46, 47, 48, 49]
     *     [50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
     *     [60, 61, 62, 63, 64, 65, 66, 67, 68, 69]
     *     [70, 71, 72, 73, 74, 75, 76, 77, 78, 79]
     *     [80, 81, 82, 83, 84, 85, 86, 87, 88, 89]
     *     [90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
     * </pre>
     *
     * @param partitionSize The size limit for each partitioned list.
     * @param <T>           The type of the elements in the collection to be partitioned.
     * @return A Collector that accumulates into a stream of lists, each with a maximum size of partitionSize.
     */
    public static <T> Collector<T, ?, Stream<List<T>>> toPartitionedStream(int partitionSize) {
        return collectingAndThen(toPartitionedList(partitionSize), List::stream);
    }

    /**
     * Builds a <code>Collector</code> that accumulates a stream of elements into a list of lists of those elements,
     * each limited to the passed <code>partitionSize</code>. For example, given the following code:
     * <pre>
     *     IntStream.range(0, 100)
     *         .boxed()
     *         .collect(CollectorUtils.toPartitionedList(10))
     *         .forEach(System.out::println);
     * </pre>
     * After execution of this code, the output would be:
     * <pre>
     *     [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     *     [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
     *     [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
     *     [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
     *     [40, 41, 42, 43, 44, 45, 46, 47, 48, 49]
     *     [50, 51, 52, 53, 54, 55, 56, 57, 58, 59]
     *     [60, 61, 62, 63, 64, 65, 66, 67, 68, 69]
     *     [70, 71, 72, 73, 74, 75, 76, 77, 78, 79]
     *     [80, 81, 82, 83, 84, 85, 86, 87, 88, 89]
     *     [90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
     * </pre>
     *
     * @param partitionSize The size limit for each partitioned list.
     * @param <T>           The type of the elements in the collection to be partitioned.
     * @return A Collector that accumulates into a list of lists, each with a maximum size of partitionSize.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T> Collector<T, ?, List<List<T>>> toPartitionedList(int partitionSize) {
        Invariants.checkArgument(partitionSize > 0, "The 'partitionSize' argument must be greater than zero");
        return Collector.of(ArrayList::new, listPartitionAccumulator(partitionSize), combiner(List::addAll));
    }

    /**
     * Builds a <code>Collector</code> that accumulates a stream of elements into a list of arbitrary objects of type
     * &lt;R&gt;, into which those elements are accumulated. For example, this method is used in the implementation of
     * {@link DblStreamUtils#toPartitionedDblList(double[], int)}:
     * <pre>
     *     public static List&lt;double[]&gt; toPartitionedDblList(double[] doubles, int partitionSize) {
     *         return defaultDblStream(doubles)
     *             .boxed()
     *             .collect(toPartitionedList(partitionSize, DblStreamUtils::toListOfArrays));
     *     }
     *
     *     private static List&lt;double[]&gt; toListOfArrays(List&lt;List&lt;Double&gt;&gt; partitions) {
     *         return partitions.stream()
     *             .map(DblStreamUtils::listToArray)
     *             .collect(toList());
     *     }
     * </pre>
     * The {@link DblStreamUtils#toPartitionedDblList(double[], int)} method partitions an array of doubles into a
     * <code>List&lt;double[]&gt;</code>.
     *
     * @param partitionSize The size limit for the maximum number of elements that may be accumulated into each object
     *                      of type &lt;R&gt;.
     * @param finisher      A finisher function that accumulates each partition List&lt;T&gt; into an object of type
     *                      &lt;R&gt;.
     * @param <T>           The type of the elements in the collection to be partitioned.
     * @param <R>           The type of the object, into which a partition of elements will be accumulated.
     * @return A Collector that accumulates into a list of objects of type &lt;R&gt;, each accumulating a maximum number
     * of partitioned elements, given by partitionSize.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T, R> Collector<T, List<List<T>>, List<R>> toPartitionedList(int partitionSize, Function<List<List<T>>, List<R>> finisher) {
        Invariants.checkArgument(partitionSize > 0, "The 'partitionSize' argument must be greater than zero");
        return Collector.of(ArrayList::new, listPartitionAccumulator(partitionSize), combiner(List::addAll), finisher);
    }

    private static <T> BiConsumer<List<List<T>>, T> listPartitionAccumulator(int partitionSize) {
        AtomicInteger batchCount = new AtomicInteger();
        AtomicInteger lastPartition = new AtomicInteger();
        return (partitions, target) -> {
            if (batchCount.intValue() % partitionSize == 0) {
                batchCount.set(0);
                lastPartition.set(partitions.size());
                partitions.add(new ArrayList<>());
            }
            batchCount.incrementAndGet();
            partitions.get(lastPartition.intValue()).add(target);
        };
    }

    /**
     * Builds a <code>Collector</code> that can be useful in dealing with strings as a stream of characters. For example,
     * given the following code:
     * <pre>
     *     System.out.println("test123".codePoints()
     *         .filter(Character::isDigit)
     *         .boxed()
     *         .collect(CollectorUtils.toStringBuilder()));
     * </pre>
     * After execution of this code, the output would be:
     * <pre>
     *     123
     * </pre>
     * If it was necessary, for performance reasons, to keep the stream as an <code>IntStream</code>, and not use the
     * <code>IntStream.boxed()</code> method, a similar collector could be created like this:
     * <pre>
     *     System.out.println("test123".codePoints()
     *         .filter(Character::isDigit)
     *         .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append));
     * </pre>
     * However, in most cases, the difference in execution speed will be negligible.
     *
     * @return A Collector that accumulates Integer instances into a StringBuilder.
     */
    public static Collector<Integer, ?, StringBuilder> toStringBuilder() {
        return Collector.of(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
    }

    /**
     * Builds a <code>Collector</code> that constructs an <code>EnumSet</code>, and accumulates enumerated values into
     * it.
     *
     * @param enumClass The class of the enum, for which to build a collector.
     * @param <E>       The type of the enum class, for which to build a collector.
     * @return An EnumSet collector.
     */
    public static <E extends Enum<E>> Collector<E, ?, EnumSet<E>> toEnumSet(Class<E> enumClass) {
        Objects.requireNonNull(enumClass, "The argument, \"enumClass\", must not be null");
        return Collector.of(supplier(EnumSet::noneOf, enumClass), accumulator(EnumSet::add), combiner(EnumSet::addAll));
    }

    @SuppressWarnings("unused")
    private static <T, C extends Collection<T>> BiConsumer<C, T> accumulator(BiConsumer<C, T> biConsumer) {
        return biConsumer;
    }

    /**
     * Returns a <code>Collector</code> that will add a default element into a resulting collection, if it would have
     * otherwise been empty. This overload defaults the collection type to a <code>List</code>, and is equivalent to
     * either {@link #withDefault(Object, Supplier)} passing <code>ArrayList::new</code> as the <code>Supplier</code>.
     * For example:
     * <pre>
     *     List&lt;String&gt; strings = Collections.singletonList('').stream()
     *         .filter(PredicateUtils.not(String::isEmpty))
     *         .collect(CollectorUtils.toListWithDefault('default'));
     * </pre>
     * The <code>strings</code> collection would contain one string, <code>'default'</code> after executing this code.
     *
     * @param defaultElement A default element to be added to a resulting collection.
     * @param <T>            The type of the input elements being streamed.
     * @return A Collector that adds a default element into a result collection.
     */
    public static <T> Collector<T, ?, List<T>> toListWithDefault(T defaultElement) {
        return withDefault(defaultElement, ArrayList::new);
    }

    /**
     * Returns a <code>Collector</code> that will add a default element into a resulting collection, if it would have
     * otherwise been empty. This overload defaults the collection type to a <code>Set</code>, and is equivalent to
     * either {@link #withDefault(Object, Supplier)} passing <code>HashSet::new</code> as the <code>Supplier</code>.
     * For example:
     * <pre>
     *     Set&lt;String&gt; strings = Collections.singletonList('').stream()
     *         .filter(PredicateUtils.not(String::isEmpty))
     *         .collect(CollectorUtils.toSetWithDefault('default'));
     * </pre>
     * The <code>strings</code> set would contain one string, <code>'default'</code> after executing this code.
     *
     * @param defaultElement A default element to be added to a resulting collection.
     * @param <T>            The type of the input elements being streamed.
     * @return A Collector that adds a default element into a resulting collection.
     */
    public static <T> Collector<T, ?, Set<T>> toSetWithDefault(T defaultElement) {
        return withDefault(defaultElement, HashSet::new);
    }

    /**
     * Returns a <code>Collector</code> that will add a default element into a resulting collection, if it would have
     * otherwise been empty. For example:
     * <pre>
     *     List&lt;String&gt; strings = Collections.singletonList('').stream()
     *         .filter(PredicateUtils.not(String::isEmpty))
     *         .collect(CollectorUtils.withDefault('default', LinkedList::new));
     * </pre>
     * The <code>strings</code> linked list would contain one string, <code>'default'</code> after executing this code.
     *
     * @param defaultElement A default element to be added to a resulting collection.
     * @param supplier       A supplier of the collection, into which elements are to be added.
     * @param <T>            The type of the input elements being streamed.
     * @param <R>            The type of collection to group into, as specified by the passed supplier.
     * @return A Collector that adds a default element into a resulting collection.
     */
    public static <T, R extends Collection<T>> Collector<T, ?, R> withDefault(T defaultElement, Supplier<R> supplier) {
        return collectingAndThen(toCollection(supplier), defaultFinisher(defaultElement));
    }

    private static <T, U extends Collection<T>> UnaryOperator<U> defaultFinisher(T defaultValue) {
        return collection -> {
            if (collection.isEmpty()) {
                collection.add(defaultValue);
            }
            return collection;
        };
    }

    /**
     * Builds a <code>Collector</code> that accumulates into an unmodifiable list.
     *
     * @param <T> The type of the input elements being streamed.
     * @return A Collector that accumulates into an unmodifiable list.
     */
    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return collectingAndThen(toList(), Collections::unmodifiableList);
    }

    /**
     * Builds a <code>Collector</code> that accumulates into an unmodifiable set.
     *
     * @param <T> The type of the input elements being streamed.
     * @return A Collector that accumulates into an unmodifiable set.
     */
    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return collectingAndThen(toSet(), Collections::unmodifiableSet);
    }

    /**
     * Builds a <code>Collector</code> that accumulates into a collection supplied by <code>wrappedCollectionSupplier</code>,
     * which will be wrapped by a unmodifiable collection.
     *
     * @param wrappedCollectionSupplier A supplier of a collection instance, that will be wrapped by an unmodifiable
     *                                  collection.
     * @param <T>                       The type of the input elements being streamed.
     * @return A Collector that accumulates into an unmodifiable collection.
     */
    public static <T> Collector<T, ?, Collection<T>> toUnmodifiableCollection(Supplier<Collection<T>> wrappedCollectionSupplier) {
        return collectingAndThen(toCollection(wrappedCollectionSupplier), Collections::unmodifiableCollection);
    }

    /**
     * Builds a <code>Collector</code> that accumulates into an synchronized list.
     *
     * @param <T> The type of the input elements being streamed.
     * @return A Collector that accumulates into an synchronized list.
     */
    public static <T> Collector<T, ?, List<T>> toSynchronizedList() {
        return collectingAndThen(toList(), Collections::synchronizedList);
    }

    /**
     * Builds a <code>Collector</code> that accumulates into an synchronized set.
     *
     * @param <T> The type of the input elements being streamed.
     * @return A Collector that accumulates into an synchronized set.
     */
    public static <T> Collector<T, ?, Set<T>> toSynchronizedSet() {
        return collectingAndThen(toSet(), Collections::synchronizedSet);
    }

    /**
     * Builds a <code>Collector</code> that accumulates into a collection supplied by <code>wrappedCollectionSupplier</code>,
     * which will be wrapped by a synchronized collection.
     *
     * @param wrappedCollectionSupplier A supplier of a collection instance, that will be wrapped by an synchronized
     *                                  collection.
     * @param <T>                       The type of the input elements being streamed.
     * @return A Collector that accumulates into an synchronized collection.
     */
    public static <T> Collector<T, ?, Collection<T>> toSynchronizedCollection(Supplier<Collection<T>> wrappedCollectionSupplier) {
        return collectingAndThen(toCollection(wrappedCollectionSupplier), Collections::synchronizedCollection);
    }
}
