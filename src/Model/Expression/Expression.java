package Model.Expression;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Constructs.SequenceBuilder;
import Re.Type;
//import Model.GlobalMatching.DirectMatch;
import Model.Matching.GlobalMatching;
import Model.Matching.InClassMatching;
import Model.Matching.Matched;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class Expression implements Iterable<Construct>{
    private String pattern;
    private Sequence sequence;
    private GlobalMatching globalMatching;
    private InClassMatching classMatching;
    private String selectedMatch = "";


    private Map<String,Sequence> groups;
    private List<String> groupsNames;


    private final SequenceBuilder cBuilder = SequenceBuilder.getInstance();

    public boolean use(String pattern, String testString) {
        try {
            this.pattern = pattern;
            this.sequence = cBuilder.toComposition(pattern, Type.EXPRESSION);
            this.globalMatching = new GlobalMatching(pattern, testString);
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
        return globalMatching.getMatches(group);
    }

    public String getGroupID(int group) {
        return groupsNames.get(group);
    }

    public int groupCount() {
        return globalMatching.groupCount();
    }

    public void getSeparateConstructsMatches(String matched) {
        getSeparateConstructsMatches(matched, sequence);
    }

    public void getSeparateConstructsMatches(Matched matched) {
        String m = globalMatching.getTestString().substring(matched.getStart(),matched.getEnd());
        getSeparateConstructsMatches(m, sequence);
    }

    public void getSeparateConstructsMatches(String matched, Sequence sequence) {
           //new DirectMatch(sequence,matched);
    }

    public void setGlobalMode(boolean mode) {
        globalMatching.setGlobalMode(mode);
    }

    /** throws null if didn't find any */
    public Matched getMatchByIndex(int index) {
        try {
            if (globalMatching.getMatchByIndex(index) != null) {
                Matched selected = globalMatching.getMatchByIndex(index);
                selectedMatch = globalMatching.getTestString().substring(selected.getStart(), selected.getEnd());
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
