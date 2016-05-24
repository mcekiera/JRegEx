package Model.Regex;

import Model.Lib.DescLib;
import Model.Segment;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class. Breaks String with regular expression into sequence of Construct objects, representing separate
 * pattern elements. In the process it also retrieves fragments of pattern that contains groups, and String with names
 * of named capturing groups.
 */

public class CompositeBuilder {
    /**
     * Only instance of class.
     */
    public static final CompositeBuilder INSTANCE = new CompositeBuilder();
    /**
     * Instance of factory class for Construct objects creation.
     */
    public final ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();
    /**
     * Previously created Construct object.
     */
    private Construct previous;
    /**
     * Map containing retrieved fragments with grouping constructs.
     */
    private Map<Integer,Construct> groups;
    /**
     * Map containing retrieved names of named capturing groups.
     */
    private Map<Integer,String> names;
    /**
     * Integer representing depth level of currently processing grouping construct.
     */
    private int currentGroup;
    /**
     * Flag variable, determines if given pattern is processed as valid or invalid regular expression.
     */
    private boolean valid;

    /**
     * @return only instance of class.
     */
    public static CompositeBuilder getInstance() {
        return INSTANCE;
    }

    /**
     * Creates Composite object containing Construct objects identified from given String.
     * @param pattern regular expression to break into separate constructs.
     * @return Composite object.
     */
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

    /**
     * Determines, if currently processed regular expression is valid.
     * @return true, if it is valid.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Removes data about previously processes regular expression from fields.
     */
    private void reset() {
        valid = true;
        currentGroup = 0;
        groups = new HashMap<>();
        names = new HashMap<>();
        previous = null;
    }

    /**
     * Recognizes separate regular expression withing given Composite object, which represents more complex construct.
     * It could start from any index of given String pattern, so it can process whole patterns and its elements.
     * @param container Object representing complex regular expression element to break into basic constructs.
     * @param index index ot pattern String to start from.
     */
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

    /**
     * Retrieve data about capturing groups and named capturing groups if present.
     * @param composite object to could contain group.
     */
    private void groupCheck(Composite composite) {
        if(isGroup(composite)) {
            currentGroup++;
            addGroup(composite);
            if(isNamed(composite)) {
                addName(composite);
            }
        }
    }

    /**
     * Examine, if given element represents error in regular expression. If so, it change whole pattern status to
     * invalid, and it is processed in that manner further.
     * @param construct object to examine, if represents error.
     */
    private void validityCheck(Construct construct) {
         if(isError(construct)) {
             valid = false;
         }

    }

    /**
     * Adds retrieved group to collection of groups.
     * @param construct representing group.
     */
    private void addGroup(Construct construct) {
        groups.put(currentGroup,construct);
    }

    /**
     * Retrieves and adds retrieved name to collection of names of capturing groups.
     * @param composite object representing named capturing group, from which name is retrieved.
     */
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

    /**
     * Process simple construct.
     * @param container Complex regular expression construct.
     * @param single Separate construct.
     */
    private void process(Composite container,Single single) {
        container.addConstruct(single);
    }

    /**
     * Process given construct. Overridden method for processing constructs representing quantifiers.
     * @param container Complex regular expression construct.
     * @param quantifier Separate quantifier construct.
     */
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

    /**
     * Process given construct. Overridden method for processing constructs representing grouping constructs,
     * look around constructs.
     * @param container Complex regular expression construct.
     * @param composite Separate composite construct.
     */
    private void process(Composite container,Composite composite) {
        container.addConstruct(composite);
        breakExpression(composite, composite.getStart());
    }

    /**
     * Java regular expression allows specific situation of multiple intervals, one after another. However, just first
     * interval has content, every consecutive is empty, and multiply nothing, and in effect always match. This effect
     * is kept for integrity with Java Pattern class behaviour.
     * It adds empty Construct into intervals constructs which follow another interval/quantifier.
     */
    private void addEmpty(Composite container, Quantifier quantifier) {
        Construct empty = new Single(container, Type.INTERVAL,
                new Segment(quantifier.getPattern(), quantifier.getStart(), quantifier.getStart()));
        container.addConstruct(empty);
        container.addConstruct(quantifier, empty, empty);
    }

    /**
     * Adds construct representing error in regular expression, instead of quantifier, if it is next consecutive
     * quantifier.
     * @param container Composite object, which quantifier is element of.
     * @param quantifier separate quantifier construct.
     */
    private void addError(Composite container, Quantifier quantifier) {
        Construct construct = new Single(container,Type.INVALID_QUANTIFIER,
                new Segment(container.getPattern(),quantifier.getStart(),quantifier.getEnd()));
        container.addConstruct(construct);
        construct.setDescription(DescLib.getInstance().getDescription(construct));
        valid = false;

    }

    /**
     * Determines, if previous construct is proper for placing inside quantifier.
     * @return true, if it is not proper for placing inside quantifier.
     */
    private boolean ifPreviousEmptyOrQuantifier() {
        return previous == null || previous instanceof Quantifier;
    }

    /**
     * Determines if given Composite object represents capturing group construct.
     * @param composite Object to examine.
     * @return true, if object represents capturing group.
     */
    private boolean isGroup(Composite composite) {
        return composite.getType() == Type.CAPTURING;
    }

    /**
     * Determines if given Composite object represents named capturing group.
     * @param composite Object to examine.
     * @return true, if object represents named capturing group.
     */
    private boolean isNamed(Composite composite) {
        return composite.getText().matches("\\(\\?<[^>]+>.+");
    }

    /**
     * Determines if given Construct type represents error in regular expression.
     * @param construct Object to examine.
     * @return true, if object type represents error.
     */
    private boolean isError(Construct construct) {
        return construct.getType() == Type.INCOMPLETE ||
                construct.getType() == Type.UNBALANCED ||
                construct.getType() == Type.INVALID_BACKREFERENCE ||
                construct.getType() == Type.INVALID_INTERVAL ||
                construct.getType() == Type.INVALID_RANGE ||
                construct.getType() == Type.INVALID_QUANTIFIER;
    }
}
