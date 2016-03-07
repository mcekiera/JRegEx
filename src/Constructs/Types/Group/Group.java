package Constructs.Types.Group;

import Constructs.Construct;
import Constructs.ConstructsFactory;
import Expression.Expression;

import java.util.regex.Pattern;

public class Group extends Construct {

    public Group(String pattern, String match) {

        super(pattern, match);
        int i = 1;
        while(i < pattern.length()-1) {
            Construct construct = ConstructsFactory.getInstance().create(new Expression(Pattern.compile(pattern),""), i);
            i += construct.size();
        }
    }
}
