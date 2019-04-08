package org.perro.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToLongBiFunction

import static org.perro.functions.internal.StringUtils.defaultString
import static LongSupplierUtils.lazyLongSupplier
import static LongSupplierUtils.longSupplier
import static SupplierUtils.constantValues

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

    def 'lazy long supplier should be called once after multiple invocations'() {
        expect:
        def counter = 0L
        def lazySupplier = lazyLongSupplier() { -> counter++ }
        (0..2).each { lazySupplier.getAsLong() }
        counter == 1
    }

    def 'lazy long supplier passing constant should be called once after multiple invocations'() {
        expect:
        def counter = 0L
        def lazySupplier = lazyLongSupplier({str -> counter++ }, '')
        (0..2).each { lazySupplier.getAsLong() }
        counter == 1
    }

    def 'lazy long supplier passing constant values should be called once after multiple invocations'() {
        expect:
        def counter = 0L
        def lazySupplier = lazyLongSupplier({strOne, strTwo -> counter++ } as ToLongBiFunction, constantValues('', ''))
        (0..2).each { lazySupplier.getAsLong() }
        counter == 1
    }
}
