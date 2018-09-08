package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.ObjDoubleConsumer
import java.util.function.ObjIntConsumer
import java.util.function.ObjLongConsumer


import static org.hringsak.functions.ConsumerUtils.doubleConsumer
import static org.hringsak.functions.ConsumerUtils.doubleSetter
import static org.hringsak.functions.ConsumerUtils.intConsumer
import static org.hringsak.functions.ConsumerUtils.intSetter
import static org.hringsak.functions.ConsumerUtils.inverseConsumer
import static org.hringsak.functions.ConsumerUtils.inverseDoubleConsumer
import static org.hringsak.functions.ConsumerUtils.inverseIntConsumer
import static org.hringsak.functions.ConsumerUtils.inverseLongConsumer
import static org.hringsak.functions.ConsumerUtils.longConsumer
import static org.hringsak.functions.ConsumerUtils.longSetter
import static org.hringsak.functions.ConsumerUtils.setter

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
    def 'inverse consumer for bi-consumer passing values "#paramOne" and "#paramTwo" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        inverseConsumer(consumer, paramOne).accept(paramTwo)

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
    def 'setter passing #bean does not throw NPE'() {
        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        setter(consumer, extractor).accept(bean)

        then:
        noExceptionThrown()

        where:
        bean << [null, [:]]
    }

    def 'setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 'test']
        setter(consumer, extractor).accept(bean)
        bean.test == 'test'
    }
}
