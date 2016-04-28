package Re.Regex.Type;

import Model.Constructs.Type;
import Re.Regex.*;
import Re.Segment;

public class CompositeBuilder {
    public static final CompositeBuilder INSTANCE = new CompositeBuilder();
    public final ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();
    private Construct previous;

    private CompositeBuilder() {
    }

    public static CompositeBuilder getInstance() {
        return INSTANCE;
    }

    public Composite toComposite(String pattern) {
        Composite composite = new Composite(null, Type.EXPRESSION,new Segment(pattern,0,pattern.length(),0));
        breakExpression(composite);
        return composite;
    }

    private void breakExpression(Composite container) {
        int index = container.getStart();
        Construct construct;
        while(index < container.getEnd()) {
            construct = factory.createConstruct(container,container.getPattern(),index);
            System.out.println(construct.getType() + "," + index);
            processConstruct(container,construct);
            index += construct.length();
            previous = construct;
        }
    }

    private void processConstruct(Composite container,Construct construct) {
        if (construct instanceof Single) {
            process(container,(Single)construct);
        } else if (construct instanceof Quantifier) {
            process(container,(Quantifier)construct);
        } else {
            process(container,(Composite)construct);
        }
    }

    private void process(Composite container,Single single) {
        container.addConstruct(single);
    }

    private void process(Composite container,Quantifier quantifier) {
            if(ifPreviousEmptyOrQuantifier()) {
                if(quantifier.getType() == Type.INTERVAL) {
                    addEmpty(container,quantifier);
                } else {
                    addError(container,quantifier);
                }
            } else {
                container.addConstruct(quantifier, previous);
            }
    }

    private void addEmpty(Composite container, Quantifier quantifier) {
        Construct empty = new Single(container, Type.ERROR,
                new Segment(quantifier.getPattern(), quantifier.getStart(), quantifier.getStart(),quantifier.getStart()));
        container.addConstruct(empty);                                                              //TODO: creation should be only in ConstructFactory
        container.addConstruct(quantifier, empty);
    }

    private void addError(Composite container, Quantifier quantifier) {
        container.addConstruct(new Single(container,Type.ERROR,
                new Segment(quantifier.getPattern(),quantifier.getStart(),quantifier.getEnd(),quantifier.getStart())));
    }

    private boolean ifPreviousEmptyOrQuantifier() {
        return previous == null || previous instanceof Quantifier;
    }

    private void process(Composite container,Composite composite) {
        container.addConstruct(composite);
        breakExpression(composite);
    }



}
