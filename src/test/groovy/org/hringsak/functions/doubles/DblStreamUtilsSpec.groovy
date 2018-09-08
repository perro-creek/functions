package org.hringsak.functions.doubles

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.IntStream

import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.doubles.DblPredicateUtils.dblDistinctByKey
import static org.hringsak.functions.doubles.DblPredicateUtils.dblDistinctByKeyParallel
import static org.hringsak.functions.doubles.DblStreamUtils.*

class DblStreamUtilsSpec extends Specification {

    static final int RAW_LIST_SIZE = 1000
    static final int DISTINCT_KEY_SIZE = 100
    def keyExtractor = { double d -> Integer.valueOf((int) d % DISTINCT_KEY_SIZE) }

    def 'double distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().length == DISTINCT_KEY_SIZE
    }

    double[] makeEntriesDistinctByKey() {
        IntStream.range(0, RAW_LIST_SIZE)
                .mapToDouble({ i -> (double) i })
                .filter(dblDistinctByKey(keyExtractor))
                .toArray()
    }

    def 'double distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().size() == DISTINCT_KEY_SIZE
    }

    Collection makeEntriesDistinctByKeyParallel() {
        IntStream.range(0, RAW_LIST_SIZE).parallel()
                .mapToDouble({ i -> (double) i })
                .filter(dblDistinctByKeyParallel(keyExtractor))
                .toArray()
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
        findAnyDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefaultSupplier(predicate, supplier)) == expected

        where:
        compareValue | expected
        2.0D         | 3.0D
        3.0D         | -1.0D
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
        findFirstDblDefault([1.0D, 2.0D, 3.0D] as double[], findDblDefaultSupplier(predicate, supplier)) == expected

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
    def 'double none match returns #expected for compareValue #compareValue'() {

        expect:
        def predicate = { d -> d > compareValue }
        dblNoneMatch([1.0D, 2.0D, 3.0D] as double[], predicate) == expected

        where:
        compareValue | expected
        2.0D         | false
        3.0D         | true
    }

    def 'double join returns expected results'() {
        expect:
        dblJoin([1.0D, 2.0D, 3.0D] as double[], { d -> String.valueOf(d) }) == '1.0,2.0,3.0'
    }

    @Unroll
    def 'double join returns "#expected" for delimiter "#delimiter"'() {

        expect:
        dblJoin([1.0D, 2.0D, 3.0D] as double[], { d -> String.valueOf(d) }, delimiter) == expected

        where:
        delimiter || expected
        ','       || '1.0,2.0,3.0'
        ', '      || '1.0, 2.0, 3.0'
    }

    @Unroll
    def 'double join returns "#expected" for delimiter "#delimiter", prefix "#prefix", and suffix "#suffix"'() {

        expect:
        def joiner = joining(delimiter, prefix, suffix)
        dblJoin([1.0D, 2.0D, 3.0D] as double[], { d -> String.valueOf(d) }, joiner) == expected

        where:
        delimiter | prefix | suffix  || expected
        ','       | 'pre+' | '+post' || 'pre+1.0,2.0,3.0+post'
        ', '      | ''     | ''      || '1.0, 2.0, 3.0'
    }

    @Unroll
    def 'double join returns empty string for #scenario parameter'() {

        expect:
        dblJoin(values, { d -> String.valueOf(d) }) == ''

        where:
        scenario | values
        'empty'  | [] as double[]
        'null'   | null
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
