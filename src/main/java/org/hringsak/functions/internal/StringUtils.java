package org.hringsak.functions.internal;

/**
 * Internal class intended only to be used by classes in this library.
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String target) {
        return target == null || target.isEmpty();
    }

    public static String defaultString(String target) {
        return target == null ? "" : target;
    }

    public static String toUpperCase(String target) {
        return target == null ? null : target.toUpperCase();
    }
}
