package Model.Constructs;

import Model.Constructs.Types.Composition;
import Model.Constructs.Types.Quantifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpressionBuilder  implements Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    public Composition divideIntoConstructs(Composition container, String pattern, int start, int end) {
        int i = start;
        Construct construct = null;
        boolean alternative = false;
        while (i < end) {

            construct = crateNewConstruct(container, pattern, i);

            System.out.println("construct: " + construct.getType());
            System.out.println("container:" + ((Construct)container).getType());

            if (isGroup(construct)) {
                construct = (Construct) divideIntoConstructs((Composition) construct, pattern, ((Composition) construct).getInteriorStart(), ((Composition) construct).getInteriorEnd());
                System.out.println("in");
                container.addConstruct(construct);
            } else if (construct instanceof Quantifier) {
                ((Composition) container).absorbLast((Quantifier) construct);
            } else {
                container.addConstruct(construct);
            }
            i += construct.size() == 0 ? 1 : construct.size();
        }

        return container;
    }

    public Construct crateNewConstruct(Composition container, String pattern, int i) {
        return ((Construct)container).getType() == Type.CHAR_CLASS ? ConstructsFactory.getInstance().createConstructInCharClass(pattern, i) :
                ConstructsFactory.getInstance().createConstruct(pattern, i);
    }

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }
}
