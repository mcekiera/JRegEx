package Constructs;

public class Metacharacter implements Construct {
    private String match;
    private String description;

    @Override
    public int size() {
        return 1;
    }
}
