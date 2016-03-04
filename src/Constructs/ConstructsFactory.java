package Constructs;

import Expression.Expression;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();

    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        return null;
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }
}
