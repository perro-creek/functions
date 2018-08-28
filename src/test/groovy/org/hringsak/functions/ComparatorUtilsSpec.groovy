package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

import static java.util.stream.Collectors.toList

class ComparatorUtilsSpec extends Specification {

    @Unroll
    def 'comparing nulls first selects min value for #scenario'() {
        expect:
        def sorted = input.stream()
                .sorted(ComparatorUtils.comparingNullsFirst(Function.identity()))
                .collect(toList()) as List
        sorted.get(0) == expectedMinValue

        where:
        scenario                        | input                 || expectedMinValue
        'collection with null value'    | ['a', 'A', '0', null] || null
        'collection without null value' | ['a', 'A', '0']       || '0'
    }

    @Unroll
    def 'comparing nulls last selects max value for #scenario'() {
        expect:
        def sorted = input.stream()
                .sorted(ComparatorUtils.comparingNullsLast(Function.identity()))
                .collect(toList()) as List
        sorted.get(sorted.size() - 1) == expectedMaxValue

        where:
        scenario                        | input                 || expectedMaxValue
        'collection with null value'    | ['a', 'A', '0', null] || null
        'collection without null value' | ['a', 'A', '0']       || 'a'
    }
}
