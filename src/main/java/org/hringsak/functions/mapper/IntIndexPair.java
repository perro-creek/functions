package org.hringsak.functions.mapper;

import java.util.Objects;

public class IntIndexPair {

    private final int left;
    private final int right;

    private IntIndexPair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public static IntIndexPair of(int left, int right) {
        return new IntIndexPair(left, right);
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
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
        IntIndexPair other = (IntIndexPair) obj;
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
