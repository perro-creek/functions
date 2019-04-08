package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.Objects;

/**
 * An immutable object that pairs a primitive <code>int</code> value, with an associated <code>int</code> index. Note
 * that, unlike {@link Pair}, this class does <i>not</i> implement
 * <code>Map.Entry</code>, because it deals with primitive values only.
 */
public class IntIndexPair {

    private final int intValue;
    private final int index;

    private IntIndexPair(int intValue, int index) {
        this.intValue = intValue;
        this.index = index;
    }

    /**
     * Builds a new instance of this class, with the given <code>intValue</code> value, and an associated
     * <code>index</code>.
     *
     * @param intValue An int value to be paired with an associated index.
     * @param index    An int index.
     * @return A new instance of this class.
     */
    public static IntIndexPair of(int intValue, int index) {
        return new IntIndexPair(intValue, index);
    }

    /**
     * Getter for the <code>int</code> value associated with an index.
     *
     * @return An int value associated with an index.
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Getter for the index.
     *
     * @return An index associated with an int value.
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
        return Objects.hash(intValue, index);
    }

    /**
     * Indicates whether some other object is "equal to" this one. They will be considered equal if the given object is
     * of the same class, and is either the identical object, or the primitive <code>int</code> and index attributes are
     * equal.
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
        IntIndexPair other = (IntIndexPair) obj;
        return Objects.equals(intValue, other.intValue) &&
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
        String template = "%s@%s[intValue=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, intValue, index);
    }
}
