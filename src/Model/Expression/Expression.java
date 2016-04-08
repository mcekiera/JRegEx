package Model.Expression;

import Model.Constructs.Composition;
import Model.Constructs.CompositionBuilder;
import Model.Constructs.Construct;
import Model.Constructs.Type;
import Model.Matching.Matched;
import Model.Matching.Matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Expression implements Iterable<Construct>{
    private String pattern;
    private Composition composition;
    private Map<String,Composition> groups;
    private List<String> groupsNames;
    private List<Matched> currentMatching;
    private Matching matching;

    private final CompositionBuilder cBuilder = CompositionBuilder.getInstance();

    public Expression(){
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        Composition composition = cBuilder.toComposition(pattern, Type.EXPRESSION);
        this.composition = composition;
        setGroups(cBuilder.getGroups());
    }

    public String getPattern() {
        return pattern;
    }

    public void setMatch(String match) {
        matching = new Matching(pattern,match);
    }

    public List<Matched> getMatch(int group) {
        return matching.getMatches(group);
    }

    public void setGroups(Map<String,Composition> groups) {
        this.groups = groups;
        this.groupsNames = new ArrayList<>(groups.keySet());
    }

    public int groupCount() {
        return matching.groupCount();
    }

    public void reset() {
        currentMatching = new ArrayList<>();
    }

    public void getSeparateConstructsMatches(String matched) {
        getSeparateConstructsMatches(matched,composition);

    }


    public void getSeparateConstructsMatches(String matched, Composition composition) {
           for(Construct construct : composition) {
               if(construct instanceof Composition && construct.getType() != Type.CHAR_CLASS) {
                   getSeparateConstructsMatches(matched, (Composition) construct);
               } else if (construct.getType() == Type.QUANTIFIER) {
                   //currentMatching.add(((Quantifier)construct).getConstruct().getCurrentMatch(matched));
                   construct.getCurrentMatch(matched);
               } else if(construct.getType() != Type.COMPONENT) {
                   construct.getCurrentMatch(matched);
               }
           }
    }

    public List<Matched> getCurrentMatching() {
        return currentMatching;
    }

    @Override
    public Iterator<Construct> iterator() {
        return composition.iterator();
    }
}
