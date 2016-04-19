package Model.Constructs;

import Model.Matching.Matched;

public class Construct {
    private Type type;
    protected Sequence parent;
    protected String pattern;
    protected String asString;
    protected int start;
    protected int end;
    protected Matched current;

    public Construct(Type type, String pattern,int start, int end) {
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        this.type = type;
        asString = pattern.substring(start,end);
        current = new Matched(0,0,"");
    }

    public Type getType() {
        return type;
    }

    public void setParent(Sequence construct) {
        this.parent = construct;
    }

    public Sequence getParent() {
        return parent;
    }

    public int size() {
        return asString.length();
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return asString;
    }



    public Matched getCurrentMatch() {
        return current;
    }

    public void setCurrentMatch(Matched matched) {
        this.current = matched;
    }

    public int getCurrentMatchStart() {
        return current.getStart();
    }

    public int getCurrentMatchEnd() {
        return current.getEnd();
    }

    @Override
    public boolean equals(Object object) {
        Construct c = (Construct)object;
        return this.getType() == c.getType() && this.getPattern() == c.getPattern()
                && this.getStart() == c.getStart() && this.getEnd() == c.getEnd();

    }

    public static boolean isComposed(Construct construct) {
        return construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.ATOMIC ||
                construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING ||
                construct.getType() == Type.CHAR_CLASS;
    }
}


