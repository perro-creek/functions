package org.hringsak.functions.mapper

import org.hringsak.functions.internal.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToLongBiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.mapper.LongMapperUtils.*

class LongMapperUtilsSpec extends Specification {

    @Unroll
    def 'long mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { l, s -> "long: $l, string: $s" }
        longMapper(mapper, constantValue).apply(1L) == expected

        where:
        constantValue || expected
        'test'        || 'long: 1, string: test'
        ''            || 'long: 1, string: '
        null          || 'long: 1, string: null'
    }

    @Unroll
    def 'inverse long mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, l -> "long: $l, string: $s" }
        inverseLongMapper(mapper, constantValue).apply(1L) == expected

        where:
        constantValue || expected
        'test'        || 'long: 1, string: test'
        ''            || 'long: 1, string: '
        null          || 'long: 1, string: null'
    }

    @Unroll
    def 'to long mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> (long) s.length() }
        toLongMapperDefault(mapper, -1).applyAsLong(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to long mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((long) defaultString(a).length()) + defaultString(b).length() } as ToLongBiFunction<String, String>
        toLongMapper(mapper, paramOne).applyAsLong(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4L
        'test'   | ''       | 4L
        'test'   | 'test'   | 8L
        null     | 'test'   | 4L
        ''       | 'test'   | 4L
    }

    @Unroll
    def 'inverse to long mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((long) defaultString(a).length()) + defaultString(b).length() } as ToLongBiFunction<String, String>
        inverseToLongMapper(mapper, paramOne).applyAsLong(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4L
        'test'   | ''       | 4L
        'test'   | 'test'   | 8L
        null     | 'test'   | 4L
        ''       | 'test'   | 4L
    }

    @Unroll
    def 'flat long mapper taking function passing value "#target" returns #expected'() {

        expect:
        def function = { l -> longs }
        longFlatMapper(function).apply(1L).toArray() == expected

        where:
        longs                        || expected
        [1.0D, 2.0D, 3.0D] as long[] || [1.0D, 2.0D, 3.0D] as long[]
        [] as long[]                 || [] as long[]
        null as long[]               || [] as long[]
    }

    @Unroll
    def 'flat mapper to long passing target "#target" returns #expected'() {

        expect:
        def function = { s ->
            s.codePoints()
                    .mapToLong { int i -> (long) i }
                    .toArray()
        }
        flatMapperToLong(function).apply(target).toArray() == expected

        where:
        target || expected
        'test' || [116L, 101L, 115L, 116L] as long[]
        null   || [] as long[]
    }

    @Unroll
    def 'long pair of with mapper function returning "#right" returns #expected'() {

        expect:
        def rightFunction = { i -> right }
        longPairOf(rightFunction).apply(1L) == expected

        where:
        right   || expected
        'right' || LongObjectPair.of(1L, 'right')
        null    || LongObjectPair.of(1L, null)
    }

    @Unroll
    def 'long pair of passing key value mapper with values "#left" and "#right" returns #expected'() {

        expect:
        def keyValueMapper = longKeyValueMapper({ l -> left }, { l -> right })
        longPairOf(keyValueMapper).apply(1L) == expected

        where:
        left   | right   || expected
        'object' | 'right' || Pair.of('object', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'object' | null    || Pair.of('object', null)
    }

    @Unroll
    def 'long pair of passing two mapper functions returning values "#left" and "#right" returns #expected'() {

        expect:
        def leftFunction = { i -> left }
        def rightFunction = { i -> right }
        longPairOf(leftFunction, rightFunction).apply(1L) == expected

        where:
        left   | right   || expected
        'object' | 'right' || Pair.of('object', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'object' | null    || Pair.of('object', null)
    }

    @Unroll
    def 'long pair with list passing values "#listParam" returns #expected'() {

        expect:
        longPairWith(listParam).apply(1) == expected

        where:
        listParam     || expected
        ['listParam'] || LongObjectPair.of(1L, 'listParam')
        []            || LongObjectPair.of(1L, null)
        null          || LongObjectPair.of(1L, null)
    }

    @Unroll
    def 'long pair with list passing function with values and "#listParam" returns #expected'() {

        expect:
        longPairWith({ l -> l }, listParam).apply(1L) == expected

        where:
        listParam     || expected
        ['listParam'] || Pair.of(1L, 'listParam')
        []            || Pair.of(1L, null)
        null          || Pair.of(1L, null)
    }

    def 'long pair with index calling multiple times returns incremented indexes'() {
        expect:
        def mapper = longPairWithIndex()
        (0..5).each { l ->
            with(mapper.apply(1L)) {
                getIndex() == l
            }
        }
    }

    def 'long pair with index returns expected'() {
        expect:
        longPairWithIndex().apply(1L) == LongIndexPair.of(1L, 0)
    }

    @Unroll
    def 'long pair with index passing function returns #expected'() {

        expect:
        def longFunction = { l -> value }
        longPairWithIndex(longFunction).apply(1L) == expected

        where:
        value   | expected
        'value' | Pair.of('value', 0)
        null    | Pair.of(null, 0)
        ''      | Pair.of('', 0)
    }

    @Unroll
    def 'long true/false mappers passing target #target expecting "#expected'() {

        expect:
        def predicate = { l -> l == 1L }
        def trueExtractor = { l -> 'trueValue' }
        def falseExtractor = { l -> 'falseValue' }
        def result = longTernary(predicate, longTrueFalseMappers(trueExtractor, falseExtractor)).apply(target)
        result == expected

        where:
        target || expected
        1L     || 'trueValue'
        2L     || 'falseValue'
    }
}
