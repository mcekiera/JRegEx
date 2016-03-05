package Constructs.Types;

import Constructs.Construct;

public class Metacharacter implements Construct {
    private String match;
    private String description;

    public Metacharacter(String mark) {
        System.out.println(mark);
    }

    @Override
    public int size() {
        return 1;
    }
}
