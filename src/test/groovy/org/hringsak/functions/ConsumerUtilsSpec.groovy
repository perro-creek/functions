package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.ObjDoubleConsumer
import java.util.function.ObjIntConsumer
import java.util.function.ObjLongConsumer

class ConsumerUtilsSpec extends Specification {

    @Unroll
    def 'consumer for bi-consumer passing values "#paramOne" and "#paramTwo" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.consumer(consumer, paramOne).accept(paramTwo)

        then:
        noExceptionThrown()

        where:
        paramOne | paramTwo
        'test'   | null
        'test'   | ''
        'test'   | 'test'
        null     | 'test'
        ''       | 'test'
    }

    @Unroll
    def 'consumer for bi-consumer as second parameter passing values "#paramOne" and "#paramTwo" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.consumer(paramOne, consumer).accept(paramTwo)

        then:
        noExceptionThrown()

        where:
        paramOne | paramTwo
        'test'   | null
        'test'   | ''
        'test'   | 'test'
        null     | 'test'
        ''       | 'test'
    }

    @Unroll
    def 'double consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.doubleConsumer(consumer, value).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'double consumer for bi-consumer as second parameter passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjDoubleConsumer
        ConsumerUtils.doubleConsumer(value, consumer).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'int consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.intConsumer(consumer, value).accept(1)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'int consumer for bi-consumer as second parameter passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjIntConsumer
        ConsumerUtils.intConsumer(value, consumer).accept(1)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'long consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        ConsumerUtils.longConsumer(consumer, value).accept(1L)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'long consumer for bi-consumer as second parameter passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjLongConsumer
        ConsumerUtils.longConsumer(value, consumer).accept(1L)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    def 'setter passing null does not throw NPE'() {
        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        ConsumerUtils.setter(consumer, null).accept(null)

        then:
        noExceptionThrown()
    }

    def 'setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def bean = [:]
        ConsumerUtils.setter(consumer, 'test').accept(bean)
        bean.test == 'test'
    }
}
