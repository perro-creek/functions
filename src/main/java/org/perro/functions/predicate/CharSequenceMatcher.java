package org.perro.functions.predicate;

import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static org.perro.functions.predicate.IntPredicateUtils.intPredicate;

class CharSequenceMatcher {

    private final CharSequence target;
    private final CharSequence searchSequence;

    CharSequenceMatcher(CharSequence target, CharSequence searchSequence) {
        this.target = target;
        this.searchSequence = searchSequence;
    }

    boolean containsSearchSequence() {
        return target != null && searchSequence != null &&
                (bothSequencesAreEmpty() ||
                        containsAllCodePoints(CharSequenceUtils::equals));
    }

    private boolean bothSequencesAreEmpty() {
        return target.length() == 0 && searchSequence.length() == 0;
    }

    private boolean containsAllCodePoints(BiPredicate<CharSequence, CharSequence> equalityPredicate) {
        return IntStream.range(0, target.length())
                .anyMatch(intPredicate(this::indexWithinLimit)
                        .and(intPredicate(this::sequenceMatches, equalityPredicate)));
    }

    private boolean indexWithinLimit(int index) {
        return target.length() >= index + searchSequence.length();
    }

    private boolean sequenceMatches(int index, BiPredicate<CharSequence, CharSequence> equalityPredicate) {
        int searchLength = searchSequence.length();
        CharSequence targetSubSequence = target.subSequence(index, index + searchLength);
        return equalityPredicate.test(targetSubSequence, searchSequence);
    }

    boolean containsSearchSequenceIgnoreCase() {
        return target != null && searchSequence != null &&
                (bothSequencesAreEmpty() ||
                        containsAllCodePoints(CharSequenceUtils::equalsIgnoreCase));
    }
}
