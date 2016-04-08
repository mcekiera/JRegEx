package Model.Expression;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Constructs.SequenceBuilder;
import Model.Constructs.Type;
import Model.Matching.Matched;
import Model.Matching.Matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Expression implements Iterable<Construct>{
    private String pattern;
    private Sequence sequence;
    private Matching matching;


    private Map<String,Sequence> groups;
    private List<String> groupsNames;


    private final SequenceBuilder cBuilder = SequenceBuilder.getInstance();

    public boolean use(String pattern, String testString) {
        try {
            Pattern test = Pattern.compile(pattern);
            this.pattern = pattern;
            this.sequence = cBuilder.toComposition(pattern, Type.EXPRESSION);
            this.matching = new Matching(pattern, testString);
            this.groups = cBuilder.getGroups();
            this.groupsNames = new ArrayList<>(groups.keySet());
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public String getPattern() {
        return pattern;
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

    public void getSeparateConstructsMatches(String matched, Sequence sequence) {
           for(Construct construct : sequence) {
               if(construct instanceof Sequence && construct.getType() != Type.CHAR_CLASS) {
                   getSeparateConstructsMatches(matched, (Sequence) construct);
               } else if (construct.getType() == Type.QUANTIFIER) {
                   //currentMatching.add(((Quantifier)construct).getConstruct().getCurrentMatch(matched));
                   construct.getCurrentMatch(matched);
               } else if(construct.getType() != Type.COMPONENT) {
                   construct.getCurrentMatch(matched);
               }
           }
    }

    public void setGlobalMode(boolean mode) {
        matching.setGlobalMode(mode);
    }


    @Override
    public Iterator<Construct> iterator() {
        return sequence.iterator();
    }
}
