package Model.Expression;

import Model.Constructs.*;
import Model.Matching.Fragment;
import Model.Matching.InClassMatching;
import Model.Matching.Matching;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Expression implements Iterable<Construct>{
    private String pattern;
    private Sequence sequence;
    private Matching matching;
    private InClassMatching classMatching;
    private String selectedMatch = "";
    private Map<Construct,Fragment> directMatch;


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
            directMatch = new LinkedHashMap<>();
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

    public List<Fragment> getMatch(int group) {
        return matching.getMatches(group);
    }

    public String getGroupID(int group) {
        return groupsNames.get(group);
    }

    public int groupCount() {
        return matching.groupCount();
    }
    /*
    public void getSeparateConstructsMatches(String matched) {
        getSeparateConstructsMatches(matched, sequence);
    }

    public void getSeparateConstructsMatches(Fragment matched) {
        String m = matching.getTestString().substring(matched.getStart(),matched.getEnd());
        getSeparateConstructsMatches(m, sequence);
    }

    public void getSeparateConstructsMatches(String matched, Sequence sequence) {
           for(Construct construct : sequence) {
               if(Construct.isComposed(construct) && construct.getType() != Type.CHAR_CLASS) {
                   getSeparateConstructsMatches(matched, (Sequence) construct);
               } else if (construct.getType() == Type.QUANTIFIER) {
                   Construct interior = ((Quantifier) construct).getConstruct();
                   construct.getCurrentMatch();
                   if(Construct.isComposed(interior)) {
                       getSeparateConstructsMatches(matched,(Sequence)interior);
                   } else {
                       interior.getCurrentMatch();
                   }
               } else if(construct.getType() != Type.COMPONENT) {
                   construct.getCurrentMatch();
               }
           }
    }      */

    public void setGlobalMode(boolean mode) {
        matching.setGlobalMode(mode);
    }

    /** throws null if didn't find any */
    public Fragment getMatchByIndex(int index) {
        try {
            if (matching.getMatchByIndex(index) != null) {
                Fragment selected = matching.getMatchByIndex(index);
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

    public void getConstructsDirectMatch(String testString) {
        getConstructsDirectMatch(sequence,testString);
    }
    public void getConstructsDirectMatch(Sequence sequence, String text) {
        for(Construct construct : sequence) {
            getDMatch(construct, text);
        }
    }

    public void getDMatch(Construct construct, String test){

            directMatch(construct, test);
        try {
            if (Construct.isComposed(construct)) {
                getConstructsDirectMatch((Sequence) construct, construct.getCurrentMatch().getFragment());
            } else if (construct instanceof Quantifier) {
                Construct interior = ((Quantifier) construct).getConstruct();
                System.out.println(interior);
                getCMatch(interior, construct.getCurrentMatch().getFragment());
                if (Construct.isComposed(interior)) {
                    getDMatch(interior, interior.getCurrentMatch().getFragment());
                }
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("nuuuuul");
        }
    }

    public void getCMatch(Construct construct, String test) {
        Matcher matcher;
        int i = 0;
        System.out.println("!!!" + test);
        try {
            matcher = Pattern.compile(construct.toString()).matcher(test.substring(i));
            if(matcher.lookingAt()) {
                System.out.println(matcher.group() + "," + matcher.start() + "," + matcher.end() + "," + matcher.pattern());
                construct.setCurrentMatch(new Fragment(matcher.start(),matcher.end(),matcher.group()));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }



    public String getAsSeparateGroup(Construct construct, String groupName) {
        String begin = pattern.substring(sequence.getStart(),construct.getStart());
        String mid = pattern.substring(construct.getStart(),construct.getEnd());
        String finish = pattern.substring(construct.getEnd());
        String result = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        System.out.println(result);
        return result;
    }

    public void directMatch(Construct construct, String match) {
        try {
            Matcher matcher = Pattern.compile(getAsSeparateGroup(construct,"test")).matcher(match);

            if (matcher.find()) {
                construct.setCurrentMatch(new Fragment(matcher.start("test"), matcher.end("test"),matcher.group()));
            } else {
                construct.setCurrentMatch(new Fragment(0, 0,""));
            }
        } catch (PatternSyntaxException e) {
            construct.setCurrentMatch(new Fragment(0, 0,""));
        }

    }
}
