package Model.Expression;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Constructs.SequenceBuilder;
import Model.Constructs.Type;
//import Model.Matching.DirectMatch;
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
           //new DirectMatch(sequence,matched);
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
