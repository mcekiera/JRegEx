package Constructs;

public class Construct {
    private String mark;

    public Construct() {}
    public Construct(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    public int size() {
        return mark.length();
    }
}
