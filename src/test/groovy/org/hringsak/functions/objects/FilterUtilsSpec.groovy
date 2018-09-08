package org.hringsak.functions.objects

import org.hringsak.functions.objects.FilterCollector
import org.hringsak.functions.objects.FilterUtils
import org.hringsak.functions.objects.PredicateUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.objects.FilterUtils.filterAndThen

class FilterUtilsSpec extends Specification {

    def 'generic filter returns expected values'() {
        expect:
        FilterUtils.filter([1, 2, 3], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'generic filter returns #expected for #scenario parameter'() {

        expect:
        FilterUtils.filter(contents, PredicateUtils.predicateConstant(true)) == expected

        where:
        scenario                     | contents || expected
        'empty'                      | []       || []
        'null'                       | null     || []
        'collection containing null' | [null]   || [null]
    }

    def 'filter to set returns expected results'() {
        expect:
        FilterUtils.filterToSet([1, 2, 3], { i -> i % 2 == 0 }) == [2] as Set
    }

    @Unroll
    def 'filter to set returns #expected for #scenario parameter'() {

        expect:
        FilterUtils.filterToSet(collection, PredicateUtils.predicateConstant(true)) == expected as Set

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
        FilterUtils.filter(list, FilterCollector.of(predicate, toList())) == ['TWO']
    }

    @Unroll
    def 'filter with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        def predicate = PredicateUtils.predicateConstant(true) as Predicate
        FilterUtils.filter(collection, filterAndThen(predicate, toList())) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'filter distinct returns list with distinct values'() {
        expect:
        FilterUtils.filterDistinct([1, 2, 3, 2], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'filter distinct returns #expected for #scenario parameter'() {

        expect:
        FilterUtils.filterDistinct(collection, PredicateUtils.predicateConstant(true)) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }
}
