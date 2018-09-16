package org.hringsak.functions.mapper;

import java.util.Objects;

public class ObjectIndexPair<T> {

    private final T object;
    private final int index;

    private ObjectIndexPair(T object, int index) {
        this.object = object;
        this.index = index;
    }

    public static <T> ObjectIndexPair<T> of(T left, int right) {
        return new ObjectIndexPair<>(left, right);
    }

    public T getObject() {
        return object;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ObjectIndexPair other = (ObjectIndexPair) obj;
        return Objects.equals(object, other.object) &&
                index == other.index;
    }

    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[object=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, object, index);
    }
}
