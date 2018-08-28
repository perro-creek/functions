package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer

class ConsumerUtilsSpec extends Specification {

    @Unroll
    def 'consumer for bi-consumer passing values "#paramOne" and "#paramTwo" does not throw NPE'() {
        when:
        def consumer = { a, b -> println String.format('a: "%s", b: "%s"', a, b) } as BiConsumer
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
        def consumer = { a, b -> println String.format('a: "%s", b: "%s"', a, b) } as BiConsumer
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
