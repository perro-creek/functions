package org.perro.functions.mapper;

import org.perro.functions.internal.CollectionUtils;
import org.perro.functions.internal.Pair;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class PairWithBuilder<U, V> {

    private final AtomicInteger idx;
    private final List<V> pairedList;

    PairWithBuilder(List<V> pairedList) {
        idx = new AtomicInteger();
        this.pairedList = CollectionUtils.defaultList(pairedList);
    }

    Pair<U, V> buildPair(U extracted) {
        int i = idx.getAndIncrement();
        return (i < pairedList.size()) ? Pair.of(extracted, pairedList.get(i)) : Pair.of(extracted, null);
    }
}
