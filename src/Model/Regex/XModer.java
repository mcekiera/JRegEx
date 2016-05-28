package Model.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Determine if in given regular expression pattern occurs ?x modifier. TODO check if \\ is processed correctly
 */
public class XModer {
    private final static XModer INSTANCE = new XModer();
    private final Matcher mode = Pattern.compile("\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?\\)").matcher("");
    private final Matcher group = Pattern.compile("(?s)\\(\\?([imdsuxU]+(-[imdsuxU]+)?|-[imdsuxU]+)?:.*\\)").matcher("");
    private boolean[] indices;
    private boolean[] comments;

    private XModer() {
    }

    public static XModer getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        String[] pattern = {"(?x)a   #aa"};
        String[] result = {
                "FFFFFTTTTTT"
        };
        XModer XModer = new XModer();
        for(int i = 0; i < pattern.length; i++) {
            System.out.println("__________________");
            System.out.println(pattern[i]);
            XModer.process(pattern[i]);
        }
    }

    public void process(String pattern) {

        indices = new boolean[pattern.length()];
        comments  = new boolean[pattern.length()];

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.substring(i).startsWith("\\Q")) {
                disableRange(i, quotation(pattern, i), indices);
            } else if (isMode(pattern, i)) {
                if (modeHasXModifier()) {
                    findModifierRange(pattern, i, isPositiveModifier(), Type.MODE);
                }
            } else if (isInGroupMode(pattern, i)) {
                if (inGroupModeHasXModifier()) {
                    findModifierRange(pattern, i, isPositiveInGroupModifier(), Type.NON_CAPTURING);
                }
            } else if (pattern.charAt(i) == '[') {
                disableRange(i, charClass(pattern, i),indices);
            } else if (pattern.charAt(i) == '#' && indices[i]) {
                int result = extractComment(pattern, i);
                enableRange(i, result, comments);
                i = result;
            } else if (Character.isWhitespace(pattern.charAt(i)) && indices[i]) {
                int result = extractWhitespace(pattern,i);
                enableRange(i, result, comments);
                i = result-1;
            }

        }
        System.out.println("_______");
        System.out.println(printArray(indices));
        System.out.println(printArray(comments));
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
