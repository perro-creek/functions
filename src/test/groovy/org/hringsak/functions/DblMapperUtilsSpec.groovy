package org.hringsak.functions

import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.ToDoubleBiFunction

import static org.apache.commons.lang3.StringUtils.defaultString
import static DblMapperUtils.*

class DblMapperUtilsSpec extends Specification {

    @Unroll
    def 'double mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { d, s -> "double: $d, string: $s" }
        dblMapper(mapper, constantValue).apply(1.0D) == expected

        where:
        constantValue || expected
        'test'        || 'double: 1.0, string: test'
        ''            || 'double: 1.0, string: '
        null          || 'double: 1.0, string: null'
    }

    @Unroll
    def 'inverse double mapper for bi-function passing values "#constantValue" returns #expected'() {

        expect:
        def mapper = { s, d -> "double: $d, string: $s" }
        inverseDblMapper(mapper, constantValue).apply(1.0D) == expected

        where:
        constantValue || expected
        'test'        || 'double: 1.0, string: test'
        ''            || 'double: 1.0, string: '
        null          || 'double: 1.0, string: null'
    }

    @Unroll
    def 'to double mapper with default passing value "#value" returns "#expected"'() {

        expect:
        def mapper = { s -> (double) s.length() }
        toDblMapperDefault(mapper, -1).applyAsDouble(value) == expected

        where:
        value  | expected
        null   | -1
        ''     | 0
        'test' | 4
    }

    @Unroll
    def 'to double mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((double) defaultString(a).length()) + defaultString(b).length() } as ToDoubleBiFunction<String, String>
        toDblMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4.0D
        'test'   | ''       | 4.0D
        'test'   | 'test'   | 8.0D
        null     | 'test'   | 4.0D
        ''       | 'test'   | 4.0D
    }

    @Unroll
    def 'inverse to double mapper for bi-function passing values "#paramOne" and "#paramTwo" returns #expected'() {

        expect:
        def mapper = { String a, String b -> ((double) defaultString(a).length()) + defaultString(b).length() } as ToDoubleBiFunction<String, String>
        inverseToDblMapper(mapper, paramOne).applyAsDouble(paramTwo) == expected

        where:
        paramOne | paramTwo | expected
        'test'   | null     | 4.0D
        'test'   | ''       | 4.0D
        'test'   | 'test'   | 8.0D
        null     | 'test'   | 4.0D
        ''       | 'test'   | 4.0D
    }
}