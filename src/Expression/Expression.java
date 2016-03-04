package Expression;

import Constructs.Construct;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Expression {
    private final List<Construct> elements = new ArrayList<Construct>();
    private final Pattern pattern;
    private final String match;

    public Expression(Pattern pattern, String match) {
        this.pattern = pattern;
        this.match = match;
    }

    public void addConstruct(Construct construct) {
        elements.add(construct);
    }

    public void addAll(Expression expression) {
        elements.addAll(expression.getElements());
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getMatch() {
        return match;
    }

    private List<Construct> getElements() {
        return elements;
    }
}
