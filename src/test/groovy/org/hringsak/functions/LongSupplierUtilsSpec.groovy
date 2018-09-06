package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToLongBiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static org.hringsak.functions.LongSupplierUtils.longSupplier
import static org.hringsak.functions.SupplierUtils.constantValues

class LongSupplierUtilsSpec extends Specification {

    @Unroll
    def 'long supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> (long) defaultString(s).length() }
        longSupplier(function, value).getAsLong() == expected

        where:
        value  | expected
        null   | 0
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'long supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> ((long) defaultString(l).length()) + defaultString(r).length() } as ToLongBiFunction
        longSupplier(function, constantValues(left, right)).getAsLong() == expected

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
