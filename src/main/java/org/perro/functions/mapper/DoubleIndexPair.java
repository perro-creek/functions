package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.Objects;

/**
 * An immutable object that pairs a primitive <code>double</code> value, with an associated <code>int</code> index. Note
 * that, unlike {@link Pair}, this class does <i>not</i> implement
 * <code>Map.Entry</code>, because it deals with primitive values only.
 */
public class DoubleIndexPair {

    private final double doubleValue;
    private final int index;

    private DoubleIndexPair(double doubleValue, int index) {
        this.doubleValue = doubleValue;
        this.index = index;
    }

    /**
     * Builds a new instance of this class, with the given <code>doubleValue</code> and <code>index</code> values.
     *
     * @param doubleValue A double value to be paired with an associated index.
     * @param index       An int index.
     * @return A new instance of this class.
     */
    public static DoubleIndexPair of(double doubleValue, int index) {
        return new DoubleIndexPair(doubleValue, index);
    }

    /**
     * Getter for the <code>double</code> value associated with an index.
     *
     * @return A double value associated with an index.
     */
    public double getDoubleValue() {
        return doubleValue;
    }

    /**
     * Getter for the index.
     *
     * @return An index associated with a double value.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Generates a hash code based on the hash codes of its primitive attributes.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(doubleValue, index);
    }

    /**
     * Indicates whether some other object is "equal to" this one. The will be considered equal if the given object is
     * of the same class, and is either the identical object, or the primitive <code>double</code> and <code>int</code>
     * attributes are equal.
     *
     * @param obj The target object with which to compare the current instance.
     * @return true if this object is considered equal to the obj argument; false otherwise.
     */
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

    /**
     * Returns a string representation of the object, containing the primitive attribute values of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[doubleValue=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, doubleValue, index);
    }
}
