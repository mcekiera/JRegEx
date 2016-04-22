package Model.Constructs;

import Model.Matching.Matched;

public class Singular extends Construct{
    protected Matched current;

    public Singular(Type type, String pattern, int start, int end) {
        super(type,pattern,start,end);
        current = new Matched(0,0,"");
    }

}


