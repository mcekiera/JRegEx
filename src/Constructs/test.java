package Constructs;

import Constructs.Lib.MatcherLib;
import Expression.Expression;

import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        String str = "ab[1-9]cd";
        MatcherLib lib = MatcherLib.getInstance();
        ConstructsFactory cf = ConstructsFactory.getInstance();
        Expression expression = new Expression(Pattern.compile(str),"ab6cd");

        int i = 0;
        while(i < str.length()) {
            Construct construct = cf.create(expression,i);
           i += construct.size();
        }

    }

}


//todo inny podzia³: pointmaching, charactermaching, conceptmatching, simple and composed
