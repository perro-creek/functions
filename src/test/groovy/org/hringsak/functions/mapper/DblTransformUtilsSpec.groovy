package org.hringsak.functions.mapper

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toCollection
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.mapper.DblMapperUtils.dblKeyValueMapper
import static org.hringsak.functions.mapper.DblTransformUtils.*

class DblTransformUtilsSpec extends Specification {

    def 'generic transform returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def mapper = { d -> String.valueOf(d) }
        dblTransform(doubles, mapper) == ['1.0', '2.0', '3.0']
    }

    @Unroll
    def 'generic transform returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransform(doubles, mapper) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'transform to set returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def mapper = { d -> String.valueOf(d) }
        dblTransformToSet(doubles, mapper) == Sets.newHashSet('1.0', '2.0', '3.0')
    }

    @Unroll
    def 'transform to set returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransformToSet(doubles, mapper) == expected as Set

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'transform with collector returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblTransform(doubles, toCollection({ new LinkedList() })) == [1.0D, 2.0D, 3.0D]
    }

    @Unroll
    def 'transform with collector returns #expected for #scenario parameter'() {

        expect:
        dblTransform(doubles, toList()) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'transform with mapping and collector returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D] as double[]
        def transformer = { double d -> String.valueOf(d) }
        dblTransform(doubles, dblTransformAndThen(transformer, toCollection({ new LinkedList() })))
    }

    @Unroll
    def 'transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransform(doubles, dblTransformAndThen(mapper, toCollection({ new LinkedList() }))) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'transform distinct returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D, 2.0D] as double[]
        dblTransformDistinct(doubles, { i -> String.valueOf(i) }) == ['1.0', '2.0', '3.0']
    }

    @Unroll
    def 'transform distinct returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransformDistinct(doubles, mapper) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'transform to map returns expected'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def keyMapper = { double d -> Double.valueOf(d) }
        def valueMapper = { double d -> Double.valueOf(d).toString() }
        def expectedMap = [(1.0D): '1.0', (2.0D): '2.0', (3.0D): '3.0']
        dblTransformToMap(doubles, dblKeyValueMapper(keyMapper, valueMapper)) == expectedMap
    }

    @Unroll
    def 'transform to map returns empty list for #scenario parameter'() {

        expect:
        def keyMapper = { double d -> Double.valueOf(d) }
        def valueMapper = { double d -> Double.valueOf(d).toString() }
        dblTransformToMap(doubles, dblKeyValueMapper(keyMapper, valueMapper)) == [:]

        where:
        scenario | doubles
        'empty'  | [] as double[]
        'null'   | null as double[]
    }
}
