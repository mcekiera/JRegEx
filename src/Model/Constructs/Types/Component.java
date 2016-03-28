package Model.Constructs.Types;

import Model.Constructs.Construct;

public class Component extends Construct {
    private Construct parent;
    public Component(String pattern, int start, int end) {
        super(pattern, start, end);
    }

    public Component(String pattern, Construct parent, int start, int end) {
        super(pattern, start, end);
        this.parent = parent;
    }

}
