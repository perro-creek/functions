package org.hringsak.functions;

import java.util.Objects;

public class LongObjectPair<T> {

    private final long left;
    private final T right;

    private LongObjectPair(long left, T right) {
        this.left = left;
        this.right = right;
    }

    public static <T> LongObjectPair<T> of(long left, T right) {
        return new LongObjectPair<>(left, right);
    }

    public long getLeft() {
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
        LongObjectPair other = (LongObjectPair) obj;
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
