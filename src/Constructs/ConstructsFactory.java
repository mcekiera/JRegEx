package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.*;
import Constructs.Types.Group.Capturing;
import Expression.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();
    private final GroupFactory groupFactory = GroupFactory.getInstance();
    private final MatcherLib lib = MatcherLib.getInstance();
    private ConstructsFactory() {}

    public Construct createConstruct(Expression expression, int patternIndex) {
        String current = expression.getPatternAsString(patternIndex);

        if(regexMatch(Type.BOUNDARY,current)) {
            String match = getDirectMatch(expression,Type.BOUNDARY,patternIndex);
            return new Boundary(lib.getMatcher(Type.BOUNDARY).group(),match);
        } else if(regexMatch(Type.MODE,current)) {
            String match = getDirectMatch(expression,Type.MODE,patternIndex);
            return new Mode(lib.getMatcher(Type.MODE).group(),match);
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            String match = getDirectMatch(expression,Type.CHAR_CLASS,patternIndex);
            return new CharClass(lib.getMatcher(Type.CHAR_CLASS).group(),match);
        } else if(regexMatch(Type.LOGICAL,current)) {
            String match = getDirectMatch(expression,Type.LOGICAL,patternIndex);
            return new Logical(lib.getMatcher(Type.LOGICAL).group(),match);
        } else if(regexMatch(Type.PREDEFINED,current)) {
            String match = getDirectMatch(expression,Type.PREDEFINED,patternIndex);
            return new Predefined(lib.getMatcher(Type.PREDEFINED).group(),match);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            String match = getDirectMatch(expression,Type.QUANTIFIER,patternIndex);
            return new Quantifier(lib.getMatcher(Type.QUANTIFIER).group(),match);
        } else if(regexMatch(Type.QUOTATION,current)) {
            String match = getDirectMatch(expression,Type.QUOTATION,patternIndex);
            return new Quotation(lib.getMatcher(Type.QUOTATION).group(),match);
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            String match = getDirectMatch(expression,Type.SPECIFIC_CHAR,patternIndex);
            return new SpecificChar(lib.getMatcher(Type.SPECIFIC_CHAR).group(),match);
        } else if(regexMatch(Type.GROUP,current)) {
            return createGroup(expression,patternIndex);
        } else {
            regexMatch(Type.SIMPLE,current);
            String match = getDirectMatch(expression, Type.SIMPLE, patternIndex);
            return new Construct(current.substring(0,1),match);
        }
    }

    public Construct createGroup(Expression expression, int patternIndex) {
        String current = extractGroup(expression.getPatternAsString(patternIndex));
        if (regexMatch(Type.LOOK_AROUND, current)) {
            String match = getDirectMatch(expression, Type.LOOK_AROUND, patternIndex);
            return new Boundary(lib.getMatcher(Type.LOOK_AROUND).group(), match);
        } else if (regexMatch(Type.ATOMIC, current)) {
            String match = getDirectMatch(expression, Type.ATOMIC, patternIndex);
            return new Mode(lib.getMatcher(Type.ATOMIC).group(), match);
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            String match = getDirectMatch(expression, Type.NON_CAPTURING, patternIndex);
            return new CharClass(lib.getMatcher(Type.NON_CAPTURING).group(), match);
        } else {
            regexMatch(Type.CAPTURING, current);
            System.out.println(lib.getMatcher(Type.CAPTURING).group());
            String match = getDirectMatch(expression, Type.CAPTURING, patternIndex);
            return new Capturing(lib.getMatcher(Type.CAPTURING).group(), match);
        }
    }

    private boolean regexMatch(Type type, String patter) {
        return lib.getMatcher(type).reset(patter).lookingAt();
    }

    public static ConstructsFactory getInstance() {
        return instance;
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
            if(depth==0){
                return pattern.substring(0,index+1);
            }
        }
        return "";
    }

    private String injectGroup(String pattern, int start, int end) {
        System.out.println(pattern);
        return pattern.substring(0, start) + "(?<temp>" + pattern.substring(start, end) + ")" + pattern.substring(end);
    }

    private String getDirectMatch(Expression expression, Type type, int index) {
        System.out.println(type);
        String pattern = injectGroup(expression.getPatternAsString(0), index, index + lib.getMatcher(type).group().length());
        System.out.println(pattern);
        Matcher matcher = Pattern.compile(pattern).matcher(expression.getMatch());
        matcher.find();
        return matcher.group("temp");
    }

}
