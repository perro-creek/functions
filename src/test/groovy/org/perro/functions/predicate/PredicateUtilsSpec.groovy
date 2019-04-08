package org.perro.functions.predicate


import org.perro.functions.TestValue
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static PredicateUtils.*

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
    def 'is sequence empty passing "#target" with function value "#functionValue" returns #expected'() {

        expect:
        def predicate = isSeqEmpty { s -> functionValue }
        predicate.test(target) == expected

        where:
        target | functionValue | expected
        null   | null          | true
        ''     | ''            | true
        ''     | null          | true
        'test' | 'test'        | false
    }

    @Unroll
    def 'is sequence not empty passing "#target" returns #expected'() {

        expect:
        def predicate = isSeqNotEmpty { String s -> s.reverse() }
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
        target | constantValue | expected
        null   | null          | true
        null   | 'test'        | false
        ''     | 'test'        | false
        'test' | 'test'        | true
        'TEST' | 'test'        | true
        'test' | 'TEST'        | true
        'test' | null          | false
        'test' | ''            | false
    }

    @Unroll
    def 'not equals ignore case passing constantValue #constantValue and target #target returns #expected'() {

        expect:
        def predicate = notEqualsIgnoreCase({ String s -> s.toString() }, constantValue)
        predicate.test(target) == expected

        where:
        target | constantValue | expected
        null   | null          | false
        null   | 'test'        | true
        ''     | 'test'        | true
        'test' | 'test'        | false
        'TEST' | 'test'        | false
        'test' | 'TEST'        | false
        'test' | null          | true
        'test' | ''            | true
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
        def predicate = contains(collection, { String s -> s.toString() })
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
        null       | 'test' | false
    }

    @Unroll
    def 'inverse contains passing collection "#collection" and target "#target" returns "#expected"'() {

        expect:
        def predicate = inverseContains({ s -> collection }, target)
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
        null       | 'test' | false
    }

    @Unroll
    def 'contains all passing collection #collection and function result #functionResult returns #expected'() {

        expect:
        def predicate = containsAll(collection, { obj -> functionResult })
        predicate.test(new Object()) == expected

        where:
        collection         | functionResult | expected
        ['test1', 'test2'] | [null]         | false
        ['test1', 'test2'] | ['']           | false
        ['test1', 'test2'] | []             | true
        ['test1', 'test2'] | ['test1']      | true
        [null]             | ['test1']      | false
        [null]             | [null]         | true
        ['']               | ['test1']      | false
        []                 | ['test1']      | false
        null               | ['test1']      | false
    }

    @Unroll
    def 'inverse contains all passing function result #functionResult and collection #collection returns #expected'() {

        expect:
        def predicate = inverseContainsAll({ obj -> functionResult }, collection)
        predicate.test(new Object()) == expected

        where:
        functionResult     | collection | expected
        [null]             | ['test1']  | false
        ['']               | ['test1']  | false
        []                 | ['test1']  | false
        ['test1', 'test2'] | ['test1']  | true
        ['test1', 'test2'] | [null]     | false
        [null]             | [null]     | true
        ['test1', 'test2'] | ['']       | false
        ['test1', 'test2'] | []         | true
        ['test1', 'test2'] | null       | false
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
    def 'contains char ignore case with string extractor passing value "#extractedString" and "#searchChar" returns "#expected"'() {

        expect:
        def predicate = containsCharIgnoreCase(Function.identity(), searchChar.codePointAt(0))
        predicate.test(extractedString) == expected

        where:
        extractedString | searchChar | expected
        null            | '\0'       | false
        null            | 'E'        | false
        'test'          | '\0'       | false
        'test'          | 'E'        | true
    }

    @Unroll
    def 'contains sequence with string extractor passing value "#extractedString" and "#searchSeq" returns "#expected"'() {

        expect:
        def predicate = containsSeq(Function.identity(), searchSeq)
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
    def 'contains sequence ignore case with string extractor passing value "#extractedString" and "#searchSeq" returns "#expected"'() {

        expect:
        def predicate = containsSeqIgnoreCase(Function.identity(), searchSeq)
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
        ''              | true
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
        ''              | true
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
        ''              | true
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
    def 'any characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = anyCharsMatch(identity(), { int c -> Character.isLetter(c) })
        predicate.test(target) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | true
        '123'     | false
        null      | false
        ''        | false
    }

    @Unroll
    def 'all characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = allCharsMatch(identity(), { int c -> Character.isLetter(c) })
        predicate.test(target) == expected

        where:
        target    | expected
        'test'    | true
        'test123' | false
        '123'     | false
        null      | false
        ''        | true
    }

    @Unroll
    def 'no characters match passing value "#target" returns #expected'() {

        expect:
        def predicate = noCharsMatch(identity(), { int c -> Character.isLetter(c) })
        predicate.test(target) == expected

        where:
        target    | expected
        'test'    | false
        'test123' | false
        '123'     | true
        null      | true
        ''        | true
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
        'a'    | null  | false
        null   | 'a'   | true
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
        'a'    | null  | false
        null   | 'a'   | true
        'b'    | 'a'   | false
        'a'    | 'b'   | true
        'a'    | ''    | false
        2      | 1     | false
        1      | 2     | true
    }

    @Unroll
    def 'is collection empty passing #target returns #expected'() {

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
    def 'is collection not empty passing #target returns #expected'() {

        expect:
        def predicate = isCollNotEmpty { List l -> l.asCollection() }
        predicate.test(target) == expected

        where:
        target   | expected
        ['test'] | true
        null     | false
        []       | false
    }

    @Unroll
    def 'all match passing target "#target" returns #expected'() {

        expect:
        def function = { s -> s.codePoints().boxed().collect(toList()) }
        def predicate = allMatch(function, { i -> i > 100 })
        predicate.test(target) == expected

        where:
        target | expected
        'test' | true
        ' '    | false
        ''     | true
        null   | false
    }

    @Unroll
    def 'any match passing target "#target" returns #expected'() {

        expect:
        def function = { s -> s.codePoints().boxed().collect(toList()) }
        def predicate = anyMatch(function, { i -> i == 101 })
        predicate.test(target) == expected

        where:
        target | expected
        'test' | true
        ''     | false
        null   | false
    }

    @Unroll
    def 'none match passing target "#target" returns #expected'() {

        expect:
        def function = { s -> s.codePoints().boxed().collect(toList()) }
        def predicate = noneMatch(function, { i -> i == 101 })
        predicate.test(target) == expected

        where:
        target | expected
        'test' | false
        ''     | true
        null   | false
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
