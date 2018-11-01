package org.hringsak.functions.mapper

import org.hringsak.functions.TestValue
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toCollection
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.collector.CollectorUtils.toEnumSet
import static org.hringsak.functions.mapper.MapperUtils.keyValueMapper
import static org.hringsak.functions.mapper.TransformUtils.*

class TransformUtilsSpec extends Specification {

    def 'generic transform returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        transform(list, { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'generic transform returns #expected for #scenario parameter'() {

        expect:
        transform(collection, identity()) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform to set returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        transformToSet(list, { i -> String.valueOf(i) }) == ['1', '2', '3'] as HashSet
    }

    @Unroll
    def 'transform to set returns #expected for #scenario parameter'() {

        expect:
        transformToSet(collection, identity()) == expected as Set

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform with mapping and collector returns expected results'() {

        given:
        def list = ['ONE', 'TWO']
        def transformer = { String enumName -> TestValue.valueOf(enumName) } as Function<String, TestValue>

        when:
        def enumSet = transform(list, transformAndThen(transformer, toEnumSet(TestValue)))

        then:
        enumSet == EnumSet.allOf(TestValue)
        enumSet instanceof EnumSet
    }

    @Unroll
    def 'transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        transform(collection, transformAndThen(identity(), toList())) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform distinct returns expected results'() {
        expect:
        transformDistinct([1, 2, 3, 2], { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'transform distinct returns #expected for #scenario parameter'() {

        expect:
        transformDistinct(collection, identity()) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform to map returns expected'() {
        expect:
        transformToMap(['foo', 'bar', 'bazz'], keyValueMapper({ s -> s }, { String s -> s.length() })) == [foo: 3, bar: 3, bazz: 4]
    }

    @Unroll
    def 'transform to map returns empty list for #scenario parameter'() {

        expect:
        transformToMap(collection, keyValueMapper({ s -> s }, { String s -> s.length() })) == [:]

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    @Unroll
    def 'generic flat map returns expected results passing collection #collection'() {

        given:
        def function = { s -> ['a', 'b', 'c'] }

        expect:
        flatMap(collection, function) == expectedList

        where:
        collection || expectedList
        ['test']   || ['a', 'b', 'c']
        [null]     || []
        null       || []
    }

    @Unroll
    def 'flat map to set returns expected results passing collection #collection'() {

        given:
        def function = { s -> ['a', 'b', 'c'] }

        expect:
        flatMapToSet(collection, function) == expectedList

        where:
        collection || expectedList
        ['test']   || ['a', 'b', 'c'] as Set
        [null]     || [] as Set
        null       || [] as Set
    }

    @Unroll
    def 'flat map distinct returns expected results passing collection #collection'() {

        given:
        def function = { s -> ['a', 'b', 'c'] }

        expect:
        flatMapDistinct(collection, function) == expectedList

        where:
        collection         || expectedList
        ['test1', 'test2'] || ['a', 'b', 'c']
        [null]             || []
        null               || []
    }

    @Unroll
    def 'flat map with function and collector returns expected results passing collection #collection'() {

        given:
        def function = { s -> ['a', 'b', 'c'] }

        expect:
        def result = flatMap(collection, flatMapAndThen(function, toCollection({ new LinkedList() })))
        result == expectedList
        result instanceof LinkedList

        where:
        collection || expectedList
        ['test1']  || ['a', 'b', 'c']
        [null]     || []
        null       || []
    }
}
