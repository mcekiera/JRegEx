package Constructs.Types;

import Constructs.Construct;

public class SpecificChar  extends Construct {

    public SpecificChar(String pattern, String match) {
        super(pattern,match);
    }
    //\uFFFF,\xFF,\0377,\x{h...h},\cX,//
}
