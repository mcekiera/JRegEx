package Model.Constructs.Types.Quantifiable;

import Model.Constructs.Construct;
import Model.Constructs.Types.Quantifiable.Quantifiable;

public class Quantifier  extends Construct implements Quantifiable {
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
        return getClass().getName() + ":" + asString + " with " + construct.toString();
    }

    @Override
    public int size() {
        return asString.length();
    }

    @Override
    public int getStart() {
        return construct.getStart();
    }

    public int getEnd() {
        return this.end;
    }

    @Override
    public Construct getConstruct() {
        return construct;
    }
}
