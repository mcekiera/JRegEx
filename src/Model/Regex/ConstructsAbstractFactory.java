package Model.Regex;

import Model.Lib.DescLib;
import Model.Lib.MatcherLib;
import Model.Segment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsAbstractFactory {
    private static final ConstructsAbstractFactory INSTANCE = new ConstructsAbstractFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private final DescLib desc = DescLib.getInstance();
    private String currentPattern;
    private int groupCount;

    private ConstructsAbstractFactory() {}

    public Construct createConstruct(Construct parent, String pattern, int startIndex) {
        Construct construct;
        if(parent.getType() == Type.CHAR_CLASS) {
            construct = createConstructInCharClass(parent, pattern, startIndex);
        } else {
            construct = createCommonConstruct(parent,pattern,startIndex);
        }

        return construct;
    }

    public Construct createCommonConstruct(Construct parent, String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct;
        updateGroupsCount(pattern);
        if(isComponentOfGroup(parent, startIndex)) {
            construct = createInGroupConstruct(parent, pattern, startIndex);
        } else if(regexMatch(Type.COMPONENT, current)) {
            construct = new Single(parent,Type.COMPONENT,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.COMPONENT).end()));
        }else if(isFrontalAlternative(parent, pattern, startIndex)) {
            construct = createFrontalAlternative(parent, pattern, startIndex);
        }else if(isEndingAlternative(parent, pattern, startIndex)) {
            construct = createEndingAlternative(parent,pattern,startIndex);
        }else if(regexMatch(Type.BOUNDARY,current)) {
            construct = new Single(parent, Type.BOUNDARY, new Segment(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.BOUNDARY)));
        } else if(regexMatch(Type.MODE,current)) {
            construct = new Single(parent, Type.MODE,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.MODE ).end()));
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            construct = createCharacterClass(parent, pattern, startIndex);
        } else if(regexMatch(Type.PREDEFINED,current)) {
            construct = createPredefined(parent, pattern, startIndex);
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct = createSpecificChar(parent,pattern,startIndex);
        }else if(regexMatch(Type.BACKREFERENCE,current)) {
            construct = createBackreference(parent, pattern, startIndex);
        } else if(regexMatch(Type.QUOTATION,current)) {
            construct = createQuotation(parent, pattern, startIndex);
        } else if(regexMatch(Type.GROUP,current)) {
            construct = createGroupConstruct(parent, pattern, startIndex);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            construct = createQuantifier(parent, pattern, startIndex);
        } else if(regexMatch(Type.INTERVAL,current)) {
            construct = createInterval(parent, pattern, startIndex);
        }else if(regexMatch(Type.COMMENT,current)) {
            construct = createComment(parent,pattern,startIndex);
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
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?|\\[|\\)|]").matcher(parent.getPattern().substring(startIndex));
        if(matcher.find()) {
            Segment matched = new Segment(pattern, startIndex + matcher.start(), startIndex + matcher.end());
            return new Single(parent, Type.COMPONENT, matched);
        }
        return createEmptyConstruct(parent,pattern,startIndex);
    }

    public Construct createConstructInCharClass(Construct parent, String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if(isComponentOfGroup(parent, startIndex)) {
            return createInGroupConstruct(parent, pattern, startIndex);
        } else if(isEndOfGroup(parent, startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 1));
        } else if(isLogicalAndConstruct(pattern, startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 2));
        } else if (isLogicalNotConstruct(pattern, startIndex)) {
            return new Single(parent, Type.COMPONENT, new Segment(pattern, startIndex, startIndex + 1));
        } else if (regexMatch(Type.RANGE, current)) {
            return createRangeConstruct(parent, pattern, startIndex);
        } else if (regexMatch(Type.PREDEFINED, current)) {
            return createPredefined(parent,pattern,startIndex);
        } else if (regexMatch(Type.SPECIFIC_CHAR, current)) {
            return createSpecificChar(parent,pattern,startIndex);
        } else if (regexMatch(Type.INCOMPLETE, current)) {
            return new Single(parent, Type.INCOMPLETE, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end()));
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            return createCharacterClass(parent, pattern,startIndex);
        } else {
            regexMatch(Type.SIMPLE, current);
            return new Single(parent, Type.SIMPLE,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end()));
        }
    }

    private Construct createQuantifier(Construct parent, String pattern, int startIndex) {
        Construct construct = new Quantifier(parent, Type.QUANTIFIER, new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.QUANTIFIER)));
        ((Complex)construct).addConstruct(new Single(construct,Type.COMPONENT,new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.QUANTIFIER))));
        return construct;
    }

    private Construct createBackreference(Construct parent, String pattern, int startIndex) {
        if(isValidBackreference(lib.getMatcher(Type.BACKREFERENCE).group(), pattern)) {
            return new Single(parent, Type.BACKREFERENCE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end()));
        } else {
            return new Single(parent, Type.INVALID_BACKREFERENCE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.BACKREFERENCE).end()));
        }
    }

    /**
     * Creates Construct object holding specific character representation, defined in hexadecimal, octal or unicode
     * values. Uses "hexa" named group of regex, capturing interior part("#####") of hexadecimal construct "\x{#####}".
     * If in-text representation fits specific char form, but contains error, it creates Construct of INCOMPLETE type.
     * @param parent Object within created Construct object will be placed.
     * @param pattern Analyzed regular expression in String form
     * @param startIndex currently analyzed index of pattern
     * @return new Construct of with SPECIFIC_CHAR or INCOMPLETE type.
     */
    private Construct createSpecificChar(Construct parent, String pattern, int startIndex) {
        if(isHexadecimal(pattern, startIndex)) {
            if (lib.getMatcher(Type.SPECIFIC_CHAR).group("hexa").matches("(?i)[0-9a-f]{1,5}")) {
                return new Single(parent, Type.SPECIFIC_CHAR, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SPECIFIC_CHAR).end()));
            } else {
                return new Single(parent, Type.INCOMPLETE, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SPECIFIC_CHAR).end()));
            }
        } else {
            return new Single(parent, Type.SPECIFIC_CHAR, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SPECIFIC_CHAR).end()));
        }
    }

    public boolean isHexadecimal(String pattern, int startIndex) {
        return pattern.substring(startIndex).startsWith("\\x{");
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
        //TODO add incomplete if just //E
        if(regexMatch(Type.INCOMPLETE, pattern.substring(startIndex))) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.INCOMPLETE)));
        }
        return new Single(parent, Type.QUOTATION,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end()));
    }

    private Construct createPredefined(Construct parent, String pattern, int startIndex) {
        if (lib.getMatcher(Type.PREDEFINED).group().matches("\\\\[dDsSwW]|\\\\[pP](\\{[^}]+})|\\.")) {
            Single single =  new Single(parent, Type.PREDEFINED,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end()));
            if(DescLib.getInstance().contains(single.getText())) {
                return single;
            } else {
                return new Single(parent, Type.INCOMPLETE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end()));
            }
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
            if(strAsChar[index] == '\\') {
                index++;
            } else if(strAsChar[index]==start) {
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

    public boolean isComponentOfGroup(Construct parent, int index) {
        return (parent.getType() != Type.EXPRESSION && parent.getType() != Type.ALTERNATION && parent.isComplex() && index == parent.getStart()) ||
                (parent.getType() != Type.EXPRESSION && parent.getType() != Type.ALTERNATION && parent.isComplex() && index == parent.getEnd()-1);
    }

    public boolean isEndOfGroup(Construct parent, int index) {
        return parent.getType() != Type.EXPRESSION && parent.isComplex() && index == parent.getEnd()-1;
    }

    private boolean isValidInterval(String interval) {
        if(interval.matches("\\{\\d,?\\}[+?]?")) return true;
        String temp = interval.substring(1, interval.indexOf('}'));
        String[] elements = temp.split(",");
        Integer[] integers = new Integer[2];
        integers[0] = Integer.valueOf(elements[0]);
        integers[1] = Integer.valueOf(elements[1]);
        return integers[0].compareTo(integers[1]) < 0;
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

    private boolean isFrontalAlternative(Construct parent, String pattern, int startIndex) {
        if(parent.getType() != Type.ALTERNATION) {
            int level = 0;
            for (char ch : pattern.substring(startIndex,parent.getEnd()).toCharArray()) {
                if (ch == '(') {
                    level++;
                } else if (ch == ')') {
                    level--;
                } else if (ch == '|' && level == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isEndingAlternative(Construct parent, String pattern, int startIndex) {
        return parent.getType() != Type.ALTERNATION && startIndex > 0 && pattern.charAt(startIndex-1) == '|';
    }
    private Construct createFrontalAlternative(Construct parent,String pattern, int startIndex) {
        int level = 0;
        int pos = startIndex;
        for(char ch :  pattern.substring(startIndex,parent.getEnd()).toCharArray()) {
            if(ch == '(') {
                level++;
            } else if(ch == ')') {
                level--;
            } else if(ch == '|' && level ==0) {
                return new Composite(parent,Type.ALTERNATION,new Segment(pattern,startIndex,pos));
            }
            pos++;
        }
        return null;
    }

    public Construct createEndingAlternative(Construct parent,String pattern, int startIndex) {
        int end = parent.getType() == Type.EXPRESSION ? parent.getEnd() : parent.getEnd() - 1;
        return new Composite(parent,Type.ALTERNATION,new Segment(pattern,startIndex,end));
    }

    public Construct createEmptyAlternative(Construct parent, String pattern, int index) {
        Single single = new Single(parent,Type.SIMPLE,new Segment(pattern,index,index));
        single.setDescription(desc.getDescription(single));
        Composite alternative = new Composite(parent,Type.ALTERNATION,new Segment(pattern,index,index));
        alternative.setDescription(desc.getDescription(alternative));
        alternative.addConstruct(single);
        return alternative;
    }

    public Construct createEmptyConstruct(Construct parent, String pattern, int index) {
        return new Single(parent,Type.SIMPLE,new Segment(pattern,index,index));
    }

    public Construct createComment(Construct parent, String pattern, int index) {
        return new Single(parent, Type.COMMENT, new Segment(pattern, index, pattern.length()));
    }
}
