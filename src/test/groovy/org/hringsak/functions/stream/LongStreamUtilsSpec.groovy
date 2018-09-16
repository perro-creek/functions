package org.hringsak.functions.stream

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.stream.LongStreamUtils.*

class LongStreamUtilsSpec extends Specification {

    @Unroll
    def 'long all match passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longAllMatch(longs, predicate) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | false
        [] as long[]           | true
        null as long[]         | false
    }

    @Unroll
    def 'long any match passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longAnyMatch(longs, predicate) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | true
        [] as long[]           | false
        null as long[]         | false
    }

    @Unroll
    def 'long none match passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longNoneMatch(longs, predicate) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | false
        [] as long[]           | true
        null as long[]         | false
    }

    @Unroll
    def 'long count passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longCount(longs, predicate) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | 2L
        [] as long[]           | 0L
        null as long[]         | 0L
    }

    @Unroll
    def 'long max default passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longMaxDefault(longs, findLongDefault(predicate, -1L)) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | 3L
        [] as long[]           | -1L
        null as long[]         | -1L
    }

    @Unroll
    def 'long max default supplier passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longMaxDefaultSupplier(longs, findLongDefaultSupplier(predicate, { -1L })) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | 3L
        [] as long[]           | -1L
        null as long[]         | -1L
    }

    @Unroll
    def 'long min default passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longMinDefault(longs, findLongDefault(predicate, -1L)) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | 2L
        [] as long[]           | -1L
        null as long[]         | -1L
    }

    @Unroll
    def 'long min default supplier passing longs #longs returns #expected'() {

        expect:
        def predicate = { l -> l > 1L }
        longMinDefaultSupplier(longs, findLongDefaultSupplier(predicate, { -1L })) == expected

        where:
        longs                  | expected
        [1L, 2L, 3L] as long[] | 2L
        [] as long[]           | -1L
        null as long[]         | -1L
    }

    @Unroll
    def 'find any long default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        findAnyLongDefaultNull([1L, 2L, 3L] as long[], predicate) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | null
    }

    @Unroll
    def 'find any long default returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        findAnyLongDefault([1L, 2L, 3L] as long[], findLongDefault(predicate, -1L)) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | -1L
    }

    @Unroll
    def 'find any long default supplier returns #expected value for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        def supplier = { -1L }
        findAnyLongDefault([1L, 2L, 3L] as long[], findLongDefaultSupplier(predicate, supplier)) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | -1L
    }

    @Unroll
    def 'find first long default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        findFirstLongDefaultNull([1L, 2L, 3L] as long[], predicate) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | null
    }

    @Unroll
    def 'find first long default returns #expected for compareLength #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        findFirstLongDefault([1L, 2L, 3L] as long[], findLongDefault(predicate, -1)) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | -1L
    }

    @Unroll
    def 'find first long default supplier returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        def supplier = { -1L }
        findFirstLongDefaultSupplier([1L, 2L, 3L] as long[], findLongDefaultSupplier(predicate, supplier)) == expected

        where:
        compareValue | expected
        2L           | 3L
        3L           | -1L
    }

    @Unroll
    def 'index of first long returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        indexOfFirstLong([1L, 2L, 3L] as long[], predicate) == expected

        where:
        compareValue | expected
        2L           | 2
        3L           | -1
    }

    @Unroll
    def 'long any match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        longAnyMatch([1L, 2L, 3L] as long[], predicate) == expected

        where:
        compareValue | expected
        2L           | true
        3L           | false
    }

    @Unroll
    def 'long none match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { l -> l > compareValue }
        longNoneMatch([1L, 2L, 3L] as long[], predicate) == expected

        where:
        compareValue | expected
        2L           | false
        3L           | true
    }

    def 'long join returns expected results'() {
        expect:
        longJoin([1L, 2L, 3L] as long[], { l -> String.valueOf(l) }) == '1,2,3'
    }

    @Unroll
    def 'long join returns "#expected" for delimiter "#delimiter"'() {

        expect:
        longJoin([1L, 2L, 3L] as long[], { l -> String.valueOf(l) }, delimiter) == expected

        where:
        delimiter || expected
        ','       || '1,2,3'
        ', '      || '1, 2, 3'
    }

    @Unroll
    def 'long join returns "#expected" for delimiter "#delimiter", prefix "#prefix", and suffix "#suffix"'() {

        expect:
        def joiner = joining(delimiter, prefix, suffix)
        longJoin([1L, 2L, 3L] as long[], { l -> String.valueOf(l) }, joiner) == expected

        where:
        delimiter | prefix | suffix  || expected
        ','       | 'pre+' | '+post' || 'pre+1,2,3+post'
        ', '      | ''     | ''      || '1, 2, 3'
    }

    @Unroll
    def 'long join returns empty string for #scenario parameter'() {

        expect:
        longJoin(values, { l -> String.valueOf(l) }) == ''

        where:
        scenario | values
        'empty'  | [] as long[]
        'null'   | null
    }

    @Unroll
    def 'to partitioned long list returns #expectedPartitions for #scenario'() {

        when:
        def partitions = toPartitionedLongList(inputArray, 2)

        then:
        partitions == expectedPartitions

        where:
        scenario          | inputArray                         || expectedPartitions
        'populated list'  | [1L, 2L, 3L, 4L, 5L, 6L] as long[] || [[1L, 2L] as long[], [3L, 4L] as long[], [5L, 6L] as long[]]
        'empty list'      | [] as long[]                       || []
        'null collection' | null                               || []
    }

    @Unroll
    def 'to partitioned long stream returns #expectedPartitions for #scenario'() {

        when:
        def partitionStream = toPartitionedLongStream(inputArray, 2)

        then:
        partitionStream.collect(toList()) == expectedPartitions

        where:
        scenario          | inputArray                         || expectedPartitions
        'populated list'  | [1L, 2L, 3L, 4L, 5L, 6L] as long[] || [[1L, 2L] as long[], [3L, 4L] as long[], [5L, 6L] as long[]]
        'empty list'      | [] as long[]                       || []
        'null collection' | null                               || []
    }
}
