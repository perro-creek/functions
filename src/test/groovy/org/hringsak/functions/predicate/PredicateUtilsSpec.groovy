package org.hringsak.functions.predicate

import org.hringsak.functions.TestValue
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.PredicateUtils.*

class PredicateUtilsSpec extends Specification {

    static final int RAW_LIST_SIZE = 1000
    static final int DISTINCT_KEY_SIZE = 100
    def keyExtractor = { obj -> obj.key }

    def 'predicate passing null value throws NPE'() {

        when:
        def predicate = predicate { String s -> s.isEmpty() }
        predicate.test(null)

        then:
        thrown(NullPointerException)
    }

    def 'predicate passing non-null returns expected value'() {
        expect:
        def predicate = predicate { String s -> s.isEmpty() }
        predicate.test('')
    }

    @Unroll
    def 'predicate default passing parameter "#booleanParameter" and target "#target" returns #expected'() {

        expect:
        def predicate = predicateDefault({ String s -> s.isEmpty() }, defaultParameter)
        predicate.test(target) == expected

        where:
        defaultParameter | target | expected
        true             | null   | true
        true             | 'test' | false
        true             | ''     | true
        false            | null   | false
        false            | 'test' | false
        false            | ''     | true
    }

    @Unroll
    def 'predicate for bi-predicate passing constantValue "#constantValue" and target "#target" returns #expected'() {

        expect:
        def predicate = predicate({ a, b -> a.equals(b) }, constantValue)
        predicate.test(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'inverse predicate for bi-predicate passing constantValue "#constantValue" and target "#target" returns #expected'() {

        expect:
        def predicate = inversePredicate({ a, b -> a.equals(b) }, constantValue)
        predicate.test(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'predicate constant passing parameter "#booleanParameter" and target "#target" returns #expected'() {

        expect:
        def predicate = constant booleanParameter
        predicate.test(target) == expected

        where:
        booleanParameter | target | expected
        true             | null   | true
        true             | 'test' | true
        false            | null   | false
        false            | 'test' | false
    }

    @Unroll
    def 'from mapper passing target "#target" returns #expected'() {

        expect:
        def predicate = fromMapper { String s -> !s.isEmpty() }
        predicate.test(target) == expected

        where:
        target | expected
        null   | false
        ''     | false
        'test' | true
    }

    def 'not passing null value throws NPE'() {

        when:
        def predicate = not { String s -> s.isEmpty() }
        predicate.test(null)

        then:
        thrown(NullPointerException)
    }

    def 'not passing non-null returns expected value'() {
        expect:
        def predicate = not { String s -> s.isEmpty() }
        predicate.test('test')
    }

    @Unroll
    def 'is string empty passing "#target" returns #expected'() {

        expect:
        def predicate = isStrEmpty { String s -> s.reverse() }
        predicate.test(target) == expected

        where:
        target | expected
        null   | true
        ''     | true
        'test' | false
    }

    @Unroll
    def 'is string not empty passing "#target" returns #expected'() {

        expect:
        def predicate = isStrNotEmpty { String s -> s.reverse() }
        predicate.test(target) == expected

        where:
        target | expected
        null   | false
        ''     | false
        'test' | true
    }

    @Unroll
    def 'equals ignore case passing constantValue #constantValue and target #target returns #expected'() {

        expect:
        def predicate = equalsIgnoreCase({ String s -> s.toString() }, constantValue)
        predicate.test(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        'test'        | 'TEST' | true
        'TEST'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'is equal passing constantValue #constantValue and target #target returns #expected'() {

        expect:
        def predicate = isEqual({ String s -> s.toString() }, constantValue)
        predicate.test(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | false
        'test'        | ''     | false
        'test'        | 'test' | true
        null          | 'test' | false
        ''            | 'test' | false
    }

    @Unroll
    def 'is not equal passing constantValue #constantValue and target #target returns #expected'() {

        expect:
        def predicate = isNotEqual({ String s -> s.toString() }, constantValue)
        predicate.test(target) == expected

        where:
        constantValue | target | expected
        'test'        | null   | true
        'test'        | ''     | true
        'test'        | 'test' | false
        null          | 'test' | true
        ''            | 'test' | true
    }

    @Unroll
    def 'contains passing collection "#collection" and target "#target" returns "#expected"'() {

        expect:
        def predicate = contains({ String s -> collection }, target)
        predicate.test(target) == expected

        where:
        collection | target | expected
        ['test']   | null   | false
        ['test']   | ''     | false
        ['test']   | 'test' | true
        [null]     | 'test' | false
        [null]     | null   | false
        ['']       | 'test' | false
        []         | 'test' | false
    }

    @Unroll
    def 'inverse contains passing collection "#collection" and target "#target" returns "#expected"'() {

        expect:
        def predicate = inverseContains(collection, { String s -> s.toString() })
        predicate.test(target) == expected

        where:
        collection | target | expected
        ['test']   | null   | false
        ['test']   | ''     | false
        ['test']   | 'test' | true
        [null]     | 'test' | false
        [null]     | null   | false
        ['']       | 'test' | false
        []         | 'test' | false
    }

    @Unroll
    def 'contains key for map #map passing #enumValue returns #expected'() {

        expect:
        def predicate = containsKey(map, { TestValue t -> t.name() })
        predicate.test(enumValue) == expected

        where:
        map                            | enumValue     | expected
        null                           | TestValue.ONE | false
        [:]                            | TestValue.ONE | false
        TestValue.makeNameToValueMap() | null          | false
        TestValue.makeNameToValueMap() | TestValue.ONE | true
        TestValue.makeNameToValueMap() | TestValue.TWO | true
    }

    @Unroll
    def 'contains value for map #map passing #enumValue returns #expected'() {

        expect:
        def predicate = containsValue(map, { TestValue t -> t.name() })
        predicate.test(enumValue) == expected

        where:
        map                            | enumValue     | expected
        null                           | TestValue.ONE | false
        [:]                            | TestValue.ONE | false
        TestValue.makeValueToNameMap() | null          | false
        TestValue.makeValueToNameMap() | TestValue.ONE | true
        TestValue.makeValueToNameMap() | TestValue.TWO | true
    }

    @Unroll
    def 'contains char with string extractor passing value "#extractedString" and "#searchChar" returns "#expected"'() {

        expect:
        def predicate = containsChar(Function.identity(), searchChar.codePointAt(0))
        predicate.test(extractedString) == expected

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
        def predicate = containsSequence(Function.identity(), searchSeq)
        predicate.test(extractedString) == expected

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
        def predicate = containsIgnoreCase(Function.identity(), searchSeq)
        predicate.test(extractedString) == expected

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
        def predicate = isAlpha Function.identity()
        predicate.test(extractedString) == expected

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
        def predicate = isAlphanumeric Function.identity()
        predicate.test(extractedString) == expected

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
        def predicate = isNumeric Function.identity()
        predicate.test(extractedString) == expected

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
        def predicate = startsWith(Function.identity(), prefix)
        predicate.test(extractedString) == expected

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
        def predicate = startsWithIgnoreCase(Function.identity(), prefix)
        predicate.test(extractedString) == expected

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
        def predicate = endsWith(Function.identity(), suffix)
        predicate.test(extractedString) == expected

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
        def predicate = endsWithIgnoreCase(Function.identity(), suffix)
        predicate.test(extractedString) == expected

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
        def predicate = isNull { AbstractMap.SimpleEntry e -> e.getValue() }
        predicate.test(target) == expected

        where:
        target                                      | expected
        new AbstractMap.SimpleEntry("key", "value") | false
        new AbstractMap.SimpleEntry("key", null)    | true
        null                                        | true
    }

    @Unroll
    def 'not null passing value "#target" returns "#expected"'() {

        expect:
        def predicate = notNull { AbstractMap.SimpleEntry e -> e.getValue() }
        predicate.test(target) == expected

        where:
        target                                      | expected
        new AbstractMap.SimpleEntry("key", "value") | true
        new AbstractMap.SimpleEntry("key", null)    | false
        null                                        | false
    }

    @Unroll
    def 'gt with "#target" and "#value" returns #expected'() {
        expect:
        def predicate = gt(identity(), value as Comparable)
        predicate.test(target) == expected

        where:
        target | value | expected
        null   | null  | false
        ''     | 'a'   | false
        'a'    | 'a'   | false
        'a'    | null  | false
        null   | 'a'   | true
        'b'    | 'a'   | true
        'a'    | 'b'   | false
        'a'    | ''    | true
        2      | 1     | true
        1      | 2     | false
    }

    @Unroll
    def 'gte with "#target" and "#value" returns #expected'() {

        expect:
        def predicate = gte(identity(), value as Comparable)
        predicate.test(target) == expected

        where:
        target | value | expected
        null   | null  | true
        ''     | 'a'   | false
        'a'    | 'a'   | true
        'a'    | null  | false
        null   | 'a'   | true
        'b'    | 'a'   | true
        'a'    | 'b'   | false
        'a'    | ''    | true
        2      | 1     | true
        1      | 2     | false
    }

    @Unroll
    def 'lt with "#target" and "#value" returns #expected'() {

        expect:
        def predicate = lt(identity(), value as Comparable)
        predicate.test(target) == expected

        where:
        target | value | expected
        null   | null  | false
        ''     | 'a'   | true
        'a'    | 'a'   | false
        'a'    | null  | true
        null   | 'a'   | false
        'b'    | 'a'   | false
        'a'    | 'b'   | true
        'a'    | ''    | false
        2      | 1     | false
        1      | 2     | true
    }

    @Unroll
    def 'lte with "#target" and "#value" returns #expected'() {

        expect:
        def predicate = lte(identity(), value as Comparable)
        predicate.test(target) == expected

        where:
        target | value | expected
        null   | null  | true
        ''     | 'a'   | true
        'a'    | 'a'   | true
        'a'    | null  | true
        null   | 'a'   | false
        'b'    | 'a'   | false
        'a'    | 'b'   | true
        'a'    | ''    | false
        2      | 1     | false
        1      | 2     | true
    }

    @Unroll
    def 'is empty passing #target returns #expected'() {

        expect:
        def predicate = isCollEmpty { List l -> l.asCollection() }
        predicate.test(target) == expected

        where:
        target   | expected
        ['test'] | false
        null     | true
        []       | true
    }

    @Unroll
    def 'is not empty passing #target returns #expected'() {

        expect:
        def predicate = isCollNotEmpty { List l -> l.asCollection() }
        predicate.test(target) == expected

        where:
        target   | expected
        ['test'] | true
        null     | false
        []       | false
    }

    def 'distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().size() == DISTINCT_KEY_SIZE
    }

    Collection makeEntriesDistinctByKey() {
        (0..<RAW_LIST_SIZE).stream()
                .map({ i -> makeKeyValuePair(i) })
                .filter(distinctByKey(keyExtractor))
                .collect(toList()) as Collection
    }

    Object makeKeyValuePair(i) {
        [key: i % DISTINCT_KEY_SIZE, value: i]
    }

    def 'distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().size() == DISTINCT_KEY_SIZE
    }

    Collection makeEntriesDistinctByKeyParallel() {
        (0..<RAW_LIST_SIZE).parallelStream()
                .map({ i -> makeKeyValuePair(i) })
                .filter(distinctByKeyParallel(keyExtractor))
                .collect(toList()) as Collection
    }

    @Unroll
    def 'map and filter passing input "#input"'() {

        expect:
        def predicate = mapAndFilter({ String s -> s.length() }, isEqual(Function.identity(), 4))
        predicate.test(input) == expected

        where:
        input  | expected
        null   | false
        ''     | false
        'test' | true
    }
}
