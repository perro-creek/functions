package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.FilterUtils.*
import static org.hringsak.functions.predicate.PredicateUtils.constant

class FilterUtilsSpec extends Specification {

    def 'generic filter returns expected values'() {
        expect:
        filter([1, 2, 3], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'generic filter returns #expected for #scenario parameter'() {

        expect:
        filter(contents, constant(true)) == expected

        where:
        scenario                     | contents || expected
        'empty'                      | []       || []
        'null'                       | null     || []
        'collection containing null' | [null]   || [null]
    }

    def 'filter to set returns expected results'() {
        expect:
        filterToSet([1, 2, 3], { i -> i % 2 == 0 }) == [2] as Set
    }

    @Unroll
    def 'filter to set returns #expected for #scenario parameter'() {

        expect:
        filterToSet(collection, constant(true)) == expected as Set

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'filter with predicate and collector returns expected results'() {

        given:
        def list = ['ONE', 'TWO', 'THREE']
        def predicate = { String s -> s == 'TWO' } as Predicate<String>

        expect:
        filter(list, FilterCollector.of(predicate, toList())) == ['TWO']
    }

    @Unroll
    def 'filter with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        def predicate = constant(true) as Predicate
        filter(collection, filterAndThen(predicate, toList())) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'filter distinct returns list with distinct values'() {
        expect:
        filterDistinct([1, 2, 3, 2], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'filter distinct returns #expected for #scenario parameter'() {

        expect:
        filterDistinct(collection, constant(true)) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }
}
