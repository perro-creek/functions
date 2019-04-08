package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.Objects;

/**
 * An immutable object that pairs a primitive <code>long</code> value, with an associated object of type &lt;T&gt;.
 * Note that, unlike {@link Pair}, this class does <i>not</i> implement
 * <code>Map.Entry</code>, because its <code>left</code> attribute is a primitive <code>long</code>.
 */
public class LongObjectPair<T> {

    private final long left;
    private final T right;

    private LongObjectPair(long left, T right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Builds a new instance of this class, with the given <code>long</code> and <code>&lt;T&gt;</code> values.
     *
     * @param left  A long value to be paired with an associated object of type &lt;T&gt;.
     * @param right An object of type &lt;T&gt;.
     * @param <T>   The type of the right attribute for the resulting LongObjectPair instance.
     * @return A new instance of this class.
     */
    public static <T> LongObjectPair<T> of(long left, T right) {
        return new LongObjectPair<>(left, right);
    }

    /**
     * Getter for the left <code>long</code> value associated with an object of type &lt;T&gt;.
     *
     * @return A long value associated with an object of type &lt;T&gt;.
     */
    public long getLeft() {
        return left;
    }

    /**
     * Getter for the right object of type &lt;T&gt; associated with a <code>long</code> value.
     *
     * @return An object of type &lt;T&gt;.
     */
    public T getRight() {
        return right;
    }

    /**
     * Generates a hash code based on the hash codes of its primitive <code>long</code> attribute, and its object of
     * type &lt;T&gt;.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    /**
     * Indicates whether some other object is "equal to" this one. The will be considered equal if the given object is
     * of the same class, and is either the identical object, or the primitive <code>long</code> attribute and its
     * object of type &lt;T&gt; are equal.
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
        LongObjectPair other = (LongObjectPair) obj;
        return Objects.equals(left, other.left) &&
                Objects.equals(right, other.right);
    }

    /**
     * Returns a string representation of the object, containing the primitive <code>long</code> left attribute and its
     * associated object of type &lt;T&gt;.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[left=%s,right=%s]";
        return String.format(template, getClass().getName(), identity, left, right);
    }
}
