package Model;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.ConstructsFactory;
import Model.Constructs.Types.CharClass;
import Model.Constructs.Types.Quantifiable.Quantifiable;
import Model.Constructs.Types.Reversible;

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
        Complex current;
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


    public Expression create(String pattern)  {
        int i = 0;
        Construct construct;
        Expression expression = new Expression(pattern);
        Complex container = expression;

        while(i < pattern.length()) {

            construct = container instanceof CharClass ? ConstructsFactory.getInstance().createConstructInCharClass(pattern,i) :
                    ConstructsFactory.getInstance().createConstruct(pattern, i);

            if(construct instanceof Quantifiable) {
                ((Reversible)container).absorbLast((Quantifiable) construct);
            }


            container.addConstruct(construct);
            i = construct.getEnd();
            System.out.println(construct.getClass().getName());


            if(construct instanceof Complex) {
                construct.setParent(container);
                container = (Complex)construct;
                i = container.getInteriorStart();
            }



            if(container instanceof Expression && i == container.getInteriorEnd()){
                return expression;
            }

            if(i == container.getInteriorEnd()) {
                i = ((Construct)container).getEnd();
                container = ((Construct)container).getParent();

            }

        }
        return expression;
    }
}
