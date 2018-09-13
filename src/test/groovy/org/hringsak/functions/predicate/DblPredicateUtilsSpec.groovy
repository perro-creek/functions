package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.DoubleFunction
import java.util.function.DoublePredicate
import java.util.stream.IntStream

import static com.mrll.javelin.cmcs.util.StreamUtils.filter
import static org.hringsak.functions.predicate.DblFilterUtils.dblFilter
import static org.hringsak.functions.predicate.DblPredicateUtils.*

class DblPredicateUtilsSpec extends Specification {

    static final int RAW_LIST_SIZE = 1000
    static final double DISTINCT_KEY_SIZE = 100.0D
    def keyExtractor = { double d -> Double.valueOf(d % DISTINCT_KEY_SIZE) }

    def 'double predicate passing target returns expected value'() {
        expect:
        def predicate = dblPredicate { d -> d % 2 == 0 } as DoublePredicate
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double predicate for bi-predicate returns expected'() {
        expect:
        def predicate = dblPredicate({ d1, d2 -> d1 == d2 }, 2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'inverse double predicate for bi-predicate returns expected'() {
        expect:
        def predicate = inverseDblPredicate({ d1, d2 -> d1 == d2 }, 2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    @Unroll
    def 'double constant passing booleanValue "#booleanValue" returns #expected'() {

        expect:
        def predicate = dblConstant booleanValue
        predicate.test(1.0D) == expected

        where:
        booleanValue | expected
        true         | true
        false        | false
    }

    def 'from double mapper passing target "#target" returns #expected'() {
        expect:
        def predicate = fromDblMapper { d -> d % 2 == 0.0D }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'not double returns expected value'() {
        expect:
        def predicate = notDbl { d -> d % 2 == 0.0D }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'is double string empty returns expected value'() {
        expect:
        def predicate = isDblStrEmpty { d -> d == 2.0D ? '' : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double string not empty returns expected value'() {
        expect:
        def predicate = isDblStrNotEmpty { d -> d == 2.0D ? String.valueOf(d) : '' }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = dblEqualsIgnoreCase({ d -> [(1.0D): 'One', (2.0D): 'Two', (3.0D): 'Three'].get(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double equal passing constant value returns expected value'() {
        expect:
        def predicate = isDblEqual(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isDblEqual({ d -> d + 1.0D }, 2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D] as double[]
    }

    def 'is double mapper equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isDblMapperEqual({ d -> String.valueOf(d) }, '2.0')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double not equal passing constant value returns expected value'() {
        expect:
        def predicate = isDblNotEqual(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'is double not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isDblNotEqual({ d -> d + 1.0D }, 2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D, 3.0D] as double[]
    }

    def 'is double mapper not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isDblMapperNotEqual({ d -> String.valueOf(d) }, '2.0')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'double contains passing function and constant value returns expected value'() {
        expect:
        def predicate = dblContains({ d -> [String.valueOf(d)] }, '2.0')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'inverse double contains passing collection and function returns expected value'() {
        expect:
        def predicate = inverseDblContains(['2.0'], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains key passing map returns expected value'() {
        expect:
        def predicate = dblContainsKey(['2.0': 2.0D], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains value passing map returns expected value'() {
        expect:
        def predicate = dblContainsValue([(2.0D): '2.0'], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains char passing function and search char returns expected value'() {
        expect:
        def predicate = dblContainsChar({ d -> String.valueOf(d) }, 50)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains sequence passing function and char sequence returns expected value'() {
        expect:
        def predicate = dblContainsSequence({ d -> String.valueOf(d) }, '2')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains ignore case passing function and search sequence returns expected value'() {
        expect:
        def predicate = dblContainsIgnoreCase({ d -> [(1.0D): '1.0 - One', (2.0D): '2.0 - Two', (3.0D): '3.0 - Three'].get(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double is alpha passing function returns expected value'() {
        expect:
        def predicate = dblIsAlpha { d -> d == 2.0D ? 'Two' : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double is alphanumeric passing function returns expected value'() {
        expect:
        def predicate = dblIsAlphanumeric { d -> d == 2.0D ? '2Two' : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double is numeric passing function returns expected'() {
        expect:
        def predicate = dblIsNumeric { d -> d == 2.0D ? '2' : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double starts with passing value "#extractedString" and "#prefix" returns "#expected"'() {
        expect:
        def predicate = dblStartsWith({ d -> String.valueOf(d) }, '2')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double starts with ignore case passing function returns expected value'() {
        expect:
        def predicate = dblStartsWithIgnoreCase({ d -> d == 2.0D ? 'Two - 2.0' : String.valueOf(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double ends with passing function returns expected value'() {
        expect:
        def predicate = dblEndsWith({ d -> d == 2.0D ? '2.0 - Two' : String.valueOf(d) }, 'Two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double ends with ignore case passing function returns expected value'() {
        expect:
        def predicate = dblEndsWithIgnoreCase({ d -> d == 2.0D ? '2.0 - Two' : String.valueOf(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double null passing function returns expected value'() {
        expect:
        def predicate = isDblNull { d -> d == 2.0D ? null : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double not null passing function returns expected value'() {
        expect:
        def predicate = isDblNotNull { d -> d == 2.0D ? null : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'double greater than passing constant value returns expected value'() {
        expect:
        def predicate = dblGt(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [3.0D] as double[]
    }

    def 'double greater than passing function and constant value returns expected value'() {
        expect:
        def function = { d -> [(1.0D): 'a', (2.0D): 'b', (3.0D): 'c'].get(d) } as DoubleFunction
        def predicate = dblGt(function, 'b')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [3.0D] as double[]
    }

    def 'double greater than or equal passing constant value returns expected value'() {
        expect:
        def predicate = dblGte(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D, 3.0D] as double[]
    }

    def 'double greater than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { d -> [(1.0D): 'a', (2.0D): 'b', (3.0D): 'c'].get(d) } as DoubleFunction
        def predicate = dblGte(function, 'b')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D, 3.0D] as double[]
    }

    def 'double less than passing constant value returns expected value'() {
        expect:
        def predicate = dblLt(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D] as double[]
    }

    def 'double less than passing function and constant value returns expected value'() {
        expect:
        def function = { d -> [(1.0D): 'a', (2.0D): 'b', (3.0D): 'c'].get(d) } as DoubleFunction
        def predicate = dblLt(function, 'b')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D] as double[]
    }

    def 'double less than or equal passing constant value returns expected value'() {
        expect:
        def predicate = dblLte(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 2.0D] as double[]
    }

    def 'double less than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { d -> [(1.0D): 'a', (2.0D): 'b', (3.0D): 'c'].get(d) } as DoubleFunction
        def predicate = dblLte(function, 'b')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 2.0D] as double[]
    }

    def 'is double collection empty passing function returns expected value'() {
        expect:
        def predicate = isDblCollEmpty { d -> d == 2.0D ? [] : [String.valueOf(d)] }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double collection not empty passing function returns expected value'() {
        expect:
        def predicate = isDblCollNotEmpty() { d -> d == 2.0D ? [] : [String.valueOf(d)] }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'double distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().length == DISTINCT_KEY_SIZE
    }

    double[] makeEntriesDistinctByKey() {
        IntStream.range(0, RAW_LIST_SIZE)
                .asDoubleStream()
                .filter(dblDistinctByKey(keyExtractor))
                .toArray()
    }

    def 'double distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().length == DISTINCT_KEY_SIZE
    }

    double[] makeEntriesDistinctByKeyParallel() {
        IntStream.range(0, RAW_LIST_SIZE).parallel()
                .asDoubleStream()
                .filter(dblDistinctByKeyParallel(keyExtractor))
                .toArray()
    }

    def 'map to double and filter returns expected value'() {
        expect:
        def predicate = mapToDblAndFilter({ String s -> Double.valueOf(s) }, { d -> d == 2.0D })
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == ['2.0']
    }

    def 'double map and filter returns expected value'() {
        expect:
        def predicate = dblMapAndFilter({ d -> String.valueOf(d) }, { s -> s == '2.0' })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }
}
