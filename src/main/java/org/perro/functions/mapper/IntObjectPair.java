package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.Objects;

/**
 * An immutable object that pairs a primitive <code>int</code> value, with an associated object of type &lt;T&gt;.
 * Note that, unlike {@link Pair}, this class does <i>not</i> implement
 * <code>Map.Entry</code>, because its <code>left</code> attribute is a primitive <code>int</code>.
 */
public class IntObjectPair<T> {

    private final int left;
    private final T right;

    private IntObjectPair(int left, T right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Builds a new instance of this class, with the given <code>int</code> and <code>&lt;T&gt;</code> values.
     *
     * @param left  An int value to be paired with an associated object of type &lt;T&gt;.
     * @param right An object of type &lt;T&gt;.
     * @param <T>   The type of the right attribute for the resulting IntObjectPair instance.
     * @return A new instance of this class.
     */
    public static <T> IntObjectPair<T> of(int left, T right) {
        return new IntObjectPair<>(left, right);
    }

    /**
     * Getter for the left <code>int</code> value associated with an object of type &lt;T&gt;.
     *
     * @return An int value associated with an object of type &lt;T&gt;.
     */
    public int getLeft() {
        return left;
    }

    /**
     * Getter for the right object of type &lt;T&gt; associated with an <code>int</code> value.
     *
     * @return An object of type &lt;T&gt;.
     */
    public T getRight() {
        return right;
    }

    /**
     * Generates a hash code based on the hash codes of its primitive <code>int</code> attribute, and its object of
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
     * of the same class, and is either the identical object, or the primitive <code>int</code> attribute and its
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
        IntObjectPair other = (IntObjectPair) obj;
        return Objects.equals(left, other.left) &&
                Objects.equals(right, other.right);
    }

    /**
     * Returns a string representation of the object, containing the primitive <code>int</code> left attribute and its
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
