package Model;

import Model.Constructs.Construct;
import Model.Constructs.ConstructsFactory;
import Model.Constructs.Types.Quantifiable.Quantifiable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionBuilder  implements Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    public Expression toExpression(String pattern) {
        createConstructs(pattern);
        Expression expression = new Expression(pattern);
        for(Construct construct : this) {
            expression.addConstruct(construct);
        }
        return expression;
    }

    private void createConstructs(String pattern) {
        int i = 0;
        Construct construct;
        while(i < pattern.length()) {
            construct = ConstructsFactory.getInstance().createConstruct(pattern, i);

            i += construct.size();

            if(construct instanceof Quantifiable) {
                elements.set(elements.indexOf(((Quantifiable) construct).getConstruct()),construct);
            } else {
                elements.add(construct);
            }
        }
    }
}
