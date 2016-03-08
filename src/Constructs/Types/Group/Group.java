package Constructs.Types.Group;

import Constructs.Construct;
import Constructs.ConstructsFactory;
import Expression.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct {

    public Group(String pattern, String match) {

        super(pattern, match);
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(pattern);
        matcher.find();
        int i = matcher.end();
        while(i < pattern.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstruct(new Expression(Pattern.compile(pattern), match), i);
            i += construct.size();
        }
    }
}
