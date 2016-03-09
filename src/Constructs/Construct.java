package Constructs;

public class Construct {
    protected String pattern;

    public Construct(String pattern) {
        this.pattern = pattern;
    }

    public int size() {
        return pattern.length();
    }

    public String getPattern() {
        return pattern;
    }

    public String getAsSeparateGroup(String groupName) {
        return "(?<" + groupName +">" + getPattern() + ")";
    }

    @Override
    public String toString() {
        return pattern;
    }
}
