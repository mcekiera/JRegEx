package Expressions;

import Syntax.Element;

import java.util.ArrayList;
import java.util.List;

public class Expression {
    private final List<Expression> components = new ArrayList<Expression>();
    private final Element element;
    private final String match;

    public Expression(Element element, String match) {
        this.element = element;
        this.match = match;
    }

    public void addComponent(Expression component) {
        components.add(component);
    }


}
