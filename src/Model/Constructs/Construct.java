package Model.Constructs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Construct {
    private Type type;
    protected Complex parent;
    protected String pattern;
    protected String asString;
    protected int start;
    protected int end;

    public Construct(String pattern, int start, int end) {
        this.pattern = pattern;
        this.start = start;
        this.end = end;
        asString = pattern.substring(start,end);

    }

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

    public void setParent(Complex construct) {
        this.parent = construct;
    }

    public Complex getParent() {
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
        return asString;
    }

    public String getAsSeparateGroup(String groupName) {
        String begin = pattern.substring(0,getStart());
        String mid = pattern.substring(getStart(),getEnd());
        String finish = pattern.substring(getEnd());
        return begin + "(?<" + groupName + ">" + mid + ")" + finish;
    }

    @Override
    public String toString() {
        return asString;
    }

    public String directMatch(String match) {
        Matcher matcher = Pattern.compile(getAsSeparateGroup("test")).matcher(match);
        matcher.find();
        String result;
        try {
            result = matcher.group("test");
        }catch (Exception ex) {
            result = "empty";
        }
        return getPattern() + ":" + result;
    }
}


