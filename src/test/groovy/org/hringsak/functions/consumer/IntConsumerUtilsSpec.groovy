package org.hringsak.functions.consumer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.DoubleConsumer
import java.util.function.DoubleFunction
import java.util.function.IntConsumer
import java.util.function.ObjDoubleConsumer
import java.util.function.ObjIntConsumer
import java.util.function.ToDoubleFunction
import java.util.function.ToIntFunction

import static org.hringsak.functions.consumer.DblConsumerUtils.dblMapAndConsume
import static org.hringsak.functions.consumer.DblConsumerUtils.mapToDblAndConsume
import static org.hringsak.functions.consumer.IntConsumerUtils.*

class IntConsumerUtilsSpec extends Specification {

    def mockBiConsumer = Mock(BiConsumer)
    def mockObjIntConsumer = Mock(ObjIntConsumer)
    def mockConsumer = Mock(Consumer)
    def mockIntConsumer = Mock(IntConsumer)

    @Unroll
    def 'int consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        intConsumer(mockBiConsumer, value).accept(1)

        then:
        noExceptionThrown()
        1 * mockBiConsumer.accept(1, value)

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse int consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        inverseIntConsumer(mockObjIntConsumer, value).accept(1)

        then:
        noExceptionThrown()
        1 * mockObjIntConsumer.accept(value, 1)

        where:
        value << ['test', null, '']
    }

    def 'int setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 1]
        intSetter(consumer, extractor).accept(bean)
        bean.test == 1
    }

    def 'int setter passing null does not throw NPE'() {

        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        intSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    @Unroll
    def 'map to int and consume behaves as expected passing target "#target"'() {

        when:
        def function = { str -> 1 }
        mapToIntAndConsume(function, mockIntConsumer).accept(target)

        then:
        noExceptionThrown()
        calls * mockIntConsumer.accept(1)

        where:
        target || calls
        null   || 0
        'test' || 1
    }

    @Unroll
    def 'int map and consume behaves as expected for value "#value"'() {

        when:
        def function = { d -> value }
        intMapAndConsume(function, mockConsumer).accept(1)

        then:
        noExceptionThrown()
        1 * mockConsumer.accept(value)

        where:
        value  << [null, 'test']
    }
}
