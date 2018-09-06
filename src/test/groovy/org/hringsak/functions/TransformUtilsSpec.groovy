package org.hringsak.functions

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.CollectorUtils.toEnumSet
import static org.hringsak.functions.MapperUtils.keyValueMapper
import static org.hringsak.functions.TransformUtils.transformAndThen
import static org.hringsak.functions.TransformUtils.transformToMap

class TransformUtilsSpec extends Specification {

    def 'generic transform returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        TransformUtils.transform(list, { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'generic transform returns #expected for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, identity()) == expected

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
        TransformUtils.transformToSet(list, { i -> String.valueOf(i) }) == Sets.newHashSet('1', '2', '3')
    }

    @Unroll
    def 'transform to set returns #expected for #scenario parameter'() {

        expect:
        TransformUtils.transformToSet(collection, identity()) == expected as Set

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform with collector returns expected results'() {

        given:
        def list = [TestValue.ONE, TestValue.TWO]

        when:
        def enumSet = TransformUtils.transform(list, toEnumSet(TestValue))

        then:
        enumSet == EnumSet.allOf(TestValue)
        enumSet instanceof EnumSet
    }

    @Unroll
    def 'transform with collector returns #expected for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, toList()) == expected

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
        def enumSet = TransformUtils.transform(list, transformAndThen(transformer, toEnumSet(TestValue)))

        then:
        enumSet == EnumSet.allOf(TestValue)
        enumSet instanceof EnumSet
    }

    @Unroll
    def 'transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, transformAndThen(identity(), toList())) == expected

        where:
        scenario                     | collection || expected
        'empty'                      | []         || []
        'null'                       | null       || []
        'collection containing null' | [null]     || [null]
    }

    def 'transform distinct returns expected results'() {
        expect:
        TransformUtils.transformDistinct([1, 2, 3, 2], { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'transform distinct returns #expected for #scenario parameter'() {

        expect:
        TransformUtils.transformDistinct(collection, identity()) == expected

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
}
