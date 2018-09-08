package org.hringsak.functions.mapper;

import java.util.Objects;

public class IntObjectPair<T> {

    private final int left;
    private final T right;

    private IntObjectPair(int left, T right) {
        this.left = left;
        this.right = right;
    }

    public static <T> IntObjectPair<T> of(int left, T right) {
        return new IntObjectPair<>(left, right);
    }

    public int getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IntObjectPair other = (IntObjectPair) obj;
        return Objects.equals(left, other.left) &&
                Objects.equals(right, other.right);
    }

    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[left=%s,right=%s]";
        return String.format(template, getClass().getName(), identity, left, right);
    }
}
