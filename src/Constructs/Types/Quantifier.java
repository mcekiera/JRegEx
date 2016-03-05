package Constructs.Types;

import Constructs.Construct;

public class Quantifier implements Construct {
    private Construct construct;
    String mark;

    public Quantifier(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
