package org.hringsak.functions.internal;

import java.util.AbstractMap;
import java.util.Map;

public class Pair <K, V> implements Map.Entry<K, V> {

    private final Map.Entry<K, V> wrappedEntry;

    private Pair(K key, V value) {
        wrappedEntry = new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    @Override
    public K getKey() {
        return wrappedEntry.getKey();
    }

    @Override
    public V getValue() {
        return wrappedEntry.getValue();
    }

    @Override
    public V setValue(V value) {
        return wrappedEntry.setValue(value);
    }

    public K getLeft() {
        return wrappedEntry.getKey();
    }

    public V getRight() {
        return wrappedEntry.getValue();
    }
}
