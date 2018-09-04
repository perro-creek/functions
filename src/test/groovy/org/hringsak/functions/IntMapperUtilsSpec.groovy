package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToIntBiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static org.hringsak.functions.IntMapperUtils.*

class IntMapperUtilsSpec extends Specification {

    @Unroll
    def 'int mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { i, s -> "int: $i, string: $s" }
        intMapper(mapper, constantValue).apply(1) == expected

        where:
        constantValue || expected
        'test'        || 'int: 1, string: test'
        ''            || 'int: 1, string: '
        null          || 'int: 1, string: null'
    }

    @Unroll
    def 'inverse int mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, i -> "int: $i, string: $s" }
        inverseIntMapper(mapper, constantValue).apply(1) == expected

        where:
        constantValue || expected
        'test'        || 'int: 1, string: test'
        ''            || 'int: 1, string: '
        null          || 'int: 1, string: null'
    }

    @Unroll
    def 'to int mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> s.length() }
        toIntMapperDefault(mapper, -1).applyAsInt(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to int mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> defaultString(a).length() + defaultString(b).length() } as ToIntBiFunction<String, String>
        toIntMapper(mapper, paramOne).applyAsInt(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4
        'test'   | ''       | 4
        'test'   | 'test'   | 8
        null     | 'test'   | 4
        ''       | 'test'   | 4
    }

    @Unroll
    def 'inverse to int mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> defaultString(a).length() + defaultString(b).length() } as ToIntBiFunction<String, String>
        inverseToIntMapper(mapper, paramOne).applyAsInt(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4
        'test'   | ''       | 4
        'test'   | 'test'   | 8
        null     | 'test'   | 4
        ''       | 'test'   | 4
    }
}
