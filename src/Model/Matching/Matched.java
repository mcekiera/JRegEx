package Model.Matching;

public class Matched {
    private final int start;
    private final int end;
    private final String fragment;

    public Matched(int start, int end, String fragment) {
        this.start = start;
        this.end = end;
        this.fragment = fragment;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return fragment;
    }
}
