package Constructs.Types;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.List;

public class CharClass extends Construct {
    private final List<Construct> elements = new ArrayList<Construct>();

    public CharClass(String pattern, int start, int end) {
        super(pattern, start, end);
        //read();
    }


    private void read() {
        int i = 1;
        while(i < asString.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstructInCharClass(asString, i);
            i += construct.size();
            this.elements.add(construct);
        }
    }

    @Override
    public String toString() {
        return asString;
    }
}
