package Model.Constructs;

import Lib.MatcherLib;
import Re.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsFactory {
    private static final ConstructsFactory INSTANCE = new ConstructsFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    //private Construct previous;
    private String currentPattern;
    private int groupCount;

    private ConstructsFactory() {}

    public Construct createConstruct(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct;
        updateGroupsCount(pattern);

        if(regexMatch(Type.BOUNDARY,current)) {
            construct = new Singular(Type.BOUNDARY,pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.BOUNDARY));
        } else if(regexMatch(Type.MODE,current)) {
            construct = new Singular(Type.MODE,pattern,startIndex,startIndex+lib.getMatcher(Type.MODE ).end());
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            construct = createCharacterClass(pattern,startIndex);
        } else if(regexMatch(Type.LOGICAL,current)) {
            construct = new Singular(Type.COMPONENT,pattern,startIndex,startIndex+lib.getMatcher(Type.LOGICAL).end());
        } else if(regexMatch(Type.PREDEFINED,current)) {
            construct = createPredefined(pattern,startIndex);
        }else if(regexMatch(Type.BACKREFERENCE,current)) {
            construct = createBackreference(pattern,startIndex);
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct = new Singular(Type.SPECIFIC_CHAR,pattern,startIndex,startIndex+lib.getMatcher(Type.SPECIFIC_CHAR).end());
        } else if(regexMatch(Type.QUOTATION,current)) {
            construct = createQuotation(pattern,startIndex);
        } else if(regexMatch(Type.GROUP,current)) {
            construct = createGroupConstruct(pattern, startIndex);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            construct = new Quantifier(Type.QUANTIFIER, pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.QUANTIFIER));
        } else if(regexMatch(Type.INTERVAL,current)) {
            construct = new Quantifier(Type.INTERVAL, pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.INTERVAL));
        } else {
            construct = createSimpleConstruct(pattern,startIndex);
        }

        return construct;
    }

    public Construct createGroupConstruct(String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex), '(', ')');
        if(current.equals("")) {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new Error(Type.UNBALANCED,pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end());
        }
        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new Sequence(Type.LOOK_AROUND,pattern,startIndex,startIndex+lib.getMatcher(Type.LOOK_AROUND).end());
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Sequence(Type.ATOMIC,pattern,startIndex,startIndex+lib.getMatcher(Type.ATOMIC).end());
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new Sequence(Type.NON_CAPTURING,pattern,startIndex,startIndex+lib.getMatcher(Type.NON_CAPTURING).end());
        } else if(regexMatch(Type.CAPTURING, current)) {
            return new Sequence(Type.CAPTURING,pattern,startIndex,startIndex+lib.getMatcher(Type.CAPTURING).end());
        } else {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new Error(Type.UNBALANCED,pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end());
    }
    }

    public Construct createConstructInCharClass(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if (regexMatch(Type.RANGE, current)) {
            return createRangeConstruct(pattern,startIndex);
        } else if (regexMatch(Type.PREDEFINED, current)) {
            return createPredefined(pattern,startIndex);
        } else if (regexMatch(Type.SPECIFIC_CHAR, current)) {
            return new Singular(Type.SPECIFIC_CHAR, pattern, startIndex, startIndex + lib.getMatcher(Type.SPECIFIC_CHAR).end());
        } else if (regexMatch(Type.INCOMPLETE, current)) {
            return new Error(Type.INCOMPLETE, pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end());
        } else {
            regexMatch(Type.SIMPLE, current);
            return new Singular(Type.SIMPLE,pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end());
        }
    }

    private Construct createBackreference(String pattern, int startIndex) {
        if(isValidBackreference(lib.getMatcher(Type.BACKREFERENCE).group(),pattern)) {
            return new Singular(Type.BACKREFERENCE,pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end());
        } else {
            return new Error(Type.INVALID_BACKREFERENCE,pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end());
        }
    }

    private Construct createCharacterClass(String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex),'[',']');
        int end = startIndex + (current.length() == 0 ? 1 : current.length());
        if(current.length()<=2) {
            return new Error(Type.INCOMPLETE,pattern,startIndex,end);
        } else {
            return new Sequence(Type.CHAR_CLASS,pattern, startIndex, end);
        }
    }

    private Construct createQuotation(String pattern, int startIndex) {

        if(regexMatch(Type.INCOMPLETE,pattern.substring(startIndex))) {
            return new Error(Type.INCOMPLETE,pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.INCOMPLETE));
        }
        return new Singular(Type.QUOTATION,pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end());
    }

    private Construct createPredefined(String pattern, int startIndex) {
        if (lib.getMatcher(Type.PREDEFINED).group().matches("\\\\[dDsSwW]|\\\\[pP](\\{[^}]+})|\\.")) {
            return new Singular(Type.PREDEFINED,pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end());
        } else {
            regexMatch(Type.INCOMPLETE, pattern.substring(startIndex));
            return new Error(Type.INCOMPLETE,pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end());
        }
    }

    private Construct createSimpleConstruct(String pattern, int startIndex) {
        if(pattern.substring(startIndex, startIndex + 1).matches("[\\[\\(\\)]")) {
            return new Error(Type.UNBALANCED,pattern,startIndex,startIndex+1);
        } else {
            regexMatch(Type.SIMPLE, pattern.substring(startIndex));
            return new Singular(Type.SIMPLE,pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end());
        }
    }

    private Construct createRangeConstruct(String pattern, int startIndex) {
        if(isValidRange(lib.getMatcher(Type.RANGE).group())) {
            return new Singular(Type.RANGE,pattern, startIndex, startIndex + lib.getMatcher(Type.RANGE).end());
        } else {
            return new Error(Type.INVALID_RANGE,pattern,startIndex,startIndex + lib.getMatcher(Type.RANGE).end());
        }
    }

    private boolean regexMatch(Type type, String pattern) {
        return lib.getMatcher(type).reset(pattern).lookingAt();
    }

    public static ConstructsFactory getInstance() {
        return INSTANCE;
    }

    private String extractGroup(String pattern, char start, char end) {
        char[] strAsChar = pattern.toCharArray();
        int depth = 0;
        for(int index = 0; index < pattern.length(); index++) {
            if(strAsChar[index]==start) {
                depth++;
            } else if(strAsChar[index]==end) {
                depth--;
            }
            if(index!=0 && depth==0){
                return pattern.substring(0,index+1);
            }
        }
        return "";
    }

    private boolean isValidRange(String range) {
        String[] elements = range.split("-");
        System.out.println(elements[0].compareTo(elements[1])<0);
        return elements[0].compareTo(elements[1])<0;
    }

    private boolean isValidInterval(String interval) {
        String temp = interval.substring(1, interval.length() - 1);
        String[] elements = temp.split(",");
        return interval.matches("\\{\\d,?\\}") || elements[0].compareTo(elements[1]) < 0;
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
