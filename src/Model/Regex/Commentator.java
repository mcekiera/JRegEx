package Model.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Determine if in given regular expression pattern occurs ?x modifier.
 */
public class Commentator {
    boolean[] indices;
    Matcher mode = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?\\)").matcher("");
    Matcher group = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?:.*\\)").matcher("");

    public static void main(String[] args) {
        String[] pattern = {"(?x)\\Q2#34\\E#678","(?x)\\Q2#34\\E#678aa\n(?-x)aaaaa(?x:aa(?-x:uu)a#a)bc[abc&&[#abc]]ab#",
                "(?x)aaaa(?-x:#)bbb#ccccc", "(?x)abc  #  abc\ncde   #   cde\nefg   #   efg"};
        Commentator Commentator = new Commentator();
        for(String p : pattern) {
            System.out.println("__________________");
            System.out.println(p);
            Commentator.process(p);
        }
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

    public void process(String pattern) {

        indices = new boolean[pattern.length()];
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.substring(i).startsWith("\\Q")) {
                disableComments(i, quotation(pattern, i));
                System.out.println(pattern.substring(i, quotation(pattern, i)));
            } else if (isMode(pattern, i)) {
                if (modeHasXModifier()) {
                    findSeparateModifierRange(pattern, i, isPositiveModifier());
                }
            } else if (isInGroupMode(pattern, i)) {
                if (inGroupModeHasXModifier()) {
                    findNonCapturingGroupModifierRange(pattern, i, isPositiveInGroupModifier());
                } else if (pattern.charAt(i) == '[') {
                    disableComments(i, charClass(pattern, i));
                    System.out.println(pattern.substring(i, charClass(pattern, i)));
                } else if (pattern.charAt(i) == '#' && indices[i]) {
                    System.out.println(pattern.substring(i, extractComment(pattern, i)));
                    i = extractComment(pattern, i);
                }

            }
            printArray();
        }
    }

    private void disableComments(int start, int end) {
        for(int q = start; q < end; q++) {
            indices[q] = false;
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

    private void printArray() {
        for(boolean b : indices) {
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
}
