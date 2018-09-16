package org.hringsak.functions.predicate;

import java.util.Collection;

final class CollectionUtils {

    private CollectionUtils() {
    }

    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
