package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.DblFilterUtils.*
import static org.hringsak.functions.predicate.DblPredicateUtils.*

class DblFilterUtilsSpec extends Specification {

    def 'double generic filter returns expected values'() {
        expect:
        dblFilter([1.0D, 2.0D, 3.0D] as double[], dblGt(2.0D)) == [3.0D] as double[]
    }

    @Unroll
    def 'double generic filter returns #expected for #scenario parameter'() {

        expect:
        dblFilter(doubles, dblConstant(true)) == expected

        where:
        scenario                     | doubles            || expected
        'empty'                      | [] as double[]     || [] as double[]
        'null'                       | null as double[]   || [] as double[]
    }

    def 'double filter to set returns expected results'() {
        expect:
        dblFilterToSet([1.0D, 2.0D, 3.0D] as double[], dblGt(2.0D)) == [3.0D] as Set
    }

    @Unroll
    def 'double filter to set returns #expected for #scenario parameter'() {

        expect:
        dblFilterToSet(doubles, dblConstant(true)) == expected as Set

        where:
        scenario                     | doubles            || expected
        'empty'                      | [] as double[]     || [] as Set
        'null'                       | null as double[]   || [] as Set
    }

    def 'double filter with predicate and collector returns expected results'() {
        expect:
        dblFilter([1.0D, 2.0D, 3.0D] as double[], DoubleFilterCollector.of(dblGt(2.0D), toList())) == [3.0D]
    }

    @Unroll
    def 'double filter with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        dblFilter(doubles, dblFilterAndThen(dblConstant(true), toList())) == expected

        where:
        scenario                     | doubles            || expected
        'empty'                      | [] as double[]     || []
        'null'                       | null as double[]   || []
    }

    def 'double filter distinct returns list with distinct values'() {
        expect:
        dblFilterDistinct([1.0D, 2.0D, 3.0D, 2.0D] as double[], isDblEqual(2.0D)) == [2.0D]
    }

    @Unroll
    def 'double filter distinct returns #expected for #scenario parameter'() {

        expect:
        dblFilterDistinct(doubles, dblConstant(true)) == expected

        where:
        scenario                     | doubles            || expected
        'empty'                      | [] as double[]     || []
        'null'                       | null as double[]   || []
    }
}
