package Model.Constructs;

import Lib.MatcherLib;

import java.util.*;

public class SequenceBuilder {
    private static final SequenceBuilder INSTANCE = new SequenceBuilder();
    private Map<String,Sequence> groups;
    private int count;

    private SequenceBuilder(){}

    public static SequenceBuilder getInstance() {
        return INSTANCE;
    }

    public Sequence toComposition(String pattern, Type type) {
        Sequence sequence = new Sequence(type,pattern,0,pattern.length());
        groups = new LinkedHashMap<>();
        count = 0;
        divideIntoConstructs(sequence);
        return sequence;
    }

    private Sequence divideIntoConstructs(Sequence container) {
        int i = container.getInteriorStart();
        addGroup(container);
        Construct construct;
        Construct previous = null;
        while (i < container.getInteriorEnd()) {

            construct = crateNewConstruct(container, container.getPattern(), i);
            if (isGroup(construct)) {

                construct = (divideIntoConstructs((Sequence) construct));
                container.addConstruct(construct);
            } else if(isQuantifier(construct)) {

                if(isValidQuantifier(construct, previous)) {
                    if(previous == null || previous.getType() == Type.QUANTIFIER || previous.getType() == Type.INTERVAL) {
                        ((Quantifier)construct).setConstruct(new Singular(Type.SIMPLE, container.getPattern(), i, i));
                        container.addConstruct(construct);
                    } else {
                        ((Quantifier)construct).setConstruct(previous);
                        container.removeLastAdded();
                        container.addConstruct(construct);
                    }
                } else {
                    container.addConstruct(new Error(Type.ERROR,container.getPattern(),i,construct.getEnd()));
                }

            } else {
                container.addConstruct(construct);
            }
            i += construct.size() == 0 ? 1 : construct.size();
            construct.setParent(container);
            previous = construct;
        }

        return container;
    }

    private Construct crateNewConstruct(Sequence container, String pattern, int i) {
        Construct construct = (container).getType() == Type.CHAR_CLASS ? ConstructsFactory.getInstance().createConstructInCharClass(pattern, i) :
                ConstructsFactory.getInstance().createConstruct(pattern, i);
        construct.setParent(container);
        return construct;
    }

    private void addGroup(Sequence sequence) {
        if(sequence.getType() == Type.EXPRESSION || sequence.getType() == Type.CAPTURING) {
                groups.put((MatcherLib.getInstance().getGroup(Type.CAPTURING,"name") == null ?
                        Integer.toString(count) : MatcherLib.getInstance().getGroup(Type.CAPTURING,"name")), sequence);
            count++;
        }
    }

    private boolean isValidQuantifier(Construct quantifier, Construct previous) {
        if(quantifier.getType() == Type.QUANTIFIER) {
              if(previous == null || previous.getType() == Type.QUANTIFIER || previous.getType() == Type.INTERVAL) {
                  return false;
              } else {
                  return true;
              }
        } else {
              return true;
        }
    }

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }

    private boolean isQuantifier(Construct construct) {
        return (construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL);
    }

    public Map<String,Sequence> getGroups() {
        return groups;
    }

    public int groupCount() {
        return groups.size()-1;
    }

    public List<String> getGroupsId() {
        return new ArrayList<String>(groups.keySet());
    }
}
