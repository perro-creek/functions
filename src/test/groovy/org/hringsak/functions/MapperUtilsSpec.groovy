package org.hringsak.functions

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList

class MapperUtilsSpec extends Specification {

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
    def 'mapper for bi-function as second parameter passing values "#paramOne" and "#paramTwo" returns #expected'() {
        expect:
        def mapper = { a, b -> Objects.equals(a, b) } as BiFunction<String, String, Boolean>
        MapperUtils.mapper(paramOne, mapper).apply(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'stream of passing value "#target" finding first returns "#expected"'() {
        expect:
        def stream = MapperUtils.streamOf(identity()).apply(target)
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
        def codePoints = MapperUtils.flatMapper(function).apply(paramOne).collect(toList())
        codePoints == expected

        where:
        paramOne || expected
        'test'   || ['t', 'e', 's', 't']
        null     || []
        ''       || []
    }

    @Unroll
    def 'pair of with values "#target" and "#right" returns #expected'() {
        expect:
        def leftFunction = { t -> t } as Function<String, String>
        def rightFunction = { t -> right } as Function<String, String>
        MapperUtils.pairOf(leftFunction, rightFunction).apply(target) == expected

        where:
        target   | right   || expected
        null     | 'right' || Pair.of(null, 'right')
        'target' | 'right' || Pair.of('target', 'right')
        'target' | null    || Pair.of('target', null)
    }

    @Unroll
    def 'pair with list passing values "#target" and "#listParam" returns #expected'() {
        expect:
        def identity = { t -> t } as Function<String, String>
        MapperUtils.pairWith(identity, listParam).apply(target) == expected

        where:
        target   | listParam     || expected
        null     | ['listParam'] || Pair.of(null, 'listParam')
        'target' | ['listParam'] || Pair.of('target', 'listParam')
        'target' | null          || Pair.of('target', null)
    }

    @Unroll
    def 'ternary testing predicateParameter=#predicateParameter'() {
        expect:
        def predicate = {
            String s -> s.isEmpty()
        } as Predicate
        def trueValueFunction = { s -> trueValue } as Function
        def falseValueFunction = { s -> falseValue } as Function
        def result = MapperUtils.ternary(predicate, trueValueFunction, falseValueFunction).apply(predicateParameter)
        expectedResult == result

        where:
        predicateParameter | trueValue | falseValue || expectedResult
        'a'                | 'blargh'  | 'snorp'    || 'snorp'
        ''                 | 'blargh'  | 'snorp'    || 'blargh'
    }
}