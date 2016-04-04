package Model.Constructs;

import Model.Constructs.Lib.MatcherLib;

import java.util.*;

public class CompositionBuilder {
    private static final CompositionBuilder INSTANCE = new CompositionBuilder();
    private Map<String,Composition> groups;
    private int count;

    private CompositionBuilder(){}

    public static CompositionBuilder getInstance() {
        return INSTANCE;
    }

    public Composition toComposition(String pattern, Type type) {
        Composition composition = new Composition(type,pattern,0,pattern.length());
        groups = new LinkedHashMap<>();
        count = 0;
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
                groups.put((MatcherLib.getInstance().getGroup(Type.CAPTURING,"name") == null ?
                        Integer.toString(count) : MatcherLib.getInstance().getGroup(Type.CAPTURING,"name")),composition);
            count++;
        }
    }

    private boolean isGroup(Construct construct) {
        return (construct.getType() == Type.ATOMIC || construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING || construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.CHAR_CLASS);
    }

    public Map<String,Composition> getGroups() {
        return groups;
    }

    public int groupCount() {
        return groups.size()-1;
    }

    public List<String> getGroupsId() {
        return new ArrayList<String>(groups.keySet());
    }
}
