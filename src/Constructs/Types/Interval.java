package Constructs.Types;

import Constructs.Construct;

public class Interval extends Construct implements Quantifiable{
    private Construct construct;

    public Interval(String pattern, int start, int end) {
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
        System.out.println(asString.length());return  asString.length();
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
