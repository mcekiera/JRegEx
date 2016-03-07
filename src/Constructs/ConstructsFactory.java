package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.*;
import Expression.Expression;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();
    private final GroupFactory groupFactory = GroupFactory.getInstance();
    private final MatcherLib lib = MatcherLib.getInstance();
    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        String current = expression.getPatternAsString(patternIndex);

        if(regexMatch(Type.BOUNDARY,current)) {
            System.out.print(Type.BOUNDARY);
            return new Boundary(lib.getMatcher(Type.BOUNDARY).group(),"");
        } else if(regexMatch(Type.MODE,current)) {
            System.out.print(Type.MODE);
            return new Mode(lib.getMatcher(Type.MODE).group(),"");
        } else if(regexMatch(Type.CHAR_CLASS,current)) {
            System.out.print(Type.CHAR_CLASS);
            return new CharClass(lib.getMatcher(Type.CHAR_CLASS).group(),"");
        } else if(regexMatch(Type.LOGICAL,current)) {
            System.out.print(Type.LOGICAL);
            return new Logical(lib.getMatcher(Type.LOGICAL).group(),"");
        } else if(regexMatch(Type.PREDEFINED,current)) {
            System.out.print(Type.PREDEFINED);
            return new Predefined(lib.getMatcher(Type.PREDEFINED).group(),"");
        } else if(regexMatch(Type.QUANTIFIER,current)) {
            System.out.print(Type.QUANTIFIER);
            return new Quantifier(lib.getMatcher(Type.QUANTIFIER).group(),"");
        } else if(regexMatch(Type.QUOTATION,current)) {
            System.out.print(Type.QUOTATION);
            return new Quotation(lib.getMatcher(Type.QUOTATION).group(),"");
        } else if(regexMatch(Type.SPECIFIC_CHAR,current)) {
            System.out.print(Type.SPECIFIC_CHAR);
            return new SpecificChar(lib.getMatcher(Type.SPECIFIC_CHAR).group(),"");
        } else if(regexMatch(Type.GROUP,current)) {
            String group = extractGroup(current);
            return groupFactory.create(group,"");
        } else {
            System.out.print("SIMPLE ");
            return new Construct(current.substring(0,1),"");
        }
    }

    private boolean regexMatch(Type type, String patter) {
        return lib.getMatcher(type).reset(patter).lookingAt();
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }

    public String extractGroup(String pattern) {
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
}
