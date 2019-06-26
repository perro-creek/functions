package org.perro.functions.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> List<T> defaultList(List<T> targetList) {
        return targetList == null ? new ArrayList<>() : targetList;
    }
}
