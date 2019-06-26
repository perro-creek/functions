package org.perro.functions.predicate;

import java.util.function.IntPredicate;

import static org.perro.functions.predicate.IntPredicateUtils.isIntEqual;

final class CharSequenceUtils {

    private CharSequenceUtils() {
    }

    static boolean equals(CharSequence left, CharSequence right) {
        return new CharSequenceEqualityEvaluator(left, right).equals();
    }

    static boolean equalsIgnoreCase(CharSequence left, CharSequence right) {
        return new CharSequenceEqualityEvaluator(left, right).equalsIgnoreCase();
    }

    static boolean contains(CharSequence sequence, int searchChar) {
        return sequence != null && sequence.codePoints()
                .anyMatch(isIntEqual(searchChar));
    }

    static boolean containsIgnoreCase(CharSequence sequence, int searchChar) {
        int lowerSearchChar = Character.toLowerCase(searchChar);
        return sequence != null && sequence.codePoints()
                .map(Character::toLowerCase)
                .anyMatch(isIntEqual(lowerSearchChar));
    }

    static boolean contains(CharSequence sequence, CharSequence searchSequence) {
        return new CharSequenceMatcher(sequence, searchSequence)
                .containsSearchSequence();
    }

    static boolean containsIgnoreCase(CharSequence sequence, CharSequence searchSequence) {
        return new CharSequenceMatcher(sequence, searchSequence)
                .containsSearchSequenceIgnoreCase();
    }

    static boolean anyCharacterMatches(CharSequence sequence, IntPredicate charPredicate) {
        return sequence != null &&
                sequence.codePoints().anyMatch(charPredicate);
    }

    static boolean isCharacterMatch(CharSequence sequence, IntPredicate charPredicate) {
        return sequence != null &&
                sequence.codePoints().allMatch(charPredicate);
    }

    static boolean noCharactersMatch(CharSequence sequence, IntPredicate charPredicate) {
        return sequence == null ||
                sequence.codePoints().noneMatch(charPredicate);
    }

    static boolean startsWith(CharSequence sequence, CharSequence prefix) {
        return (sequence == prefix) ||
                (neitherAreNull(sequence, prefix) &&
                        sequence.length() >= prefix.length() &&
                        equals(getStartsWithSubSequence(sequence, prefix), prefix));
    }

    private static boolean neitherAreNull(CharSequence left, CharSequence right) {
        return left != null && right != null;
    }

    private static CharSequence getStartsWithSubSequence(CharSequence sequence, CharSequence prefix) {
        return sequence.subSequence(0, prefix.length());
    }

    static boolean startsWithIgnoreCase(CharSequence sequence, CharSequence prefix) {
        return (sequence == prefix) ||
                (neitherAreNull(sequence, prefix) &&
                        sequence.length() >= prefix.length() &&
                        equalsIgnoreCase(getStartsWithSubSequence(sequence, prefix), prefix));
    }

    static boolean endsWith(CharSequence sequence, CharSequence suffix) {
        return (sequence == suffix) ||
                (neitherAreNull(sequence, suffix) &&
                        sequence.length() >= suffix.length() &&
                        equals(getEndsWithSubSequence(sequence, suffix), suffix));
    }

    private static CharSequence getEndsWithSubSequence(CharSequence sequence, CharSequence suffix) {
        int len = sequence.length();
        return sequence.subSequence(len - suffix.length(), len);
    }

    static boolean endsWithIgnoreCase(CharSequence sequence, CharSequence suffix) {
        return (sequence == suffix) ||
                (neitherAreNull(sequence, suffix) &&
                        sequence.length() >= suffix.length() &&
                        equalsIgnoreCase(getEndsWithSubSequence(sequence, suffix), suffix));
    }
}
