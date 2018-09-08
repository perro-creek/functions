package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiConsumer
import java.util.function.ObjDoubleConsumer

import static org.hringsak.functions.DblConsumerUtils.dblConsumer
import static org.hringsak.functions.DblConsumerUtils.dblSetter
import static org.hringsak.functions.DblConsumerUtils.inverseDblConsumer

class DblConsumerUtilsSpec extends Specification {

    @Unroll
    def 'double consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as BiConsumer
        dblConsumer(consumer, value).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    @Unroll
    def 'inverse double consumer for bi-consumer passing value "#value" does not throw NPE'() {
        when:
        def consumer = { a, b -> println "a: '$a', b: '$b'" } as ObjDoubleConsumer
        inverseDblConsumer(consumer, value).accept(1.0D)

        then:
        noExceptionThrown()

        where:
        value << ['test', null, '']
    }

    def 'double setter passing null does not throw NPE'() {
        when:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        dblSetter(consumer, extractor).accept(null)

        then:
        noExceptionThrown()
    }

    def 'double setter passing value behaves as expected'() {
        expect:
        def consumer = { Map m, v -> m.test = v } as BiConsumer
        def extractor = { Map m -> m.toExtract}
        def bean = [toExtract: 1.0D]
        dblSetter(consumer, extractor).accept(bean)
        bean.test == 1.0D
    }
}
