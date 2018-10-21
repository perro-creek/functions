package org.hringsak.functions.predicate

import spock.lang.Specification
import spock.lang.Unroll

class CharSequenceUtilsSpec extends Specification {

    @Unroll
    def 'equals passing left "#left" and right "#right" returns #expected'() {

        expect:
        CharSequenceUtils.equals(left, right) == expected

        where:
        left       | right      | expected
        'left'     | 'right'    | false
        'right'    | 'left'     | false
        'testtest' | 'test'     | false
        'test'     | 'testtest' | false
        'test'     | 'TEST'     | false
        'test'     | 'test'     | true
        'test'     | null       | false
        null       | 'test'     | false
        null       | null       | true
    }

    @Unroll
    def 'equals ignore case passing left "#left" and right "#right" returns #expected'() {

        expect:
        CharSequenceUtils.equalsIgnoreCase(left, right) == expected

        where:
        left       | right      | expected
        'left'     | 'right'    | false
        'right'    | 'left'     | false
        'testtest' | 'test'     | false
        'test'     | 'testtest' | false
        'test'     | 'TEST'     | true
        'TEST'     | 'test'     | true
        'test'     | 'test'     | true
        'test'     | null       | false
        null       | 'test'     | false
        null       | null       | true
    }

    @Unroll
    def 'contains search char passing sequence "#sequence" returns #expected'() {

        expect:
        CharSequenceUtils.contains(sequence, 101) == expected

        where:
        sequence | expected
        'test'   | true
        ''       | false
        null     | false
    }

    @Unroll
    def 'contains search char ignore case passing sequence "#sequence" returns #expected'() {

        expect:
        CharSequenceUtils.containsIgnoreCase(sequence, 69) == expected

        where:
        sequence | expected
        'test'   | true
        ''       | false
        null     | false
    }

    @Unroll
    def 'contains search sequence passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.contains(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence | expected
        'searchSequence' | 'Sequence'     | true
        'searchSequence' | 'SEQUENCE'     | false
        'searchSequence' | 'test'         | false
        'searchSequence' | null           | false
        'searchSequence' | ''             | true
        ''               | ''             | true
        null             | ''             | false
    }

    @Unroll
    def 'contains search sequence ignore case passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.containsIgnoreCase(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence | expected
        'searchSequence' | 'Sequence'     | true
        'searchSequence' | 'SEQUENCE'     | true
        'searchSequence' | 'test'         | false
        'searchSequence' | null           | false
        'searchSequence' | ''             | true
        ''               | ''             | true
        null             | ''             | false
    }

    @Unroll
    def 'is alpha passing sequence "#sequence" returns #expected'() {

        expect:
        CharSequenceUtils.isCharacterMatch(sequence, { c -> Character.isLetter(c) }) == expected

        where:
        sequence  | expected
        null      | false
        ''        | true
        'test'    | true
        'test123' | false
    }

    @Unroll
    def 'starts with passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.startsWith(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence    | expected
        'searchSequence' | 'search'          | true
        'searchSequence' | 'searchSequence'  | true
        'searchSequence' | 'SEARCH'          | false
        'searchSequence' | 'searchSequence1' | false
        'searchSequence' | 'test'            | false
        'searchSequence' | null              | false
        'searchSequence' | ''                | true
        ''               | ''                | true
        null             | ''                | false
    }

    @Unroll
    def 'starts with ignore case passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.startsWithIgnoreCase(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence    | expected
        'searchSequence' | 'search'          | true
        'searchSequence' | 'searchSequence'  | true
        'searchSequence' | 'SEARCH'          | true
        'searchSequence' | 'SEARCHSEQUENCE'  | true
        'searchSequence' | 'sequence'        | false
        'searchSequence' | 'searchSequence1' | false
        'searchSequence' | 'test'            | false
        'searchSequence' | null              | false
        'searchSequence' | ''                | true
        ''               | ''                | true
        null             | ''                | false
    }

    @Unroll
    def 'ends with passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.endsWith(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence    | expected
        'searchSequence' | 'Sequence'        | true
        'searchSequence' | 'searchSequence'  | true
        'searchSequence' | 'SEQUENCE'        | false
        'searchSequence' | 'SEARCHSEQUENCE'  | false
        'searchSequence' | 'searchSequence1' | false
        'searchSequence' | 'test'            | false
        'searchSequence' | null              | false
        'searchSequence' | ''                | true
        ''               | ''                | true
        null             | ''                | false
    }

    @Unroll
    def 'ends with ignore case passing target sequence "#targetSequence" and search sequence "#searchSequence" returns #expected'() {

        expect:
        CharSequenceUtils.endsWithIgnoreCase(targetSequence, searchSequence) == expected

        where:
        targetSequence   | searchSequence    | expected
        'searchSequence' | 'sequence'        | true
        'searchSequence' | 'searchSequence'  | true
        'searchSequence' | 'SEQUENCE'        | true
        'searchSequence' | 'SEARCHSEQUENCE'  | true
        'searchSequence' | 'searchSequence1' | false
        'searchSequence' | 'test'            | false
        'searchSequence' | null              | false
        'searchSequence' | ''                | true
        ''               | ''                | true
        null             | ''                | false
    }
}
