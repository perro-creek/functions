package org.hringsak.functions.supplier

import org.hringsak.functions.internal.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.supplier.SupplierUtils.constantValues
import static org.hringsak.functions.supplier.SupplierUtils.supplier

class SupplierUtilsSpec extends Specification {

    @Unroll
    def 'supplier passing value parameter "#value"'() {

        expect:
        def function = { String -> StringUtils.toUpperCase(value) }
        supplier(function, value).get() == expected

        where:
        value  | expected
        null   | null
        ''     | ''
        'test' | 'TEST'
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
