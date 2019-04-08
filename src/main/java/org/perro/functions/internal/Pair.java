package org.perro.functions.internal;

import org.perro.functions.mapper.MapperUtils;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Although this class resides in the "internal" package, it will be returned as the result of some of the mapping
 * functions built by the {@link MapperUtils} methods. It can be referred to directly
 * since it is a public class, although to avoid dependencies on it, it may be wise to just refer to its type as the
 * Java <code>Map.Entry</code> interface. This will allow you to only to use the {@link #getKey()} and
 * {@link #getValue()} methods, however, they are functionally the same as {@link #getLeft()} and {@link #getRight()}.
 *
 * @param <K> The type of the key or left object.
 * @param <V> The type of the value or right object.
 */
public class Pair<K, V> implements Map.Entry<K, V> {

    private final Map.Entry<K, V> wrappedEntry;

    private Pair(K key, V value) {
        wrappedEntry = new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    /**
     * Builds an object of this type using the given <code>key</code> and <code>value</code> instances.
     *
     * @param key   The key of the map entry, or left object in the pair.
     * @param value The value of the map entry, or right object in the pair.
     * @param <K>   The type of the key or left object.
     * @param <V>   The type of the value or right object.
     * @return A map entry, or pair object.
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    /**
     * Getter for the map entry key, or left object in the pair.
     *
     * @return The key or left object.
     */
    @Override
    public K getKey() {
        return wrappedEntry.getKey();
    }

    /**
     * Getter for the map entry value, or right object in the pair.
     *
     * @return The value or right object.
     */
    @Override
    public V getValue() {
        return wrappedEntry.getValue();
    }

    /**
     * Because this is an immutable map entry object, this method is not unsupported.
     *
     * @throws UnsupportedOperationException Always throws this exception.
     */
    @Override
    public V setValue(V value) {
        return wrappedEntry.setValue(value);
    }

    /**
     * Getter for the map entry key, or left object in the pair.
     *
     * @return The key or left object.
     */
    public K getLeft() {
        return wrappedEntry.getKey();
    }

    /**
     * Getter for the map entry value, or right object in the pair.
     *
     * @return The value or right object.
     */
    public V getRight() {
        return wrappedEntry.getValue();
    }
}
