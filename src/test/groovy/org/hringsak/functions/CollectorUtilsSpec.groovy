package org.hringsak.functions

import com.google.common.base.Strings
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import java.util.function.Function
import java.util.stream.Stream

import static java.util.stream.Collectors.*

class CollectorUtilsSpec extends Specification {

    def 'conditional group by collector'() {
        given:
        def pairStream = Stream.of(pair(1, null), pair(1, ''), pair(2, 'stringOne'), pair(2, 'stringTwo'))
        def nonEmptyPredicate = { String str -> !Strings.isNullOrEmpty(str) }
        Function getLeftFunction = { Pair pair -> pair.getLeft() }
        Function getRightFunction = { Pair pair -> pair.getRight() }

        when:
        def stringByIntMap = pairStream.collect(groupingBy(getLeftFunction, mapping(getRightFunction, CollectorUtils.conditionalCollector(nonEmptyPredicate))))

        then:
        with(stringByIntMap) {
            get(1) == []
            get(2) == ['stringOne', 'stringTwo']
        }
    }

    def 'to map from entry'() {
        given:
        def sourceMap = [keyOne: 'valueOne', keyTwo: 'valueTwo', keyThree: 'valueThree']

        when:
        def actual = sourceMap.entrySet().stream().collect(CollectorUtils.toMapFromEntry())

        then:
        actual == sourceMap
    }

    def 'to partitioned stream'() {
        given:
        def partitionSize = 10
        def elements = (1..100).collect({ "element${StringUtils.leftPad("$it", 3, '0')}" })

        when:
        def partitions = elements.stream().collect(CollectorUtils.toPartitionedStream(partitionSize)).collect(toList())  as Collection<List>

        then:
        partitions.size() == 10
        partitions.each { List partition -> partition.size() == partitionSize }
    }

    def 'to partitioned list'() {
        given:
        def partitionSize = 10
        def elements = (1..100).collect({ "element${StringUtils.leftPad("$it", 3, '0')}" })

        when:
        def partitions = elements.stream().collect(CollectorUtils.toPartitionedList(partitionSize)) as Collection<List>

        then:
        partitions.size() == 10
        partitions.each { List partition -> partition.size() == partitionSize }
    }

    def 'to string builder'() {
        when:
        def actual = 'test123'.codePoints()
                .filter { codePoint -> Character.isDigit(codePoint) }
                .boxed()
                .collect(CollectorUtils.toStringBuilder())
                .toString()

        then:
        actual == '123'
    }

    def 'to enum set'() {
        expect:
        Arrays.stream(TestEnum.values()).collect(CollectorUtils.toEnumSet(TestEnum)) == EnumSet.allOf(TestEnum)
    }

    def 'to enum set passing null enum class'() {
        when:
        [].stream().collect(CollectorUtils.toEnumSet(null))

        then:
        def e = thrown(NullPointerException)
        e.message =~ '"enumClass"'
    }

    def 'collect with default for empty stream'() {
        expect:
        [].stream().collect(CollectorUtils.collectWithDefault(toList(), 'default')) == ['default']
    }

    def 'collect with default for populated stream'() {
        expect:
        ['test'].stream().collect(CollectorUtils.collectWithDefault(toList(), 'default')) == ['test']
    }

    def 'to unmodifiable list'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toUnmodifiableList())
        result.add(4)

        then:
        thrown(UnsupportedOperationException)
        result instanceof List
    }

    def 'to unmodifiable set'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toUnmodifiableSet())
        result.add(4)

        then:
        thrown(UnsupportedOperationException)
        result instanceof Set
    }

    def 'to unmodifiable collection'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toUnmodifiableCollection { new LinkedList<>() })
        result.add(4)

        then:
        thrown(UnsupportedOperationException)
        result instanceof Collection
    }

    def 'to synchronized list'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toSynchronizedList())

        then:
        result.getClass().getSimpleName() == 'SynchronizedRandomAccessList'
    }

    def 'synchronized set'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toSynchronizedSet())

        then:
        result.getClass().getSimpleName() == 'SynchronizedSet'
    }

    def 'to synchronized collection'() {
        given:
        def numStream = Stream.of(1, 2, 3)

        when:
        def result = numStream.collect(CollectorUtils.toSynchronizedCollection { new LinkedList<>() })

        then:
        result.getClass().getSimpleName() == 'SynchronizedCollection'
    }

    def pair(key, value) {
        Pair.of(key, value)
    }

    enum TestEnum {
        VALUE_ONE,
        VALUE_TWO
    }
}
