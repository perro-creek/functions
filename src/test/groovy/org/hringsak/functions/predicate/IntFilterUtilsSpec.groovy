package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.IntFilterUtils.*
import static org.hringsak.functions.predicate.IntPredicateUtils.*

class IntFilterUtilsSpec extends Specification {

    def 'int generic filter returns expected values'() {
        expect:
        intFilter([1, 2, 3] as int[], intGt(2)) == [3] as int[]
    }

    @Unroll
    def 'int generic filter returns #expected for #scenario parameter'() {

        expect:
        intFilter(ints, intConstant(true)) == expected

        where:
        scenario | ints          || expected
        'empty'  | [] as int[]   || [] as int[]
        'null'   | null as int[] || [] as int[]
    }

    def 'int filter to set returns expected results'() {
        expect:
        intFilterToSet([1, 2, 3] as int[], intGt(2)) == [3] as Set
    }

    @Unroll
    def 'int filter to set returns #expected for #scenario parameter'() {

        expect:
        intFilterToSet(ints, intConstant(true)) == expected as Set

        where:
        scenario | ints          || expected
        'empty'  | [] as int[]   || [] as Set
        'null'   | null as int[] || [] as Set
    }

    def 'int filter distinct returns list with distinct values'() {
        expect:
        intFilterDistinct([1, 2, 3, 2] as int[], isIntEqual(2)) == [2] as int[]
    }

    @Unroll
    def 'int filter distinct returns #expected for #scenario parameter'() {

        expect:
        intFilterDistinct(ints, intConstant(true)) == expected as int[]

        where:
        scenario | ints          || expected
        'empty'  | [] as int[]   || []
        'null'   | null as int[] || []
    }

    def 'int filter distinct with predicate and collector returns expected results'() {
        expect:
        intFilter([1, 2, 3] as int[], IntFilterCollector.of(intGt(2), toList())) == [3]
    }

    @Unroll
    def 'int filter with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        intFilter(ints, intFilterAndThen(intConstant(true), toList())) == expected

        where:
        scenario | ints          || expected
        'empty'  | [] as int[]   || []
        'null'   | null as int[] || []
    }

    def 'int filter with predicate and collector returns expected results'() {
        expect:
        intFilterDistinct([3, 1, 2, 3] as int[], IntFilterCollector.of(intGt(2), toList())) == [3]
    }

    @Unroll
    def 'int filter distinct with predicate and collector returns #expected for #scenario parameter'() {

        expect:
        intFilterDistinct(ints, intFilterAndThen(intConstant(true), toList())) == expected

        where:
        scenario | ints          || expected
        'empty'  | [] as int[]   || []
        'null'   | null as int[] || []
    }
}
