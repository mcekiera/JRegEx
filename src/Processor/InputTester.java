package Processor;

import Constructs.Types.Type;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTester{
    private Map<Color,String> regexTests;

    public  InputTester() {
        regexTests = new LinkedHashMap<>();
        addRegexTest();

    }
    
    private void addRegexTest() {
        regexTests.put(new Color(255, 0, 0), "(?<=^|(?<!(\\\\\\\\){0,999}\\\\)\\(\\?(?!\\<\\w+\\>|:|=|!|<[=!]|>|[idmsuxU-]|\\))");
        regexTests.put(new Color(255, 0, 0), "(?<=^|(?<!(\\\\\\\\){0,999}\\\\)\\()([*+][?+]?|\\?\\?+)|(?<=((?<!(\\\\\\\\){0,999}\\\\)[?*+][?+]))[*?+]+|(?<=(?<!(\\\\\\\\){0,999}\\\\)\\*)\\*+");
        regexTests.put(new Color(200, 0, 200), Type.MODE.getRegex());
        regexTests.put(new Color(255, 150, 0), "(?<!(\\\\\\\\){0,999}\\\\)" + Type.CHAR_CLASS.getRegex());
        regexTests.put(new Color(100, 100, 255), Type.PREDEFINED.getRegex() +
                "|" + Type.SPECIFIC_CHAR.getRegex() +
                "|" + "(?<!(\\\\\\\\){0,999}\\\\)\\|");
        regexTests.put(new Color(200, 200, 200), Type.QUOTATION.getRegex());
        regexTests.put(new Color(100, 255, 100),
                "(?<!(\\\\\\\\){0,999}\\\\)(\\((\\?(<(\\w+>|=|!)|=|!|>|[idmsuxU-]*:))?|\\))(" +
                        Type.QUANTIFIER.getRegex() + ")?");
    }

    public List<Highlight> getRegExElements(String pattern) {
        Matcher matcher;
        List<Highlight> result = new ArrayList<Highlight>();
        for(Color color : regexTests.keySet()) {
            matcher = Pattern.compile(regexTests.get(color)).matcher(pattern);
            while(matcher.find()) {
                result.add(new Highlight(matcher.start(), matcher.end(), color));
            }

        }
        return result;
    }

    public List<Highlight> getUnbalancedBrackets(String patter) {
        java.util.List<Integer> temp = new ArrayList<Integer>();
        temp.addAll(getUnbalancedParentheses(patter));
        temp.addAll(getUnbalancedSquareBrackets(patter));
        List<Highlight> result = new ArrayList<Highlight>();
        for(Integer i : temp) {
            result.add(new Highlight(i, i+1, Color.RED));
        }
        return result;
    }

    private java.util.List<Integer> getUnbalancedParentheses(String patter) {
        Stack<Integer> start = new Stack<Integer>();
        Stack<Integer> end = new Stack<Integer>();
        for (int i = 0; i < patter.length(); i++) {
            char ch = patter.charAt(i);
            if(ch == '\\') {
                i++;
                continue;
            }
            if(ch == '(') {
                start.push(i);
            }else if(ch == ')') {
                if(start.size()!=0) {
                    start.pop();
                }else {
                    end.push(i);
                }
            }
        }
        java.util.List<Integer> result = new ArrayList<>(start);
        result.addAll(end);
        return result;
    }

    private java.util.List<Integer> getUnbalancedSquareBrackets(String str) {
        Stack<Integer> start = new Stack<Integer>();
        Stack<Integer> end = new Stack<Integer>();
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if(ch == '\\') {
                i++;
                continue;
            }
            if(ch == '[') {
                start.push(i);
            }else if(ch == ']') {
                if(start.size()!=0 && start.peek()!= i-1) {
                    start.pop();
                }else {
                    end.push(i);
                }
            }
        }
        java.util.List<Integer> result = new ArrayList<>(start);
        result.addAll(end);
        return result;
    }
}
