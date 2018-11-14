package org.hringsak.functions.mapper

import org.hringsak.functions.TestValue
import org.hringsak.functions.internal.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.mapper.MapperUtils.*

class MapperUtilsSpec extends Specification {

    @Unroll
    def 'mapper casting method reference to function passing target "#target" returns expected'() {

        expect:
        def function = { s -> s.length() }
        mapper(function).apply(target) == expected

        where:
        target || expected
        'test' || 4
        ''     || 0
        null   || null
    }

    @Unroll
    def 'mapper with default returns default when target is "#target"'() {

        expect:
        mapperDefault('default').apply(target) == expectedValue

        where:
        target   || expectedValue
        'target' || 'target'
        null     || 'default'
    }

    @Unroll
    def 'mapper with default supplier returns default when target is "#target"'() {

        expect:
        def supplier = { 'default' } as Supplier
        mapperDefault(supplier).apply(target) == expectedValue

        where:
        target   || expectedValue
        'target' || 'target'
        null     || 'default'
    }

    @Unroll
    def 'mapper with default passing target "#target" and mapperResult "#mapperResult" returns "#expected"'() {

        expect:
        def function = { s -> mapperResult }
        mapperDefault(function, 'default').apply(target) == expected

        where:
        target | mapperResult | expected
        null   | 'test'       | 'default'
        'test' | null         | 'default'
        ''     | 'test'       | 'test'
        'test' | 'test'       | 'test'
    }

    @Unroll
    def 'mapper with default supplier passing target "#target" and mapperResult "#mapperResult" returns "#expected"'() {

        expect:
        def function = { s -> mapperResult }
        def supplier = { 'default' } as Supplier
        mapperDefault(function, supplier).apply(target) == expected

        where:
        target | mapperResult | expected
        null   | 'test'       | 'default'
        'test' | null         | 'default'
        ''     | 'test'       | 'test'
        'test' | 'test'       | 'test'
    }

    @Unroll
    def 'mapper for bi-function passing constantValue "#constantValue" and target "#target" returns #expected'() {

        expect:
        def function = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        mapper(function, constantValue).apply(target) == expected

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
        def function = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        inverseMapper(function, constantValue).apply(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'mapper with left and right functions passing target "#target" returns "#expected"'() {

        expect:
        def leftFunction = { s -> leftValue } as Function
        def rightFunction = { s -> s.toUpperCase() }
        mapper(leftFunction, rightFunction).apply(target) == expected

        where:
        target | leftValue || expected
        'test' | 'test'    || 'TEST'
        'test' | null      || null
        null   | _         || null
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
    def 'flat mapper taking function passing value "#target" returns #expected'() {

        expect:
        Function function = { String param -> param.codePoints().boxed().collect(toList()) }
        def codePoints = flatMapper(function).apply(target).collect(toList())
        codePoints == expected

        where:
        target || expected
        'test' || ['t', 'e', 's', 't']
        null   || []
        ''     || []
    }

    @Unroll
    def 'flat array mapper taking function passing value "#target" returns #expected'() {

        expect:
        def function = { String param -> param.codePoints().boxed().toArray({ i -> new Integer[i] }) }
        def codePoints = flatArrayMapper(function).apply(target).collect(toList())
        codePoints == expected

        where:
        target || expected
        'test' || ['t', 'e', 's', 't']
        null   || []
        ''     || []
    }

    @Unroll
    def 'pair of with values "#target" and "#right" returns #expected'() {

        expect:
        def rightFunction = { t -> right }
        pairOf(rightFunction).apply(target) == expected

        where:
        target   | right   || expected
        null     | 'right' || Pair.of(null, null)
        'target' | 'right' || Pair.of('target', 'right')
        'target' | null    || Pair.of('target', null)
    }

    @Unroll
    def 'pair of with key value mapper passing target "#target" and rightValue "#rightValue" returns #expected'() {

        expect:
        def leftFunction = { s -> s.toUpperCase() }
        def rightFunction = { s -> rightValue }
        pairOf(keyValueMapper(leftFunction, rightFunction)).apply(target) == expected

        where:
        target   | rightValue || expected
        null     | 'right'    || Pair.of(null, null)
        'target' | 'right'    || Pair.of('TARGET', 'right')
        'target' | null       || Pair.of('TARGET', null)
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
        def identity = { t -> t }
        pairWith(identity, listParam).apply(target) == expected

        where:
        target   | listParam     || expected
        null     | ['listParam'] || Pair.of(null, 'listParam')
        'target' | ['listParam'] || Pair.of('target', 'listParam')
        'target' | null          || Pair.of('target', null)
    }

    def 'pair with index calling multiple times returns incremented indexes'() {
        expect:
        def function = pairWithIndex()
        (0..5).each { i ->
            with(function.apply('test')) {
                getIndex() == i
            }
        }
    }

    @Unroll
    def 'pair with index returns #expected'() {

        expect:
        pairWithIndex().apply(value) == expected

        where:
        value   | expected
        'value' | ObjectIndexPair.of('value', 0)
        null    | ObjectIndexPair.of(null, 0)
        ''      | ObjectIndexPair.of('', 0)
    }

    @Unroll
    def 'pair with index passing function returns #expected'() {

        expect:
        pairWithIndex(identity()).apply(value) == expected

        where:
        value   | expected
        'value' | ObjectIndexPair.of('value', 0)
        null    | ObjectIndexPair.of(null, 0)
        ''      | ObjectIndexPair.of('', 0)
    }

    @Unroll
    def 'true/false mappers passing target "#target" expecting "#expected'() {

        expect:
        def predicate = { String s -> s.isEmpty() }
        def trueExtractor = { s -> 'trueValue' }
        def falseExtractor = { s -> 'falseValue' }
        def result = ternary(predicate, trueFalseMappers(trueExtractor, falseExtractor)).apply(target)
        result == expected

        where:
        target || expected
        'test' || 'falseValue'
        ''     || 'trueValue'
        null   || 'falseValue'
    }
}
