package Constructs;

public class Construct {
    protected String pattern;
    protected String match;

    public Construct(String pattern, String match) {
        this.pattern = pattern;
        this.match = match;
        System.out.println("\"" + pattern + ":" + match + "\"");
    }

    public int size() {
        return pattern.length();
    }
}
