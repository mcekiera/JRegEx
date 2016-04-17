package Model.Constructs;

import Model.Matching.Fragment;

public class Construct {
    private Type type;
    private Sequence parent;
    private String pattern;
    private Fragment inPattern;
    private Fragment inText;

    public Construct(Type type, String pattern,int start, int end) {
        inPattern = new Fragment(start,end,pattern.substring(start,end));
        this.pattern = pattern;
        this.type = type;
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
        return inPattern.getFragment().length();
    }

    public int getStart() {
        return inPattern.getStart();
    }

    public int getEnd() {
        return inPattern.getEnd();
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return inPattern.getFragment();
    }


    public void setCurrentMatch(Fragment fragment) {
        this.inText = fragment;
    }

    public Fragment getCurrentMatch() {
        return inText;
    }

    public int getCurrentMatchStart() {
        return inText.getStart();
    }

    public int getCurrentMatchEnd() {
        return inText.getEnd();
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


