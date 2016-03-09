package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.*;
import Constructs.Types.Group.Capturing;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private ConstructsFactory() {}

    public Construct createConstruct(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        Construct construct;

        if(regexMatch(Type.BOUNDARY,current)) {
            construct = new Boundary(pattern,startIndex,startIndex+lib.getMatcher(Type.BOUNDARY).end());
        } else if(regexMatch(Type.MODE,current)) {
            construct =  new Mode(pattern,startIndex,startIndex+lib.getMatcher(Type.MODE ).end());
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            construct =  new CharClass(pattern,startIndex,startIndex+lib.getMatcher(Type.CHAR_CLASS).end());
        } else if(regexMatch(Type.LOGICAL,current)) {
            construct =  new Logical(pattern,startIndex,startIndex+lib.getMatcher(Type.LOGICAL).end());
        } else if(regexMatch(Type.PREDEFINED,current)) {
            construct =  new Predefined(pattern,startIndex,startIndex+lib.getMatcher(Type.PREDEFINED).end());
        } else if(regexMatch(Type.QUOTATION,current)) {
            construct =  new Quotation(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end());
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct =  new SpecificChar(pattern,startIndex,startIndex+lib.getMatcher(Type.SPECIFIC_CHAR).end());
        } else if(regexMatch(Type.GROUP,current)) {
            construct =  createGroupConstruct(pattern, startIndex);
        } else {
            regexMatch(Type.SIMPLE,current);
            construct =  new Construct(pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end());
        }


        if(regexMatch(Type.QUANTIFIER,current.substring(construct.size()))) {
            int index = startIndex + construct.size();
            Construct quantifier = new Quantifier(pattern,index,index + lib.getMatcher(Type.QUANTIFIER).end());
            ((Quantifier) quantifier).setConstruct(construct);
            return quantifier;
        } else {
            return construct;
        }
    }

    public Construct createGroupConstruct(String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex));

        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new Boundary(pattern,startIndex,startIndex+lib.getMatcher(Type.LOOK_AROUND).end());
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Mode(pattern,startIndex,startIndex+lib.getMatcher(Type.ATOMIC).end());
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new CharClass(pattern,startIndex,startIndex+lib.getMatcher(Type.NON_CAPTURING).end());
        } else {
            regexMatch(Type.CAPTURING, current);
            return new Capturing(pattern,startIndex,startIndex+lib.getMatcher(Type.CAPTURING).end());
        }
    }

    public Construct createConstructInCharClass(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if (regexMatch(Type.RANGE, current)) {
            return new Range(pattern,startIndex,startIndex+lib.getMatcher(Type.RANGE).end());
        } else if (regexMatch(Type.PREDEFINED, current)) {
            return new Predefined(pattern,startIndex,startIndex+lib.getMatcher(Type.PREDEFINED).end());
        } else {
            regexMatch(Type.SIMPLE, current);
            return new Construct(pattern,startIndex,startIndex+lib.getMatcher(Type.SIMPLE).end());
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
    /*
    private String injectGroup(String pattern, int start, int end) {
        System.out.println(pattern);
        return pattern.substring(0, start) + "(?<temp>" + pattern.substring(start, end) + ")" + pattern.substring(end);
    }


    private String getDirectMatch(Expressions expression, Type type, int index) {
        System.out.println(type);
        String pattern = injectGroup(expression.getPatternAsString(0), index, index + lib.getMatcher(type).group().length());
        System.out.println(pattern);
        //Matcher matcher = Pattern.compile(pattern).matcher(expression.getMatch());
        //matcher.find();
        //return matcher.group("temp");
    } */

}
