package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.FilterUtils.filterCollector

class FilterUtilsSpec extends Specification {

    def 'generic filter returns expected values'() {
        expect:
        FilterUtils.filter([1, 2, 3], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'generic filter returns empty list for #scenario parameter'() {

        expect:
        FilterUtils.filter(contents, PredicateUtils.predicate(false)) == []

        where:
        scenario | contents
        'empty'  | []
        'null'   | null
    }

    def 'filter to set returns expected results'() {
        expect:
        FilterUtils.filterToSet([1, 2, 3], { i -> i % 2 == 0 }) == [2] as Set
    }

    @Unroll
    def 'filter to set returns empty list for #scenario parameter'() {

        expect:
        FilterUtils.filterToSet(collection, PredicateUtils.predicate(true)) == [] as Set

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    def 'filter with predicate and collector returns expected results'() {

        given:
        def list = ['ONE', 'TWO', 'THREE']
        def predicate = { String s -> s == 'TWO' } as Predicate<String>

        expect:
        FilterUtils.filter(list, FilterCollector.of(predicate, toList())) == ['TWO']
    }

    @Unroll
    def 'filter with predicate and collector returns empty list for #scenario parameter'() {

        expect:
        def predicate = PredicateUtils.predicate(true) as Predicate
        FilterUtils.filter(collection, filterCollector(predicate, toList())) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    def 'filter distinct returns list with distinct values'() {
        expect:
        FilterUtils.filterDistinct([1, 2, 3, 2], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'filter distinct returns empty list for #scenario parameter'() {

        expect:
        FilterUtils.filterDistinct(collection, PredicateUtils.predicate(true)) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }
}
