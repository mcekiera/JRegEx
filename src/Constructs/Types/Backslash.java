package Constructs.Types;

import Constructs.Construct;

public class Backslash implements Construct {
    String mark;

    public Backslash(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
