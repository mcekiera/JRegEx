package Model.Regex;

import Model.Lib.DescLib;
import Model.Segment;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO pomys� na commenty: utowrzenie nowej factory method, dla construct�w tw�rzonych w modzie ?x - je�eli ?x w��czone
 * do # u�ywa normalnego a p�niej ucina pattern
 */

public class CompositeBuilder {
    public static final CompositeBuilder INSTANCE = new CompositeBuilder();
    public final ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();
    private Construct previous;
    private Map<Integer,Construct> groups;
    private Map<Integer,String> names;
    private int currentGroup;
    private boolean valid;

    public static CompositeBuilder getInstance() {
        return INSTANCE;
    }

    public Composite toComposite(String pattern) {
        reset();
        Composite composite = new Composite(null, Type.EXPRESSION,new Segment(pattern,0,pattern.length()));
        composite.setDescription(DescLib.getInstance().getDescription(composite));
        breakExpression(composite, composite.getStart());
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
     * @return Map
     */
    public Map<Integer,String> getNames() {
        return names;
    }

    public boolean isValid() {
        return valid;
    }

    private void reset() {
        valid = true;
        currentGroup = 0;
        groups = new HashMap<>();
        names = new HashMap<>();
        previous = null;
    }

    private void breakExpression(Composite container, int index) {
        groupCheck(container);
        Construct construct;
        int end = container.getEnd();
        while(index < end) {
            construct = factory.createConstruct(container,container.getPattern(),index);

            if(previous != null && previous.equals(construct)) {
                break;
            }

            if((!construct.isComplex()) && index == container.getStart() && construct.getText().equals("|")){
                container.addConstruct(factory.createEmptyAlternative(container, container.getPattern(), index));
            }
            validityCheck(construct);       //TODO:bez invalid quantifier!!
            processConstruct(container, construct);
            construct.setDescription(DescLib.getInstance().getDescription(construct));
            index += construct.length();
            previous = construct;
        }
        if(previous != null && previous.getText().equals("|")) {
            container.addConstruct(factory.createEmptyAlternative(container, container.getPattern(), index));
        }
    }

    private void groupCheck(Composite composite) {
        if(isGroup(composite)) {
            currentGroup++;
            addGroup(composite);
            if(isNamed(composite)) {
                addName(composite);
            }
        }
    }

    private void validityCheck(Construct construct) {
         if(isError(construct)) {
             valid = false;
         }

    }

    private void addGroup(Construct construct) {
        groups.put(currentGroup,construct);
    }

    private void addName(Composite composite) {
        String name = composite.getText().substring(composite.getText().indexOf('<') + 1,
                composite.getText().indexOf('>'));
        names.put(currentGroup,name);
    }

    private void processConstruct(Composite container,Construct construct) {
        if (construct instanceof Single) {
            process(container, (Single) construct);
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
                    addError(container, quantifier);
                    System.out.println("Invalid quantifier");
                }
            } else {
                Construct replace;
                if(previous instanceof Composite) {
                    replace = new Composite(quantifier,previous.getType(),new Segment(previous.getPattern(),previous.getStart(),previous.getEnd()));
                    for(Construct c : (Composite)previous) {
                        ((Complex)replace).addConstruct(c);
                    }
                } else {
                    replace = new Single(quantifier,previous.getType(),new Segment(previous.getPattern(),previous.getStart(),previous.getEnd()));
                }
                replace.setDescription(DescLib.getInstance().getDescription(replace));
                container.addConstruct(quantifier, previous,replace);
            }
    }


    private void process(Composite container,Composite composite) {
        container.addConstruct(composite);
        breakExpression(composite, composite.getStart());
    }

    private void addEmpty(Composite container, Quantifier quantifier) {
        Construct empty = new Single(container, Type.INTERVAL,
                new Segment(quantifier.getPattern(), quantifier.getStart(), quantifier.getStart()));
        container.addConstruct(empty);
        container.addConstruct(quantifier, empty, empty);
    }

    private void addError(Composite container, Quantifier quantifier) {
        Construct construct = new Single(container,Type.INVALID_QUANTIFIER,
                new Segment(container.getPattern(),quantifier.getStart(),quantifier.getEnd()));
        container.addConstruct(construct);
        construct.setDescription(DescLib.getInstance().getDescription(construct));
        valid = false;

    }

    private boolean ifPreviousEmptyOrQuantifier() {
        return previous == null || previous instanceof Quantifier;
    }

    private boolean isGroup(Composite composite) {
        return composite.getType() == Type.CAPTURING;
    }

    private boolean isNamed(Composite composite) {
        return composite.getText().matches("\\(\\?<[^>]+>.+");
    }

    private boolean isError(Construct construct) {
        return construct.getType() == Type.INCOMPLETE ||
                construct.getType() == Type.UNBALANCED ||
                construct.getType() == Type.INVALID_BACKREFERENCE ||
                construct.getType() == Type.INVALID_INTERVAL ||
                construct.getType() == Type.INVALID_RANGE ||
                construct.getType() == Type.INVALID_QUANTIFIER;
    }
}
