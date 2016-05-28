package Model.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Determine if in given regular expression pattern occurs ?x modifier. TODO check if \\ is processed correctly
 */
public class XModer {
    private final static XModer INSTANCE = new XModer();
    private final Matcher mode = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?\\)").matcher("");
    private final Matcher group = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?:.*\\)").matcher("");
    private boolean[] indices;
    private boolean[] comments;


    public static void main(String[] args) {
        String[] pattern = {"(?x)\\Q2#34\\E#678","(?x)\\Q2#34\\E#678aa\n(?-x)aaaaa(?x:aa(?-x:uu)a#a)bc[abc&&[#abc]]ab#",
                "(?x)aaaa(?-x:#)bbb#ccccc", "(?x)abc  #  abc\ncde   #   cde\nefg   #   efg"};
        XModer XModer = new XModer();
        for(String p : pattern) {
            System.out.println("__________________");
            System.out.println(p);
            XModer.process(p);
        }
    }

    public void process(String pattern) {

        indices = new boolean[pattern.length()];
        comments  = new boolean[pattern.length()];

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.substring(i).startsWith("\\Q")) {
                disableRange(i, quotation(pattern, i), indices);
                System.out.println("QUOTE: " + pattern.substring(i, quotation(pattern, i)));
            } else if (isMode(pattern, i)) {
                if (modeHasXModifier()) {
                    findSeparateModifierRange(pattern, i, isPositiveModifier());
                }
            } else if (isInGroupMode(pattern, i)) {
                if (inGroupModeHasXModifier()) {
                    findNonCapturingGroupModifierRange(pattern, i, isPositiveInGroupModifier());
                }
            } else if (pattern.charAt(i) == '[') {
                disableRange(i, charClass(pattern, i),indices);
                System.out.println(pattern.substring(i, charClass(pattern, i)));
            } else if (pattern.charAt(i) == '#' && indices[i]) {
                System.out.println("COMMENT: " + pattern.substring(i, extractComment(pattern, i)));
                int result = extractComment(pattern, i);
                enableRange(i, result, comments);
                i = result;
            }

        }
        printBooleanArray(indices);
        System.out.println("COMMENTS:");
        printBooleanArray(comments);
    }

    public boolean isInCommentRange(int index) {
        return comments[index];
    }

    private void disableRange(int start, int end, boolean[] arr) {
        for(int q = start; q < end; q++) {
            arr[q] = false;
        }
    }

    private void enableRange(int start, int end, boolean[] arr) {
        for(int q = start; q < end; q++) {
            arr[q] = true;
        }
    }

    private void findSeparateModifierRange(String pattern, int i, boolean x) {
        int depth = 0;
        for(int r = i; r < pattern.length(); r++) {
            if(pattern.charAt(r)=='(') {
                depth++;
            } else if(pattern.charAt(r) == ')') {
                depth--;
            }
            indices[r] = x;
            if(depth < 0) {
                break;
            }
        }
    }

    private void findNonCapturingGroupModifierRange(String pattern, int i, boolean x) {
        int depth = 0;
        for(int r = i; r < pattern.length(); r++) {
            if(pattern.charAt(r)=='(') {
                depth++;
            } else if(pattern.charAt(r) == ')') {
                depth--;
            }
            indices[r] = x;
            if(depth == 0) {
                break;
            }
        }
    }

    private void printBooleanArray(boolean[] arr) {
        for(boolean b : arr) {
            if(b) {
                System.out.print("T");
            } else {
                System.out.print("F");
            }
        }
        System.out.println();
    }

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

    private int quotation(String pattern, int i) {
        for(int k = i; k < pattern.length(); k++) {
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

    private int charClass(String pattern, int i) {
        int depth = 0;
        int result = i;
        for(int k = i; k < pattern.length(); k++) {
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

    private boolean isMode(String pattern, int index) {
        return mode.reset(pattern.substring(index)).lookingAt();
    }

    private boolean modeHasXModifier() {
        return mode.group().contains("x");
    }

    private boolean isPositiveModifier() {
        return !mode.group().substring(0,mode.group().indexOf("x")).contains("-");
    }

    private boolean isInGroupMode(String pattern, int index) {
        return group.reset(pattern.substring(index)).lookingAt();
    }

    private boolean isPositiveInGroupModifier() {
        return !group.group().substring(0, group.group().indexOf(":")).substring(0,group.group()
                .substring(0, group.group().indexOf(":")).lastIndexOf("x")).contains("-");
    }

    private boolean inGroupModeHasXModifier() {
        return group.group().substring(0,group.group().indexOf(":")).contains("x");
    }
}
