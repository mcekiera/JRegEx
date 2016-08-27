package Model.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provide a tool for recognition and process patterns with enabled (?x) modifiers: comment and whitespace allowed.
 * It creates a map of indices, which are within a comment, if it is allowed, which is use during Constructs creation.
 * Singleton class.
 */
public class XModer {
    /**
     * Only instance of class.
     */
    private final static XModer INSTANCE = new XModer();
    /**
     * Matcher object for matching modifiers in regular expression ('(?x)')
     */
    private final Matcher mode = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?\\)").matcher("");
    /**
     * Matcher object for matching modifiers in non-capturing groups ('(?x:xxx)'),
     */
    private final Matcher group = Pattern.compile("(?s)\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?:.*\\)").matcher("");
    /**
     * Array of booleans, which determine if on given string the comment is allowed.
     */
    private boolean[] indices;
    /**
     * Array of booleans, which determine if on given string the comment occurs.
     */
    private boolean[] comments;

    private XModer() {
    }

    /**
     * @return Only instance on class.
     */
    public static XModer getInstance() {
        return INSTANCE;
    }

    /**
     * Analyze pattern structure, searches for positions where x mode apply and where comments occur.
     * @param pattern to analysis.
     */
    public void process(String pattern) {

        indices = new boolean[pattern.length()];
        comments  = new boolean[pattern.length()];

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.substring(i).startsWith("\\Q")) {
                modifyRange(i, quotation(pattern, i), indices, false);
            } else if (isMode(pattern, i)) {
                if (modeHasXModifier()) {
                    findModifierRange(pattern, i, isPositiveModifier(), Type.MODE);
                }
            } else if (isInGroupMode(pattern, i)) {
                if (inGroupModeHasXModifier()) {
                    findModifierRange(pattern, i, isPositiveInGroupModifier(), Type.NON_CAPTURING);
                }
            } else if (pattern.charAt(i) == '[') {
                modifyRange(i, charClass(pattern, i), indices,false);
            } else if (pattern.charAt(i) == '#' && indices[i]) {
                int result = extractComment(pattern, i);
                modifyRange(i, result, comments, true);
                i = result;
            } else if (Character.isWhitespace(pattern.charAt(i)) && indices[i]) {
                int result = extractWhitespace(pattern,i);
                modifyRange(i, result, comments, true);
                i = result-1;
            }

        }
        System.out.println("_______");
        System.out.println(printArray(indices));
        System.out.println(printArray(comments));
    }

    /**
     * Iterate over boolean array, from start to end index, and change elements values.
     * @param start index of array.
     * @param end index of array.
     * @param arr array of booleans.
     */
    private void modifyRange(int start, int end, boolean[] arr, boolean value) {
        for(int q = start; q < end; q++) {
            arr[q] = value;
        }
    }

    /**
     * Determine if given index position lay within range of comment construct.
     * @param index index in pattern String.
     * @return true if on given index is comment constuct.
     */
    public boolean isInCommentRange(int index) {
        return comments[index];
    }

    /**
     * Determine range where given modifier apply.
     * @param pattern regular expression.
     * @param i start index for searching.
     * @param x determine if x mode apply.
     * @param type type of construct within which modifier could be found.
     */
    private void findModifierRange(String pattern, int i, boolean x, Type type) {
        int depth = 0;
        for(int r = i; r < pattern.length(); r++) {

            if (pattern.charAt(r)=='\\') {
                r++;
            } else if (pattern.charAt(r)=='(') {
                depth++;
            } else if(pattern.charAt(r) == ')') {
                depth--;
            } else if(x && pattern.charAt(r) == '#') {
                for(int t = r; t < pattern.length(); t++) {
                    r=t;
                    indices[r] = x;
                    if(pattern.charAt(t)=='\n') {
                        break;
                    }
                }
            }

            indices[r] = x;
            if(type == Type.MODE && depth < 0) {
                break;
            }else if(type == Type.NON_CAPTURING && depth == 0) {
                break;
            }
        }
    }

    /**
     * Provide easy to read interpretation of boolean array.
     * @param arr array of booleans to print.
     * @return String composed of 'F' for false, and 'T' for true
     */
    private String printArray(boolean[] arr) {
        String result = "";
        for(boolean b : arr) {
            if(b) {
                result += "T";
            } else {
                result += "F";
            }
        }
        return result;
    }

    /**
     * Extract the String containing fragment of pattern with comment construct.
     * @param pattern regular expression.
     * @param start index.
     * @return String with comment.
     */
    private int extractComment(String pattern, int start) {
        int temp = 0;
        for(int v = start; v < pattern.length(); v++) {
            temp = v;
            if(pattern.charAt(v) == '\n') {
                break;
            } else if(v == pattern.length()-1) {
                temp = v+1;
            }
        }
        return temp;
    }

    /**
     * Extract the String containing fragment of pattern with whitespaces constructs, which in x mode are treated like
     * comments.
     * @param pattern regular expression.
     * @param start index.
     * @return String with comment.
     */
    private int extractWhitespace(String pattern, int start) {
        int end = start;
        for(int c = start; c < pattern.length(); c++) {
            end = c;
            if(!Character.isWhitespace(pattern.charAt(c))) {
                break;
            }
        }
        return end;
    }

    /**
     * Provide end index of quotation construct, which content is ignored by x mode.
     * @param pattern regular expression.
     * @param start index.
     * @return end index of quotation construct.
     */
    private int quotation(String pattern, int start) {
        for(int k = start; k < pattern.length(); k++) {
            if(pattern.substring(k).startsWith("\\E")) {
                return k+2;
            } else if (pattern.substring(k).startsWith("\\")) {
                k++;
            } else if (k == pattern.length()-1) {
                return k;
            }
        }
        return 0;
    }

    /**
     * Provide end index of character class construct, which content is ignored by x mode.
     * @param pattern regular expression.
     * @param start index.
     * @return end index of character class construct.
     */
    private int charClass(String pattern, int start) {
        int depth = 0;
        int result = start;
        for(int k = start; k < pattern.length(); k++) {
            if(pattern.charAt(k) == '[') {
                depth++;
            } else if (pattern.charAt(k) == ']') {
                depth--;
                if(depth == 0) {
                    return k+1;
                }
            }
            result = k+1;
        }
        return result;
    }

    /**
     * Determine if on given index starts separate mode construct, like '(?x)'.
     * @param pattern regular expression.
     * @param start index.
     * @return true if on given index starts separate mode construct
     */
    private boolean isMode(String pattern, int start) {
        return mode.reset(pattern.substring(start)).lookingAt();
    }

    /**
     * Determines if given separate mode construct consist x modifier.
     * @return true if currently matched mode consist x modifier.
     */
    private boolean modeHasXModifier() {
        return mode.group().contains("x");
    }

    /**
     * Determines if x modifier within currently matched mode construct is positive.
     * @return true if currently matched mode construct is positive.
     */
    private boolean isPositiveModifier() {
        return !mode.group().substring(0,mode.group().indexOf("x")).contains("-");
    }

    /**
     * Determine if on given index starts non-capturing group with modifiers, like '(?x: ... )'.
     * @param pattern regular expression.
     * @param start index.
     * @return true if on given index starts non-capturing group with modifiers
     */
    private boolean isInGroupMode(String pattern, int start) {
        return group.reset(pattern.substring(start)).lookingAt();
    }

    /**
     * Determines if x modifier within currently matched non-capturing group with modifiers is positive.
     * @return true if currently matched non-capturing group with modifiers is positive.
     */
    private boolean isPositiveInGroupModifier() {
        return !group.group().substring(0, group.group().indexOf(":")).substring(0,group.group()
                .substring(0, group.group().indexOf(":")).lastIndexOf("x")).contains("-");
    }
    /**
     * Determines if currently matched non-capturing group with modifiers consist x modifier.
     * @return true if currently matched non-capturing group with modifiers consist x modifier.
     */
    private boolean inGroupModeHasXModifier() {
        return group.group().substring(0,group.group().indexOf(":")).contains("x");
    }

}
