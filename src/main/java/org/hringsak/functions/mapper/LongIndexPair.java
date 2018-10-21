package org.hringsak.functions.mapper;

import java.util.Objects;

/**
 * An immutable object that pairs a primitive <code>long</code> value, with an associated <code>int</code> index. Note
 * that, unlike {@link org.hringsak.functions.internal.Pair}, this class does <i>not</i> implement
 * <code>Map.Entry</code>, because it deals with primitive values only.
 */
public class LongIndexPair {

    private final long longValue;
    private final int index;

    private LongIndexPair(long longValue, int index) {
        this.longValue = longValue;
        this.index = index;
    }

    /**
     * Builds a new instance of this class, with the given <code>longValue</code> value, and an associated
     * <code>index</code>.
     *
     * @param longValue A long value to be paired with an associated index.
     * @param index    An int index.
     * @return A new instance of this class.
     */
    public static LongIndexPair of(long longValue, int index) {
        return new LongIndexPair(longValue, index);
    }

    /**
     * Getter for the <code>long</code> value associated with an index.
     *
     * @return A long value associated with an index.
     */
    public long getLongValue() {
        return longValue;
    }

    /**
     * Getter for the index.
     *
     * @return An index associated with a long value.
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
        return Objects.hash(longValue, index);
    }

    /**
     * Indicates whether some other object is "equal to" this one. They will be considered equal if the given object is
     * of the same class, and is either the identical object, or the primitive <code>long</code> and index attributes are
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
        LongIndexPair other = (LongIndexPair) obj;
        return Objects.equals(longValue, other.longValue) &&
                Objects.equals(index, other.index);
    }

    /**
     * Returns a string representation of the object, containing the primitive attribute values of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[longValue=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, longValue, index);
    }
}
