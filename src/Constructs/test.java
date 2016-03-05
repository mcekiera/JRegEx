package Constructs;

import Expression.Expression;

import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\[[^\\[]*&&\\[[^\\]]+\\]\\]|\\[[^\\]]+\\]");
        Expression expression = new Expression(pattern,"a d");
        int i = 0;
        while(i < expression.getPattern().toString().length()) {
            Construct construct = ConstructsFactory.getInstance().create(expression,i);
            i += construct.size();
        }


    }
}
