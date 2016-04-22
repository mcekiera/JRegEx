package Model.Constructs;

import Model.Matching.Matched;

public class Error extends Construct{

    public Error(Type type, String pattern, int start, int end) {
        super(type, pattern, start, end);
        System.out.println(type);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void addMatched(Matched matched) {
    }

    @Override
    public void matchDirectly(String match) {

    }
}
