package Constructs;

import Expression.Expression;

import java.util.regex.Matcher;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();

    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        Matcher matcher;
        return new Construct();
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }
}
