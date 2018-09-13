package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.LongFunction
import java.util.function.LongPredicate
import java.util.stream.LongStream

import static com.mrll.javelin.cmcs.util.StreamUtils.filter
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

    def 'not long returns expected value'() {
        expect:
        def predicate = notLong { l -> l % 2L == 0L }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [1L, 3L] as long[]
    }

    def 'is long string empty returns expected value'() {
        expect:
        def predicate = isLongStrEmpty { l -> l == 2L ? '' : String.valueOf(l) }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'is long string not empty returns expected value'() {
        expect:
        def predicate = isLongStrNotEmpty { l -> l == 2L ? String.valueOf(l) : '' }
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long equals ignore case passing function and constant value returns expected value'() {
        expect:
        def predicate = longEqualsIgnoreCase({ l -> [(1L): 'One', (2L): 'Two', (3L): 'Three'].get(l) }, 'two')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
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

    def 'long contains passing function and constant value returns expected value'() {
        expect:
        def predicate = longContains({ l -> [String.valueOf(l)] }, '2')
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'inverse long contains passing collection and function returns expected value'() {
        expect:
        def predicate = inverseLongContains(['2'], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains key passing map returns expected value'() {
        expect:
        def predicate = longContainsKey(['2': 2L], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
    }

    def 'long contains value passing map returns expected value'() {
        expect:
        def predicate = longContainsValue([(2L): '2'], { l -> String.valueOf(l) })
        def longs = [1L, 2L, 3L] as long[]
        longFilter(longs, predicate) == [2L] as long[]
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
