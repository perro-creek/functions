package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToLongBiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static org.hringsak.functions.LongMapperUtils.*

class LongMapperUtilsSpec extends Specification {

    @Unroll
    def 'long mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { l, s -> "long: $l, string: $s" }
        longMapper(mapper, constantValue).apply(1L) == expected

        where:
        constantValue || expected
        'test'        || 'long: 1, string: test'
        ''            || 'long: 1, string: '
        null          || 'long: 1, string: null'
    }

    @Unroll
    def 'inverse long mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, l -> "long: $l, string: $s" }
        inverseLongMapper(mapper, constantValue).apply(1L) == expected

        where:
        constantValue || expected
        'test'        || 'long: 1, string: test'
        ''            || 'long: 1, string: '
        null          || 'long: 1, string: null'
    }

    @Unroll
    def 'to long mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> (long) s.length() }
        toLongMapperDefault(mapper, -1).applyAsLong(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to long mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((long) defaultString(a).length()) + defaultString(b).length() } as ToLongBiFunction<String, String>
        toLongMapper(mapper, paramOne).applyAsLong(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4L
        'test'   | ''       | 4L
        'test'   | 'test'   | 8L
        null     | 'test'   | 4L
        ''       | 'test'   | 4L
    }

    @Unroll
    def 'inverse to long mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((long) defaultString(a).length()) + defaultString(b).length() } as ToLongBiFunction<String, String>
        inverseToLongMapper(mapper, paramOne).applyAsLong(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4L
        'test'   | ''       | 4L
        'test'   | 'test'   | 8L
        null     | 'test'   | 4L
        ''       | 'test'   | 4L
    }
}
