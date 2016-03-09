package Constructs.Types;

import Constructs.Construct;

public class Quantifier  extends Construct {
    private Construct construct;

    public Quantifier(String pattern) {
        super(pattern);
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    @Override
    public String getPattern() {
        return construct.getPattern() + pattern;
    }

    @Override
    public String toString() {
        return construct.toString() + pattern;
    }

    @Override
    public int size() {
        return construct.size() + pattern.length();
    }


}
