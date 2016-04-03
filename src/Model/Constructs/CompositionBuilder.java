package Model.Constructs;

public class CompositionBuilder {

    public Composition toComposition(String pattern) {
        Composition composition = new Composition(Type.EXPRESSION,pattern,0,pattern.length());
        divideIntoConstructs(composition);
        return composition;
    }

    private Composition divideIntoConstructs(Composition container) {
        int i = container.getInteriorStart();
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

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }
}
