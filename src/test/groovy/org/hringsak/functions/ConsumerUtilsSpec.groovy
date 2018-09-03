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
    def 'double consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        doubleConsumer(consumer, value).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse double consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjDoubleConsumer
        inverseDoubleConsumer(consumer, value).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

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

    def 'double setter passing null does not throw NPE'() {
        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        doubleSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    def 'double setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 1.0D]
        doubleSetter(consumer, extractor).accept(bean)
        bean.test == 1.0D
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
