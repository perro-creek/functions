package org.hringsak.functions;

class Arguments<U, V> {

    private final U left;
    private final V right;

    private Arguments(U left, V right) {
        this.left = left;
        this.right = right;
    }

    static <U, V> Arguments<U, V> of(U left, V right) {
        return new Arguments<>(left, right);
    }

    U getLeft() {
        return left;
    }

    V getRight() {
        return right;
    }
}
