package org.hringsak.functions.mapper;

import java.util.Objects;

public class DoubleIndexPair {

    private final double doubleValue;
    private final int index;

    private DoubleIndexPair(double doubleValue, int index) {
        this.doubleValue = doubleValue;
        this.index = index;
    }

    public static DoubleIndexPair of(double left, int right) {
        return new DoubleIndexPair(left, right);
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubleValue, index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        DoubleIndexPair other = (DoubleIndexPair) obj;
        return Objects.equals(doubleValue, other.doubleValue) &&
                index == other.index;
    }

    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[doubleValue=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, doubleValue, index);
    }
}
