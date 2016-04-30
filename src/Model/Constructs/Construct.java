package Model.Constructs;

import Model.Matching.Matched;
import Re.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class Construct {
    private final Type type;
    private Construct parent;
    private final String pattern;
    private final String asString;
    private final int start;
    private final int end;
    private final List<Matched> matched;

    public Construct(Type type, String pattern, int start, int end) {
        //this.parent = parent;
        this.start = start;
        this.end = end;
        this.type = type;
        this.pattern = pattern;
        asString = pattern.substring(start, end);
        matched = new ArrayList<>();
        System.out.println("Constructor: " + type + "," + pattern + "," + start + "," + end);
    }

    public Type getType() {
        return type;
    }

    public Construct getParent() {
        return parent;
    }

    public void setParent(Construct construct) {
        System.out.println("setParent: " + construct.getType());
        this.parent = construct;
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

    @Override
    public boolean equals(Object object) {
        Singular c = (Singular) object;
        return this.getType() == c.getType() && this.getPattern() == c.getPattern()
                && this.getStart() == c.getStart() && this.getEnd() == c.getEnd();

    }

    public List<Matched> matched() {
        return matched;
    }

    public void addMatched(Matched m) {
        matched.add(m);
    }

    public static boolean isComposed(Construct singular) {
        return singular.getType() == Type.LOOK_AROUND ||
                singular.getType() == Type.ATOMIC ||
                singular.getType() == Type.CAPTURING ||
                singular.getType() == Type.NON_CAPTURING ||
                singular.getType() == Type.CHAR_CLASS;
    }

    public String getAsSeparateGroup(Construct construct,String groupName) {
        String begin = construct.getPattern().substring(0, construct.getStart());
        String mid = construct.getPattern().substring(construct.getStart(), construct.getEnd());
        String finish = construct.getPattern().substring(construct.getEnd());
        String re = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        return re;
    }
}
