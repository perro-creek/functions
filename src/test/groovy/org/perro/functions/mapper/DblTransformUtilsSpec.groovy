package org.perro.functions.mapper

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toCollection
import static java.util.stream.Collectors.toList
import static DblMapperUtils.dblKeyValueMapper
import static DblTransformUtils.*

class DblTransformUtilsSpec extends Specification {

    @Unroll
    def 'double unary transform passing doubles #doubles returns #expected'() {

        expect:
        def operator = { d -> d + 1.0D }
        dblUnaryTransform(doubles, operator) == expected

        where:
        doubles                        || expected
        [1.0D, 2.0D, 3.0D] as double[] || [2.0D, 3.0D, 4.0D] as double[]
        [] as double[]                 || [] as double[]
        null as double[]               || [] as double[]
    }

    @Unroll
    def 'double unary transform distinct passing doubles #doubles returns #expected'() {

        expect:
        def operator = { d -> d + 1.0D }
        dblUnaryTransformDistinct(doubles, operator) == expected

        where:
        doubles                              || expected
        [1.0D, 2.0D, 3.0D, 2.0D] as double[] || [2.0D, 3.0D, 4.0D] as double[]
        [] as double[]                       || [] as double[]
        null as double[]                     || [] as double[]
    }

    def 'double generic transform returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def mapper = { d -> String.valueOf(d) }
        dblTransform(doubles, mapper) == ['1.0', '2.0', '3.0']
    }

    @Unroll
    def 'double generic transform returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransform(doubles, mapper) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'double transform to set returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def mapper = { d -> String.valueOf(d) }
        dblTransformToSet(doubles, mapper) == ['1.0', '2.0', '3.0'] as HashSet
    }

    @Unroll
    def 'double transform to set returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransformToSet(doubles, mapper) == expected as Set

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'double transform with collector returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        dblTransform(doubles, toCollection({ new LinkedList() })) == [1.0D, 2.0D, 3.0D]
    }

    @Unroll
    def 'double transform with collector returns #expected for #scenario parameter'() {

        expect:
        dblTransform(doubles, toList()) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'double transform with mapping and collector returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D] as double[]
        def transformer = { double d -> String.valueOf(d) }
        dblTransform(doubles, dblTransformAndThen(transformer, toCollection({ new LinkedList() })))
    }

    @Unroll
    def 'double transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransform(doubles, dblTransformAndThen(mapper, toCollection({ new LinkedList() }))) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'double transform distinct returns expected results'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D, 2.0D] as double[]
        dblTransformDistinct(doubles, { d -> String.valueOf(d) }) == ['1.0', '2.0', '3.0']
    }

    @Unroll
    def 'double transform distinct returns #expected for #scenario parameter'() {

        expect:
        def mapper = { double d -> Double.valueOf(d) }
        dblTransformDistinct(doubles, mapper) == expected

        where:
        scenario                      | doubles            || expected
        'empty'                       | [] as double[]     || []
        'null'                        | null as double[]   || []
        'collection containing value' | [1.0D] as double[] || [1.0D]
    }

    def 'double transform to map returns expected'() {
        expect:
        def doubles = [1.0D, 2.0D, 3.0D] as double[]
        def keyMapper = { double d -> Double.valueOf(d) }
        def valueMapper = { double d -> Double.valueOf(d).toString() }
        def expectedMap = [(1.0D): '1.0', (2.0D): '2.0', (3.0D): '3.0']
        dblTransformToMap(doubles, dblKeyValueMapper(keyMapper, valueMapper)) == expectedMap
    }

    @Unroll
    def 'double transform to map returns empty list for #scenario parameter'() {

        expect:
        def keyMapper = { double d -> Double.valueOf(d) }
        def valueMapper = { double d -> Double.valueOf(d).toString() }
        dblTransformToMap(doubles, dblKeyValueMapper(keyMapper, valueMapper)) == [:]

        where:
        scenario | doubles
        'empty'  | [] as double[]
        'null'   | null as double[]
    }

    @Unroll
    def 'double flat map passing doubles #doubles returns #expected'() {

        expect:
        def operator = { d -> [d, d + 1.0D] as double[] }
        dblFlatMap(doubles, operator) == expected

        where:
        doubles                        || expected
        [1.0D, 2.0D, 3.0D] as double[] || [1.0D, 2.0D, 2.0D, 3.0D, 3.0D, 4.0D] as double[]
        [] as double[]                 || [] as double[]
        null as double[]               || [] as double[]
    }

    @Unroll
    def 'double flat map distinct passing doubles #doubles returns #expected'() {

        expect:
        def operator = { d -> [d, d + 1.0D] as double[] }
        dblFlatMapDistinct(doubles, operator) == expected

        where:
        doubles                        || expected
        [1.0D, 2.0D, 3.0D] as double[] || [1.0D, 2.0D, 3.0D, 4.0D] as double[]
        [] as double[]                 || [] as double[]
        null as double[]               || [] as double[]
    }

    @Unroll
    def 'flat map to double passing objects #objects returns #expected'() {

        expect:
        def strToDoubleArray = { s -> s.codePoints().asDoubleStream().toArray() }
        flatMapToDbl(objects, strToDoubleArray) == expected

        where:
        objects  || expected
        ['test'] || [116.0D, 101.0D, 115.0D, 116.0D] as double[]
        [null]   || [] as double[]
        []       || [] as double[]
        null     || [] as double[]
    }

    @Unroll
    def 'flat map to double distinct passing objects #objects returns #expected'() {

        expect:
        def strToDoubleArray = { s -> s.codePoints().asDoubleStream().toArray() }
        flatMapToDblDistinct(objects, strToDoubleArray) == expected

        where:
        objects  || expected
        ['test'] || [116.0D, 101.0D, 115.0D] as double[]
        [null]   || [] as double[]
        []       || [] as double[]
        null     || [] as double[]
    }
}
