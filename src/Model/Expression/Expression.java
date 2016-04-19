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
        getConstructsDirectMatch(sequence,testString, 0);
    }
    public void getConstructsDirectMatch(Sequence sequence, String text, int corr) {
        for(Construct construct : sequence) {
            if(construct.getType() != Type.COMPONENT)   getDMatch(construct, text, corr);
        }
    }

    public void getDMatch(Construct construct, String test, int corr){
        directMatch(construct, test, corr);
        System.out.println(construct.getCurrentMatch().toString());
        try {
            if (Construct.isComposed(construct) && construct.getType() != Type.CHAR_CLASS) {
                getConstructsDirectMatch((Sequence) construct, construct.getCurrentMatch().getFragment(), construct.getCurrentMatchStart());
            } else if (construct instanceof Quantifier) {

                Construct interior = ((Quantifier) construct).getConstruct();
                getCMatch(interior, construct.getCurrentMatch().getFragment(),construct.getCurrentMatchStart());
                if (Construct.isComposed(interior)) {
                    getDMatch(interior, interior.getCurrentMatch().getFragment(),construct.getCurrentMatchStart());
                }
            } else if (construct.getType() == Type.CHAR_CLASS) {
                new InClassMatching().setSequence((Sequence)construct,construct.getCurrentMatch().getFragment());
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("nuuuuul");
        }
    }

    public void getCMatch(Construct construct, String test, int correction) {
        Matcher matcher;
        System.out.println("!!!" + test);
        try {
            matcher = Pattern.compile(construct.toString()).matcher(test);
            if(matcher.lookingAt()) {
                System.out.println(matcher.group() + "," + matcher.start() + "," + matcher.end() + "," + matcher.pattern());
                construct.setCurrentMatch(new Fragment(correction+matcher.start(),correction+matcher.end(),matcher.group()));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            System.out.println("CM null");
        }
    }



    public String getAsSeparateGroup(Construct construct, String groupName) {
        Construct parent = construct.getParent();
        String regex = construct.getParent().toString();
        String begin = regex.substring(sequence.getStart(),construct.getStart()-parent.getStart());
        String mid = regex.substring(construct.getStart()-parent.getStart(),construct.getEnd()-parent.getStart());
        String finish = regex.substring(construct.getEnd()-parent.getStart());
        String result = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        System.out.println(result);
        return result;
    }
           //TODO   WYMIESZANIE START POITÓW
    public void directMatch(Construct construct, String match, int correct) {
        try {
            Matcher matcher = Pattern.compile(getAsSeparateGroup(construct,"test")).matcher(match);

            if (matcher.find()) {
                construct.setCurrentMatch(new Fragment(correct+matcher.start("test"), correct+matcher.end("test"),matcher.group("test")));
            } else {
                construct.setCurrentMatch(new Fragment(0, 0,""));
            }
        } catch (PatternSyntaxException e) {
            construct.setCurrentMatch(new Fragment(0, 0,""));
        }

    }


}
