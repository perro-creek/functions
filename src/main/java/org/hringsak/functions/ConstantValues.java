package org.hringsak.functions;

class ConstantValues<U, V> {

    private final U left;
    private final V right;

    private ConstantValues(U left, V right) {
        this.left = left;
        this.right = right;
    }

    static <U, V> ConstantValues<U, V> of(U left, V right) {
        return new ConstantValues<>(left, right);
    }

    U getLeft() {
        return left;
    }

    V getRight() {
        return right;
    }
}
