package Constructs.Types;

import Constructs.Construct;

public class Mode implements Construct {
    private String mark;

    public Mode(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
