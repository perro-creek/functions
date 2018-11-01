package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function
import java.util.function.IntFunction
import java.util.function.IntPredicate
import java.util.stream.IntStream

import static org.hringsak.functions.predicate.FilterUtils.filter
import static org.hringsak.functions.predicate.IntFilterUtils.intFilter
import static org.hringsak.functions.predicate.IntPredicateUtils.*

class IntPredicateUtilsSpec extends Specification {

    static final int RAW_LIST_SIZE = 1000
    static final int DISTINCT_KEY_SIZE = 100
    def keyExtractor = { int i -> Integer.valueOf(i % DISTINCT_KEY_SIZE) }

    def 'int predicate passing target returns expected value'() {
        expect:
        def predicate = intPredicate { i -> i % 2 == 0 } as IntPredicate
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int predicate for bi-predicate returns expected'() {
        expect:
        def predicate = intPredicate({ d1, d2 -> d1 == d2 }, 2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'inverse int predicate for bi-predicate returns expected'() {
        expect:
        def predicate = inverseIntPredicate({ d1, d2 -> d1 == d2 }, 2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    @Unroll
    def 'int constant passing booleanValue "#booleanValue" returns #expected'() {

        expect:
        def predicate = intConstant booleanValue
        predicate.test(1) == expected

        where:
        booleanValue | expected
        true         | true
        false        | false
    }

    def 'int not returns expected value'() {
        expect:
        def predicate = intNot { i -> i % 2 == 0 }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 3] as int[]
    }

    def 'is int equal passing constant value returns expected value'() {
        expect:
        def predicate = isIntEqual(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'is int equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isIntEqual({ i -> i + 1 }, 2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1] as int[]
    }

    def 'is int not equal passing constant value returns expected value'() {
        expect:
        def predicate = isIntNotEqual(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 3] as int[]
    }

    def 'is int not equal passing function and constant value returns expected value'() {
        expect:
        def predicate = isIntNotEqual({ i -> i + 1 }, 2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2, 3] as int[]
    }

    def 'int to object contains returns expected value'() {
        expect:
        def predicate = intToObjContains(['2'], { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int to object contains passing null collection returns expected value'() {
        expect:
        def predicate = intToObjContains(null, { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [] as int[]
    }

    def 'inverse int to object contains returns expected value'() {
        expect:
        def predicate = inverseIntToObjContains({ i -> i == 1 ? null : [String.valueOf(i)] }, '2')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'object to ints contains returns expected value'() {
        expect:
        def predicate = objToIntsContains({ String s -> [Integer.valueOf(s)] as int[] }, 2)
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'inverse object to int contains returns expected value'() {
        expect:
        def predicate = inverseObjToIntContains([2] as int[], { String s -> Integer.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'inverse object to int contains passing null int array returns expected value'() {
        expect:
        def predicate = inverseObjToIntContains(null as int[], { String s -> Integer.valueOf(s) })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == []
    }

    def 'int contains key passing map returns expected value'() {
        expect:
        def predicate = intContainsKey(['2': 2], { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int contains key passing null map returns expected value'() {
        expect:
        def predicate = intContainsKey(null, { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [] as int[]
    }

    def 'int contains value passing map returns expected value'() {
        expect:
        def predicate = intContainsValue([(2): '2'], { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int contains value passing null map returns expected value'() {
        expect:
        def predicate = intContainsValue(null, { i -> String.valueOf(i) })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [] as int[]
    }

    def 'int contains char passing function and search char returns expected value'() {
        expect:
        def predicate = intContainsChar({ i -> String.valueOf(i) }, 50)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int contains sequence passing function and char sequence returns expected value'() {
        expect:
        def predicate = intContainsSequence({ i -> String.valueOf(i) }, '2')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int contains ignore case passing function and search sequence returns expected value'() {
        expect:
        def predicate = intContainsIgnoreCase({ i -> [(1): '1 - One', (2): '2 - Two', (3): '3 - Three'].get(i) }, 'two')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int is alpha passing function returns expected value'() {
        expect:
        def predicate = intIsAlpha { i -> i == 2 ? 'Two' : String.valueOf(i) }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int is alphanumeric passing function returns expected value'() {
        expect:
        def predicate = intIsAlphanumeric { i -> i == 2 ? '2Two' : String.valueOf((double) i) }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int is numeric passing function returns expected'() {
        expect:
        def predicate = intIsNumeric { i -> i == 2 ? '2' : String.valueOf((double) i) }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int starts with passing value "#extractedString" and "#prefix" returns "#expected"'() {
        expect:
        def predicate = intStartsWith({ i -> String.valueOf(i) }, '2')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int starts with ignore case passing function returns expected value'() {
        expect:
        def predicate = intStartsWithIgnoreCase({ i -> i == 2 ? 'Two - 2' : String.valueOf(i) }, 'two')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int ends with passing function returns expected value'() {
        expect:
        def predicate = intEndsWith({ i -> i == 2 ? '2 - Two' : String.valueOf(i) }, 'Two')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'int ends with ignore case passing function returns expected value'() {
        expect:
        def predicate = intEndsWithIgnoreCase({ i -> i == 2 ? '2 - Two' : String.valueOf(i) }, 'two')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    @Unroll
    def 'int any characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = intAnyCharsMatch({ i -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | true
        '123'     | false
        null      | false
        ''        | false
    }

    @Unroll
    def 'int all characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = intAllCharsMatch({ i -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | false
        '123'     | false
        null      | false
        ''        | true
    }

    @Unroll
    def 'int no characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = intNoCharsMatch({ i -> target }, { int c -> Character.isLetter(c) })
        predicate.test(1) == expected

        where:
        target    | expected
        'test'    | false
        'test123' | false
        '123'     | true
        null      | false
        ''        | true
    }

    def 'is int null passing function returns expected value'() {
        expect:
        def predicate = isIntNull { i -> i == 2 ? null : String.valueOf(i) }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'is int not null passing function returns expected value'() {
        expect:
        def predicate = isIntNotNull { i -> i == 2 ? null : String.valueOf(i) }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 3] as int[]
    }

    def 'int greater than passing constant value returns expected value'() {
        expect:
        def predicate = intGt(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [3] as int[]
    }

    def 'int greater than passing function and constant value returns expected value'() {
        expect:
        def function = { i -> [(1): 'a', (2): 'b', (3): 'c'].get(i) } as IntFunction
        def predicate = intGt(function, 'b')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [3] as int[]
    }

    def 'int greater than or equal passing constant value returns expected value'() {
        expect:
        def predicate = intGte(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2, 3] as int[]
    }

    def 'int greater than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { i -> [(1): 'a', (2): 'b', (3): 'c'].get(i) } as IntFunction
        def predicate = intGte(function, 'b')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2, 3] as int[]
    }

    def 'int less than passing constant value returns expected value'() {
        expect:
        def predicate = intLt(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1] as int[]
    }

    def 'int less than passing function and constant value returns expected value'() {
        expect:
        def function = { i -> [(1): 'a', (2): 'b', (3): 'c'].get(i) } as IntFunction
        def predicate = intLt(function, 'b')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1] as int[]
    }

    def 'int less than or equal passing constant value returns expected value'() {
        expect:
        def predicate = intLte(2)
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 2] as int[]
    }

    def 'int less than or equal passing function and constant value returns expected value'() {
        expect:
        def function = { i -> [(1): 'a', (2): 'b', (3): 'c'].get(i) } as IntFunction
        def predicate = intLte(function, 'b')
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 2] as int[]
    }

    def 'is int collection empty passing function returns expected value'() {
        expect:
        def predicate = isIntCollEmpty { i -> i == 2 ? [] : [String.valueOf(i)] }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }

    def 'is int collection not empty passing function returns expected value'() {
        expect:
        def predicate = isIntCollNotEmpty() { i -> i == 2 ? [] : [String.valueOf(i)] }
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [1, 3] as int[]
    }

    def 'object to ints all match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().toArray() } as Function
        def predicate = objToIntsAllMatch(function, { i -> i > 100 })
        filter(['test', ' ', '', null], predicate) == ['test', '']
    }

    def 'int to objects all match returns expected value'() {
        expect:
        def function = { i -> [String.valueOf(i)] }
        def predicate = intToObjectsAllMatch(function, { s -> s == '2' })
        intFilter([1, 2, 3] as int[], predicate) == [2] as int[]
    }

    def 'object to ints any match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().toArray() }
        def predicate = objToIntsAnyMatch(function, { i -> i == 101 })
        filter(['test', '', null], predicate) == ['test']
    }

    def 'int to objects any match returns expected value'() {
        expect:
        def function = { i -> [String.valueOf(i)] }
        def predicate = intToObjectsAnyMatch(function, { s -> s == '2' })
        intFilter([1, 2, 3] as int[], predicate) == [2] as int[]
    }

    def 'object to ints none match returns expected value'() {
        expect:
        def function = { s -> s.codePoints().toArray() }
        def predicate = objToIntsNoneMatch(function, { i -> i > 100 })
        filter(['test', '', null], predicate) == ['']
    }

    def 'int to objects none match returns expected value'() {
        expect:
        def function = { i -> [String.valueOf(i)] }
        def predicate = intToObjectsNoneMatch(function, { s -> s == '2' })
        intFilter([1, 2, 3] as int[], predicate) == [1, 3] as int[]
    }

    def 'int distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().length == DISTINCT_KEY_SIZE
    }

    int[] makeEntriesDistinctByKey() {
        IntStream.range(0, RAW_LIST_SIZE)
                .filter(intDistinctByKey(keyExtractor))
                .toArray()
    }

    def 'int distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().length == DISTINCT_KEY_SIZE
    }

    int[] makeEntriesDistinctByKeyParallel() {
        IntStream.range(0, RAW_LIST_SIZE).parallel()
                .filter(intDistinctByKeyParallel(keyExtractor))
                .toArray()
    }

    def 'map to int and filter returns expected value'() {
        expect:
        def predicate = mapToIntAndFilter({ String s -> Integer.valueOf(s) }, { i -> i == 2 })
        def strings = ['1', '2', '3']
        filter(strings, predicate) == ['2']
    }

    def 'int map and filter returns expected value'() {
        expect:
        def predicate = intMapAndFilter({ i -> String.valueOf(i) }, { s -> s == '2' })
        def ints = [1, 2, 3] as int[]
        intFilter(ints, predicate) == [2] as int[]
    }
}
