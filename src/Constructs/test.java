package Constructs;

import Expressions.Expression;

public class test {

    public static void main(String[] args) {
        Expression expression = new Expression("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+" +
                "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        expression.setCurrentMatch("marcin.cekiera@gmail.com");
        expression.matchElements();
    }

}

