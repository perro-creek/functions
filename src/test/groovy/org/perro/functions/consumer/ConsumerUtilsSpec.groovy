package org.perro.functions.consumer

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

import static ConsumerUtils.*

class ConsumerUtilsSpec extends Specification {

    def mockConsumer = Mock(Consumer)

    @Unroll
    def 'consumer for bi-consumer passing values "#paramOne" and "#paramTwo" does not throw NPE'() {

        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.consumer(consumer, value).accept(target)

        then:
        noExceptionThrown()

        where:
        target | value
        null   | 'test'
        ''     | 'test'
        'test' | 'test'
        'test' | null
        'test' | ''
    }

    @Unroll
    def 'inverse consumer for bi-consumer passing values "#paramOne" and "#paramTwo" does not throw NPE'() {

        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        inverseConsumer(consumer, value).accept(target)

        then:
        noExceptionThrown()

        where:
        target | value
        null   | 'test'
        ''     | 'test'
        'test' | 'test'
        'test' | null
        'test' | ''
    }

    def 'setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract }
        def bean = [toExtract: 'test']
        setter(consumer, extractor).accept(bean)
        bean.test == 'test'
    }

    @Unroll
    def 'setter passing #bean does not throw NPE'() {

        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract }
        setter(consumer, extractor).accept(bean)

        then:
        noExceptionThrown()

        where:
        bean << [null, [:]]
    }

    @Unroll
    def 'map and consume behaves as expected passing target "#target"'() {

        when:
        def function = { str -> str.length() } as Function
        mapAndConsume(function, mockConsumer).accept(target)

        then:
        noExceptionThrown()
        calls * mockConsumer.accept(expectedLength)

        where:
        target || expectedLength | calls
        null   || _              | 0
        ''     || 0              | 1
        'test' || 4              | 1
        'test' || 4              | 1
        'test' || 4              | 1
    }
}
