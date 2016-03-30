package Model.Constructs;

import Model.Constructs.Lib.MatcherLib;
import Model.Constructs.Types.*;
import Model.Constructs.Types.Alternation.Alternation;
import Model.Constructs.Types.Error.Error;
import Model.Constructs.Types.Error.*;
import Model.Constructs.Types.Group.Capturing;
import Model.Constructs.Types.Group.LookAround;
import Model.Constructs.Types.Group.NonCapturing;
import Model.Constructs.Types.Quantifiable.Interval;
import Model.Constructs.Types.Quantifiable.Quantifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsFactory {
    private static final ConstructsFactory INSTANCE = new ConstructsFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private Construct previous;
    private String currentPattern;
    private int groupCount;

    private ConstructsFactory() {}

    public Construct createConstruct(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct = null;
        updateGroupsCount(pattern);

        if(regexMatch(Type.BOUNDARY,current)) {
            construct = new Boundary(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.BOUNDARY));
        } else if(regexMatch(Type.MODE,current)) {
            construct =  new Mode(pattern,startIndex,startIndex+lib.getMatcher(Type.MODE ).end());
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            construct = createCharacterClass(pattern,startIndex);
        } else if(regexMatch(Type.LOGICAL,current)) {
            construct =  new Alternation(pattern,startIndex,startIndex+lib.getMatcher(Type.LOGICAL).end());
        } else if(regexMatch(Type.PREDEFINED,current)) {
            construct = createPredefined(pattern,startIndex);
        }else if(regexMatch(Type.BACKREFERENCE,current)) {
            construct = createBackreference(pattern,startIndex);
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct =  new SpecificChar(pattern,startIndex,startIndex+lib.getMatcher(Type.SPECIFIC_CHAR).end());
        } else if(regexMatch(Type.QUOTATION,current)) {
            construct = createQuotation(pattern,startIndex);
        } else if(regexMatch(Type.GROUP,current)) {
            construct = createGroupConstruct(pattern, startIndex);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            construct = createQuantifierConstruct(pattern, startIndex);
        } else {
            construct = createSimpleConstruct(pattern,startIndex);
        }

        previous = construct;
        return construct;
    }

    public Construct createGroupConstruct(String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex));
        previous = null;
        if(current == null) {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new UnbalancedStructure(pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end());
        }
        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new LookAround(pattern,startIndex,startIndex+lib.getMatcher(Type.LOOK_AROUND).end());
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Mode(pattern,startIndex,startIndex+lib.getMatcher(Type.ATOMIC).end());
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new NonCapturing(pattern,startIndex,startIndex+lib.getMatcher(Type.NON_CAPTURING).end());
        } else if(regexMatch(Type.CAPTURING, current)) {
            return new Capturing(pattern,startIndex,startIndex+lib.getMatcher(Type.CAPTURING).end());
        } else {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new UnbalancedStructure(pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end());
    }
    }

    public Construct createConstructInCharClass(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if (regexMatch(Type.RANGE, current)) {
            return createRangeConstruct(pattern,startIndex);
        } else if (regexMatch(Type.PREDEFINED, current)) {
            return new Predefined(pattern,startIndex,startIndex+lib.getMatcher(Type.PREDEFINED).end());
        } else {
            regexMatch(Type.SIMPLE, current);
            return new Construct(pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end());
        }
    }

    public Construct createQuantifierConstruct(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct = null;
        if(previous == null) {
            return new Error(pattern,startIndex,startIndex + lib.getMatcher(Type.QUANTIFIER).end());
        }
        if(previous instanceof Quantifier) {
            return new Error(pattern,startIndex,startIndex + lib.getMatcher(Type.QUANTIFIER).end());
        } else if (previous instanceof Interval) {
            if (regexMatch(Type.TOKEN,current)) {
                construct = new Quantifier(pattern,startIndex,startIndex + lib.getMatcher(Type.QUANTIFIER).end());
                ((Quantifier)construct).setConstruct(previous);
            } else {
                return new Error(pattern,startIndex,startIndex + lib.getMatcher(Type.QUANTIFIER).end());
            }
        } else {
            if (regexMatch(Type.TOKEN, current)) {
                construct = new Quantifier(pattern, startIndex, startIndex + lib.getMatcher(Type.TOKEN).end());
                ((Quantifier) construct).setConstruct(previous);
            } else {
                regexMatch(Type.INTERVAL,current);
                if (isValidInterval(lib.getMatcher(Type.INTERVAL).group())) {
                    construct = new Interval(pattern, startIndex, startIndex + lib.getMatcher(Type.INTERVAL).end());
                    ((Interval) construct).setConstruct(previous);
                } else {
                    construct = new InvalidInterval(pattern, startIndex, startIndex + lib.getMatcher(Type.INTERVAL).end());
                }
            }
        }
        return construct;
    }

    private Construct createBackreference(String pattern, int startIndex) {
        if(isValidBackreference(lib.getMatcher(Type.BACKREFERENCE).group(),pattern)) {
            return new Backreference(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end());
        } else {
            return new InvalidBackreference(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end());
        }
    }

    private Construct createCharacterClass(String pattern, int startIndex) {
        if(lib.getMatcher(Type.CHAR_CLASS).group().length()<=2) {
            return new IncompleteClass(pattern,startIndex,startIndex+lib.getMatcher(Type.CHAR_CLASS ).end());
        } else {
            return new CharClass(pattern, startIndex, startIndex + lib.getMatcher(Type.CHAR_CLASS).end());
        }
    }

    private Construct createQuotation(String pattern, int startIndex) {
        if(regexMatch(Type.INCOMPLETE,pattern)) {
            return new IncompleteConstruct(pattern,startIndex,lib.getEndOfLastMatch(Type.INCOMPLETE));
        }
        return new Quotation(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end());
    }

    private Construct createPredefined(String pattern, int startIndex) {
        if (lib.getMatcher(Type.PREDEFINED).group().matches("\\\\[dDsSwW]|\\\\[pP](\\{[^}]+})|\\.")) {
            return new Predefined(pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end());
        } else {
            regexMatch(Type.INCOMPLETE, pattern.substring(startIndex));
            return new IncompleteConstruct(pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end());
        }
    }

    private Construct createSimpleConstruct(String pattern, int startIndex) {
        if(pattern.substring(startIndex, startIndex + 1).matches("[\\[\\(\\)]")) {
            return new UnbalancedStructure(pattern,startIndex,startIndex+1);
        } else {
            regexMatch(Type.SIMPLE, pattern.substring(startIndex));
            return new Construct(pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end());
        }
    }

    private Construct createRangeConstruct(String pattern, int startIndex) {
        if(isValidRange(lib.getMatcher(Type.RANGE).group())) {
            return new Range(pattern, startIndex, startIndex + lib.getMatcher(Type.RANGE).end());
        } else {
            return new InvalidRange(pattern,startIndex,startIndex + lib.getMatcher(Type.RANGE).end());
        }
    }

    private boolean regexMatch(Type type, String pattern) {
        return lib.getMatcher(type).reset(pattern).lookingAt();
    }

    public static ConstructsFactory getInstance() {
        return INSTANCE;
    }

    private String extractGroup(String pattern) {
        char[] strAsChar = pattern.toCharArray();
        int depth = 0;
        for(int index = 0; index < pattern.length(); index++) {
            if(strAsChar[index]=='(') {
                depth++;
            } else if(strAsChar[index]==')') {
                depth--;
            }
            if(index!=0 && depth==0){
                return pattern.substring(0,index+1);
            }
        }
        return null;
    }

    private boolean isValidRange(String range) {
        String[] elements = range.split("-");
        return elements[0].compareTo(elements[1])<0;
    }

    private boolean isValidInterval(String interval) {
        String temp = interval.substring(1,interval.length()-1);
        String[] elements = temp.split(",");
        if(interval.matches("\\{\\d,?\\}")) {
            return true;
        }
        return elements[0].compareTo(elements[1])<0;
    }

    private boolean isValidBackreference(String backreference, String pattern) {
        if(backreference.startsWith("\\k")){
            Matcher m = Pattern.compile("\\(\\?" + backreference.substring(2)).matcher(pattern.substring(lib.getMatcher(Type.BACKREFERENCE).start()));
            return m.find();
        } else {
            return new Integer(backreference.substring(1)) <= groupCount;
        }
    }

    private int countGroups(String pattern) {
        String regex = "\\((\\\\\\)|\\\\\\(|[^()])*\\)";
        Matcher m = Pattern.compile(regex).matcher(pattern);
        int count = 0;
        while (m.find()) {
            pattern = m.replaceAll(getBlankString(m.group().length()));
            m.reset(pattern);
            count++;
        }
        return count;

    }

    private String getBlankString(int length) {
        String blank = "";
        for(int i = 0; i < length; i++) {
            blank += " ";
        }
        return blank;
    }

    private void updateGroupsCount(String pattern) {
        if (!pattern.equals(currentPattern)) {
            currentPattern = pattern;
            groupCount = countGroups(pattern);
        }
    }

}
