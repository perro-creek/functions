package org.hringsak.functions.doubles;

import java.util.Objects;

public class DoubleObjectPair<T> {

    private final double left;
    private final T right;

    private DoubleObjectPair(double left, T right) {
        this.left = left;
        this.right = right;
    }

    public static <T> DoubleObjectPair<T> of(double left, T right) {
        return new DoubleObjectPair<>(left, right);
    }

    public double getLeft() {
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
        DoubleObjectPair other = (DoubleObjectPair) obj;
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
