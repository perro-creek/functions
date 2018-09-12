package org.hringsak.functions.consumer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.*

import static org.hringsak.functions.consumer.DblConsumerUtils.*

class DblConsumerUtilsSpec extends Specification {

    def mockBiConsumer = Mock(BiConsumer)
    def mockObjDoubleConsumer = Mock(ObjDoubleConsumer)
    def mockConsumer = Mock(Consumer)
    def mockDoubleConsumer = Mock(DoubleConsumer)

    @Unroll
    def 'double consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        dblConsumer(mockBiConsumer, value).accept(1.0D)

        then:
        noExceptionThrown()
        1 * mockBiConsumer.accept(1.0D, value)

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse double consumer for bi-consumer passing value "#value" does not throw NPE'() {

        when:
        inverseDblConsumer(mockObjDoubleConsumer, value).accept(1.0D)

        then:
        noExceptionThrown()
        1 * mockObjDoubleConsumer.accept(value, 1.0D)

        where:
        value << ['test', null, '']
    }

    def 'double setter passing null does not throw NPE'() {

        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract }
        dblSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    def 'double setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract }
        def bean = [toExtract: 1.0D]
        dblSetter(consumer, extractor).accept(bean)
        bean.test == 1.0D
    }

    @Unroll
    def 'map to double and consume behaves as expected passing target "#target"'() {

        when:
        def function = { str -> 1.0D }
        mapToDblAndConsume(function, mockDoubleConsumer).accept(target)

        then:
        noExceptionThrown()
        calls * mockDoubleConsumer.accept(1.0D)

        where:
        target || calls
        null   || 0
        'test' || 1
    }

    @Unroll
    def 'double map and consume behaves as expected for value "#value"'() {

        when:
        def function = { d -> value }
        dblMapAndConsume(function, mockConsumer).accept(1.0D)

        then:
        noExceptionThrown()
        1 * mockConsumer.accept(value)

        where:
        value  << [null, 'test']
    }
}
