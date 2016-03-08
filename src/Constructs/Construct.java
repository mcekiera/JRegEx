package Constructs;

public class Construct {
    protected String pattern;

    public Construct(String patter) {
        this.pattern = pattern;
        System.out.println(pattern);
    }

    public int size() {
        return pattern.length();
    }
}
