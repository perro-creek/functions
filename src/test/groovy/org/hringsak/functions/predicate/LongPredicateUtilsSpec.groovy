package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.function.LongFunction
import java.util.function.LongPredicate
import java.util.stream.LongStream

import static org.hringsak.functions.predicate.FilterUtils.filter
import static org.hringsak.functions.predicate.LongFilterUtils.longFilter
import static org.hringsak.functions.predicate.LongPredicateUtils.*

class LongPredicateUtilsSpec extends Specification {

    static final long RAW_LIST_SIZE = 1000L
    static final long DISTINCT_KEY_SIZE = 100L
    def keyExtractor = { long l -> Long.valueOf(l % DISTINCT_KEY_SIZE) }

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

    def 'from long mapper passing target "#target" returns #expected'() {
        expect:
        def predicate = fromLongMapper { l -> l % 2L == 0L }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long not returns expected value'() {
        expect:
        def predicate = longNot { l -> l % 2L == 0L }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    @Unroll
    def 'is long sequence empty with function value "#functionValue" returns expected value'() {

        expect:
        def predicate = isLongSeqEmpty { l -> l == 2L ? functionValue : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == expected as long[]

        where:
        functionValue || expected
        'test'        || []
        ''            || [2L]
        null          || [2L]
    }

    def 'is long sequence not empty returns expected value'() {
        expect:
        def predicate = isLongSeqNotEmpty { l -> l == 2L ? String.valueOf(l) : '' }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = longEqualsIgnoreCase({ l -> [(1L): 'One', (2L): 'Two', (3L): 'Three'].get(l) }, 'two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long not equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = longNotEqualsIgnoreCase({ l -> [(1L): 'One', (2L): 'Two', (3L): 'Three'].get(l) }, 'two')
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

    def 'is long mapper equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isLongMapperEqual({ l -> String.valueOf(l) }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
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

    def 'is long mapper not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isLongMapperNotEqual({ l -> String.valueOf(l) }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'long to objects contains returns expected value'() {
        expect:
        def predicate = longToObjsContains({ l -> l == 1L ? null : [String.valueOf(l)] }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'object to longs contains returns expected value'() {
        expect:
        def predicate = objToLongsContains({ String s -> [Long.valueOf(s)] as long[] }, 2L)
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'inverse long to object contains returns expected value'() {
        expect:
        def predicate = inverseLongToObjContains(['2'], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'inverse long to object contains passing null collection and function returns expected value'() {
        expect:
        def predicate = inverseLongToObjContains(null, { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [] as long[]
    }

    def 'inverse object to long contains returns expected value'() {
        expect:
        def predicate = inverseObjToLongContains([2L] as long[], { String s -> Long.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'inverse object to long contains passing null long array returns expected value'() {
        expect:
        def predicate = inverseObjToLongContains(null as long[], { String s -> Long.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == []
    }

    def 'long contains key passing map returns expected value'() {
        expect:
        def predicate = longContainsKey(['2': 2L], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains key passing null map returns expected value'() {
        expect:
        def predicate = longContainsKey(null, { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [] as long[]
    }

    def 'long contains value passing map returns expected value'() {
        expect:
        def predicate = longContainsValue([(2L): '2'], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains value passing null map returns expected value'() {
        expect:
        def predicate = longContainsValue(null, { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [] as long[]
    }
    def 'long contains char passing function and search char returns expected value'() {
        expect:
        def predicate = longContainsChar({ l -> String.valueOf(l) }, 50)
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains sequence passing function and char sequence returns expected value'() {
        expect:
        def predicate = longContainsSequence({ l -> String.valueOf(l) }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains ignore case passing function and search sequence returns expected value'() {
        expect:
        def predicate = longContainsIgnoreCase({ l -> [(1L): '1 - One', (2L): '2 - Two', (3L): '3 - Three'].get(l) }, 'two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long is alpha passing function returns expected value'() {
        expect:
        def predicate = longIsAlpha { l -> l == 2L ? 'Two' : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long is alphanumeric passing function returns expected value'() {
        expect:
        def predicate = longIsAlphanumeric { l -> l == 2L ? '2Two' : String.valueOf((double) l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long is numeric passing function returns expected'() {
        expect:
        def predicate = longIsNumeric { l -> l == 2L ? '2' : String.valueOf((double) l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long starts with passing value "#extractedString" and "#prefix" returns "#expected"'() {
        expect:
        def predicate = longStartsWith({ l -> String.valueOf(l) }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long starts with ignore case passing function returns expected value'() {
        expect:
        def predicate = longStartsWithIgnoreCase({ l -> l == 2L ? 'Two - 2' : String.valueOf(l) }, 'two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long ends with passing function returns expected value'() {
        expect:
        def predicate = longEndsWith({ l -> l == 2L ? '2 - Two' : String.valueOf(l) }, 'Two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long ends with ignore case passing function returns expected value'() {
        expect:
        def predicate = longEndsWithIgnoreCase({ l -> l == 2L ? '2 - Two' : String.valueOf(l) }, 'two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'is long null passing function returns expected value'() {
        expect:
        def predicate = isLongNull { l -> l == 2L ? null : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'is long not null passing function returns expected value'() {
        expect:
        def predicate = isLongNotNull { l -> l == 2L ? null : String.valueOf(l) }
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

    def 'long distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().length == DISTINCT_KEY_SIZE
    }

    long[] makeEntriesDistinctByKey() {
        LongStream.range(0L, RAW_LIST_SIZE)
                .filter(longDistinctByKey(keyExtractor))
                .toArray()
    }

    def 'long distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().length == DISTINCT_KEY_SIZE
    }

    long[] makeEntriesDistinctByKeyParallel() {
        LongStream.range(0L, RAW_LIST_SIZE).parallel()
                .filter(longDistinctByKeyParallel(keyExtractor))
                .toArray()
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
