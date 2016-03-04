package Analysis;

import Constructs.Construct;
import Constructs.ConstructsFactory;
import Expression.Expression;

public class Analyzer {
    private Expression expression;
    private ConstructsFactory factory = ConstructsFactory.getInstance();

    public Expression analyze(Expression expression) {
        int index = 0;
        while(index != expression.getPattern().toString().length()) {
            Construct construct = factory.create(expression,index);
            expression.addConstruct(construct);
            index += construct.size();
        }
        return expression;
    }


}
