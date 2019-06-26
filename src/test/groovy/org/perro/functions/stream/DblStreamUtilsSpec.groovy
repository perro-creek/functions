package org.perro.functions.stream

import spock.lang.Specification
import spock.lang.Unroll

import static java.util.stream.Collectors.toList
import static DblStreamUtils.*

class DblStreamUtilsSpec extends Specification {

    @Unroll
    def 'double all match passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblAllMatch(doubles, predicate) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | false
        [] as double[]                 | true
        null as double[]               | false
    }

    @Unroll
    def 'double any match passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblAnyMatch(doubles, predicate) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | true
        [] as double[]                 | false
        null as double[]               | false
    }

    @Unroll
    def 'double none match passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblNoneMatch(doubles, predicate) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | false
        [] as double[]                 | true
        null as double[]               | false
    }

    @Unroll
    def 'double count passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblCount(doubles, predicate) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | 2L
        [] as double[]                 | 0L
        null as double[]               | 0L
    }

    @Unroll
    def 'double max default passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblMaxDefault(doubles, findDblDefault(predicate, -1.0D)) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | 3.0D
        [] as double[]                 | -1.0D
        null as double[]               | -1.0D
    }

    @Unroll
    def 'double max default supplier passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblMaxDefault(doubles, findDblDefault(predicate, { -1.0D })) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | 3.0D
        [] as double[]                 | -1.0D
        null as double[]               | -1.0D
    }

    @Unroll
    def 'double min default passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblMinDefault(doubles, findDblDefault(predicate, -1.0D)) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | 2.0D
        [] as double[]                 | -1.0D
        null as double[]               | -1.0D
    }

    @Unroll
    def 'double min default supplier passing doubles #doubles returns #expected'() {

        expect:
        def predicate = { d -> d > 1.0D }
        dblMinDefault(doubles, findDblDefault(predicate, { -1.0D })) == expected

        where:
        doubles                        | expected
        [1.0D, 2.0D, 3.0D] as double[] | 2.0D
        [] as double[]                 | -1.0D
        null as double[]               | -1.0D
    }

    @Unroll
    def 'find any double default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        findAnyDblDefaultNull([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | null
    }

    @Unroll
    def 'find any double default returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        findAnyDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefault(predicate, -1.0D)) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | -1.0D
    }

    @Unroll
    def 'find any double default supplier returns #expected value for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        def supplier = { -1.0D }
        findAnyDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefault(predicate, supplier)) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | -1.0D
    }

    @Unroll
    def 'find first double default null returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        findFirstDblDefaultNull([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | null
    }

    @Unroll
    def 'find first double default returns #expected for compareLength #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        findFirstDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefault(predicate, -1.0D)) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | -1.0D
    }

    @Unroll
    def 'find first double default supplier returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        def supplier = { -1.0D }
        findFirstDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefault(predicate, supplier)) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | -1.0D
    }

    @Unroll
    def 'index of first double returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        indexOfFirstDbl([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | 2
        3.0D         | -1
    }

    @Unroll
    @SuppressWarnings("GroovyAssignabilityCheck")
    def 'double any match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        dblAnyMatch([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | true
        3.0D         | false
    }

    @Unroll
    @SuppressWarnings("GroovyAssignabilityCheck")
    def 'double none match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        dblNoneMatch([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | false
        3.0D         | true
    }

    @Unroll
    def 'to partitioned double list returns #expectedPartitions for #scenario'() {

        when:
        def partitions = toPartitionedDblList(inputArray, 2)

        then:
        partitions == expectedPartitions

        where:
        scenario          | inputArray                                       || expectedPartitions
        'populated list'  | [1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D] as double[] || [[1.0D, 2.0D] as double[], [3.0D, 4.0D] as double[], [5.0D, 6.0D] as double[]]
        'empty list'      | [] as double[]                                   || []
        'null collection' | null                                             || []
    }

    @Unroll
    def 'to partitioned double stream returns #expectedPartitions for #scenario'() {

        when:
        def partitionStream = toPartitionedDblStream(inputArray, 2)

        then:
        partitionStream.collect(toList()) == expectedPartitions

        where:
        scenario          | inputArray                                       || expectedPartitions
        'populated list'  | [1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D] as double[] || [[1.0D, 2.0D] as double[], [3.0D, 4.0D] as double[], [5.0D, 6.0D] as double[]]
        'empty list'      | [] as double[]                                   || []
        'null collection' | null                                             || []
    }
}
