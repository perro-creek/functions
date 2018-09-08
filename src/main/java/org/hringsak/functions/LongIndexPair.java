package org.hringsak.functions;

import java.util.Objects;

public class LongIndexPair {

    private final long left;
    private final int right;

    private LongIndexPair(long left, int right) {
        this.left = left;
        this.right = right;
    }

    public static LongIndexPair of(long left, int right) {
        return new LongIndexPair(left, right);
    }

    public long getLeft() {
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
        LongIndexPair other = (LongIndexPair) obj;
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
