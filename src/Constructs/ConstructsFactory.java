package Constructs;

import Expression.Expression;

import java.util.regex.Matcher;

public class ConstructsFactory {
    private static final ConstructsFactory instance = new ConstructsFactory();

    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        Matcher matcher;
        return new Construct();
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }

    public String extractGroup(String pattern, int startIndex) {
        char[] strAsChar = pattern.toCharArray();
        int depth = 0;
        for(int index = startIndex; index < pattern.length(); index++) {
            if(strAsChar[index]=='(') {
                depth++;
            } else if(strAsChar[index]==')') {
                depth--;
            }
            System.out.println(strAsChar[index]);
            System.out.println(depth);

            if(depth==0){
                return pattern.substring(startIndex,index+1);
            }

        }
        return "";
    }
}
