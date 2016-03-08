package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.*;
import Constructs.Types.Group.Capturing;
import Expression.Expression;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private ConstructsFactory() {}

    public Construct createConstruct(Expression expression, int patternIndex) {
        String current = expression.getPatternAsString(patternIndex);

        if(regexMatch(Type.BOUNDARY,current)) {
            return new Boundary(lib.getMatcher(Type.BOUNDARY).group());
        } else if(regexMatch(Type.MODE,current)) {
            return new Mode(lib.getMatcher(Type.MODE).group());
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            return new CharClass(lib.getMatcher(Type.CHAR_CLASS).group());
        } else if(regexMatch(Type.LOGICAL,current)) {
            return new Logical(lib.getMatcher(Type.LOGICAL).group());
        } else if(regexMatch(Type.PREDEFINED,current)) {
            return new Predefined(lib.getMatcher(Type.PREDEFINED).group());
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            return new Quantifier(lib.getMatcher(Type.QUANTIFIER).group());
        } else if(regexMatch(Type.QUOTATION,current)) {
            return new Quotation(lib.getMatcher(Type.QUOTATION).group());
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            return new SpecificChar(lib.getMatcher(Type.SPECIFIC_CHAR).group());
        } else if(regexMatch(Type.GROUP,current)) {
            return createGroup(expression,patternIndex);
        } else {
            regexMatch(Type.SIMPLE,current);
            return new Construct(current.substring(0,1));
        }
    }

    public Construct createGroup(Expression expression, int patternIndex) {
        String current = extractGroup(expression.getPatternAsString(patternIndex));
        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new Boundary(lib.getMatcher(Type.LOOK_AROUND).group());
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Mode(lib.getMatcher(Type.ATOMIC).group());
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new CharClass(lib.getMatcher(Type.NON_CAPTURING).group());
        } else {
            regexMatch(Type.CAPTURING, current);
            System.out.println(lib.getMatcher(Type.CAPTURING).group());
            return new Capturing(lib.getMatcher(Type.CAPTURING).group());
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

    /*
    private String getDirectMatch(Expression expression, Type type, int index) {
        System.out.println(type);
        String pattern = injectGroup(expression.getPatternAsString(0), index, index + lib.getMatcher(type).group().length());
        System.out.println(pattern);
        //Matcher matcher = Pattern.compile(pattern).matcher(expression.getMatch());
        //matcher.find();
        //return matcher.group("temp");
    } */

}
