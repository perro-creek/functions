package org.hringsak.functions

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.*

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.apache.commons.lang3.StringUtils.defaultString
import static org.hringsak.functions.MapperUtils.*

class MapperUtilsSpec extends Specification {

    @Unroll
    def 'mapper with default passing value "#value" and mapperResult "#mapperResult" returns "#expected"'() {

        expect:
        def mapper = { s -> mapperResult }
        mapperDefault(mapper, 'default').apply(value) == expected

        where:
        value  | mapperResult | expected
        null   | 'test'       | 'default'
        'test' | null         | 'default'
        ''     | 'test'       | 'test'
        'test' | 'test'       | 'test'
    }

    @Unroll
    def 'mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        MapperUtils.mapper(mapper, paramOne).apply(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'inverse mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        inverseMapper(mapper, paramOne).apply(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'double mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { d, s -> "double: $d, string: $s" }
        doubleMapper(mapper, constantValue).apply(1.0D) == expected

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
        inverseDoubleMapper(mapper, constantValue).apply(1.0D) == expected

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
        toDoubleMapperDefault(mapper, -1).applyAsDouble(value) == expected

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
        toDoubleMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

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
        inverseToDoubleMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4.0D
        'test'   | ''       | 4.0D
        'test'   | 'test'   | 8.0D
        null     | 'test'   | 4.0D
        ''       | 'test'   | 4.0D
    }

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
    def 'get value for map #map and enumValue #enumValue returns #expected'() {

        expect:
        def extractor = { TestValue t -> t.name() } as Function
        getValue(map, extractor).apply(enumValue) == expected

        where:
        map                            | enumValue     || expected
        null                           | TestValue.ONE || null
        [:]                            | TestValue.ONE || null
        TestValue.makeNameToValueMap() | null          || null
        TestValue.makeNameToValueMap() | TestValue.ONE || TestValue.ONE
        TestValue.makeNameToValueMap() | TestValue.TWO || TestValue.TWO
    }

    @Unroll
    def 'stream of passing value "#target" finding first returns "#expected"'() {

        expect:
        def stream = streamOf(identity()).apply(target)
        stream.findFirst().orElse(null) == expected

        where:
        target || expected
        'test' || 'test'
        null   || null
    }

    @Unroll
    def 'flat mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().collect(toList()) }
        def codePoints = flatMapper(function).apply(paramOne).collect(toList())
        codePoints == expected

        where:
        paramOne || expected
        'test'   || ['t', 'e', 's', 't']
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat array mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().toArray({ i -> new Integer[i] }) }
        def codePoints = flatArrayMapper(function).apply(paramOne).collect(toList())
        codePoints == expected

        where:
        paramOne || expected
        'test'   || ['t', 'e', 's', 't']
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat double mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> [1.0D, 2.0D, 3.0D] }
        def doubles = flatDoubleMapper(function).apply(paramOne).toArray()
        doubles == expected as double[]

        where:
        paramOne || expected
        'test'   || [1.0D, 2.0D, 3.0D]
        null     || []
        ''       || [1.0D, 2.0D, 3.0D]
    }

    @Unroll
    def 'flat double array mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> [1.0D, 2.0D, 3.0D] as double[] }
        def doubles = flatDoubleArrayMapper(function).apply(paramOne).toArray()
        doubles == expected as double[]

        where:
        paramOne || expected
        'test'   || [1.0D, 2.0D, 3.0D]
        null     || []
        ''       || [1.0D, 2.0D, 3.0D]
    }

    @Unroll
    def 'flat int mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().collect(toList()) }
        def codePoints = flatIntMapper(function).apply(paramOne).toArray()
        codePoints == expected as int[]

        where:
        paramOne || expected
        'test'   || [116, 101, 115, 116]
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat int array mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().toArray() }
        def codePoints = flatIntArrayMapper(function).apply(paramOne).toArray()
        codePoints == expected as int[]

        where:
        paramOne || expected
        'test'   || [116, 101, 115, 116]
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat long mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> [1L, 2L, 3L] }
        def longs = flatLongMapper(function).apply(paramOne).toArray()
        longs == expected as long[]

        where:
        paramOne || expected
        'test'   || [1L, 2L, 3L]
        null     || []
        ''       || [1L, 2L, 3L]
    }

    @Unroll
    def 'flat long array mapper taking function passing value "#paramOne" returns #expected'() {

        expect:
        Function function = { String param -> [1L, 2L, 3L] as long[] }
        def longs = flatLongArrayMapper(function).apply(paramOne).toArray()
        longs == expected as long[]

        where:
        paramOne || expected
        'test'   || [1L, 2L, 3L]
        null     || []
        ''       || [1L, 2L, 3L]
    }

    @Unroll
    def 'pair of with values "#target" and "#right" returns #expected'() {

        expect:
        def leftFunction = { t -> t } as Function<String, String>
        def rightFunction = { t -> right } as Function<String, String>
        pairOf(leftFunction, rightFunction).apply(target) == expected

        where:
        target   | right   || expected
        null     | 'right' || Pair.of(null, 'right')
        'target' | 'right' || Pair.of('target', 'right')
        'target' | null    || Pair.of('target', null)
    }

    @Unroll
    def 'pair with list passing values "#target" and "#listParam" returns #expected'() {

        expect:
        pairWith(listParam).apply(target) == expected

        where:
        target   | listParam     || expected
        null     | ['listParam'] || Pair.of(null, 'listParam')
        'target' | ['listParam'] || Pair.of('target', 'listParam')
        'target' | null          || Pair.of('target', null)
    }

    @Unroll
    def 'pair with list passing function with values "#target" and "#listParam" returns #expected'() {

        expect:
        def identity = { t -> t } as Function<String, String>
        pairWith(identity, listParam).apply(target) == expected

        where:
        target   | listParam     || expected
        null     | ['listParam'] || Pair.of(null, 'listParam')
        'target' | ['listParam'] || Pair.of('target', 'listParam')
        'target' | null          || Pair.of('target', null)
    }

    def 'pair with index calling multiple times returns incremented indexes'() {
        expect:
        def mapper = pairWithIndex()
        (0..5).each { i ->
            with(mapper.apply('test')) {
                getRight() == i
            }
        }
    }

    @Unroll
    def 'pair with index returns #expected'() {

        expect:
        pairWithIndex().apply(value) == expected

        where:
        value   | expected
        'value' | Pair.of('value', 0)
        null    | Pair.of(null, 0)
        ''      | Pair.of('', 0)
    }

    @Unroll
    def 'pair with index passing function returns #expected'() {

        expect:
        def identity = Function.identity()
        pairWithIndex(identity).apply(value) == expected

        where:
        value   | expected
        'value' | Pair.of('value', 0)
        null    | Pair.of(null, 0)
        ''      | Pair.of('', 0)
    }

    @Unroll
    def 'ternary passing predicateParameter "#predicateParameter" expecting "#expectedResult'() {

        expect:
        def predicate = { String s -> s.isEmpty() } as Predicate
        def trueExtractor = { s -> trueValue } as Function
        def falseExtractor = { s -> falseValue } as Function
        def result = ternary(predicate, extractors(trueExtractor, falseExtractor)).apply(predicateParameter)
        result == expectedResult

        where:
        predicateParameter | trueValue   | falseValue   || expectedResult
        'test'             | 'trueValue' | 'falseValue' || 'falseValue'
        ''                 | 'trueValue' | 'falseValue' || 'trueValue'
    }
}