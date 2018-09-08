package org.hringsak.functions.objects

import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static SupplierUtils.constantValues
import static SupplierUtils.supplier

class SupplierUtilsSpec extends Specification {

    @Unroll
    def 'supplier passing value parameter "#value"'() {

        expect:
        def function = { String -> StringUtils.reverse(value) }
        supplier(function, value).get() == expected

        where:
        value  | expected
        null   | null
        ''     | ''
        'test' | 'tset'
    }

    @Unroll
    def 'supplier passing left parameter "#left" and right parameter "#right"'() {

        expect:
        def function = { String l, String r -> defaultString(l) + defaultString(r) } as BiFunction
        supplier(function, constantValues(left, right)).get() == expected

        where:
        left   | right  | expected
        null   | null   | ''
        null   | ''     | ''
        null   | 'test' | 'test'
        ''     | null   | ''
        ''     | ''     | ''
        ''     | 'test' | 'test'
        'test' | null   | 'test'
        'test' | ''     | 'test'
        'test' | 'test' | 'testtest'
    }
}
