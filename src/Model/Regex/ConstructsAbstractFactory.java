package Model.Regex;

import Model.Lib.DescLib;
import Model.Lib.MatcherLib;
import Model.Segment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory class for creation of Construct objects, which represents separate regular expression constructs. Singleton
 * class, has only one instance.
 */
public class ConstructsAbstractFactory {
    /**
     * Only instance of class.
     */
    private static final ConstructsAbstractFactory INSTANCE = new ConstructsAbstractFactory();

    private final XModer xModer = XModer.getInstance();
    /**
     * Provides matcher for particular constructs recognition.
     */
    private final MatcherLib lib = MatcherLib.getInstance();
    /**
     * Provides descriptions for identified constructs.
     */
    private final DescLib desc = DescLib.getInstance();
    /**
     * String form of currently processed regular expression.
     */
    private String currentPattern;
    /**
     * Counter of capturing grouping constructs.
     */
    private int groupCount;

    private ConstructsAbstractFactory() {}

    /**
     * @return only instance of class.
     */
    public static ConstructsAbstractFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Identifies and creates Construct objects which starts on given index within regular expression.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object.
     */
    public Construct createConstruct(Construct parent, String pattern, int startIndex) {
        Construct construct;
        if(parent.getType() == Type.CHAR_CLASS) {
            construct = createConstructInCharClass(parent, pattern, startIndex);
        } else {
            construct = createCommonConstruct(parent,pattern,startIndex);
        }

        return construct;
    }

    /**
     * Identifies and creates basic Construct objects.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object.
     */
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
        } else if(xModer.isInCommentRange(startIndex) && regexMatch(Type.COMMENT, current)) {
            construct = new Single(parent,Type.COMMENT, new Segment(pattern,startIndex,startIndex + lib.getEndOfLastMatch(Type.COMMENT)));
        } else {
            System.out.println(startIndex + "," + xModer.isInCommentRange(startIndex));
            construct = createSimpleConstruct(parent,pattern,startIndex);
        }
        return construct;
    }

    /**
     * Identifies and creates Construct objects which ara grouping constructs(CAPTURING, NON_CAPTURING, LOOK_AROUND,
     * ATOMIC type) or UNBALANCES type construct, if given construct is invalid. .
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object.
     */
    public Construct createGroupConstruct(Construct parent, String pattern, int startIndex) {
        String current = extractGroup(pattern, startIndex,'(', ')', true);
        System.out.println("CuRRENT: " + current);
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

    /**
     * Identifies and creates Construct objects of COMPONENT type, which represents structural components of grouping
     * constructs, like: '(', '(?:', '(?<=', etc.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of COMPONENT type.
     */
    public Construct createInGroupConstruct(Construct parent, String pattern, int startIndex) {
        Matcher matcher = Pattern.compile("\\((\\?(<\\w+>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|<))?|\\[|\\)|]").matcher(parent.getPattern().substring(startIndex));
        if(matcher.find()) {
            Segment matched = new Segment(pattern, startIndex + matcher.start(), startIndex + matcher.end());
            return new Single(parent, Type.COMPONENT, matched);
        }
        return createEmptyConstruct(parent,pattern,startIndex);
    }

    /**
     * Identifies and creates Construct objects which ara within character class construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object.
     */
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
            return createCharacterClass(parent, pattern, startIndex);
        } else {
            return new Single(parent, Type.SIMPLE, new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end()));
        }
    }

    /**
     * Identifies and creates Construct objects of QUANTIFIER type.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of QUANTIFIER type.
     */
    private Construct createQuantifier(Construct parent, String pattern, int startIndex) {
        return new Quantifier(parent, Type.QUANTIFIER, new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.QUANTIFIER)));
    }

    /**
     * Identifies and creates Construct objects of BACKREFERENCE type or INVALID_BACKREFERENCE type, if created
     * construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of BACKREFERENCE or INVALID_BACKREFERENCE type.
     */
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

    /**
     * Identifies and creates Construct objects of CHAR_CLASS type or INCOMPLETE type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of CHAR_CLASS or INCOMPLETE type.
     */
    private Construct createCharacterClass(Construct parent, String pattern, int startIndex) {
        String current = extractGroup(pattern, startIndex,'[',']', false);
        int end = startIndex + (current.length() == 0 ? 1 : current.length());
        if(current.length()<=2) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,end));
        } else {
            return new Composite(parent, Type.CHAR_CLASS,new Segment(pattern, startIndex, end));
        }
    }

    /**
     * Identifies and creates Construct objects of INTERVAL type or INVALID_INTERVAL type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of INTERVAL or INVALID_INTERVAL type.
     */
    private Construct createInterval(Construct parent, String pattern, int startIndex) {
        if(isValidInterval(lib.getMatcher(Type.INTERVAL).group())) {
            return new Quantifier(parent, Type.INTERVAL, new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.INTERVAL)));
        } else {
            return new Single(parent,Type.INVALID_INTERVAL,new Segment(pattern, startIndex, startIndex + lib.getEndOfLastMatch(Type.INTERVAL)));
        }
    }

    /**
     * Identifies and creates Construct objects of QUOTATION type or INCOMPLETE type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of QUOTATION or INCOMPLETE type.
     */
    private Construct createQuotation(Construct parent,String pattern, int startIndex) {
        if(regexMatch(Type.INCOMPLETE, pattern.substring(startIndex))) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,startIndex+lib.getEndOfLastMatch(Type.INCOMPLETE)));
        }
        return new Single(parent, Type.QUOTATION,new Segment(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end()));
    }

    /**
     * Identifies and creates Construct objects of PREDEFINED type or INCOMPLETE type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of PREDEFINED or INCOMPLETE type.
     */
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


    /**
     * Identifies and creates Construct objects of SIMPLE type or UNBALANCED type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of SIMPLE or UNBALANCED type.
     */
    private Construct createSimpleConstruct(Construct parent, String pattern, int startIndex) {
        if(pattern.substring(startIndex, startIndex + 1).matches("[\\[\\(\\)]")) {
            return new Single(parent, Type.UNBALANCED,new Segment(pattern,startIndex,startIndex+1));
        } else if(pattern.substring(startIndex, startIndex + 1).matches("\\\\")) {
            return new Single(parent, Type.INCOMPLETE,new Segment(pattern,startIndex,startIndex+1));
        } else {
            regexMatch(Type.SIMPLE, pattern.substring(startIndex));
            return new Single(parent, Type.SIMPLE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end()));
        }
    }

    /**
     * Identifies and creates Construct objects of RANGE type or INVALID_RANGE type, if created construct is invalid.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of RANGE type.
     */
    private Construct createRangeConstruct(Construct parent, String pattern, int startIndex) {
        if(isValidRange(lib.getMatcher(Type.RANGE).group())) {
            return new Single(parent, Type.RANGE,new Segment(pattern, startIndex, startIndex + lib.getMatcher(Type.RANGE).end()));
        } else {
            return new Single(parent, Type.INVALID_RANGE,new Segment(pattern,startIndex,startIndex + lib.getMatcher(Type.RANGE).end()));
        }
    }

    /**
     * Identifies and creates Construct objects of ALTERNATION type, which holds first or following alternative from
     * available options within alternation construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of ALTERNATION type.
     */
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

    /**
     * Identifies and creates Construct objects of ALTERNATION type, which holds last alternative from
     * available options within alternation construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex Index position where given construct starts.
     * @return Construct object of ALTERNATION type.
     */
    public Construct createEndingAlternative(Construct parent,String pattern, int startIndex) {
        int end = parent.getType() == Type.EXPRESSION ? parent.getEnd() : parent.getEnd() - 1;
        return new Composite(parent,Type.ALTERNATION,new Segment(pattern,startIndex,end));
    }

    /**
     * Identifies and creates Construct objects of ALTERNATION type, which is valid but is 'empty' - contains a Single
     * construct of 0 length.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param index Index position where given construct starts.
     * @return Construct object of ALTERNATION type.
     */
    public Construct createEmptyAlternative(Construct parent, String pattern, int index) {
        Single single = new Single(parent,Type.SIMPLE,new Segment(pattern,index,index));
        single.setDescription(desc.getDescription(single));
        Composite alternative = new Composite(parent,Type.ALTERNATION,new Segment(pattern,index,index));
        alternative.setDescription(desc.getDescription(alternative));
        alternative.addConstruct(single);
        return alternative;
    }

    /**
     * Creates Construct object which represents zero-length construct.
     * available options within alternation construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param index Index position where given construct starts.
     * @return Construct object.
     */
    public Construct createEmptyConstruct(Construct parent, String pattern, int index) {
        return new Single(parent,Type.SIMPLE,new Segment(pattern,index,index));
    }


    /**
     * Determines if given pattern fragment holds a Construct representing curly braced hexadecimal value.
     * @param pattern Analyzed regular expression in String form
     * @param startIndex currently analyzed index of pattern
     * @return true if given fragment contains hexadecimal value
     */
    public boolean isHexadecimal(String pattern, int startIndex) {
        return pattern.substring(startIndex).startsWith("\\x{");
    }

    /**
     * Determines if given String contains valid range constructs. Invalid range is such range, which elements are
     * in not wrong sequence, for example '9-1', 'z-a', etx.
     * @param range String with range construct.
     * @return true if given range is valid.
     */
    private boolean isValidRange(String range) {
        String[] elements = range.split("-");
        return elements[0].compareTo(elements[1])<0;
    }

    /**
     * Determines if construct is within grouping or look around construct (not within root expression or alternation).
     * @param parent parent construct
     * @param index start index of examined construct
     * @return true if parent is grouping construct
     */
    public boolean isComponentOfGroup(Construct parent, int index) {
        return (parent.getType() != Type.EXPRESSION && parent.getType() != Type.ALTERNATION && parent.isComplex() && index == parent.getStart()) ||
                (parent.getType() != Type.EXPRESSION && parent.getType() != Type.ALTERNATION && parent.isComplex() && index == parent.getEnd()-1);
    }

    /**
     * Determines if end of currently processed grouping construct is reached.
     * @param parent parent construct
     * @param index start index of examined construct
     * @return true if end is reached.
     */
    public boolean isEndOfGroup(Construct parent, int index) {
        return parent.getType() != Type.EXPRESSION && parent.isComplex() && index == parent.getEnd()-1;
    }

    /**
     * Determines if given String contains valid interval constructs. Invalid interval is such range, which elements are
     * in not wrong sequence, for example '3,1', '9,6, etc.
     * @param interval String with interval construct.
     * @return true if given interval is valid.
     */
    private boolean isValidInterval(String interval) {
        if(interval.matches("\\{\\d,?}[+?]?")) return true;
        String temp = interval.substring(1, interval.indexOf('}'));
        String[] elements = temp.split(",");
        Integer[] integers = new Integer[2];
        integers[0] = Integer.valueOf(elements[0]);
        integers[1] = Integer.valueOf(elements[1]);
        return integers[0].compareTo(integers[1]) < 0;
    }

    /**
     * Determine if on given index position  within character class, starts an internal construct 'and'("&&").
     * @param pattern Analyzed regular expression in String form
     * @param startIndex currently analyzed index of pattern
     * @return true if on given index starts construct 'and'.
     */
    private boolean isLogicalAndConstruct(String pattern,int startIndex) {
        return pattern.substring(startIndex).startsWith("&&");
    }

    /**
     * Determine if on given index position within character class ,starts an internal construct 'not'("^").
     * @param pattern Analyzed regular expression in String form
     * @param startIndex currently analyzed index of pattern
     * @return true if on given index starts construct 'not'.
     */
    private boolean isLogicalNotConstruct(String pattern, int startIndex) {
        return pattern.charAt(startIndex) == '^' && pattern.charAt(startIndex-1) == '[';
    }

    /**
     * Determines if given String contains valid backreference constructs. Invalid backreference is such backreference,
     * which ordinal number is bigger than number of capturing groups, or name is different than used in named
     * capturing groups.
     * @param backreference String with backreference construct.
     * @return true if given backreference is valid.
     */
    private boolean isValidBackreference(String backreference, String pattern) {
        if(backreference.startsWith("\\k")){
            Matcher m = Pattern.compile("\\(\\?" + backreference.substring(2)).matcher(pattern.substring(lib.getMatcher(Type.BACKREFERENCE).start()));
            return m.find();
        } else {
            return new Integer(backreference.substring(1)) <= groupCount;
        }
    }

    /**
     * Determines if given construct on given index, is first or following options within alternation construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex index position where given construct starts.
     * @return true if given construct is alternative within alternation construct.
     */
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

    /**
     * Determines if given construct on given index, is last options within alternation construct.
     * @param parent Object within which given construct is placed.
     * @param pattern Regular expression.
     * @param startIndex index position where given construct starts.
     * @return true if given construct is last alternative within alternation construct.
     */
    private boolean isEndingAlternative(Construct parent, String pattern, int startIndex) {
        return parent.getType() != Type.ALTERNATION && startIndex > 0 && pattern.charAt(startIndex-1) == '|';
    }

    /**
     * Determine how many grouping constructs given pattern has.
     * @param pattern regular expression.
     * @return integer, number of groups.
     */
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

    /**
     * Provide String composed only of space characters ' ', with length depending on passed integer value.
     * @param length of String
     * @return String composed of space characters.
     */
    private String getBlankString(int length) {
        String blank = "";
        for(int i = 0; i < length; i++) {
            blank += " ";
        }
        return blank;
    }

    /**
     * Provide current information about number of groups within given pattern.
     * @param pattern regular expression
     */
    private void updateGroupsCount(String pattern) {
        if (!pattern.equals(currentPattern)) {
            currentPattern = pattern;
            groupCount = countGroups(pattern);
        }
    }

    /**
     * Matches defined for particular construct categories regular expressions to recognize given concept within pattern.
     * @param type currently matching type.
     * @param pattern regular expression.
     * @return true, if pattern match anything.
     */
    private boolean regexMatch(Type type, String pattern) {
        return lib.getMatcher(type).reset(pattern).lookingAt();
    }

    /**
     * Determine and returns fragment of regular expression recognized as separate grouping construct.
     * @param pattern whole regular expression.
     * @param start index of examined part.
     * @param end index of examined part.
     * @return String with retrieved grouping construct.
     */
    private String extractGroup(String pattern, int startIndex, char start, char end, boolean xmode) {
        char[] strAsChar = pattern.substring(startIndex).toCharArray();
        int depth = 0;
        for(int index = 0; index < pattern.substring(startIndex).length(); index++) {
            if(strAsChar[index] == '\\') {
                index++;
            } else if(strAsChar[index]==start) {
                depth++;
            } else if(strAsChar[index]==end) {
                depth--;
            } else if(xmode && strAsChar[index] == '#' && xModer.isInCommentRange(startIndex+index)) {
                for(int i = index; i < pattern.substring(startIndex).length(); i++) {
                    System.out.println(pattern.substring(i));
                    index = i;
                    if(strAsChar[i] == '\n') {
                        break;
                    }
                }
            }
            if(index!=0 && depth==0){
                return pattern.substring(startIndex,startIndex+index+1);
            }
            if(index==pattern.length()-1) {
                break;
            }
        }
        return "";
    }
}
