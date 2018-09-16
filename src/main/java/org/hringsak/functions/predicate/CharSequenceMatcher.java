package org.hringsak.functions.predicate;

import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.hringsak.functions.predicate.IntPredicateUtils.intPredicate;

class CharSequenceMatcher {

    private final CharSequence target;
    private final CharSequence searchSequence;

    CharSequenceMatcher(CharSequence target, CharSequence searchSequence) {
        this.target = target;
        this.searchSequence = searchSequence;
    }

    boolean containsSearchSequence() {
        IntPredicate matchPredicate = intPredicate(this::sequenceMatches, CharSequenceUtils::equals);
        return target != null && searchSequence != null &&
                (bothSequencesAreEmpty() ||
                        containsAllCodePoints(matchPredicate));
    }

    private boolean bothSequencesAreEmpty() {
        return target.length() == 0 && searchSequence.length() == 0;
    }

    private boolean containsAllCodePoints(IntPredicate predicate) {
        return IntStream.range(0, target.length())
                .anyMatch(predicate);
    }

    private boolean sequenceMatches(int index, BiPredicate<CharSequence, CharSequence> matchPredicate) {
        int searchLength = searchSequence.length();
        int endIndex = index + searchLength;
        if (endIndex <= target.length()) {
            CharSequence targetSubSequence = target.subSequence(index, index + searchLength);
            return matchPredicate.test(targetSubSequence, searchSequence);
        }
        return false;
    }

    boolean containsSearchSequenceIgnoreCase() {
        IntPredicate matchPredicate = intPredicate(this::sequenceMatches, CharSequenceUtils::equalsIgnoreCase);
        return target != null && searchSequence != null &&
                (bothSequencesAreEmpty() ||
                        containsAllCodePoints(matchPredicate));
    }
}
