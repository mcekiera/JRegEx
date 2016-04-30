package Model.Regex;

import Model.Lib.MatcherLib;
import Model.Segment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsAbstractFactory {
    private static final ConstructsAbstractFactory INSTANCE = new ConstructsAbstractFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private String currentPattern;
    private int groupCount;

    private ConstructsAbstractFactory() {}

    public Construct createConstruct(Construct parent, String pattern, int startIndex) {
        if(parent.getType() == Type.CHAR_CLASS) {
            return createConstructInCharClass(parent,pattern,startIndex);
        } else {
            return createCommonConstruct(parent,pattern,startIndex);
        }
    }

    public Construct createCommonConstruct(Construct parent, String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct;
        updateGroupsCount(pattern);
        if(isBeginningOfGroup(parent, startIndex)) {
            construct = createInGroupConstruct(parent,pattern,startIndex);
        }else if(regexMatch(Type.BOUNDARY,current)) {
            construct = new Single(parent, Type.BOUNDARY, new Segment(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.BOUNDARY)));
        } else if(regexMatch(Type.MODE,current)) {
            construct = new Single(parent, Type.MODE,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.MODE ).end()));
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            construct = createCharacterClass(parent, pattern,startIndex);
        } else if(regexMatch(Type.COMPONENT,current)) {
            construct = new Single(parent, Type.COMPONENT,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.COMPONENT).end()));
        } else if(regexMatch(Type.PREDEFINED,current)) {
            construct = createPredefined(parent,pattern,startIndex);
        }else if(regexMatch(Type.BACKREFERENCE,current)) {
            construct = createBackreference(parent, pattern,startIndex);
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct = new Single(parent, Type.SPECIFIC_CHAR,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.SPECIFIC_CHAR).end()));
        } else if(regexMatch(Type.QUOTATION,current)) {
            construct = createQuotation(parent,pattern,startIndex);
        } else if(regexMatch(Type.GROUP,current)) {
            construct = createGroupConstruct(parent,pattern, startIndex);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            construct = new Quantifier(parent, Type.QUANTIFIER, new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.QUANTIFIER)));
        } else if(regexMatch(Type.INTERVAL,current)) {
            construct = createInterval(parent,pattern,startIndex);
        } else {
            construct = createSimpleConstruct(parent,pattern,startIndex);
        }
        return construct;
    }

    public Construct createGroupConstruct(Construct parent, String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex), '(', ')');
        if(current.equals("")) {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new Single(parent, Type.UNBALANCED,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end()));
        }
        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new Composite(parent, Type.LOOK_AROUND,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.LOOK_AROUND).end()));
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Composite(parent, Type.ATOMIC,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.ATOMIC).end()));
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new Composite(parent, Type.NON_CAPTURING,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.NON_CAPTURING).end()));
        } else if(regexMatch(Type.CAPTURING, current)) {
            return new Composite(parent, Type.CAPTURING,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.CAPTURING).end()));
        } else {
            regexMatch(Type.UNBALANCED, pattern.substring(startIndex));
            return new Single(parent, Type.UNBALANCED,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end()));
        }
    }

    public Construct createInGroupConstruct(Construct parent, String pattern, int startIndex) {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?|\\[").matcher(parent.toString());
        matcher.find();
        int i = matcher.end();
        Segment matched = new Segment(pattern,parent.getStart() + matcher.start(),parent.getStart() + matcher.end());
        return new Single(parent, Type.COMPONENT, matched);
    }

    public Construct createConstructInCharClass(Construct parent, String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if(isBeginningOfGroup(parent, startIndex)) {
            return createInGroupConstruct(parent, pattern, startIndex);
        } else if(isEndOfGroup(parent, startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 1));
        } else if(isLogicalAndConstruct(pattern,startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 2));
        } else if (isLogicalNotConstruct(pattern,startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 1));
        } else if (regexMatch(Type.RANGE, current)) {
            return createRangeConstruct(parent,pattern,startIndex);
        } else if (regexMatch(Type.PREDEFINED, current)) {
            return createPredefined(parent,pattern,startIndex);
        } else if (regexMatch(Type.SPECIFIC_CHAR, current)) {
            return new Single(parent, Type.SPECIFIC_CHAR, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SPECIFIC_CHAR).end()));
        } else if (regexMatch(Type.INCOMPLETE, current)) {
            return new Single(parent, Type.INCOMPLETE, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end()));
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            return createCharacterClass(parent, pattern,startIndex);
        } else {
            regexMatch(Type.SIMPLE, current);
            return new Single(parent, Type.SIMPLE,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end()));
        }
    }

    private Construct createBackreference(Construct parent, String pattern, int startIndex) {
        if(isValidBackreference(lib.getMatcher(Type.BACKREFERENCE).group(), pattern)) {
            return new Single(parent, Type.BACKREFERENCE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end()));
        } else {
            return new Single(parent, Type.INVALID_BACKREFERENCE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end()));
        }
    }

    private Construct createCharacterClass(Construct parent, String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex),'[',']');
        int end = startIndex + (current.length() == 0 ? 1 : current.length());
        if(current.length()<=2) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,end));
        } else {
            return new Composite(parent, Type.CHAR_CLASS,new Segment(pattern, startIndex, end));
        }
    }

    private Construct createInterval(Construct parent, String pattern, int startIndex) {
        if(isValidInterval(lib.getMatcher(Type.INTERVAL).group())) {
            return new Quantifier(parent, Type.INTERVAL, new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.INTERVAL)));
        } else {
            return new Single(parent,Type.INVALID_INTERVAL,new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.INTERVAL)));
        }
    }

    private Construct createQuotation(Construct parent,String pattern, int startIndex) {

        if(regexMatch(Type.INCOMPLETE, pattern.substring(startIndex))) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.INCOMPLETE)));
        }
        return new Single(parent, Type.QUOTATION,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end()));
    }

    private Construct createPredefined(Construct parent, String pattern, int startIndex) {
        if (lib.getMatcher(Type.PREDEFINED).group().matches("\\\\[dDsSwW]|\\\\[pP](\\{[^}]+})|\\.")) {
            return new Single(parent, Type.PREDEFINED,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end()));
        } else {
            regexMatch(Type.INCOMPLETE, pattern.substring(startIndex));
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end()));
        }
    }

    private Construct createSimpleConstruct(Construct parent, String pattern, int startIndex) {
        if(pattern.substring(startIndex, startIndex + 1).matches("[\\[\\(\\)]")) {
            return new Single(parent, Type.UNBALANCED,new Segment(pattern,startIndex,startIndex+1));
        } else {
            regexMatch(Type.SIMPLE, pattern.substring(startIndex));
            return new Single(parent, Type.SIMPLE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end()));
        }
    }

    private Construct createRangeConstruct(Construct parent, String pattern, int startIndex) {
        if(isValidRange(lib.getMatcher(Type.RANGE).group())) {
            return new Single(parent, Type.RANGE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.RANGE).end()));
        } else {
            return new Single(parent, Type.INVALID_RANGE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.RANGE).end()));
        }
    }

    private boolean regexMatch(Type type, String pattern) {
        return lib.getMatcher(type).reset(pattern).lookingAt();
    }

    public static ConstructsAbstractFactory getInstance() {
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
        return elements[0].compareTo(elements[1])<0;
    }

    public boolean isBeginningOfGroup(Construct parent, int index) {
        return parent.getType() != Type.EXPRESSION && parent.isComplex() && index == parent.getStart();
    }

    public boolean isEndOfGroup(Construct parent, int index) {
        return parent.getType() != Type.EXPRESSION && parent.isComplex() && index == parent.getEnd()-1;
    }

    private boolean isValidInterval(String interval) {
        String temp = interval.substring(1, interval.length() - 1);
        String[] elements = temp.split(",");
        return interval.matches("\\{\\d,?\\}") || elements[0].compareTo(elements[1]) < 0;
    }

    private boolean isLogicalAndConstruct(String pattern,int startIndex) {
        return pattern.substring(startIndex).startsWith("&&");
    }

    private boolean isLogicalNotConstruct(String pattern, int startIndex) {
        return pattern.charAt(startIndex) == '^' && pattern.charAt(startIndex-1) == '[';
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
