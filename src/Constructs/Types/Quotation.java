package Constructs.Types;

import Constructs.Construct;

public class Quotation implements Construct {
    String mark;

    public Quotation(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }


    @Override
    public int size() {
        return mark.length();
    }
}
