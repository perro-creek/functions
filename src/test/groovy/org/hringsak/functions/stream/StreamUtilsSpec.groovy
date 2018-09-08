package org.hringsak.functions.stream

import org.hringsak.functions.stream.StreamUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate
import java.util.function.Supplier

import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.hringsak.functions.predicate.PredicateUtils.isEqual
import static org.hringsak.functions.stream.StreamUtils.findAnyWithDefault
import static org.hringsak.functions.stream.StreamUtils.findFirstWithDefault
import static org.hringsak.functions.stream.StreamUtils.findWithDefault

class StreamUtilsSpec extends Specification {

    @Unroll
    def 'find any returns expected value for string length "#length"'() {

        expect:
        StreamUtils.findAny([null, '', 'test', ''], isEqual(length, { String s -> s.length() })) == expected

        where:
        length | expected
        0      | ''
        1      | null
        4      | 'test'
    }

    @Unroll
    def 'find any with default returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual(length, { String s -> s.length() })
        findAnyWithDefault([null, '', 'test', ''], findWithDefault(predicate, 'default')) == expected

        where:
        length | expected
        0      | ''
        1      | 'default'
        4      | 'test'
    }

    @Unroll
    def 'find any with default supplier returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual(length, { String s -> s.length() }) as Predicate<String>
        def supplier = { 'default' } as Supplier<String>
        findAnyWithDefault([null, '', 'test', ''], findWithDefault(predicate, supplier)) == expected

        where:
        length | expected
        0      | ''
        1      | 'default'
        4      | 'test'
    }

    @Unroll
    def 'find first returns expected value for string length "#length"'() {

        expect:
        StreamUtils.findFirst([null, '', 'test', ''], isEqual(length, { String s -> s.length() })) == expected

        where:
        length | expected
        0      | ''
        1      | null
        4      | 'test'
    }

    @Unroll
    def 'find first with default returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual(length, { String s -> s.length() })
        findFirstWithDefault([null, '', 'test', ''], findWithDefault(predicate, 'default')) == expected

        where:
        length | expected
        0      | ''
        1      | 'default'
        4      | 'test'
    }

    @Unroll
    def 'find first with default supplier returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual(length, { String s -> s.length() }) as Predicate<String>
        def supplier = { 'default' } as Supplier<String>
        findFirstWithDefault([null, '', 'test', ''], findWithDefault(predicate, supplier)) == expected

        where:
        length | expected
        0      | ''
        1      | 'default'
        4      | 'test'
    }

    @Unroll
    def 'index of first returns expected value for string length "#length"'() {

        expect:
        StreamUtils.indexOfFirst([null, '', 'test', ''], isEqual(length, { String s -> s.length() })) == expected

        where:
        length | expected
        0      | 1
        1      | -1
        4      | 2
    }

    @Unroll
    def 'any match returns expected value for string length "#length"'() {

        expect:
        StreamUtils.anyMatch([null, '', 'test', ''], isEqual(length, { String s -> s.length() })) == expected

        where:
        length | expected
        0      | true
        1      | false
        4      | true
    }

    @Unroll
    def 'none match returns expected value for string length "#length"'() {

        expect:
        StreamUtils.noneMatch([null, '', 'test', ''], isEqual(length, { String s -> s.length() })) == expected

        where:
        length | expected
        0      | false
        1      | true
        4      | false
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

    @Unroll
    def 'to partitioned stream returns expected value for #scenario'() {

        when:
        def partitionStream = StreamUtils.toPartitionedStream(inputCollection, 2)

        then:
        partitionStream.collect(toList()) == expectedPartitions

        where:
        scenario          | inputCollection    || expectedPartitions
        'populated list'  | [1, 2, 3, 4, 5, 6] || [[1, 2], [3, 4], [5, 6]]
        'empty list'      | []                 || []
        'null collection' | null               || []
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
}
