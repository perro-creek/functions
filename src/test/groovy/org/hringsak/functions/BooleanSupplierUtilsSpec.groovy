package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction

import static org.apache.commons.lang3.StringUtils.isEmpty
import static org.hringsak.functions.BooleanSupplierUtils.booleanSupplier
import static org.hringsak.functions.SupplierUtils.arguments

class BooleanSupplierUtilsSpec extends Specification {

    @Unroll
    def 'boolean supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> isEmpty(s) }
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
        def function = { String l, String r -> isEmpty(l) && isEmpty(r) } as BiFunction
        booleanSupplier(function, arguments(left, right)).getAsBoolean() == expected

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
