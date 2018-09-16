package org.hringsak.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToDoubleBiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.supplier.DblSupplierUtils.dblSupplier
import static org.hringsak.functions.supplier.SupplierUtils.constantValues

class DblSupplierUtilsSpec extends Specification {

    @Unroll
    def 'double supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> (double) defaultString(s).length() }
        dblSupplier(function, value).getAsDouble() == expected

        where:
        value  | expected
        null   | 0.0D
        ''     | 0.0D
        'test' | 4.0D
    }

    @Unroll
    def 'double supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> ((double) defaultString(l).length()) + defaultString(r).length() } as ToDoubleBiFunction
        dblSupplier(function, constantValues(left, right)).getAsDouble() == expected

        where:
        left   | right  | expected
        null   | null   | 0.0D
        null   | ''     | 0.0D
        null   | 'test' | 4.0D
        ''     | null   | 0.0D
        ''     | ''     | 0.0D
        ''     | 'test' | 4.0D
        'test' | null   | 4.0D
        'test' | ''     | 4.0D
        'test' | 'test' | 8.0D
    }
}
