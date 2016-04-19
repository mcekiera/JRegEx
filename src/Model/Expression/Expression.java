package Model.Expression;

import Model.Constructs.*;
import Model.Matching.InClassMatching;
import Model.Matching.Matched;
import Model.Matching.Matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class Expression implements Iterable<Construct>{
    private String pattern;
    private Sequence sequence;
    private Matching matching;
    private InClassMatching classMatching;
    private String selectedMatch = "";


    private Map<String,Sequence> groups;
    private List<String> groupsNames;


    private final SequenceBuilder cBuilder = SequenceBuilder.getInstance();

    public boolean use(String pattern, String testString) {
        try {
            this.pattern = pattern;
            this.sequence = cBuilder.toComposition(pattern, Type.EXPRESSION);
            this.matching = new Matching(pattern, testString);
            this.groups = cBuilder.getGroups();
            this.groupsNames = new ArrayList<>(groups.keySet());
            return true;
        } catch (PatternSyntaxException e) {
            this.sequence = cBuilder.toComposition(pattern, Type.EXPRESSION);
            return false;
        }
    }

    public String getPattern() {
        return pattern;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public String getSelectedMatch() {
        return selectedMatch;
    }

    public List<Matched> getMatch(int group) {
        return matching.getMatches(group);
    }

    public String getGroupID(int group) {
        return groupsNames.get(group);
    }

    public int groupCount() {
        return matching.groupCount();
    }

    public void getSeparateConstructsMatches(String matched) {
        getSeparateConstructsMatches(matched, sequence);
    }

    public void getSeparateConstructsMatches(Matched matched) {
        String m = matching.getTestString().substring(matched.getStart(),matched.getEnd());
        getSeparateConstructsMatches(m, sequence);
    }

    public void getSeparateConstructsMatches(String matched, Sequence sequence) {
           for(Construct construct : sequence) {
               if(Construct.isComposed(construct) && construct.getType() != Type.CHAR_CLASS) {
                   getSeparateConstructsMatches(matched, (Sequence) construct);
               } else if (construct.getType() == Type.QUANTIFIER) {
                   Construct interior = ((Quantifier) construct).getConstruct();
                   if(Construct.isComposed(interior)) {
                       getSeparateConstructsMatches(matched,(Sequence)interior);
                   } else {
                       interior.getCurrentMatch(matched);
                   }
                   construct.getCurrentMatch(matched);
               } else if(construct.getType() != Type.COMPONENT) {
                   construct.getCurrentMatch(matched);
               }
           }
    }

    public void setGlobalMode(boolean mode) {
        matching.setGlobalMode(mode);
    }

    /** throws null if didn't find any */
    public Matched getMatchByIndex(int index) {
        try {
            if (matching.getMatchByIndex(index) != null) {
                Matched selected = matching.getMatchByIndex(index);
                selectedMatch = matching.getTestString().substring(selected.getStart(), selected.getEnd());
                return selected;
            }
            return null;
        }catch (NullPointerException e) {
            return null;
        }
    }


    @Override
    public Iterator<Construct> iterator() {
        return sequence.iterator();
    }
}
