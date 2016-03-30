package Model.Constructs;

import Model.Expression;
import Model.ExpressionBuilder;

public class test {

    public static void main(String[] args) {
        Expression e = new Expression("((\\uFFFF{2,}\\d\\s)+(?<=aaa)(?!\\p{Latin}))(([a-c1-9])b{4,6})[]]\\1[a-z&&[ghl]]");
        Complex expression = (new ExpressionBuilder()).divideIntoConstructs(e,e.getPattern(),0,e.getPattern().length());
        //expression.setCurrentMatch("marcin.cekiera@gmail.com");
        int i = 0;
        for(Construct construct : (Expression)expression) {
            System.out.println(construct.toString());
        }
        //TODO potrzebna rekursja
    }

}