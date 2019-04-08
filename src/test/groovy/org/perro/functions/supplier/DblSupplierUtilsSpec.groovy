package org.perro.functions.supplier

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToDoubleBiFunction

import static org.perro.functions.internal.StringUtils.defaultString
import static DblSupplierUtils.dblSupplier
import static DblSupplierUtils.lazyDblSupplier
import static SupplierUtils.constantValues

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

    def 'lazy double supplier should be called once after multiple invocations'() {
        expect:
        def counter = 0.0D
        def lazySupplier = lazyDblSupplier { -> counter++ }
        (0..2).each { lazySupplier.getAsDouble() }
        counter == 1
    }

    def 'lazy double supplier passing constant should be called once after multiple invocations'() {
        expect:
        def counter = 0.0D
        def lazySupplier = lazyDblSupplier({str -> counter++ }, '')
        (0..2).each { lazySupplier.getAsDouble() }
        counter == 1
    }

    def 'lazy double supplier passing constant values should be called once after multiple invocations'() {
        expect:
        def counter = 0.0D
        def lazySupplier = lazyDblSupplier({strOne, strTwo -> counter++ } as ToDoubleBiFunction, constantValues('', ''))
        (0..2).each { lazySupplier.getAsDouble() }
        counter == 1
    }
}
