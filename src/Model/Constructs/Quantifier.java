package Model.Constructs;

public class Quantifier  extends Construct{
    private Construct construct;

    public Quantifier(Type type, String pattern, int start, int end) {
        super(type,pattern,start,end);
        System.out.println(type + "," + pattern + "," + start + "," + end);
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    public Construct getConstruct() {return construct;}

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
}
