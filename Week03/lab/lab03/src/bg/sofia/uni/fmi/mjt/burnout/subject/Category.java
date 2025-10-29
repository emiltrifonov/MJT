package bg.sofia.uni.fmi.mjt.burnout.subject;

public enum Category {

    MATH(0.2, 0),
    PROGRAMMING(0.1, 1),
    THEORY(0.15, 2),
    PRACTICAL(0.05, 3);

    private final double coefficient;
    private final int index;
    public static final int count = 4;

    Category(double coefficient, int index) {
        this.coefficient = coefficient;
        this.index = index;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public int getIndex() {
        return index;
    }
}