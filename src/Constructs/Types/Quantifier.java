package Constructs.Types;

import Constructs.Construct;

public class Quantifier  extends Construct {
    private Construct construct;

    public Quantifier(String pattern, int start, int end) {
        super(pattern, start, end);
    }


    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    @Override
    public String getPattern() {
        return construct.getPattern() + this.asString;
    }

    @Override
    public String toString() {
        return construct.getPattern() + this.getPattern();
    }

    @Override
    public int size() {
        return construct.size() + this.asString.length();
    }

    @Override
    public int getStart() {
        return construct.getStart();
    }

    public int getEnd() {
        return this.end;
    }
}
