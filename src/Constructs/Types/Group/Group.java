package Constructs.Types.Group;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct {
    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern) {

        super(pattern);
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(pattern);
        matcher.find();
        int i = matcher.end();
        while(i < pattern.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstruct(pattern, i);
            i += construct.size();
            this.elements.add(construct);
        }
    }

    @Override
    public String toString() {
        String result = pattern;
        for(Construct construct : elements) {
            result += "\n\t" + construct.toString();
        }
        return result;
    }
}
