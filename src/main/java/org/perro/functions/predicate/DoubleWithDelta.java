package org.perro.functions.predicate;

class DoubleWithDelta {

    private final double value;
    private final double delta;

    private DoubleWithDelta(double value, double delta) {
        this.value = value;
        this.delta = delta;
    }

    static DoubleWithDelta of(double value, double delta) {
        return new DoubleWithDelta(value, delta);
    }

    boolean isEqualWithinDelta(double compareTo) {
        return delta >= Math.abs(value - compareTo);
    }
}
