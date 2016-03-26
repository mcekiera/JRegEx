package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.*;
import Constructs.Types.Error.IncompleteConstruct;
import Constructs.Types.Error.InvalidRange;
import Constructs.Types.Error.UnbalancedStructure;
import Constructs.Types.Group.Capturing;
import Constructs.Types.Group.LookAround;
import Constructs.Types.Group.NonCapturing;
import Constructs.Types.Error.Error;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();
    private final MatcherLib lib = MatcherLib.getInstance();
    private ConstructsFactory() {}
    private Construct previous;

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
            if(lib.getMatcher(Type.PREDEFINED).group().matches("\\\\[dDsSwW]|\\[pP](\\{[^}]+})|\\.")) {
                construct = new Predefined(pattern, startIndex, startIndex + lib.getMatcher(Type.PREDEFINED).end());
            } else {
                regexMatch(Type.INCOMPLETE,current);
                construct = new IncompleteConstruct(pattern, startIndex, startIndex + lib.getMatcher(Type.INCOMPLETE).end());
            }
       } else if(regexMatch(Type.QUOTATION,current)) {
            construct =  new Quotation(pattern,startIndex,startIndex+lib.getMatcher(Type.QUOTATION).end());
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            construct =  new SpecificChar(pattern,startIndex,startIndex+lib.getMatcher(Type.SPECIFIC_CHAR).end());
        } else if(regexMatch(Type.GROUP,current)) {
            construct = createGroupConstruct(pattern, startIndex);
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            construct = createQuantifierConstruct(pattern, startIndex);
        } else {
            if(pattern.substring(startIndex,startIndex+1).matches("[\\[\\]\\(]")) {
                return new UnbalancedStructure(pattern,startIndex,startIndex+1);
            } else {
                regexMatch(Type.SIMPLE, current);
                construct = new Construct(pattern, startIndex, startIndex + lib.getMatcher(Type.SIMPLE).end());
            }
        }

        previous = construct;
        return construct;
    }

    public Construct createGroupConstruct(String pattern, int startIndex) {
        String current = extractGroup(pattern.substring(startIndex));

        if (regexMatch(Type.LOOK_AROUND, current)) {
            return new LookAround(pattern,startIndex,startIndex+lib.getMatcher(Type.LOOK_AROUND).end());
        } else if (regexMatch(Type.ATOMIC, current)) {
            return new Mode(pattern,startIndex,startIndex+lib.getMatcher(Type.ATOMIC).end());
        } else if (regexMatch(Type.NON_CAPTURING, current)) {
            return new NonCapturing(pattern,startIndex,startIndex+lib.getMatcher(Type.NON_CAPTURING).end());
        } else if (regexMatch(Type.CAPTURING, current)){
            return new Capturing(pattern,startIndex,startIndex+lib.getMatcher(Type.CAPTURING).end());
        } else {
            regexMatch(Type.UNBALANCED, current);
            return new UnbalancedStructure(pattern,startIndex,startIndex+lib.getMatcher(Type.UNBALANCED).end());
        }
    }

    public Construct createConstructInCharClass(String pattern, int startIndex) {
        String current = pattern.substring(startIndex);
        if (regexMatch(Type.RANGE, current)) {
            if(isValidRange(lib.getMatcher(Type.RANGE).group())) {
                return new Range(pattern, startIndex, startIndex + lib.getMatcher(Type.RANGE).end());
            } else {
                return new InvalidRange(pattern,startIndex,startIndex + lib.getMatcher(Type.RANGE).end());
            }
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
                construct = new Interval(pattern, startIndex, startIndex + lib.getMatcher(Type.TOKEN).end());
                ((Interval) construct).setConstruct(previous);
            }
        }
        return construct;
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
        return pattern;
    }

    private boolean isValidRange(String range) {
        System.out.println(range);
            String[] elements = range.split("-");
        for(String g : elements) {
            System.out.println(g);
        }
            return elements[0].compareTo(elements[1])<0;
    }

}
