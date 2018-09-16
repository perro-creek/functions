package org.hringsak.functions.mapper

import org.hringsak.functions.internal.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToIntBiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.mapper.IntMapperUtils.*

class IntMapperUtilsSpec extends Specification {

    @Unroll
    def 'int mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { i, s -> "int: $i, string: $s" }
        intMapper(mapper, constantValue).apply(1) == expected

        where:
        constantValue || expected
        'test'        || 'int: 1, string: test'
        ''            || 'int: 1, string: '
        null          || 'int: 1, string: null'
    }

    @Unroll
    def 'inverse int mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, i -> "int: $i, string: $s" }
        inverseIntMapper(mapper, constantValue).apply(1) == expected

        where:
        constantValue || expected
        'test'        || 'int: 1, string: test'
        ''            || 'int: 1, string: '
        null          || 'int: 1, string: null'
    }

    @Unroll
    def 'to int mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> s.length() }
        toIntMapperDefault(mapper, -1).applyAsInt(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to int mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> defaultString(a).length() + defaultString(b).length() } as ToIntBiFunction<String, String>
        toIntMapper(mapper, paramOne).applyAsInt(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4
        'test'   | ''       | 4
        'test'   | 'test'   | 8
        null     | 'test'   | 4
        ''       | 'test'   | 4
    }

    @Unroll
    def 'inverse to int mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> defaultString(a).length() + defaultString(b).length() } as ToIntBiFunction<String, String>
        inverseToIntMapper(mapper, paramOne).applyAsInt(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4
        'test'   | ''       | 4
        'test'   | 'test'   | 8
        null     | 'test'   | 4
        ''       | 'test'   | 4
    }

    @Unroll
    def 'int flat mapper taking function passing value "#target" returns #expected'() {

        expect:
        def function = { i -> ints }
        intFlatMapper(function).apply(1).toArray() == expected

        where:
        ints               || expected
        [1, 2, 3] as int[] || [1, 2, 3] as int[]
        [] as int[]        || [] as int[]
        null as int[]      || [] as int[]
    }

    @Unroll
    def 'flat mapper to int passing target "#target" returns #expected'() {

        expect:
        def function = { s -> s.codePoints().toArray() }
        flatMapperToInt(function).apply(target).toArray() == expected

        where:
        target || expected
        'test' || [116, 101, 115, 116] as int[]
        null   || [] as int[]
    }

    @Unroll
    def 'int pair of with mapper function returning "#right" returns #expected'() {

        expect:
        def rightFunction = { i -> right }
        intPairOf(rightFunction).apply(1) == expected

        where:
        right   || expected
        'right' || IntObjectPair.of(1, 'right')
        null    || IntObjectPair.of(1, null)
    }

    @Unroll
    def 'int pair of passing key value mapper with values "#left" and "#right" returns #expected'() {

        expect:
        def keyValueMapper = intKeyValueMapper({ i -> left }, { i -> right })
        intPairOf(keyValueMapper).apply(1) == expected

        where:
        left   | right   || expected
        'left' | 'right' || Pair.of('left', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'left' | null    || Pair.of('left', null)
    }

    @Unroll
    def 'int pair of passing two mapper functions returning values "#left" and "#right" returns #expected'() {

        expect:
        def leftFunction = { i -> left }
        def rightFunction = { i -> right }
        intPairOf(leftFunction, rightFunction).apply(1) == expected

        where:
        left   | right   || expected
        'left' | 'right' || Pair.of('left', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'left' | null    || Pair.of('left', null)
    }

    @Unroll
    def 'int pair with list passing values "#listParam" returns #expected'() {

        expect:
        intPairWith(listParam).apply(1) == expected

        where:
        listParam     || expected
        ['listParam'] || IntObjectPair.of(1, 'listParam')
        []            || IntObjectPair.of(1, null)
        null          || IntObjectPair.of(1, null)
    }

    @Unroll
    def 'int pair with list passing function with values and "#listParam" returns #expected'() {

        expect:
        intPairWith({ i -> i }, listParam).apply(1) == expected

        where:
        listParam     || expected
        ['listParam'] || Pair.of(1, 'listParam')
        []            || Pair.of(1, null)
        null          || Pair.of(1, null)
    }

    def 'int pair with index calling multiple times returns incremented indexes'() {
        expect:
        def mapper = intPairWithIndex()
        (0..5).each { i ->
            with(mapper.apply(1)) {
                getRight() == i
            }
        }
    }

    def 'int pair with index returns expected'() {
        expect:
        intPairWithIndex().apply(1) == IntIndexPair.of(1, 0)
    }

    @Unroll
    def 'int pair with index passing function returns #expected'() {

        expect:
        def intFunction = { i -> value }
        intPairWithIndex(intFunction).apply(1) == expected

        where:
        value   | expected
        'value' | Pair.of('value', 0)
        null    | Pair.of(null, 0)
        ''      | Pair.of('', 0)
    }

    @Unroll
    def 'int ternary passing target #target expecting "#expected'() {

        expect:
        def predicate = { i -> i == 1 }
        def trueExtractor = { i -> 'trueValue' }
        def falseExtractor = { i -> 'falseValue' }
        def result = intTernary(predicate, intTernaryMapper(trueExtractor, falseExtractor)).apply(target)
        result == expected

        where:
        target || expected
        1      || 'trueValue'
        2      || 'falseValue'
    }
}
