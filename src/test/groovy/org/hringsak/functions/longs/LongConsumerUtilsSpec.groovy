package org.hringsak.functions.longs

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.ObjLongConsumer

import static org.hringsak.functions.longs.LongConsumerUtils.*

class LongConsumerUtilsSpec extends Specification {

    @Unroll
    def 'long consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        longConsumer(consumer, value).accept(1L)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse long consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjLongConsumer
        inverseLongConsumer(consumer, value).accept(1L)

        then:
        noExceptionThrown()

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
}
