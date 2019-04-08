package org.perro.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToIntBiFunction

import static org.perro.functions.internal.StringUtils.defaultString
import static IntSupplierUtils.intSupplier
import static IntSupplierUtils.lazyIntSupplier
import static SupplierUtils.constantValues

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

    def 'lazy integer supplier should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazyIntSupplier { -> counter++ }
        (0..2).each { lazySupplier.getAsInt() }
        counter == 1
    }

    def 'lazy integer supplier passing constant should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazyIntSupplier({str -> counter++ }, '')
        (0..2).each { lazySupplier.getAsInt() }
        counter == 1
    }

    def 'lazy integer supplier passing constant values should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazyIntSupplier({strOne, strTwo -> counter++ } as ToIntBiFunction, constantValues('', ''))
        (0..2).each { lazySupplier.getAsInt() }
        counter == 1
    }
}
