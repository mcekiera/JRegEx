package Model.Constructs;

import Model.Expression;
import Model.ExpressionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {

        Expression expression = (new ExpressionBuilder()).create("(\\uFFFF{2,}\\d\\s)+(?<=aaa)(?!\\p{Latin})(b{4,6})[]]\\1[a-z&&[ghl]]");
        expression.setCurrentMatch("marcin.cekiera@gmail.com");
        int i = 0;
        for(Construct construct : expression) {
            System.out.println(construct.toString());
        }
        //TODO potrzebna rekursja
        Matcher matcher = Pattern.compile(Type.PREDEFINED.getRegex()).matcher("\\p{Latin})(b{4,6})[]]\\1");
        while(matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}