package org.hringsak.functions

import com.google.common.collect.Sets
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.function.Function.identity
import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.CollectorUtilsSpec.TestEnum.*

class StreamUtilsSpec extends Specification {

    static final int RAW_LIST_SIZE = 1000
    static final int DISTINCT_KEY_SIZE = 100
    def keyExtractor = { obj -> obj.key }

    def 'distinct by key filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKey().size() == DISTINCT_KEY_SIZE
    }

    Collection makeEntriesDistinctByKey() {
        (0..<RAW_LIST_SIZE).stream()
                .map({ i -> makeKeyValuePair(i) })
                .filter(StreamUtils.distinctByKey(keyExtractor))
                .collect(toList()) as Collection
    }

    Object makeKeyValuePair(i) {
        [key: i % DISTINCT_KEY_SIZE, value: i]
    }

    def 'distinct by key parallel filters objects with unique key values'() {
        expect:
        makeEntriesDistinctByKeyParallel().size() == DISTINCT_KEY_SIZE
    }

    Collection makeEntriesDistinctByKeyParallel() {
        (0..<RAW_LIST_SIZE).parallelStream()
                .map({ i -> makeKeyValuePair(i) })
                .filter(StreamUtils.distinctByKeyParallel(keyExtractor))
                .collect(toList()) as Collection
    }

    def 'generic transform returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        StreamUtils.transform(list, { i -> String.valueOf(i) }) == ['1', '2', '3']
    }

    def 'transform to set returns expected results'() {

        given:
        def list = [1, 2, 3]

        expect:
        StreamUtils.transformToSet(list, { i -> String.valueOf(i) }) == Sets.newHashSet('1', '2', '3')
    }

    def 'transform with collector returns expected results'() {

        given:
        def list = [VALUE_ONE, VALUE_TWO]

        when:
        def enumSet = StreamUtils.transform(list, CollectorUtils.toEnumSet(CollectorUtilsSpec.TestEnum))

        then:
        enumSet == EnumSet.allOf(CollectorUtilsSpec.TestEnum)
        enumSet instanceof EnumSet
    }

    def 'transform with mapping and collector returns expected results'() {

        given:
        def list = ['VALUE_ONE', 'VALUE_TWO']
        Function transformer = { String enumName -> valueOf(enumName) }

        when:
        def enumSet = StreamUtils.transform(list, transformer, CollectorUtils.toEnumSet(CollectorUtilsSpec.TestEnum))

        then:
        enumSet == EnumSet.allOf(CollectorUtilsSpec.TestEnum)
        enumSet instanceof EnumSet
    }

    @Unroll
    def 'generic transform returns empty list for #scenario parameter'() {

        expect:
        StreamUtils.transform(metadata, identity()) == []

        where:
        scenario | metadata
        'empty'  | []
        'null'   | null
    }

    @Unroll
    def 'generic transform distinct returns empty list for #scenario parameter'() {

        expect:
        StreamUtils.transformDistinct(metadata, identity()) == []

        where:
        scenario | metadata
        'empty'  | []
        'null'   | null
    }

    @Unroll
    def 'transform to set returns empty list for #scenario parameter'() {

        expect:
        StreamUtils.transformToSet(metadata, identity()) == [] as Set

        where:
        scenario | metadata
        'empty'  | []
        'null'   | null
    }

    def 'filter returns expected values'() {
        expect:
        StreamUtils.filter([1, 2, 3], { i -> i % 2 == 0 }) == [2]
    }

    @Unroll
    def 'filter returns empty list for #scenario parameter'() {

        expect:
        StreamUtils.filter(contents, PredicateUtils.predicate(false)) == []

        where:
        scenario | contents
        'empty'  | []
        'null'   | null
    }

    def 'join returns expected results'() {

        given:
        def values = ['m1', 'm2', 'm3']

        expect:
        StreamUtils.join(values, { t -> t }) == 'm1,m2,m3'
    }

    @Unroll
    def 'join returns expected results for delimiter "#delimiter"'() {

        given:
        def values = ['m1', 'm2', 'm3']

        expect:
        StreamUtils.join(values, { t -> t }, delimiter) == expectedString

        where:
        delimiter || expectedString
        ','       || 'm1,m2,m3'
        ', '      || 'm1, m2, m3'
    }

    @Unroll
    def 'join returns expected results for delimiter "#delimiter", prefix "#prefix", and suffix "#suffix"'() {

        given:
        def values = ['m1', 'm2', 'm3']

        expect:
        StreamUtils.join(values, { t -> t }, joining(delimiter, prefix, suffix)) == expectedString

        where:
        delimiter | prefix | suffix  || expectedString
        ','       | 'pre+' | '+post' || 'pre+m1,m2,m3+post'
        ', '      | ''     | ''      || 'm1, m2, m3'
    }

    @Unroll
    def 'join returns empty string for #scenario parameter'() {

        expect:
        StreamUtils.join(values, { t -> t }) == ''

        where:
        scenario | values
        'empty'  | []
        'null'   | null
    }

    def 'subtract returns expected values'() {
        expect:
        StreamUtils.subtract([1, 2, 3] as Set, [3, 4, 5] as Set) == [1, 2] as Set
    }

    @Unroll
    def 'subtract returns expected set for #scenario'() {

        expect:
        StreamUtils.subtract(from, toSubtract) == expected

        where:
        scenario               | from             | toSubtract       || expected
        'empty from set'       | [] as Set        | [3, 4, 5] as Set || [] as Set
        'null from set'        | null             | [3, 4, 5] as Set || [] as Set
        'empty toSubtract set' | [1, 2, 3] as Set | [] as Set        || [1, 2, 3] as Set
        'null toSubtract set'  | [1, 2, 3] as Set | null             || [1, 2, 3] as Set
    }

    @Unroll
    def 'from iterator returns empty stream for #scenario iterator'() {

        expect:
        StreamUtils.fromIterator(iterator).collect(toList()) == expectedList

        where:
        scenario    | iterator             || expectedList
        'empty'     | [].iterator()        || []
        'null'      | null                 || []
        'populated' | [1, 2, 3].iterator() || [1, 2, 3]
    }

    @Unroll
    def 'to partitioned list returns expected value for #scenario'() {

        when:
        def partitions = StreamUtils.toPartitionedList(inputCollection, 2)

        then:
        partitions == expectedPartitions

        where:
        scenario          | inputCollection    || expectedPartitions
        'populated list'  | [1, 2, 3, 4, 5, 6] || [[1, 2], [3, 4], [5, 6]]
        'empty list'      | []                 || []
        'null collection' | null               || []
    }
}
