package Model.Matching;

public class Matched {
    private final int startIndex;
    private final int endIndex;



    public Matched(int start, int end) {
        startIndex = start;
        endIndex = end;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
