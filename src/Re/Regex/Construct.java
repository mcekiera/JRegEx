package Re.Regex;

import Re.Segment;

public abstract class Construct {
    private final Segment textual;

    public Construct(Type type, Segment segment) {
        this.textual = segment;
    }


}
