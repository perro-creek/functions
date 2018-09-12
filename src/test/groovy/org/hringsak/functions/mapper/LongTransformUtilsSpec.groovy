package org.hringsak.functions.mapper

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toCollection
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.mapper.LongMapperUtils.longKeyValueMapper
import static org.hringsak.functions.mapper.LongTransformUtils.*

class LongTransformUtilsSpec extends Specification {

    def strToLongArray = { s ->
        s.codePoints()
                .mapToLong { int i -> (long) i }
                .toArray()
    }

    @Unroll
    def 'long unary transform passing ints #ints returns #expected'() {

        expect:
        def operator = { l -> l + 1L }
        longUnaryTransform(longs, operator) == expected

        where:
        longs                  || expected
        [1L, 2L, 3L] as long[] || [2L, 3L, 4L] as long[]
        [] as long[]           || [] as long[]
        null as long[]         || [] as long[]
    }

    @Unroll
    def 'long unary transform distinct passing ints #ints returns #expected'() {

        expect:
        def operator = { l -> l + 1L }
        longUnaryTransformDistinct(longs, operator) == expected

        where:
        longs                      || expected
        [1L, 2L, 3L, 2L] as long[] || [2L, 3L, 4L] as long[]
        [] as long[]               || [] as long[]
        null as long[]             || [] as long[]
    }

    def 'long generic transform returns expected results'() {
        expect:
        def longs = [1L, 2L, 3L] as long[]
        def mapper = { l -> String.valueOf(l) }
        longTransform(longs, mapper) == ['1', '2', '3']
    }

    @Unroll
    def 'long generic transform returns #expected for #scenario parameter'() {

        expect:
        def mapper = { long d -> Long.valueOf(d) }
        longTransform(longs, mapper) == expected

        where:
        scenario                      | longs          || expected
        'empty'                       | [] as long[]   || []
        'null'                        | null as long[] || []
        'collection containing value' | [1L] as long[] || [1L]
    }

    def 'long transform to set returns expected results'() {
        expect:
        def longs = [1L, 2L, 3L] as long[]
        def mapper = { l -> String.valueOf(l) }
        longTransformToSet(longs, mapper) == Sets.newHashSet('1', '2', '3')
    }

    @Unroll
    def 'long transform to set returns #expected for #scenario parameter'() {

        expect:
        def mapper = { long l -> Long.valueOf(l) }
        longTransformToSet(longs, mapper) == expected as Set

        where:
        scenario                      | longs          || expected
        'empty'                       | [] as long[]   || []
        'null'                        | null as long[] || []
        'collection containing value' | [1L] as long[] || [1L]
    }

    def 'long transform with collector returns expected results'() {
        expect:
        def longs = [1L, 2L, 3L] as long[]
        longTransform(longs, toCollection({ new LinkedList() })) == [1L, 2L, 3L]
    }

    @Unroll
    def 'long transform with collector returns #expected for #scenario parameter'() {

        expect:
        longTransform(longs, toList()) == expected

        where:
        scenario                      | longs          || expected
        'empty'                       | [] as long[]   || []
        'null'                        | null as long[] || []
        'collection containing value' | [1L] as long[] || [1L]
    }

    def 'long transform with mapping and collector returns expected results'() {
        expect:
        def longs = [1L, 2L] as long[]
        def transformer = { long l -> String.valueOf(l) }
        longTransform(longs, longTransformAndThen(transformer, toCollection({ new LinkedList() })))
    }

    @Unroll
    def 'long transform with mapping and collector returns #expected for #scenario parameter'() {

        expect:
        def mapper = { long l -> Long.valueOf(l) }
        longTransform(longs, longTransformAndThen(mapper, toCollection({ new LinkedList() }))) == expected

        where:
        scenario                      | longs          || expected
        'empty'                       | [] as long[]   || []
        'null'                        | null as long[] || []
        'collection containing value' | [1L] as long[] || [1L]
    }

    def 'long transform distinct returns expected results'() {
        expect:
        def longs = [1L, 2L, 3L, 2L] as long[]
        longTransformDistinct(longs, { l -> String.valueOf(l) }) == ['1', '2', '3']
    }

    @Unroll
    def 'long transform distinct returns #expected for #scenario parameter'() {

        expect:
        def mapper = { long l -> Long.valueOf(l) }
        longTransformDistinct(longs, mapper) == expected

        where:
        scenario                      | longs          || expected
        'empty'                       | [] as long[]   || []
        'null'                        | null as long[] || []
        'collection containing value' | [1L] as long[] || [1L]
    }

    def 'long transform to map returns expected'() {
        expect:
        def longs = [1L, 2L, 3L] as long[]
        def keyMapper = { long l -> Long.valueOf(l) }
        def valueMapper = { long l -> Long.valueOf(l).toString() }
        def expectedMap = [(1L): '1', (2L): '2', (3L): '3']
        longTransformToMap(longs, longKeyValueMapper(keyMapper, valueMapper)) == expectedMap
    }

    @Unroll
    def 'long transform to map returns empty list for #scenario parameter'() {

        expect:
        def keyMapper = { long l -> Long.valueOf(l) }
        def valueMapper = { long l -> Long.valueOf(l).toString() }
        longTransformToMap(longs, longKeyValueMapper(keyMapper, valueMapper)) == [:]

        where:
        scenario | longs
        'empty'  | [] as long[]
        'null'   | null as long[]
    }

    @Unroll
    def 'long flat map passing longs #longs returns #expected'() {

        expect:
        def operator = { d -> [d, d + 1L] as long[] }
        longFlatMap(longs, operator) == expected

        where:
        longs                  || expected
        [1L, 2L, 3L] as long[] || [1L, 2L, 2L, 3L, 3L, 4L] as long[]
        [] as long[]           || [] as long[]
        null as long[]         || [] as long[]
    }

    @Unroll
    def 'long flat map distinct passing longs #longs returns #expected'() {

        expect:
        def operator = { d -> [d, d + 1L] as long[] }
        longFlatMapDistinct(longs, operator) == expected

        where:
        longs                  || expected
        [1L, 2L, 3L] as long[] || [1L, 2L, 3L, 4L] as long[]
        [] as long[]           || [] as long[]
        null as long[]         || [] as long[]
    }

    @Unroll
    def 'flat map to long passing objects #objects returns #expected'() {

        expect:
        flatMapToLong(objects, strToLongArray) == expected

        where:
        objects  || expected
        ['test'] || [116L, 101L, 115L, 116L] as long[]
        [null]   || [] as long[]
        []       || [] as long[]
        null     || [] as long[]
    }

    @Unroll
    def 'flat map to long distinct passing objects #objects returns #expected'() {

        expect:
        flatMapToLongDistinct(objects, strToLongArray) == expected

        where:
        objects  || expected
        ['test'] || [116L, 101L, 115L] as long[]
        [null]   || [] as long[]
        []       || [] as long[]
        null     || [] as long[]
    }
}
