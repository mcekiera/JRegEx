package Model.Regex;

import Model.Regex.Type.Type;
import Model.Segment;

/**
 * Represents single and indivisible Construct object.
 */

public class Single extends Construct {

    public Single(Construct parent, Type type, Segment segment) {
        super(parent,type,segment);
    }


    @Override
    public boolean isComplex() {
        return false;
    }
}
