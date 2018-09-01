package org.hringsak.functions

enum TestValue {

    ONE,
    TWO

    static makeNameToValueMap() {
        def result = [:]
        values().each { value -> result << [(value.name()): value] }
        result
    }

    static makeValueToNameMap() {
        def result = [:]
        values().each { value -> result << [(value): value.name()] }
        result
    }
}