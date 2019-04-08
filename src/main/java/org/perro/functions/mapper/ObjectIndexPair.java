package org.perro.functions.mapper;

import org.perro.functions.internal.Pair;

import java.util.Objects;

/**
 * An immutable object that pairs an object of type &lt;T&gt;, with an associated <code>int</code> index. Note that,
 * unlike {@link Pair}, this class does <i>not</i> implement <code>Map.Entry</code>,
 * because it deals with a primitive index value.
 */
public class ObjectIndexPair<T> {

    private final T object;
    private final int index;

    private ObjectIndexPair(T object, int index) {
        this.object = object;
        this.index = index;
    }

    /**
     * Builds a new instance of this class, with the given <code>object</code> and <code>index</code> values.
     *
     * @param object An object of type &lt;T&gt; to be paired with an associated index.
     * @param index  An int index.
     * @param <T> The type of the object to be paired with an int index.
     * @return A new instance of this class.
     */
    public static <T> ObjectIndexPair<T> of(T object, int index) {
        return new ObjectIndexPair<>(object, index);
    }

    /**
     * Getter for an object of type &lt;T&gt; associated with an index.
     *
     * @return An object of type &lt;T&gt; associated with an index.
     */
    public T getObject() {
        return object;
    }

    /**
     * Getter for the index.
     *
     * @return An index associated with an object of type &lt;T&gt;.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Generates a hash code based on the hash codes of its attributes.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(object, index);
    }

    /**
     * Indicates whether some other object is "equal to" this one. The will be considered equal if the given object is
     * of the same class, and is either the identical object, or the object of type &lt;T&gt; and <code>int</code> index
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
        ObjectIndexPair other = (ObjectIndexPair) obj;
        return Objects.equals(object, other.object) &&
                index == other.index;
    }

    /**
     * Returns a string representation of the object, containing the object of type &lt;T&gt; and <code>int</code> index
     * attributes of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        String identity = Integer.toHexString(System.identityHashCode(this));
        String template = "%s@%s[object=%s,index=%s]";
        return String.format(template, getClass().getName(), identity, object, index);
    }
}
