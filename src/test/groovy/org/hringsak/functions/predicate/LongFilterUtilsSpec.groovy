package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.LongFilterUtils.*
import static org.hringsak.functions.predicate.LongPredicateUtils.*

class LongFilterUtilsSpec extends Specification {

    def 'long generic filter returns expected values'() {
        expect:
        longFilter([1L, 2L, 3L] as long[], longGt(2L)) == [3L] as long[]
    }

    @Unroll
    def 'long generic filter returns #expected for #scenario parameter'() {

        expect:
        longFilter(longs, longConstant(true)) == expected

        where:
        scenario | longs          || expected
        'empty'  | [] as long[]   || [] as long[]
        'null'   | null as long[] || [] as long[]
    }

    def 'long filter to set returns expected results'() {
        expect:
        longFilterToSet([1L, 2L, 3L] as long[], longGt(2L)) == [3L] as Set
    }

    @Unroll
    def 'long filter to set returns #expected for #scenario parameter'() {

        expect:
        longFilterToSet(longs, longConstant(true)) == expected as Set

        where:
        scenario | longs          || expected
        'empty'  | [] as long[]   || [] as Set
        'null'   | null as long[] || [] as Set
    }

    def 'long filter with predicate and collector returns expected results'() {
        expect:
        longFilter([1L, 2L, 3L] as long[], LongFilterCollector.of(longGt(2L), toList())) == [3L]
    }

    @Unroll
    def 'long filter with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        longFilter(longs, longFilterAndThen(longConstant(true), toList())) == expected

        where:
        scenario | longs          || expected
        'empty'  | [] as long[]   || []
        'null'   | null as long[] || []
    }

    def 'long filter distinct returns list with distinct values'() {
        expect:
        longFilterDistinct([1L, 2L, 3L, 2L] as long[], isLongEqual(2L)) == [2L]
    }

    @Unroll
    def 'long filter distinct returns #expected for #scenario parameter'() {

        expect:
        longFilterDistinct(longs, longConstant(true)) == expected

        where:
        scenario | longs          || expected
        'empty'  | [] as long[]   || []
        'null'   | null as long[] || []
    }
}
