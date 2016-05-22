package Model.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentChecker {
    private int start;
    Matcher matcher;

    public CommentChecker() {
        matcher = Pattern
                .compile("\\(\\?(?:(?<plus>[idmsuU]*x[idmsuU]*)|(?<minus>[idmsuU]*-[idmsuU]*x[idmsuU]*))(:(?<inGroup>:.+))?\\)")
                .matcher("");
    }

    public boolean hasComment(String pattern) {
        int level = 0;
        for(int i = 0; i < pattern.length(); i ++) {
            if(pattern.charAt(i) == '\\') {
                i++;
            }else if(pattern.charAt(i) == '(') {
                matcher.reset(pattern.substring(i));
                if(matcher.lookingAt()) {

                } else {
                    level++;
                }

            } else if(pattern.charAt(i) == ')') {
                level--;
            }
        }
        return false;
    }
}
