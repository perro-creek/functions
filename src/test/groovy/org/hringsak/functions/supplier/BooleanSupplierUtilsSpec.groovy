package org.hringsak.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction

import static org.hringsak.functions.internal.StringUtils.isNullOrEmpty
import static org.hringsak.functions.supplier.BooleanSupplierUtils.booleanSupplier
import static org.hringsak.functions.supplier.SupplierUtils.constantValues

class BooleanSupplierUtilsSpec extends Specification {

    @Unroll
    def 'boolean supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> isNullOrEmpty(s) }
        booleanSupplier(function, value).getAsBoolean() == expected

        where:
        value  | expected
        null   | true
        ''     | true
        'test' | false
    }

    @Unroll
    def 'boolean supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String left, String right -> isNullOrEmpty(left) && isNullOrEmpty(right) } as BiFunction
        booleanSupplier(function, constantValues(left, right)).getAsBoolean() == expected

        where:
        left   | right  | expected
        null   | null   | true
        null   | ''     | true
        null   | 'test' | false
        ''     | null   | true
        ''     | ''     | true
        ''     | 'test' | false
        'test' | null   | false
        'test' | ''     | false
        'test' | 'test' | false
    }
}
