package Constructs.Types;

import Constructs.Construct;

import java.util.List;

public class CharClass implements Construct {
    private List<Construct> constructs;
    String mark;

    public CharClass(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
