package org.hringsak.functions

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.CollectorUtils.toEnumSet
import static org.hringsak.functions.TransformUtils.transformerCollector

class TransformUtilsSpec extends Specification {

    def 'generic transform returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        TransformUtils.transform(list, { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'generic transform returns empty list for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, identity()) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    def 'transform to set returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        TransformUtils.transformToSet(list, { i -> String.valueOf(i) }) == Sets.newHashSet('1', '2', '3')
    }

    @Unroll
    def 'transform to set returns empty list for #scenario parameter'() {

        expect:
        TransformUtils.transformToSet(collection, identity()) == [] as Set

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
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
    def 'transform with collector returns empty list for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, toList()) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    def 'transform with mapping and collector returns expected results'() {

        given:
        def list = ['ONE', 'TWO']
        def transformer = { String enumName -> TestValue.valueOf(enumName) } as Function<String, TestValue>

        when:
        def enumSet = TransformUtils.transform(list, transformerCollector(transformer, toEnumSet(TestValue)))

        then:
        enumSet == EnumSet.allOf(TestValue)
        enumSet instanceof EnumSet
    }

    @Unroll
    def 'transform with mapping and collector returns empty list for #scenario parameter'() {

        expect:
        TransformUtils.transform(collection, transformerCollector(identity(), toList())) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }

    def 'transform distinct returns expected results'() {
        expect:
        TransformUtils.transformDistinct([1, 2, 3, 2], { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'transform distinct returns empty list for #scenario parameter'() {

        expect:
        TransformUtils.transformDistinct(collection, identity()) == []

        where:
        scenario | collection
        'empty'  | []
        'null'   | null
    }
}
