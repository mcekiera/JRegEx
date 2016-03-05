package Constructs.Types;

import Constructs.Construct;

public class SimpleChar implements Construct {
    String mark;

    public SimpleChar(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
