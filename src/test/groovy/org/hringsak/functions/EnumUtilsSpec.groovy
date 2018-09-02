package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

class EnumUtilsSpec extends Specification {

    @Unroll
    def 'get formatted name for method #methodName returns "#expectedValue"'() {
        expect:
        EnumUtils."$methodName"(TestValue.UPPER_UNDERSCORE_NAME) == expectedValue

        where:
        methodName           || expectedValue
        'getLowerCamelName'  || 'upperUnderscoreName'
        'getUpperCamelName'  || 'UpperUnderscoreName'
        'getLowerHyphenName' || 'upper-underscore-name'
    }

    @Unroll
    def 'get formatted name for method #methodName throws exception when enumeratedValue is null'() {
        when:
        EnumUtils."$methodName"(null)

        then:
        def e = thrown(NullPointerException)
        e.message =~ /'enumeratedValue'/

        where:
        methodName << ['getLowerCamelName', 'getUpperCamelName', 'getLowerHyphenName']
    }

    enum TestValue {
        UPPER_UNDERSCORE_NAME
    }
}
