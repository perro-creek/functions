package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.function.LongFunction
import java.util.function.LongPredicate
import java.util.function.ToLongFunction

import static org.hringsak.functions.predicate.FilterUtils.filter
import static org.hringsak.functions.predicate.LongFilterUtils.longFilter
import static org.hringsak.functions.predicate.LongPredicateUtils.*

class LongPredicateUtilsSpec extends Specification {

    def 'long predicate passing target returns expected value'() {
        expect:
        def predicate = longPredicate { l -> l % 2L == 0L } as LongPredicate
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long predicate for bi-predicate returns expected'() {
        expect:
        def predicate = longPredicate({ d1, d2 -> d1 == d2 }, 2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'inverse long predicate for bi-predicate returns expected'() {
        expect:
        def predicate = inverseLongPredicate({ d1, d2 -> d1 == d2 }, 2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    @Unroll
    def 'long constant passing booleanValue "#booleanValue" returns #expected'() {

        expect:
        def predicate = longConstant booleanValue
        predicate.test(1L) == expected

        where:
        booleanValue | expected
        true         | true
        false        | false
    }

    def 'long not returns expected value'() {
        expect:
        def predicate = longNot { l -> l % 2L == 0L }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'is long equal passing constant value returns expected value'() {
        expect:
        def predicate = isLongEqual(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'is long equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isLongEqual({ l -> l + 1L }, 2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L] as long[]
    }

    def 'is long not equal passing constant value returns expected value'() {
        expect:
        def predicate = isLongNotEqual(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'is long not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isLongNotEqual({ l -> l + 1L }, 2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L, 3L] as long[]
    }

    def 'long to object contains returns expected value'() {
        expect:
        def predicate = longToObjContains(['2'], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long to object contains passing null collection and function returns expected value'() {
        expect:
        def predicate = longToObjContains(null, { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [] as long[]
    }

    def 'inverse long to objects contains returns expected value'() {
        expect:
        def predicate = inverseLongToObjContains({ l -> l == 1L ? null : [String.valueOf(l)] }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'object to long contains returns expected value'() {
        expect:
        def predicate = objToLongContains([2L] as long[], { String s -> Long.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'object to long contains passing null long array returns expected value'() {
        expect:
        def predicate = objToLongContains(null as long[], { String s -> Long.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == []
    }

    def 'inverse object to longs contains returns expected value'() {
        expect:
        def predicate = inverseObjToLongContains({ String s -> [Long.valueOf(s)] as long[] }, 2L)
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'long is null passing function returns expected value'() {
        expect:
        def predicate = longIsNull() { l -> l == 2L ? null : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long is not null passing function returns expected value'() {
        expect:
        def predicate = longIsNotNull() { l -> l == 2L ? null : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'long greater than passing constant value returns expected value'() {
        expect:
        def predicate = longGt(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [3L] as long[]
    }

    def 'long greater than passing function and constant value returns expected value'() {
        expect:
        def function = { l -> [(1L): 'a', (2L): 'b', (3L): 'c'].get(l) } as LongFunction
        def predicate = longGt(function, 'b')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [3L] as long[]
    }

    def 'to long greater than passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1L, b: 2L, c: 3L].get(t) } as ToLongFunction<String>
        def predicate = toLongGt(function, 2L)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['c']
    }

    def 'long greater than or equal passing constant value returns expected value'() {
        expect:
        def predicate = longGte(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L, 3L] as long[]
    }

    def 'long greater than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { l -> [(1L): 'a', (2L): 'b', (3L): 'c'].get(l) } as LongFunction
        def predicate = longGte(function, 'b')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L, 3L] as long[]
    }

    def 'to long greater than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1L, b: 2L, c: 3L].get(t) } as ToLongFunction<String>
        def predicate = toLongGte(function, 2L)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['b', 'c']
    }

    def 'long less than passing constant value returns expected value'() {
        expect:
        def predicate = longLt(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L] as long[]
    }

    def 'long less than passing function and constant value returns expected value'() {
        expect:
        def function = { l -> [(1L): 'a', (2L): 'b', (3L): 'c'].get(l) } as LongFunction
        def predicate = longLt(function, 'b')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L] as long[]
    }

    def 'to long less than passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1L, b: 2L, c: 3L].get(t) } as ToLongFunction<String>
        def predicate = toLongLt(function, 2L)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a']
    }

    def 'long less than or equal passing constant value returns expected value'() {
        expect:
        def predicate = longLte(2L)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 2L] as long[]
    }

    def 'long less than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { l -> [(1L): 'a', (2L): 'b', (3L): 'c'].get(l) } as LongFunction
        def predicate = longLte(function, 'b')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 2L] as long[]
    }

    def 'to long less than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { t -> [a: 1L, b: 2L, c: 3L].get(t) } as ToLongFunction<String>
        def predicate = toLongLte(function, 2L)
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a', 'b']
    }

    def 'is long collection empty passing function returns expected value'() {
        expect:
        def predicate = isLongCollEmpty { l -> l == 2L ? [] : [String.valueOf(l)] }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'is long collection not empty passing function returns expected value'() {
        expect:
        def predicate = isLongCollNotEmpty() { l -> l == 2L ? [] : [String.valueOf(l)] }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'is long array empty passing function returns expected value'() {
        expect:
        def predicate = isLongArrayEmpty() { String s -> s == 'b' ? [] as long[] : [s.charAt(0)] as long[] }
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['b']
    }

    def 'is long array not empty passing function returns expected value'() {
        expect:
        def predicate = isLongArrayNotEmpty() { String s -> s == 'b' ? [] as long[] : [s.charAt(0)] as long[] }
        def objs = ['a', 'b', 'c']
        filter(objs, predicate) == ['a', 'c']
    }

    def 'object to longs all match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asLongStream().toArray() } as Function
        def predicate = objToLongsAllMatch(function, { l -> l > 100L })
        filter(['test', ' ', '', null], predicate) == ['test', '']
    }

    def 'long to objects all match returns expected value'() {
        expect:
        def function = { l -> [String.valueOf(l)] }
        def predicate = longToObjectsAllMatch(function, { s -> s == '2' })
        longFilter([1L, 2L, 3L] as long[], predicate) == [2L] as long[]
    }

    def 'object to longs any match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asLongStream().toArray() }
        def predicate = objToLongsAnyMatch(function, { l -> l == 101L })
        filter(['test', '', null], predicate) == ['test']
    }

    def 'long to objects any match returns expected value'() {
        expect:
        def function = { l -> [String.valueOf(l)] }
        def predicate = longToObjectsAnyMatch(function, { s -> s == '2' })
        longFilter([1L, 2L, 3L] as long[], predicate) == [2L] as long[]
    }

    def 'object to longs none match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().asLongStream().toArray() }
        def predicate = objToLongsNoneMatch(function, { l -> l > 100L })
        filter(['test', '', null], predicate) == ['']
    }

    def 'long to objects none match returns expected value'() {
        expect:
        def function = { l -> [String.valueOf(l)] }
        def predicate = longToObjectsNoneMatch(function, { s -> s == '2' })
        longFilter([1L, 2L, 3L] as long[], predicate) == [1L, 3L] as long[]
    }

    def 'map to long and filter returns expected value'() {
        expect:
        def predicate = mapToLongAndFilter({ String s -> Long.valueOf(s) }, { l -> l == 2L })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'long map and filter returns expected value'() {
        expect:
        def predicate = longMapAndFilter({ l -> String.valueOf(l) }, { s -> s == '2' })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }
}
