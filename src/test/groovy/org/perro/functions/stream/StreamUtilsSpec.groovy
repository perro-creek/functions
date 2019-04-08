package org.perro.functions.stream

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate
import java.util.function.Supplier

import static java.util.stream.Collectors.joining
import static java.util.stream.Collectors.toList
import static org.perro.functions.predicate.PredicateUtils.constant
import static org.perro.functions.predicate.PredicateUtils.isEqual
import static StreamUtils.*

class StreamUtilsSpec extends Specification {

    @Unroll
    def 'all match passing collection #collection and predicate returns #expected'() {

        expect:
        allMatch(collection, isEqual({ String s -> s.length() }, 4)) == expected

        where:
        collection         | expected
        ['test', '', null] | false
        ['test']           | true
        [null]             | false
        []                 | true
        null               | false
    }

    @Unroll
    def 'any match passing collection #collection and predicate returns #expected'() {

        expect:
        anyMatch(collection, isEqual({ String s -> s.length() }, 4)) == expected

        where:
        collection         | expected
        ['test', '', null] | true
        ['test']           | true
        [null]             | false
        []                 | false
        null               | false
    }

    @Unroll
    def 'none match passing collection #collection and predicate returns #expected'() {

        expect:
        noneMatch(collection, isEqual({ String s -> s.length() }, 4)) == expected

        where:
        collection         | expected
        ['test', '', null] | false
        ['test']           | false
        [null]             | true
        []                 | true
        null               | false
    }

    @Unroll
    def 'count passing collection #collection and predicate returns #expected'() {

        expect:
        count(collection, isEqual({ String s -> s.length() }, 4)) == expected

        where:
        collection         | expected
        ['test', '', null] | 1L
        ['test']           | 1L
        [null]             | 0L
        []                 | 0L
        null               | 0L
    }

    @Unroll
    def 'max default passing collection #collection and predicate returns #expected'() {

        expect:
        maxDefaultNull(collection, constant(true)) == expected

        where:
        collection         | expected
        ['', null, 'test'] | 'test'
        ['test']           | 'test'
        []                 | null
        null               | null
    }

    @Unroll
    def 'max default passing collection #collection and find with default returns #expected'() {

        expect:
        maxDefault(collection, findWithDefault(constant(true), 'default')) == expected

        where:
        collection         | expected
        ['', null, 'test'] | 'test'
        ['test']           | 'test'
        []                 | 'default'
        null               | 'default'
    }

    @Unroll
    def 'max default passing collection #collection and find with default supplier returns #expected'() {

        expect:
        maxDefault(collection, findWithDefault(constant(true), { 'default' } as Supplier<String>)) == expected

        where:
        collection         | expected
        ['', null, 'test'] | 'test'
        ['test']           | 'test'
        []                 | 'default'
        null               | 'default'
    }

    @Unroll
    def 'min default passing collection #collection and predicate returns #expected'() {

        expect:
        minDefaultNull(collection, constant(true)) == expected

        where:
        collection         | expected
        ['', null, 'test'] | ''
        ['test']           | 'test'
        []                 | null
    }

    @Unroll
    def 'min default passing collection #collection and find with default returns #expected'() {

        expect:
        minDefault(collection, findWithDefault(constant(true), 'default')) == expected

        where:
        collection         | expected
        ['', null, 'test'] | ''
        ['test']           | 'test'
        []                 | 'default'
    }

    @Unroll
    def 'min default passing collection #collection and find with default supplier returns #expected'() {

        expect:
        minDefault(collection, findWithDefault(constant(true), { 'default' } as Supplier<String>)) == expected

        where:
        collection         | expected
        ['', null, 'test'] | ''
        ['test']           | 'test'
        []                 | 'default'
    }

    @Unroll
    def 'find any default null returns expected value for string length "#length"'() {

        expect:
        findAnyDefaultNull([null, '', 'test', ''], isEqual({ String s -> s.length() }, length)) == expected

        where:
        length | expected
        0      | ''
        1      | null
        4      | 'test'
    }

    @Unroll
    def 'find any with default returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual({ String s -> s.length() }, length)
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
        def predicate = isEqual({ String s -> s.length() }, length) as Predicate<String>
        def supplier = { 'default' } as Supplier<String>
        findAnyWithDefault([null, '', 'test', ''], findWithDefault(predicate, supplier)) == expected

        where:
        length | expected
        0      | ''
        1      | 'default'
        4      | 'test'
    }

    @Unroll
    def 'find first default null returns expected value for string length "#length"'() {

        expect:
        findFirstDefaultNull([null, '', 'test', ''], isEqual({ String s -> s.length() }, length)) == expected

        where:
        length | expected
        0      | ''
        1      | null
        4      | 'test'
    }

    @Unroll
    def 'find first with default returns expected value for string length "#length"'() {

        expect:
        def predicate = isEqual({ String s -> s.length() }, length)
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
        def predicate = isEqual({ String s -> s.length() }, length) as Predicate<String>
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
        indexOfFirst([null, '', 'test', ''], isEqual({ String s -> s.length() }, length)) == expected

        where:
        length | expected
        0      | 1
        1      | -1
        4      | 2
    }

    @Unroll
    def 'any match returns expected value for string length "#length"'() {

        expect:
        anyMatch([null, '', 'test', ''], isEqual({ String s -> s.length() }, length)) == expected

        where:
        length | expected
        0      | true
        1      | false
        4      | true
    }

    @Unroll
    def 'none match returns expected value for string length "#length"'() {

        expect:
        noneMatch([null, '', 'test', ''], isEqual({ String s -> s.length() }, length)) == expected

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
        join(values, { t -> t }) == 'm1,m2,m3'
    }

    @Unroll
    def 'join returns expected results for delimiter "#delimiter"'() {

        given:
        def values = ['m1', 'm2', 'm3']

        expect:
        join(values, mapperWithDelimiter({ t -> t }, delimiter)) == expectedString

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
        join(values, mapperWithCollector({ t -> t }, joining(delimiter, prefix, suffix))) == expectedString

        where:
        delimiter | prefix | suffix  || expectedString
        ','       | 'pre+' | '+post' || 'pre+m1,m2,m3+post'
        ', '      | ''     | ''      || 'm1, m2, m3'
    }

    @Unroll
    def 'join returns empty string for #scenario parameter'() {

        expect:
        join(values, { t -> t }) == ''

        where:
        scenario | values
        'empty'  | []
        'null'   | null
    }

    def 'subtract returns expected values'() {
        expect:
        subtract([1, 2, 3] as Set, [3, 4, 5] as Set) == [1, 2] as Set
    }

    @Unroll
    def 'subtract returns expected set for #scenario'() {

        expect:
        subtract(from, toSubtract) == expected

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
        def partitions = toPartitionedList(inputCollection, 2)

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
        def partitionStream = toPartitionedStream(inputCollection, 2)

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
        fromIterator(iterator).collect(toList()) == expectedList

        where:
        scenario    | iterator             || expectedList
        'empty'     | [].iterator()        || []
        'null'      | null                 || []
        'populated' | [1, 2, 3].iterator() || [1, 2, 3]
    }
}
