package org.hringsak.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToIntBiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.supplier.IntSupplierUtils.intSupplier
import static org.hringsak.functions.supplier.SupplierUtils.constantValues

class IntSupplierUtilsSpec extends Specification {

    @Unroll
    def 'int supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> defaultString(s).length() }
        intSupplier(function, value).getAsInt() == expected

        where:
        value  | expected
        null   | 0
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'int supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> defaultString(l).length() + defaultString(r).length() } as ToIntBiFunction
        intSupplier(function, constantValues(left, right)).getAsInt() == expected

        where:
        left   | right  | expected
        null   | null   | 0
        null   | ''     | 0
        null   | 'test' | 4
        ''     | null   | 0
        ''     | ''     | 0
        ''     | 'test' | 4
        'test' | null   | 4
        'test' | ''     | 4
        'test' | 'test' | 8
    }
}
