package Re.Regex;

import Re.Type;
import Re.Segment;

import java.util.HashMap;
import java.util.Map;

public class CompositeBuilder {
    public static final CompositeBuilder INSTANCE = new CompositeBuilder();
    public final ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();
    private Construct previous;
    private Map<Integer,Construct> groups;
    private Map<Integer,String> names;
    private int currentGroup;

    private CompositeBuilder() {
    }

    public static CompositeBuilder getInstance() {
        return INSTANCE;
    }

    public Composite toComposite(String pattern) {
        reset();
        Composite composite = new Composite(null, Type.EXPRESSION,new Segment(pattern,0,pattern.length()));
        breakExpression(composite);
        return composite;
    }

    /**
     * It returns a Map object, containing those Constructs object, which are counted as capturing groups within
     * given regular expression. Key is a ordinal number of group, including 0 group - the whole pattern. The
     * map includes also named capturing group, but available by integer, not name String.
     * @return Map<Integer,Construct>
     */
    public Map<Integer,Construct> getGroups() {
        return groups;
    }

    /**
     * It returns a Map object, containing String with name of named capturing groups identified within
     * given regular expression. The key is a ordinal number of capturing group with name.
     * @return Map<Integer,Construct>
     */
    public Map<Integer,String> getNames() {
        return names;
    }

    private void reset() {
        currentGroup = 0;
        groups = new HashMap<>();
        names = new HashMap<>();
    }

    private void breakExpression(Composite container) {
        groupCheck(container);
        int index = container.getStart();
        Construct construct;
        while(index < container.getEnd()) {
            construct = factory.createConstruct(container,container.getPattern(),index);
            processConstruct(container,construct);
            index += construct.length();
            previous = construct;
        }
    }

    private void groupCheck(Composite composite) {
        if(isGroup(composite)) {
            addGroup(composite);
            if(isNamed(composite)) {
                addName(composite);
            }
        }
    }

    private void addGroup(Construct construct) {
        groups.put(currentGroup,construct);
    }

    private void addName(Composite composite) {
        String name = composite.toString().substring(composite.toString().indexOf('<') + 1,
                composite.toString().indexOf('>'));
        names.put(currentGroup,name);
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


    private void process(Composite container,Composite composite) {
        container.addConstruct(composite);
        breakExpression(composite);
    }

    private void addEmpty(Composite container, Quantifier quantifier) {
        Construct empty = new Single(container, Type.ERROR,
                new Segment(quantifier.getPattern(), quantifier.getStart(), quantifier.getStart()));
        container.addConstruct(empty);                                                              //TODO: creation should be only in ConstructFactory
        container.addConstruct(quantifier, empty);
    }

    private void addError(Composite container, Quantifier quantifier) {
        container.addConstruct(new Single(container,Type.ERROR,
                new Segment(quantifier.getPattern(),quantifier.getStart(),quantifier.getEnd())));
    }

    private boolean ifPreviousEmptyOrQuantifier() {
        return previous == null || previous instanceof Quantifier;
    }

    private boolean isGroup(Composite composite) {
        return composite.getType() ==Type.CAPTURING;
    }

    private boolean isNamed(Composite composite) {
        return composite.toString().matches("\\(\\?\\<[^>]+\\>.+");
    }



}
