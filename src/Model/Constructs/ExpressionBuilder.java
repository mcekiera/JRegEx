package Model.Constructs;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.ConstructsFactory;
import Model.Constructs.Type;
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

            System.out.println("construct: " + construct.getType());
            System.out.println("container:" + ((Construct)container).getType());

            if (isGroup(construct)) {
                construct = (Construct) divideIntoConstructs((Complex) construct, pattern, ((Complex) construct).getInteriorStart(), ((Complex) construct).getInteriorEnd());
                System.out.println("in");
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
        return ((Construct)container).getType() == Type.CHAR_CLASS ? ConstructsFactory.getInstance().createConstructInCharClass(pattern, i) :
                ConstructsFactory.getInstance().createConstruct(pattern, i);
    }

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }
}
