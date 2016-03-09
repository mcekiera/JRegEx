package Constructs.Types;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.List;

public class CharClass extends Construct {
    private final List<Construct> elements = new ArrayList<Construct>();

    public CharClass(String pattern) {
        super(pattern);
        int i = 1;
        while(i < pattern.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstructInCharClass(pattern, i);
            i += construct.size();
            this.elements.add(construct);
        }

    }

    @Override
    public String toString() {
        String string = "[";
        for (Construct construct : elements) {
            string += "\n\t" + construct.toString();
        }
        string += "\n]";
        return pattern;
    }
}
