package Model.Matching;

public class Fragment {
    private final int start;
    private final int end;
    private final String fragment;

    public Fragment(int start, int end, String fragment) {
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

    public String getFragment() {
        return fragment;
    }

    @Override
    public String toString() {
        return getFragment() + "," + getStart() + "," + getEnd();
    }
}
