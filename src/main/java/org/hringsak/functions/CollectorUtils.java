package org.hringsak.functions;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public final class CollectorUtils {

    private CollectorUtils() {
    }

    public static <T> Collector<T, List<T>, List<T>> conditionalCollector(Predicate<T> shouldAddToGroup) {
        return conditionalCollector(shouldAddToGroup, ArrayList::new);
    }

    public static <T, R extends Collection<T>> Collector<T, R, R> conditionalCollector(Predicate<T> shouldAddToGroup, Supplier<R> supplier) {
        return Collector.of(supplier, accumulator(shouldAddToGroup), combiner(Collection::addAll));
    }

    private static <T, R extends Collection<T>> BiConsumer<R, T> accumulator(Predicate<T> shouldAddToGroup) {
        return (groupedResults, target) -> {
            if (shouldAddToGroup.test(target)) {
                groupedResults.add(target);
            }
        };
    }

    private static <T, R extends Collection<T>> BiConsumer<R, T> accumulator(BiConsumer<R, T> biConsumer) {
        return biConsumer;
    }

    private static <R> BinaryOperator<R> combiner(BiConsumer<R, R> biConsumer) {
        return (left, right) -> {
            biConsumer.accept(left, right);
            return left;
        };
    }

    public static <K, V> Collector<Entry<K, V>, Map<K, V>, Map<K, V>> toMapFromEntry() {
        return Collector.of(HashMap::new, toMapAccumulator(), combiner(Map::putAll));
    }

    private static <K, V> BiConsumer<Map<K, V>, Entry<K, V>> toMapAccumulator() {
        return (map, entry) -> map.put(entry.getKey(), entry.getValue());
    }

    public static <T> Collector<T, List<List<T>>, List<List<T>>> toPartitionedList(int partitionSize) {
        Preconditions.checkArgument(partitionSize > 0, "The 'partitionSize' argument must be greater than zero");
        return Collector.of(ArrayList::new, listPartitionAccumulator(partitionSize), combiner(List::addAll));
    }

    public static <T> Collector<T, List<List<T>>, Stream<List<T>>> toPartitionedStream(int partitionSize) {
        return Collectors.collectingAndThen(toPartitionedList(partitionSize), List::stream);
    }

    private static <T> BiConsumer<List<List<T>>, T> listPartitionAccumulator(int partitionSize) {
        MutableInt batchCount = new MutableInt();
        MutableInt lastPartition = new MutableInt();
        return (partitions, target) -> {
            if (batchCount.intValue() % partitionSize == 0) {
                batchCount.setValue(0);
                lastPartition.setValue(partitions.size());
                partitions.add(new ArrayList<>());
            }
            batchCount.increment();
            partitions.get(lastPartition.intValue()).add(target);
        };
    }

    public static Collector<Integer, StringBuilder, StringBuilder> toStringBuilder() {
        return Collector.of(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
    }

    public static <E extends Enum<E>> Collector<E, EnumSet<E>, EnumSet<E>> toEnumSet(Class<E> enumClass) {
        Preconditions.checkNotNull(enumClass, "The argument, \"enumClass\", must not be null");
        return Collector.of(enumSetSupplier(enumClass), accumulator(EnumSet::add), combiner(EnumSet::addAll));
    }

    private static <E extends Enum<E>> Supplier<EnumSet<E>> enumSetSupplier(Class<E> enumClass) {
        return () -> EnumSet.noneOf(enumClass);
    }

    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return Collectors.collectingAndThen(toList(), Collections::unmodifiableList);
    }

    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return Collectors.collectingAndThen(toSet(), Collections::unmodifiableSet);
    }

    public static <T> Collector<T, ?, Collection<T>> toUnmodifiableCollection(Supplier<Collection<T>> wrappedCollectionSupplier) {
        return Collectors.collectingAndThen(toCollection(wrappedCollectionSupplier), Collections::unmodifiableCollection);
    }

    public static <T> Collector<T, ?, List<T>> toSynchronizedList() {
        return Collectors.collectingAndThen(toList(), Collections::synchronizedList);
    }

    public static <T> Collector<T, ?, Set<T>> toSynchronizedSet() {
        return Collectors.collectingAndThen(toSet(), Collections::synchronizedSet);
    }

    public static <T> Collector<T, ?, Collection<T>> toSynchronizedCollection(Supplier<Collection<T>> wrappedCollectionSupplier) {
        return Collectors.collectingAndThen(toCollection(wrappedCollectionSupplier), Collections::synchronizedCollection);
    }
}
