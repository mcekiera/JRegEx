package Expression;

import Constructs.Construct;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Expression {
    private final List<Construct> elements = new ArrayList<Construct>();
    private final Pattern pattern;

    public Expression(Pattern pattern) {
        this.pattern = pattern;
    }

    public void addConstruct(Construct construct) {
        elements.add(construct);
    }

    public void addAllConstructs(Expression expression) {
        elements.addAll(expression.getElements());
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getPatternAsString(int startIndex) {
        return pattern.toString().substring(startIndex);
    }

    private List<Construct> getElements() {
        return elements;
    }
}
