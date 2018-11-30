package org.hringsak.functions.supplier

import org.hringsak.functions.internal.StringUtils
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction

import static org.hringsak.functions.internal.StringUtils.defaultString
import static org.hringsak.functions.supplier.SupplierUtils.*

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

    def 'lazy supplier should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazySupplier { -> counter++ }
        (0..2).each { lazySupplier.get() }
        counter == 1
    }

    def 'lazy supplier passing constant should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazySupplier({str -> counter++ }, '')
        (0..2).each { lazySupplier.get() }
        counter == 1
    }

    def 'lazy supplier passing constant values should be called once after multiple invocations'() {
        expect:
        def counter = 0
        def lazySupplier = lazySupplier({strOne, strTwo -> counter++ } as BiFunction, constantValues('', ''))
        (0..2).each { lazySupplier.get() }
        counter == 1
    }
}
