package Constructs;

import Expression.Expression;

import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        String str = "\\w\\w\\[1-9\\](\\S\\w)";
        ConstructsFactory cf = ConstructsFactory.getInstance();
        Expression expression = new Expression(Pattern.compile(str),"ab[1-9]cd");

        int i = 0;
        while(i < str.length()) {
            Construct construct = cf.createConstruct(expression, i);
           i += construct.size();
        }

    }

}


//todo inny podzia³: pointmaching, charactermaching, conceptmatching, simple and composed
