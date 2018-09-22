package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.DoubleFunction
import java.util.function.DoublePredicate
import java.util.function.Function
import java.util.stream.IntStream

import static java.util.function.Function.identity
import static org.hringsak.functions.predicate.DblFilterUtils.dblFilter
import static org.hringsak.functions.predicate.DblPredicateUtils.*
import static org.hringsak.functions.predicate.FilterUtils.filter
import static org.hringsak.functions.predicate.PredicateUtils.noCharsMatch

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

    def 'double not returns expected value'() {
        expect:
        def predicate = dblNot { d -> d % 2 == 0.0D }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    @Unroll
    def 'is double sequence empty with function value "#functionValue" returns expected value'() {

        expect:
        def predicate = isDblSeqEmpty { d -> d == 2.0D ? functionValue : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == expected as double[]

        where:
        functionValue || expected
        'test'        || []
        ''            || [2.0D]
        null          || [2.0D]
    }

    def 'is double sequence not empty returns expected value'() {
        expect:
        def predicate = isDblSeqNotEmpty { d -> d == 2.0D ? String.valueOf(d) : '' }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = dblEqualsIgnoreCase({ d -> [(1.0D): 'One', (2.0D): 'Two', (3.0D): 'Three'].get(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double not equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = dblNotEqualsIgnoreCase({ d -> [(1.0D): 'One', (2.0D): 'Two', (3.0D): 'Three'].get(d) }, 'two')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
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

    def 'double to objects contains returns expected value'() {
        expect:
        def predicate = dblToObjsContains({ d -> d == 1.0D ? null : [String.valueOf(d)] }, '2.0')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'object to doubles contains returns expected value'() {
        expect:
        def predicate = objToDblsContains({ String s -> [Double.valueOf(s)] as double[] }, 2.0D)
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == ['2.0']
    }

    def 'inverse double to object contains returns expected value'() {
        expect:
        def predicate = inverseDblToObjContains(['2.0'], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'inverse double to object contains passing null collection returns expected value'() {
        expect:
        def predicate = inverseDblToObjContains(null, { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [] as double[]
    }

    def 'inverse object to double contains returns expected value'() {
        expect:
        def predicate = inverseObjToDblContains([2.0D] as double[], { String s -> Double.valueOf(s) })
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == ['2.0']
    }

    def 'inverse object to double contains passing null double array returns expected value'() {
        expect:
        def predicate = inverseObjToDblContains(null as double[], { String s -> Double.valueOf(s) })
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == []
    }

    def 'double contains key passing map returns expected value'() {
        expect:
        def predicate = dblContainsKey(['2.0': 2.0D], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains key passing null map returns expected value'() {
        expect:
        def predicate = dblContainsKey(null, { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [] as double[]
    }

    def 'double contains value passing map returns expected value'() {
        expect:
        def predicate = dblContainsValue([(2.0D): '2.0'], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double contains value passing null map returns expected value'() {
        expect:
        def predicate = dblContainsValue(null, { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [] as double[]
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

    @Unroll
    def 'double any characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = dblAnyCharsMatch({ d -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1.0D) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | true
        '123'     | false
        null      | false
        ''        | false
    }

    @Unroll
    def 'double all characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = dblAllCharsMatch({ d -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1.0D) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | false
        '123'     | false
        null      | false
        ''        | false
    }

    @Unroll
    def 'double no characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = dblNoCharsMatch({ d -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1.0D) == expected

        where:
        target    | expected
        'test'    | false
        'test123' | false
        '123'     | true
        null      | false
        ''        | true
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

    def 'object to doubles all match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asDoubleStream().toArray() } as Function
        def predicate = objToDblsAllMatch(function, { d -> d > 100.0D })
        filter(['test', ' ', '', null], predicate) == ['test', '']
    }

    def 'double to objects all match returns expected value'() {
        expect:
        def function = { d -> [String.valueOf(d)] }
        def predicate = dblToObjsAllMatch(function, { s -> s == '2.0' })
        dblFilter([1.0D, 2.0D, 3.0D] as double[], predicate) == [2.0D] as double[]
    }

    def 'object to doubles any match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asDoubleStream().toArray() }
        def predicate = objToDblsAnyMatch(function, { d -> d == 101.0D })
        filter(['test', '', null], predicate) == ['test']
    }

    def 'double to objects any match returns expected value'() {
        expect:
        def function = { d -> [String.valueOf(d)] }
        def predicate = dblToObjsAnyMatch(function, { s -> s == '2.0' })
        dblFilter([1.0D, 2.0D, 3.0D] as double[], predicate) == [2.0D] as double[]
    }

    def 'object to doubles none match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asDoubleStream().toArray() }
        def predicate = objToDblsNoneMatch(function, { d -> d > 100.0D })
        filter(['test', '', null], predicate) == ['']
    }

    def 'double to objects none match returns expected value'() {
        expect:
        def function = { d -> [String.valueOf(d)] }
        def predicate = dblToObjsNoneMatch(function, { s -> s == '2.0' })
        dblFilter([1.0D, 2.0D, 3.0D] as double[], predicate) == [1.0D, 3.0D] as double[]
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
