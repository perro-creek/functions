package org.hringsak.functions.objects

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.longs.LongMapperUtils.flatLongArrayMapper
import static org.hringsak.functions.longs.LongMapperUtils.flatLongMapper
import static org.hringsak.functions.objects.MapperUtils.*

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
    def 'mapper for bi-function passing constantValue "#constantValue" and target "#target" returns #expected'() {

        expect:
        def mapper = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        MapperUtils.mapper(mapper, constantValue).apply(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'inverse mapper for bi-function passing constantValue "#constantValue" and target "#target" returns #expected'() {

        expect:
        def mapper = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        inverseMapper(mapper, constantValue).apply(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
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
    def 'flat mapper taking function passing value "#target" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().collect(toList()) }
        def codePoints = flatMapper(function).apply(target).collect(toList())
        codePoints == expected

        where:
        target || expected
        'test'   || ['t', 'e', 's', 't']
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat array mapper taking function passing value "#target" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().toArray({ i -> new Integer[i] }) }
        def codePoints = flatArrayMapper(function).apply(target).collect(toList())
        codePoints == expected

        where:
        target || expected
        'test'   || ['t', 'e', 's', 't']
        null     || []
        ''       || []
    }

    @Unroll
    def 'flat long mapper taking function passing value "#target" returns #expected'() {

        expect:
        Function function = { String param -> [1L, 2L, 3L] }
        def longs = flatLongMapper(function).apply(target).toArray()
        longs == expected as long[]

        where:
        target || expected
        'test'   || [1L, 2L, 3L]
        null     || []
        ''       || [1L, 2L, 3L]
    }

    @Unroll
    def 'flat long array mapper taking function passing value "#target" returns #expected'() {

        expect:
        Function function = { String param -> [1L, 2L, 3L] as long[] }
        def longs = flatLongArrayMapper(function).apply(target).toArray()
        longs == expected as long[]

        where:
        target || expected
        'test'   || [1L, 2L, 3L]
        null     || []
        ''       || [1L, 2L, 3L]
    }

    @Unroll
    def 'pair of with values "#target" and "#right" returns #expected'() {

        expect:
        def rightFunction = { t -> right }
        pairOf(rightFunction).apply(target) == expected

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
    def 'ternary passing target "#target" expecting "#expected'() {

        expect:
        def predicate = { String s -> s.isEmpty() }
        def trueExtractor = { s -> 'trueValue' }
        def falseExtractor = { s -> 'falseValue' }
        def result = ternary(predicate, ternaryMapper(trueExtractor, falseExtractor)).apply(target)
        result == expected

        where:
        target || expected
        'test' || 'falseValue'
        ''     || 'trueValue'
    }
}