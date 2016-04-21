package Model.Constructs;

public class Quantifier  extends Construct{
    private Construct construct;

    public Quantifier(Type type, String pattern, int start, int end) {
        super(type,pattern,start,end);
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    public Construct getConstruct() {return construct;}

    @Override
    public String getPattern() {
        return super.getPattern();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int size() {
        return toString().length();
    }

    @Override
    public int getStart() {
        return construct.getStart();
    }
}
