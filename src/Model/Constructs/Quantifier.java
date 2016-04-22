package Model.Constructs;

import Model.Matching.Matched;

public class Quantifier  extends Construct{
    private Construct construct;

    public Quantifier(Type type, String pattern, int start, int end) {
        super(type,pattern,start,end);
    }

    @Override
    public Matched matchDirectly(String match) {
        return null;
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    public Construct getConstruct() {return construct;}
}
