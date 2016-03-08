package Constructs.Types;

import Constructs.Construct;

public class SpecificChar  extends Construct {

    public SpecificChar(String pattern) {
        super(pattern);
    }
    //\uFFFF,\xFF,\0377,\x{h...h},\cX,//
}
