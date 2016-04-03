package Model.Constructs;

import java.util.ArrayList;
import java.util.List;

public class CompositionBuilder {
    private static final CompositionBuilder INSTANCE = new CompositionBuilder();
    private List<Composition> groups;

    private CompositionBuilder(){}

    public static CompositionBuilder getInstance() {
        return INSTANCE;
    }

    public Composition toComposition(String pattern, Type type) {
        Composition composition = new Composition(type,pattern,0,pattern.length());
        groups = new ArrayList<Composition>();
        divideIntoConstructs(composition);
        return composition;
    }

    private Composition divideIntoConstructs(Composition container) {
        int i = container.getInteriorStart();
        addGroup(container);
        Construct construct;
        while (i < container.getInteriorEnd()) {

            construct = crateNewConstruct(container, container.getPattern(), i);

            if (isGroup(construct)) {

                construct = (divideIntoConstructs((Composition) construct));
                container.addConstruct(construct);
            } else if (construct instanceof Quantifier) {
                (container).absorbLast((Quantifier) construct);
            } else {
                container.addConstruct(construct);
            }
            i += construct.size() == 0 ? 1 : construct.size();
        }

        return container;
    }

    private Construct crateNewConstruct(Composition container, String pattern, int i) {
        Construct construct = (container).getType() == Type.CHAR_CLASS ? ConstructsFactory.getInstance().createConstructInCharClass(pattern, i) :
                ConstructsFactory.getInstance().createConstruct(pattern, i);
        construct.setParent(container);
        return construct;
    }

    private void addGroup(Composition composition) {
        if(composition.getType() == Type.EXPRESSION || composition.getType() == Type.CAPTURING) {
                groups.add(composition);
        }
    }

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }

    public List<Composition> getGroups() {
        return groups;
    }

    public int groupCount() {
        return groups.size();
    }
}
