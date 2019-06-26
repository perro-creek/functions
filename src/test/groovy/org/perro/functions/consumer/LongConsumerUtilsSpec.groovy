package org.perro.functions.consumer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.LongConsumer
import java.util.function.ObjLongConsumer

import static org.perro.functions.consumer.LongConsumerUtils.*

class LongConsumerUtilsSpec extends Specification {

    def mockBiConsumer = Mock(BiConsumer)
    def mockObjLongConsumer = Mock(ObjLongConsumer)
    def mockConsumer = Mock(Consumer)
    def mockLongConsumer = Mock(LongConsumer)

    @Unroll
    def 'long consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        longConsumer(mockBiConsumer, value).accept(1L)

        then:
        noExceptionThrown()
        1 * mockBiConsumer.accept(1L, value)

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse long consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        inverseLongConsumer(mockObjLongConsumer, value).accept(1L)

        then:
        noExceptionThrown()
        1 * mockObjLongConsumer.accept(value, 1L)

        where:
        value << ['test', null, '']
    }

    def 'long setter passing null does not throw NPE'() {

        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        longSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    def 'long setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 1L]
        longSetter(consumer, extractor).accept(bean)
        bean.test == 1L
    }

    @Unroll
    @SuppressWarnings("GroovyAssignabilityCheck")
    def 'map to long and consume behaves as expected passing target "#target"'() {

        when:
        def function = { str -> 1L }
        mapToLongAndConsume(function, mockLongConsumer).accept(target)

        then:
        noExceptionThrown()
        calls * mockLongConsumer.accept(1L)

        where:
        target || calls
        null   || 0
        'test' || 1
    }

    @Unroll
    def 'long map and consume behaves as expected for value "#value"'() {

        when:
        def function = { d -> value }
        longMapAndConsume(function, mockConsumer).accept(1L)

        then:
        noExceptionThrown()
        1 * mockConsumer.accept(value)

        where:
        value  << [null, 'test']
    }
}
