package org.hringsak.functions

import org.apache.commons.lang3.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import static org.apache.commons.lang3.StringUtils.defaultString
import static org.apache.commons.lang3.StringUtils.isEmpty

class SupplierUtilsSpec extends Specification {

    @Unroll
    def 'supplier passing value parameter "#value"'() {

        expect:
        def function = { String -> StringUtils.reverse(value) }
        SupplierUtils.supplier(function, value).get() == expected

        where:
        value  | expected
        null   | null
        ''     | ''
        'test' | 'tset'
    }

    @Unroll
    def 'supplier passing left parameter "#left" and right parameter "#right"'() {

        expect:
        def function = { String l, String r -> defaultString(l) + defaultString(r) }
        SupplierUtils.supplier(function, left, right).get() == expected

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

    @Unroll
    def 'boolean supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> isEmpty(s) }
        SupplierUtils.booleanSupplier(function, value).getAsBoolean() == expected

        where:
        value  | expected
        null   | true
        ''     | true
        'test' | false
    }

    @Unroll
    def 'boolean supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> isEmpty(l) && isEmpty(r) }
        SupplierUtils.booleanSupplier(function, left, right).getAsBoolean() == expected

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

    @Unroll
    def 'double supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> (double) defaultString(s).length() }
        SupplierUtils.doubleSupplier(function, value).getAsDouble() == expected

        where:
        value  | expected
        null   | 0.0D
        ''     | 0.0D
        'test' | 4.0D
    }

    @Unroll
    def 'double supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> ((double) defaultString(l).length()) + defaultString(r).length() }
        SupplierUtils.doubleSupplier(function, left, right).getAsDouble() == expected

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

    @Unroll
    def 'int supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> defaultString(s).length() }
        SupplierUtils.intSupplier(function, value).getAsInt() == expected

        where:
        value  | expected
        null   | 0
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'int supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> defaultString(l).length() + defaultString(r).length() }
        SupplierUtils.intSupplier(function, left, right).getAsInt() == expected

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

    @Unroll
    def 'long supplier passing value parameter "#value"'() {

        expect:
        def function = { String s -> (long) defaultString(s).length() }
        SupplierUtils.longSupplier(function, value).getAsLong() == expected

        where:
        value  | expected
        null   | 0
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'long supplier passing parameters "#left" and "#right"'() {

        expect:
        def function = { String l, String r -> ((long) defaultString(l).length()) + defaultString(r).length() }
        SupplierUtils.longSupplier(function, left, right).getAsLong() == expected

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
