package org.hringsak.functions

import org.hringsak.functions.EnumUtils
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
    def 'get formatted name for method #methodName passing enum with non-conforming name returns "#expectedValue"'() {
        expect:
        EnumUtils."$methodName"(TestValue.non_conforming_name) == expectedValue

        where:
        methodName           || expectedValue
        'getLowerCamelName'  || 'nonConformingName'
        'getUpperCamelName'  || 'NonConformingName'
        'getLowerHyphenName' || 'non-conforming-name'
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
        UPPER_UNDERSCORE_NAME,
        non_conforming_name
    }
}
