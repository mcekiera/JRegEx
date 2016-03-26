package Model.Constructs;

import Model.Expression;

public class test {

    public static void main(String[] args) {

        Expression expression = new Expression("(?<=aaa)({4,6})[]\\1");
        expression.setCurrentMatch("marcin.cekiera@gmail.com");
        int i = 0;
        for(Construct construct : expression) {
            System.out.println(construct.toString());
        }
        //expression.matchElements();
    }

}