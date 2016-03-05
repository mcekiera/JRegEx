package Constructs;

public class SpecialBackslash implements Construct{
    private String mark;

    public SpecialBackslash(String mark) {
        this.mark = mark;
        System.out.println(mark);
    }

    @Override
    public int size() {
        return mark.length();
    }
}
