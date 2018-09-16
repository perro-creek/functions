package org.hringsak.functions.mapper;

import java.util.Objects;

public class LongIndexPair {

    private final long longValue;
    private final int right;

    private LongIndexPair(long longValue, int right) {
        this.longValue = longValue;
        this.right = right;
    }

    public static LongIndexPair of(long left, int right) {
        return new LongIndexPair(left, right);
    }

    public long getLongValue() {
        return longValue;
    }

    public int getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longValue, right);
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
        return Objects.equals(longValue, other.longValue) &&
                Objects.equals(right, other.right);
    }

    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[longValue=%s,right=%s]";
        return String.format(template, getClass().getName(), identity, longValue, right);
    }
}
