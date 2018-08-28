package org.hringsak.functions

import spock.lang.Specification

class EnumUtilsSpec extends Specification {

    def 'get name for service type #type returns #name'() {
        expect:
        EnumUtils.getCamelCaseName(TestValue.UPPER_UNDERSCORE_NAME) == 'upperUnderscoreName'
    }

    def 'camelCaseName throws exception when enumeratedValue is null'() {
        when:
        EnumUtils.getCamelCaseName(null)

        then:
        def e = thrown(NullPointerException)
        e.message =~ /'enumeratedValue'/
    }

    enum TestValue {
        UPPER_UNDERSCORE_NAME
    }
}
