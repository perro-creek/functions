package org.perro.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.DoubleFunction
import java.util.function.DoublePredicate
import java.util.function.Function
import java.util.function.ToDoubleFunction

import static DblFilterUtils.dblFilter
import static DblPredicateUtils.*
import static FilterUtils.filter

class DblPredicateUtilsSpec extends Specification {

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

    def 'double not returns expected value'() {
        expect:
        def predicate = dblNot { d -> d % 2 == 0.0D }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'is double equal passing constant value returns expected value'() {
        expect:
        def predicate = isDblEqual(2.0D, 0.01D)
        def doubles = [1.0001D, 2.0001D, 3.0001D] as double[]
        dblFilter(doubles, predicate) == [2.0001D] as double[]
    }

    def 'is double equal with delta passing constant value returns expected value'() {
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

    def 'is double equal passing function and constant value with delta returns expected value'() {
        expect:
        def predicate = isDblEqual({ d -> d + 1.0D }, dblWithDelta(2.0D, 0.01D))
        def doubles = [1.0001D, 2.0001D, 3.0001D] as double[]
        dblFilter(doubles, predicate) == [1.0001D] as double[]
    }

    def 'is double not equal passing constant value returns expected value'() {
        expect:
        def predicate = isDblNotEqual(2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'is double not equal passing constant value and delta returns expected value'() {
        expect:
        def predicate = isDblNotEqual(2.0D, 0.01D)
        def doubles = [1.0001D, 2.0001D, 3.0001D] as double[]
        dblFilter(doubles, predicate) == [1.0001D, 3.0001D] as double[]
    }

    def 'is double not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isDblNotEqual({ d -> d + 1.0D }, 2.0D)
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D, 3.0D] as double[]
    }

    def 'is double not equal passing function and constant value with delta returns expected value'() {
        expect:
        def predicate = isDblNotEqual({ d -> d + 1.0D }, dblWithDelta(2.0D, 0.01D))
        def doubles = [1.0001D, 2.0001D, 3.0001D] as double[]
        dblFilter(doubles, predicate) == [2.0001D, 3.0001D] as double[]
    }

    def 'double to object contains returns expected value'() {
        expect:
        def predicate = dblToObjContains(['2.0'], { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double to object contains passing null collection returns expected value'() {
        expect:
        def predicate = dblToObjContains(null, { d -> String.valueOf(d) })
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [] as double[]
    }

    def 'inverse double to objects contains returns expected value'() {
        expect:
        def predicate = inverseDblToObjContains({ d -> d == 1.0D ? null : [String.valueOf(d)] }, '2.0')
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'object to double contains returns expected value'() {
        expect:
        def predicate = objToDblContains([2.0D] as double[], { String s -> Double.valueOf(s) })
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == ['2.0']
    }

    def 'object to double contains passing null double array returns expected value'() {
        expect:
        def predicate = objToDblContains(null as double[], { String s -> Double.valueOf(s) })
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == []
    }

    def 'inverse object to doubles contains returns expected value'() {
        expect:
        def predicate = inverseObjToDblContains({ String s -> [Double.valueOf(s)] as double[] }, 2.0D)
        def strings = ['1.0', '2.0', '3.0']
        filter(strings, predicate) == ['2.0']
    }

    def 'double is null passing function returns expected value'() {
        expect:
        def predicate = dblIsNull() { d -> d == 2.0D ? null : String.valueOf(d) }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'double is not null passing function returns expected value'() {
        expect:
        def predicate = dblIsNotNull() { d -> d == 2.0D ? null : String.valueOf(d) }
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

    def 'to double greater than passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1.0D, b: 2.0D, c: 3.0D].get(t) } as ToDoubleFunction<String>
        def predicate = toDblGt(function, 2.0D)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['c']
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

    def 'to double greater than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1.0D, b: 2.0D, c: 3.0D].get(t) } as ToDoubleFunction<String>
        def predicate = toDblGte(function, 2.0D)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['b', 'c']
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

    def 'to double less than passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1.0D, b: 2.0D, c: 3.0D].get(t) } as ToDoubleFunction<String>
        def predicate = toDblLt(function, 2.0D)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a']
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

    def 'to double less than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1.0D, b: 2.0D, c: 3.0D].get(t) } as ToDoubleFunction<String>
        def predicate = toDblLte(function, 2.0D)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a', 'b']
    }

    def 'is double collection empty passing function returns expected value'() {
        expect:
        def predicate = isDblCollEmpty { d -> d == 2.0D ? [] : [String.valueOf(d)] }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [2.0D] as double[]
    }

    def 'is double collection not empty passing function returns expected value'() {
        expect:
        def predicate = isDblCollNotEmpty { d -> d == 2.0D ? [] : [String.valueOf(d)] }
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblFilter(doubles, predicate) == [1.0D, 3.0D] as double[]
    }

    def 'is double array empty passing function returns expected value'() {
        expect:
        def predicate = isDblArrayEmpty { String s -> s == 'b' ? [] as double[] : [s.charAt(0)] as double[] }
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['b']
    }

    def 'is double array not empty passing function returns expected value'() {
        expect:
        def predicate = isDblArrayNotEmpty() { String s -> s == 'b' ? [] as double[] : [s.charAt(0)] as double[] }
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a', 'c']
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
