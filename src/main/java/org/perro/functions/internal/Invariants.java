package org.perro.functions.internal;

/**
 * Internal class intended only to be used by classes in this library.
 */
public final class Invariants {

    private Invariants() {
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }
}
