package org.perro.functions.predicate;

import org.perro.functions.mapper.IntIndexPair;
import org.perro.functions.mapper.IntMapperUtils;

import java.util.function.Predicate;

class CharSequenceEqualityEvaluator {

    private final CharSequence left;
    private final CharSequence right;

    CharSequenceEqualityEvaluator(CharSequence left, CharSequence right) {
        this.left = left;
        this.right = right;
    }

    boolean equals() {
        return evaluate(this::isCodePointEqual);
    }

    private boolean isCodePointEqual(IntIndexPair pair) {
        return pair.getIntValue() == right.charAt(pair.getIndex());
    }

    private boolean evaluate(Predicate<IntIndexPair> matchPredicate) {
        return (left == right) ||
                (neitherAreNull() &&
                        lengthsAreEqual() &&
                        allCodePointsMatch(matchPredicate));
    }

    private boolean neitherAreNull() {
        return left != null && right != null;
    }

    private boolean lengthsAreEqual() {
        return left.length() == right.length();
    }

    private boolean allCodePointsMatch(Predicate<IntIndexPair> matchPredicate) {
        return left.codePoints()
                .mapToObj(IntMapperUtils.intPairWithIndex())
                .allMatch(matchPredicate);
    }

    boolean equalsIgnoreCase() {
        return evaluate(this::isCodePointEqualIgnoreCase);
    }

    private boolean isCodePointEqualIgnoreCase(IntIndexPair pair) {
        int leftChar = pair.getIntValue();
        int rightChar = right.charAt(pair.getIndex());
        return Character.toUpperCase(leftChar) == Character.toUpperCase(rightChar) ||
                Character.toLowerCase(leftChar) == Character.toLowerCase(rightChar);
    }
}
