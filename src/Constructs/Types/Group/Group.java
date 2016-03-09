package Constructs.Types.Group;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct {
    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern, int start, int end) {
        super(pattern, start, end);
    }


    public void read() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(asString);
        matcher.find();
        int i = matcher.end();
        while(i < asString.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstruct(asString, i);
            i += construct.size();
            this.elements.add(construct);
        }
    }

    @Override
    public String toString() {
        return asString;
    }
}
