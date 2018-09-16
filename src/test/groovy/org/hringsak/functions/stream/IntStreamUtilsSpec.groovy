package org.hringsak.functions.stream

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.stream.IntStreamUtils.*

class IntStreamUtilsSpec extends Specification {

    @Unroll
    def 'int all match passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intAllMatch(ints, predicate) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | false
        [] as int[]        | true
        null as int[]      | false
    }

    @Unroll
    def 'int any match passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intAnyMatch(ints, predicate) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | true
        [] as int[]        | false
        null as int[]      | false
    }

    @Unroll
    def 'int none match passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intNoneMatch(ints, predicate) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | false
        [] as int[]        | true
        null as int[]      | false
    }

    @Unroll
    def 'int count passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intCount(ints, predicate) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | 2L
        [] as int[]        | 0L
        null as int[]      | 0L
    }

    @Unroll
    def 'int max default passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intMaxDefault(ints, findIntDefault(predicate, -1)) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | 3
        [] as int[]        | -1
        null as int[]      | -1
    }

    @Unroll
    def 'int max default supplier passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intMaxDefaultSupplier(ints, findIntDefaultSupplier(predicate, { -1 })) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | 3
        [] as int[]        | -1
        null as int[]      | -1
    }

    @Unroll
    def 'int min default passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intMinDefault(ints, findIntDefault(predicate, -1)) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | 2
        [] as int[]        | -1
        null as int[]      | -1
    }

    @Unroll
    def 'int min default supplier passing ints #ints returns #expected'() {

        expect:
        def predicate = { i -> i > 1 }
        intMinDefaultSupplier(ints, findIntDefaultSupplier(predicate, { -1 })) == expected

        where:
        ints               | expected
        [1, 2, 3] as int[] | 2
        [] as int[]        | -1
        null as int[]      | -1
    }

    @Unroll
    def 'find any int default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        findAnyIntDefaultNull([1, 2, 3] as int[], predicate) == expected

        where:
        compareValue | expected
        2            | 3
        3            | null
    }

    @Unroll
    def 'find any int default returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        findAnyIntDefault([1, 2, 3] as int[], findIntDefault(predicate, -1)) == expected

        where:
        compareValue | expected
        2            | 3
        3            | -1
    }

    @Unroll
    def 'find any int default supplier returns #expected value for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        def supplier = { -1 }
        findAnyIntDefault([1, 2, 3] as int[], findIntDefaultSupplier(predicate, supplier)) == expected

        where:
        compareValue | expected
        2            | 3
        3            | -1
    }

    @Unroll
    def 'find first int default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        findFirstIntDefaultNull([1, 2, 3] as int[], predicate) == expected

        where:
        compareValue | expected
        2            | 3
        3            | null
    }

    @Unroll
    def 'find first int default returns #expected for compareLength #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        findFirstIntDefault([1, 2, 3] as int[], findIntDefault(predicate, -1)) == expected

        where:
        compareValue | expected
        2            | 3
        3            | -1
    }

    @Unroll
    def 'find first int default supplier returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        def supplier = { -1 }
        findFirstIntDefaultSupplier([1, 2, 3] as int[], findIntDefaultSupplier(predicate, supplier)) == expected

        where:
        compareValue | expected
        2            | 3
        3            | -1
    }

    @Unroll
    def 'index of first int returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        indexOfFirstInt([1, 2, 3] as int[], predicate) == expected

        where:
        compareValue | expected
        2            | 2
        3            | -1
    }

    @Unroll
    def 'int any match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        intAnyMatch([1, 2, 3] as int[], predicate) == expected

        where:
        compareValue | expected
        2            | true
        3            | false
    }

    @Unroll
    def 'int none match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { i -> i > compareValue }
        intNoneMatch([1, 2, 3] as int[], predicate) == expected

        where:
        compareValue | expected
        2            | false
        3            | true
    }

    def 'int join returns expected results'() {
        expect:
        intJoin([1, 2, 3] as int[], { i -> String.valueOf(i) }) == '1,2,3'
    }

    @Unroll
    def 'int join returns "#expected" for delimiter "#delimiter"'() {

        expect:
        intJoin([1, 2, 3] as int[], { i -> String.valueOf(i) }, delimiter) == expected

        where:
        delimiter || expected
        ','       || '1,2,3'
        ', '      || '1, 2, 3'
    }

    @Unroll
    def 'int join returns "#expected" for delimiter "#delimiter", prefix "#prefix", and suffix "#suffix"'() {

        expect:
        def joiner = joining(delimiter, prefix, suffix)
        intJoin([1, 2, 3] as int[], { i -> String.valueOf(i) }, joiner) == expected

        where:
        delimiter | prefix | suffix  || expected
        ','       | 'pre+' | '+post' || 'pre+1,2,3+post'
        ', '      | ''     | ''      || '1, 2, 3'
    }

    @Unroll
    def 'int join returns empty string for #scenario parameter'() {

        expect:
        intJoin(values, { i -> String.valueOf(i) }) == ''

        where:
        scenario | values
        'empty'  | [] as int[]
        'null'   | null
    }

    @Unroll
    def 'to partitioned int list returns #expectedPartitions for #scenario'() {

        when:
        def partitions = toPartitionedIntList(inputArray, 2)

        then:
        partitions == expectedPartitions

        where:
        scenario          | inputArray                  || expectedPartitions
        'populated list'  | [1, 2, 3, 4, 5, 6] as int[] || [[1, 2] as int[], [3, 4] as int[], [5, 6] as int[]]
        'empty list'      | [] as int[]                 || []
        'null collection' | null                        || []
    }

    @Unroll
    def 'to partitioned int stream returns #expectedPartitions for #scenario'() {

        when:
        def partitionStream = toPartitionedIntStream(inputArray, 2)

        then:
        partitionStream.collect(toList()) == expectedPartitions

        where:
        scenario          | inputArray                  || expectedPartitions
        'populated list'  | [1, 2, 3, 4, 5, 6] as int[] || [[1, 2] as int[], [3, 4] as int[], [5, 6] as int[]]
        'empty list'      | [] as int[]                 || []
        'null collection' | null                        || []
    }
}
