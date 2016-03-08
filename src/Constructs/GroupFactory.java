package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.Type;
import Expression.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupFactory {
    private static final GroupFactory instance = new GroupFactory();
    private final MatcherLib lib = MatcherLib.getInstance();

    private GroupFactory() {}

    public static GroupFactory getInstance() {
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

    private String getDirectMatch(Expression expression, Type type, int index) {
        System.out.print(type);
        String pattern = injectGroup(expression.getPatternAsString(0), index, index + lib.getMatcher(type).group().length());
        System.out.print(pattern);
        Matcher matcher = Pattern.compile(pattern).matcher(expression.getMatch());
        matcher.find();
        return matcher.group("temp");
    }

    private String injectGroup(String pattern, int start, int end) {
        System.out.print(pattern);
        return pattern.substring(0, start) + "(?<temp>" + pattern.substring(start, end) + ")" + pattern.substring(end);
    }
}
