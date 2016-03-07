package Constructs;

import Constructs.Lib.MatcherLib;
import Expression.Expression;

import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        String str = "(?=aaa)aaa([a-z]7)[a-z]\\d\\d\u0111";
        MatcherLib lib = MatcherLib.getInstance();
        ConstructsFactory cf = ConstructsFactory.getInstance();
        Expression expression = new Expression(Pattern.compile(str),"6a");

        int i = 0;
        while(i < str.length()) {
            Construct construct = cf.create(expression,i);
            i += construct.size();
        }

    }
}


//todo inny podzia³: pointmaching, charactermaching, conceptmatching, simple and composed
