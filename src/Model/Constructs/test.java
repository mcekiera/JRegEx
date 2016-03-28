package Model.Constructs;

import Model.Expression;
import Model.ExpressionBuilder;

public class test {

    public static void main(String[] args) {

        Expression expression = (new ExpressionBuilder()).toExpression("(?<=aaa)({4,6})[]]\\1");
        expression.setCurrentMatch("marcin.cekiera@gmail.com");
        int i = 0;
        for(Construct construct : expression) {
            System.out.println(construct.toString());
        }
        //expression.matchElements();
    }

}