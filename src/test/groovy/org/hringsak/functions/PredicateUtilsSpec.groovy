package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.Predicate

import static java.util.function.Function.identity

class PredicateUtilsSpec extends Specification {

    def 'predicate passing null value throws NPE'() {

        when:
        PredicateUtils.predicate({ String s -> s.isEmpty() }).test(null)

        then:
        thrown(NullPointerException)
    }

    def 'predicate passing non-null returns expected value'() {
        expect:
        PredicateUtils.predicate({ String s -> s.isEmpty() }).test('')
    }

    @Unroll
    def 'predicate with default taking boolean parameter "#booleanParameter" and predicate parameter "#predicateParameter" returns expected result'() {

        expect:
        def predicate = { String s -> s.isEmpty() } as Predicate
        PredicateUtils.predicate(predicate, defaultParameter).test(predicateParameter) == expected

        where:
        defaultParameter | predicateParameter | expected
        true             | null               | true
        true             | 'test'             | false
        true             | ''                 | true
        false            | null               | false
        false            | 'test'             | false
        false            | ''                 | true
    }

    @Unroll
    def 'predicate taking boolean parameter "#booleanParameter" and predicate parameter "#predicateParameter" returns expected result'() {

        expect:
        PredicateUtils.predicate(booleanParameter).test(predicateParameter) == expected

        where:
        booleanParameter | predicateParameter | expected
        true             | null               | true
        true             | 'test'             | true
        false            | null               | false
        false            | 'test'             | false
    }

    @Unroll
    def 'predicate for bi-predicate returns expected value for values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def biPredicate = { a, b -> a.equals(b) } as BiPredicate
        PredicateUtils.predicate(biPredicate, paramOne).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'predicate for bi-predicate as second parameter returns expected value for values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def biPredicate = { a, b -> a.equals(b) } as BiPredicate
        PredicateUtils.predicate(paramOne, biPredicate).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    def 'not passing null value throws NPE'() {

        when:
        PredicateUtils.not({ String s -> s.isEmpty() }).test(null)

        then:
        thrown(NullPointerException)
    }

    def 'not passing non-null returns expected value'() {
        expect:
        PredicateUtils.not({ String s -> s.isEmpty() }).test('test')
    }

    @Unroll
    def 'is equal passing #paramOne and #paramTwo returns #expected'() {

        expect:
        def function = { String s -> s.toString() } as Function
        PredicateUtils.isEqual(paramOne, function).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'is not equal passing #paramOne and #paramTwo returns #expected'() {

        expect:
        def function = { String s -> s.toString() } as Function
        PredicateUtils.isNotEqual(paramOne, function).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | true
        'test'   | ''       | true
        'test'   | 'test'   | false
        null     | 'test'   | true
        ''       | 'test'   | true
    }

    @Unroll
    def 'equals ignore case passing #paramOne and #paramTwo returns #expected'() {

        expect:
        def function = { String s -> s.toString() } as Function
        PredicateUtils.equalsIgnoreCase(paramOne, function).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | false
        'test'   | ''       | false
        'test'   | 'test'   | true
        'test'   | 'TEST'   | true
        'TEST'   | 'test'   | true
        null     | 'test'   | false
        ''       | 'test'   | false
    }

    @Unroll
    def 'contains passing value "#collection" and "#predicateParameter" returns "#expected"'() {

        expect:
        def function = { String s -> s.toString() } as Function
        PredicateUtils.contains(collection, function).test(predicateParameter) == expected

        where:
        collection | predicateParameter | expected
        ['test']   | null               | false
        ['test']   | ''                 | false
        ['test']   | 'test'             | true
        [null]     | 'test'             | false
        [null]     | null               | false
        ['']       | 'test'             | false
        []         | 'test'             | false
    }

    @Unroll
    def 'contains with collection extractor passing value "#collection" and "#predicateParameter" returns "#expected"'() {

        expect:
        def function = { String s -> collection } as Function
        PredicateUtils.contains(function, predicateParameter).test(predicateParameter) == expected

        where:
        collection | predicateParameter | expected
        ['test']   | null               | false
        ['test']   | ''                 | false
        ['test']   | 'test'             | true
        [null]     | 'test'             | false
        [null]     | null               | false
        ['']       | 'test'             | false
        []         | 'test'             | false
    }

    @Unroll
    def 'contains char with string extractor passing value "#extractedString" and "#searchChar" returns "#expected"'() {

        expect:
        PredicateUtils.containsChar(Function.identity(), searchChar.codePointAt(0)).test(extractedString) == expected

        where:
        extractedString | searchChar | expected
        null            | '\0'       | false
        null            | 'e'        | false
        'test'          | '\0'       | false
        'test'          | 'e'        | true
    }

    @Unroll
    def 'contains sequence with string extractor passing value "#extractedString" and "#searchSeq" returns "#expected"'() {

        expect:
        PredicateUtils.containsSequence(Function.identity(), searchSeq).test(extractedString) == expected

        where:
        extractedString | searchSeq | expected
        null            | null      | false
        null            | ''        | false
        null            | 'es'      | false
        'test'          | null      | false
        'test'          | ''        | true
        'test'          | 'es'      | true
    }

    @Unroll
    def 'contains ignore case with string extractor passing value "#extractedString" and "#searchSeq" returns "#expected"'() {

        expect:
        PredicateUtils.containsIgnoreCase(Function.identity(), searchSeq).test(extractedString) == expected

        where:
        extractedString | searchSeq | expected
        null            | null      | false
        null            | ''        | false
        null            | 'es'      | false
        null            | 'ES'      | false
        'TEST'          | null      | false
        'TEST'          | ''        | true
        'TEST'          | 'es'      | true
        'TEST'          | 'ES'      | true
    }

    @Unroll
    def 'is alpha passing value "#extractedString" returns "#expected"'() {

        expect:
        PredicateUtils.isAlpha(Function.identity()).test(extractedString) == expected

        where:
        extractedString | expected
        null            | false
        ''              | false
        'test '         | false
        'test1'         | false
        'test'          | true
        'TEST'          | true
    }

    @Unroll
    def 'is alphanumeric passing value "#extractedString" returns "#expected"'() {

        expect:
        PredicateUtils.isAlphanumeric(Function.identity()).test(extractedString) == expected

        where:
        extractedString | expected
        null            | false
        ''              | false
        'test '         | false
        'test1'         | true
        'test'          | true
        'TEST'          | true
        '123'           | true
        '123 '          | false
    }

    @Unroll
    def 'is numeric passing value "#extractedString" returns "#expected"'() {

        expect:
        PredicateUtils.isNumeric(Function.identity()).test(extractedString) == expected

        where:
        extractedString | expected
        null            | false
        ''              | false
        '123 '          | false
        '123a'          | false
        '1.23'          | false
        '+123'          | false
        '-123'          | false
        '1,234'         | false
        '123'           | true
    }

    @Unroll
    def 'starts with passing value "#extractedString" and "#prefix" returns "#expected"'() {

        expect:
        PredicateUtils.startsWith(Function.identity(), prefix).test(extractedString) == expected

        where:
        extractedString | prefix | expected
        null            | null   | false
        null            | ''     | false
        null            | 't'    | false
        'test'          | null   | false
        'test'          | ''     | true
        'test'          | 't'    | true
    }

    @Unroll
    def 'starts with ignore case passing value "#extractedString" and "#prefix" returns "#expected"'() {

        expect:
        PredicateUtils.startsWithIgnoreCase(Function.identity(), prefix).test(extractedString) == expected

        where:
        extractedString | prefix | expected
        null            | null   | false
        null            | ''     | false
        null            | 't'    | false
        'TEST'          | null   | false
        'TEST'          | ''     | true
        'TEST'          | 't'    | true
        'TEST'          | 'T'    | true
    }

    @Unroll
    def 'ends with passing value "#extractedString" and "#suffix" returns "#expected"'() {

        expect:
        PredicateUtils.endsWith(Function.identity(), suffix).test(extractedString) == expected

        where:
        extractedString | suffix | expected
        null            | null   | false
        null            | ''     | false
        null            | 't'    | false
        'test'          | null   | false
        'test'          | ''     | true
        'test'          | 't'    | true
    }

    @Unroll
    def 'ends with ignore case passing value "#extractedString" and "#suffix" returns "#expected"'() {

        expect:
        PredicateUtils.endsWithIgnoreCase(Function.identity(), suffix).test(extractedString) == expected

        where:
        extractedString | suffix | expected
        null            | null   | false
        null            | ''     | false
        null            | 't'    | false
        'TEST'          | null   | false
        'TEST'          | ''     | true
        'TEST'          | 't'    | true
        'TEST'          | 'T'    | true
    }

    @Unroll
    def 'is null passing value "#target" returns "#expected"'() {

        expect:
        def function = { AbstractMap.SimpleEntry e -> e.getValue() } as Function
        PredicateUtils.isNull(function).test(target) == expected

        where:
        target                                      | expected
        new AbstractMap.SimpleEntry("key", "value") | false
        new AbstractMap.SimpleEntry("key", null)    | true
        null                                        | true
    }

    @Unroll
    def 'not null passing value "#target" returns "#expected"'() {

        expect:
        def function = { AbstractMap.SimpleEntry e -> e.getValue() } as Function
        PredicateUtils.notNull(function).test(target) == expected

        where:
        target                                      | expected
        new AbstractMap.SimpleEntry("key", "value") | true
        new AbstractMap.SimpleEntry("key", null)    | false
        null                                        | false
    }

    @Unroll
    def 'gt passing #paramOne and #paramTwo returns #expected'() {
        expect:
        PredicateUtils.gt(paramOne as Comparable, identity()).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        null     | null     | false
        'a'      | ''       | true
        'a'      | 'a'      | false
        'a'      | 'b'      | false
        'b'      | 'a'      | true
        ''       | 'a'      | false
        1        | 2        | false
        2        | 1        | true
    }

    @Unroll
    def 'gte passing #paramOne and #paramTwo returns #expected'() {

        expect:
        PredicateUtils.gte(paramOne as Comparable, identity()).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        null     | null     | true
        'a'      | ''       | true
        'a'      | 'a'      | true
        'a'      | 'b'      | false
        'b'      | 'a'      | true
        ''       | 'a'      | false
        1        | 2        | false
        2        | 1        | true
    }

    @Unroll
    def 'lt passing #paramOne and #paramTwo returns #expected'() {

        expect:
        PredicateUtils.lt(paramOne as Comparable, identity()).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        null     | null     | false
        'a'      | ''       | false
        'a'      | 'a'      | false
        'a'      | 'b'      | true
        'b'      | 'a'      | false
        ''       | 'a'      | true
        1        | 2        | true
        2        | 1        | false
    }

    @Unroll
    def 'lte passing #paramOne and #paramTwo returns #expected'() {

        expect:
        PredicateUtils.lte(paramOne as Comparable, identity()).test(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        null     | null     | true
        'a'      | ''       | false
        'a'      | 'a'      | true
        'a'      | 'b'      | true
        'b'      | 'a'      | false
        ''       | 'a'      | true
        1        | 2        | true
        2        | 1        | false
    }

    @Unroll
    def 'is empty passing #target returns #expected'() {

        expect:
        def function = { List l -> l.asCollection() } as Function
        PredicateUtils.isEmpty(function).test(target) == expected

        where:
        target   | expected
        ['test'] | false
        null     | true
        []       | true
    }

    @Unroll
    def 'is not empty passing #target returns #expected'() {

        expect:
        def function = { List l -> l.asCollection() } as Function
        PredicateUtils.isNotEmpty(function).test(target) == expected

        where:
        target   | expected
        ['test'] | true
        null     | false
        []       | false
    }

    @Unroll
    def 'find first passing input "#input"'() {

        expect:
        def predicate = PredicateUtils.isEqual(4, Function.identity())
        PredicateUtils.transformAndFilter({ String s -> s.length() } as Function, predicate).test(input) == expected

        where:
        input  | expected
        null   | false
        ''     | false
        'test' | true
    }
}
