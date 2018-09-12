package org.hringsak.functions.mapper

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToDoubleBiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static org.hringsak.functions.mapper.DblMapperUtils.*

class DblMapperUtilsSpec extends Specification {

    @Unroll
    def 'double mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { d, s -> "double: $d, string: $s" }
        dblMapper(mapper, constantValue).apply(1.0D) == expected

        where:
        constantValue || expected
        'test'        || 'double: 1.0, string: test'
        ''            || 'double: 1.0, string: '
        null          || 'double: 1.0, string: null'
    }

    @Unroll
    def 'inverse double mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, d -> "double: $d, string: $s" }
        inverseDblMapper(mapper, constantValue).apply(1.0D) == expected

        where:
        constantValue || expected
        'test'        || 'double: 1.0, string: test'
        ''            || 'double: 1.0, string: '
        null          || 'double: 1.0, string: null'
    }

    @Unroll
    def 'to double mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> (double) s.length() }
        toDblMapperDefault(mapper, -1).applyAsDouble(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to double mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((double) defaultString(a).length()) + defaultString(b).length() } as ToDoubleBiFunction<String, String>
        toDblMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4.0D
        'test'   | ''       | 4.0D
        'test'   | 'test'   | 8.0D
        null     | 'test'   | 4.0D
        ''       | 'test'   | 4.0D
    }

    @Unroll
    def 'inverse to double mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((double) defaultString(a).length()) + defaultString(b).length() } as ToDoubleBiFunction<String, String>
        inverseToDblMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4.0D
        'test'   | ''       | 4.0D
        'test'   | 'test'   | 8.0D
        null     | 'test'   | 4.0D
        ''       | 'test'   | 4.0D
    }

    @Unroll
    def 'double flat mapper taking function passing doubles #doubles returns #expected'() {

        expect:
        def function = { d -> doubles }
        dblFlatMapper(function).apply(1.0D).toArray() == expected

        where:
        doubles                        || expected
        [1.0D, 2.0D, 3.0D] as double[] || [1.0D, 2.0D, 3.0D] as double[]
        [] as double[]                 || [] as double[]
        null as double[]               || [] as double[]
    }

    @Unroll
    def 'flat mapper to double passing target "#target" returns #expected'() {

        expect:
        def function = { s ->
            s.codePoints()
                    .mapToDouble { int i -> (double) i }
                    .toArray()
        }
        flatMapperToDbl(function).apply(target).toArray() == expected

        where:
        target || expected
        'test' || [116.0D, 101.0D, 115.0D, 116.0D] as double[]
        null   || [] as double[]
    }

    @Unroll
    def 'double pair of with mapper function returning "#right" returns #expected'() {

        expect:
        def rightFunction = { d -> right }
        dblPairOf(rightFunction).apply(1.0D) == expected

        where:
        right   || expected
        'right' || DoubleObjectPair.of(1.0D, 'right')
        null    || DoubleObjectPair.of(1.0D, null)
    }

    @Unroll
    def 'double pair of passing key value mapper with values "#left" and "#right" returns #expected'() {

        expect:
        def keyValueMapper = dblKeyValueMapper({ d -> left }, { d -> right })
        dblPairOf(keyValueMapper).apply(1.0D) == expected

        where:
        left   | right   || expected
        'left' | 'right' || Pair.of('left', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'left' | null    || Pair.of('left', null)
    }

    @Unroll
    def 'double pair of passing two mapper functions returning values "#left" and "#right" returns #expected'() {

        expect:
        def leftFunction = { d -> left }
        def rightFunction = { d -> right }
        dblPairOf(leftFunction, rightFunction).apply(1.0D) == expected

        where:
        left   | right   || expected
        'left' | 'right' || Pair.of('left', 'right')
        null   | 'right' || Pair.of(null, 'right')
        'left' | null    || Pair.of('left', null)
    }

    @Unroll
    def 'double pair with list passing values "#listParam" returns #expected'() {

        expect:
        dblPairWith(listParam).apply(1.0) == expected

        where:
        listParam     || expected
        ['listParam'] || DoubleObjectPair.of(1.0D, 'listParam')
        []            || DoubleObjectPair.of(1.0D, null)
        null          || DoubleObjectPair.of(1.0D, null)
    }

    @Unroll
    def 'double pair with list passing function with values and "#listParam" returns #expected'() {

        expect:
        dblPairWith({ d -> d }, listParam).apply(1.0D) == expected

        where:
        listParam     || expected
        ['listParam'] || Pair.of(1.0D, 'listParam')
        []            || Pair.of(1.0D, null)
        null          || Pair.of(1.0D, null)
    }

    def 'double pair with index calling multiple times returns incremented indexes'() {
        expect:
        def mapper = dblPairWithIndex()
        (0..5).each { d ->
            with(mapper.apply(1.0D)) {
                getRight() == d
            }
        }
    }

    def 'double pair with index returns expected'() {
        expect:
        dblPairWithIndex().apply(1.0D) == DoubleIndexPair.of(1.0D, 0)
    }

    @Unroll
    def 'double pair with index passing function returns #expected'() {

        expect:
        def dblFunction = { d -> value }
        dblPairWithIndex(dblFunction).apply(1.0D) == expected

        where:
        value   | expected
        'value' | Pair.of('value', 0)
        null    | Pair.of(null, 0)
        ''      | Pair.of('', 0)
    }

    @Unroll
    def 'double ternary passing target #target expecting "#expected'() {

        expect:
        def predicate = { d -> d == 1.0D }
        def trueExtractor = { d -> 'trueValue' }
        def falseExtractor = { d -> 'falseValue' }
        def result = dblTernary(predicate, dblTernaryMapper(trueExtractor, falseExtractor)).apply(target)
        result == expected

        where:
        target || expected
        1.0D   || 'trueValue'
        2.0D   || 'falseValue'
    }
}