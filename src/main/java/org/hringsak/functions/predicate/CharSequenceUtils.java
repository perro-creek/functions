package org.hringsak.functions.predicate;

import org.hringsak.functions.mapper.IntIndexPair;
import org.hringsak.functions.mapper.IntMapperUtils;

import java.util.function.IntPredicate;

import static org.hringsak.functions.predicate.IntPredicateUtils.isIntEqual;
import static org.hringsak.functions.predicate.PredicateUtils.predicate;

final class CharSequenceUtils {

    private CharSequenceUtils() {
    }

    static boolean equals(CharSequence left, CharSequence right) {
        return (left == right) ||
                (neitherAreNull(left, right) &&
                        lengthsAreEqual(left, right) &&
                        allCodePointsMatch(left, right));
    }

    private static boolean neitherAreNull(CharSequence left, CharSequence right) {
        return left != null && right != null;
    }

    private static boolean lengthsAreEqual(CharSequence left, CharSequence right) {
        return left.length() == right.length();
    }

    private static boolean allCodePointsMatch(CharSequence left, CharSequence right) {
        return left.codePoints()
                .mapToObj(IntMapperUtils.intPairWithIndex())
                .allMatch(predicate(CharSequenceUtils::isCodePointEqual, right));
    }

    private static boolean isCodePointEqual(IntIndexPair pair, CharSequence right) {
        return pair.getLeft() == right.charAt(pair.getRight());
    }

    static boolean equalsIgnoreCase(CharSequence left, CharSequence right) {
        return (left == right) ||
                (neitherAreNull(left, right) &&
                        lengthsAreEqual(left, right) &&
                        allCodePointsMatchIgnoreCase(left, right));
    }

    private static boolean allCodePointsMatchIgnoreCase(CharSequence left, CharSequence right) {
        return left.codePoints()
                .mapToObj(IntMapperUtils.intPairWithIndex())
                .allMatch(predicate(CharSequenceUtils::isCodePointEqualIgnoreCase, right));
    }

    private static boolean isCodePointEqualIgnoreCase(IntIndexPair pair, CharSequence right) {
        int leftChar = pair.getLeft();
        int rightChar = right.charAt(pair.getRight());
        return Character.toUpperCase(leftChar) == Character.toUpperCase(rightChar) ||
                Character.toLowerCase(leftChar) == Character.toLowerCase(rightChar);
    }

    static boolean contains(CharSequence sequence, int searchChar) {
        return sequence != null && sequence.codePoints()
                .anyMatch(isIntEqual(searchChar));
    }

    static boolean contains(CharSequence sequence, CharSequence searchSequence) {
        return new CharSequenceMatcher(sequence, searchSequence)
                .containsSearchSequence();
    }

    static boolean containsIgnoreCase(CharSequence sequence, CharSequence searchSequence) {
        return new CharSequenceMatcher(sequence, searchSequence)
                .containsSearchSequenceIgnoreCase();
    }

    static boolean isAlpha(CharSequence sequence) {
        return isCharachterMatch(sequence, Character::isLetter);
    }

    private static boolean isCharachterMatch(CharSequence sequence, IntPredicate charPredicate) {
        return !isNullOrEmpty(sequence) &&
                sequence.codePoints().allMatch(charPredicate);
    }

    static boolean isAlphaNumeric(CharSequence sequence) {
        return isCharachterMatch(sequence, Character::isLetterOrDigit);
    }

    static boolean isNumeric(CharSequence sequence) {
        return isCharachterMatch(sequence, Character::isDigit);
    }

    static boolean startsWith(CharSequence sequence, CharSequence prefix) {
        return (sequence == prefix) ||
                (neitherAreNull(sequence, prefix) &&
                        sequence.length() >= prefix.length() &&
                        equals(getStartsWithSubSequence(sequence, prefix), prefix));
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

    static boolean isNullOrEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }
}
