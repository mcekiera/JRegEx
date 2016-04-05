package Model.Constructs;

import Model.Matching.Matched;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Construct {
    private Type type;
    protected Composition parent;
    protected String pattern;
    protected String asString;
    protected int start;
    protected int end;

    public Construct(Type type, String pattern,int start, int end) {
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        this.type = type;
        asString = pattern.substring(start,end);
    }

    public Type getType() {
        return type;
    }

    public void setParent(Composition construct) {
        this.parent = construct;
    }

    public Composition getParent() {
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

    public String getAsSeparateGroup(String groupName) {
        String begin = pattern.substring(0,getStart());
        String mid = pattern.substring(getStart(),getEnd());
        String finish = pattern.substring(getEnd());
        String re = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        System.out.println(re);
        return re;
    }

    @Override
    public String toString() {
        return asString;
    }

    public Matched directMatch(String match) {
        try {
            Matcher matcher = Pattern.compile(getAsSeparateGroup("test")).matcher(match);

            if (matcher.find()) {
                return new Matched(matcher.start("test"), matcher.end("test"));
            } else {
                return new Matched(0,0);
            }
        } catch (PatternSyntaxException e) {
            return new Matched(0,0);
        }

    }


    public boolean equals(Construct c) {
        return this.getType() == c.getType() && this.getPattern() == c.getPattern()
                && this.getStart() == c.getStart() && this.getEnd() == c.getEnd();

    }

}


