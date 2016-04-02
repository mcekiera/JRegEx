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

    public Complex divideIntoConstructs(Complex container, String pattern, int start, int end) {
        int i = start;
        Construct construct = null;
        boolean alternative = false;
        while (i < end) {

            construct = crateNewConstruct(container, pattern, i);

            System.out.println("construct: " + construct.toString());
            //System.out.println("container:" + container.getClass().getName());

            if (construct instanceof Complex) {
                construct = (Construct) divideIntoConstructs((Complex) construct, pattern, ((Complex) construct).getInteriorStart(), ((Complex) construct).getInteriorEnd());
                container.addConstruct(construct);
            } else if (construct instanceof Quantifiable) {
                ((Reversible) container).absorbLast((Quantifiable) construct);
            } else {
                container.addConstruct(construct);
            }
            i += construct.size() == 0 ? 1 : construct.size();
        }

        return container;
    }

    public Construct crateNewConstruct(Complex container, String pattern, int i) {
        return container instanceof CharClass ? ConstructsFactory.getInstance().createConstructInCharClass(pattern, i) :
                ConstructsFactory.getInstance().createConstruct(pattern, i);
    }
}
