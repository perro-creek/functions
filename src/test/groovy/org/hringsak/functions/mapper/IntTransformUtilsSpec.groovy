package org.hringsak.functions.mapper

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toCollection
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.mapper.IntMapperUtils.intKeyValueMapper
import static org.hringsak.functions.mapper.IntTransformUtils.*

class IntTransformUtilsSpec extends Specification {

    def 'int generic transform returns expected results'() {
        expect:
        def ints = [1, 2, 3] as int[]
        def mapper = { i -> String.valueOf(i) }
        intTransform(ints, mapper) == ['1', '2', '3']
    }

    @Unroll
    def 'int generic transform returns #expected for #scenario parameter'() {

        expect:
        def mapper = { int i -> Integer.valueOf(i) }
        intTransform(ints, mapper) == expected

        where:
        scenario                      | ints          || expected
        'empty'                       | [] as int[]   || []
        'null'                        | null as int[] || []
        'collection containing value' | [1] as int[]  || [1]
    }

    def 'int transform to set returns expected results'() {
        expect:
        def ints = [1, 2, 3] as int[]
        def mapper = { d -> String.valueOf(d) }
        intTransformToSet(ints, mapper) == Sets.newHashSet('1', '2', '3')
    }

    @Unroll
    def 'int transform to set returns #expected for #scenario parameter'() {

        expect:
        def mapper = { int i -> Integer.valueOf(i) }
        intTransformToSet(ints, mapper) == expected as Set

        where:
        scenario                      | ints          || expected
        'empty'                       | [] as int[]   || []
        'null'                        | null as int[] || []
        'collection containing value' | [1] as int[]  || [1]
    }

    def 'int transform with collector returns expected results'() {
        expect:
        def ints = [1, 2, 3] as int[]
        intTransform(ints, toCollection({ new LinkedList() })) == [1, 2, 3]
    }

    @Unroll
    def 'int transform with collector returns #expected for #scenario parameter'() {

        expect:
        intTransform(ints, toList()) == expected

        where:
        scenario                      | ints          || expected
        'empty'                       | [] as int[]   || []
        'null'                        | null as int[] || []
        'collection containing value' | [1] as int[]  || [1]
    }

    def 'int transform with mapping and collector returns expected results'() {
        expect:
        def ints = [1, 2] as int[]
        def transformer = { int i -> String.valueOf(i) }
        intTransform(ints, intTransformAndThen(transformer, toCollection({ new LinkedList() })))
    }

    @Unroll
    def 'int transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        def mapper = { int i -> Integer.valueOf(i) }
        intTransform(ints, intTransformAndThen(mapper, toCollection({ new LinkedList() }))) == expected

        where:
        scenario                      | ints          || expected
        'empty'                       | [] as int[]   || []
        'null'                        | null as int[] || []
        'collection containing value' | [1] as int[]  || [1]
    }

    def 'int transform distinct returns expected results'() {
        expect:
        def ints = [1, 2, 3, 2] as int[]
        intTransformDistinct(ints, { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    @Unroll
    def 'int transform distinct returns #expected for #scenario parameter'() {

        expect:
        def mapper = { int i -> Integer.valueOf(i) }
        intTransformDistinct(ints, mapper) == expected

        where:
        scenario                      | ints          || expected
        'empty'                       | [] as int[]   || []
        'null'                        | null as int[] || []
        'collection containing value' | [1] as int[]  || [1]
    }

    def 'int transform to map returns expected'() {
        expect:
        def ints = [1, 2, 3] as int[]
        def keyMapper = { int i -> Integer.valueOf(i) }
        def valueMapper = { int i -> Integer.valueOf(i).toString() }
        def expectedMap = [(1): '1', (2): '2', (3): '3']
        intTransformToMap(ints, intKeyValueMapper(keyMapper, valueMapper)) == expectedMap
    }

    @Unroll
    def 'int transform to map returns empty list for #scenario parameter'() {

        expect:
        def keyMapper = { int i -> Integer.valueOf(i) }
        def valueMapper = { int i -> Integer.valueOf(i).toString() }
        intTransformToMap(ints, intKeyValueMapper(keyMapper, valueMapper)) == [:]

        where:
        scenario | ints
        'empty'  | [] as int[]
        'null'   | null as int[]
    }
}
