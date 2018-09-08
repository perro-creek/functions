package org.hringsak.functions.consumer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.ObjIntConsumer

import static org.hringsak.functions.consumer.IntConsumerUtils.*

class IntConsumerUtilsSpec extends Specification {

    @Unroll
    def 'int consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        intConsumer(consumer, value).accept(1)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse int consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjIntConsumer
        inverseIntConsumer(consumer, value).accept(1)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    def 'int setter passing null does not throw NPE'() {
        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        intSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    def 'int setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 1]
        intSetter(consumer, extractor).accept(bean)
        bean.test == 1
    }
}
