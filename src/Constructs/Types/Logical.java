package Constructs.Types;

import Constructs.Construct;

public class Logical implements Construct {
    String mark;

    public Logical(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }


    @Override
    public int size() {
        return mark.length();
    }
}
