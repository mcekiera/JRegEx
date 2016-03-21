package GUI;

import Constructs.Types.Type;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexInputController{
    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;
    private JTextField field;
    private Matcher matcher;
    private Map<Color,String> inputTest;

    public RegexInputController(JTextField field) {
        this.field = field;
        highlighter = this.field.getHighlighter();
        config();
        fillMap();
    }

    private void config() {
        field.getDocument().addDocumentListener(new TextListener());
        field.registerKeyboardAction(new ParenthesesListener(), KeyStroke.getKeyStroke('('), JComponent.WHEN_FOCUSED);
        field.registerKeyboardAction(new SquareBracketListener(), KeyStroke.getKeyStroke('['), JComponent.WHEN_FOCUSED);
        field.setFont(new Font("Arial", Font.BOLD, 26));
        field.setColumns(40);
    }

    private void fillMap(){
        inputTest = new LinkedHashMap<>();
        inputTest.put(new Color(255, 0, 0), "(?<=^|(?<!(\\\\\\\\){0,999}\\\\)\\(\\?(?!\\<\\w+\\>|:|=|!|<[=!]|>|[idmsuxU-]|\\))");
        inputTest.put(new Color(255, 0, 0), "(?<=^|(?<!(\\\\\\\\){0,999}\\\\)\\()[?*+][?+]?|(?<=((?<!(\\\\\\\\){0,999}\\\\)[?*+][?+]))[*?+]+|(?<=(?<!(\\\\\\\\){0,999}\\\\)\\*)\\*+");
        inputTest.put(new Color(200, 0, 200), Type.MODE.getRegex());
        inputTest.put(new Color(255, 150, 0), "(?<!(\\\\\\\\){0,999}\\\\)" + Type.CHAR_CLASS.getRegex());
        inputTest.put(new Color(100, 100, 255), Type.PREDEFINED.getRegex() +
                "|" + Type.SPECIFIC_CHAR.getRegex() +
                "|" + "(?<!(\\\\\\\\){0,999}\\\\)\\|");
        inputTest.put(new Color(200, 200, 200), Type.QUOTATION.getRegex());
        inputTest.put(new Color(100, 255, 100),
                "(?<!(\\\\\\\\){0,999}\\\\)(\\((\\?(<(\\w+>|=|!)|=|!|>|[idmsuxU-]*:))?|\\))(" +
                        Type.QUANTIFIER.getRegex() + ")?");
    }

    private void highlightUnbalancedBrackets() {
        for(int index : getUnbalancedBrackets(field.getText())) {
            painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,65,65));
            try {
                highlighter.addHighlight(index, index + 1, painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<Integer> getUnbalancedBrackets(String patter) {
        List<Integer> result = new ArrayList<Integer>();
        result.addAll(getUnbalancedParentheses(patter));
        result.addAll(getUnbalancedSquareBrackets(patter));
        return result;
    }

    private List<Integer> getUnbalancedParentheses(String patter) {
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
        List<Integer> result = new ArrayList<>(start);
        result.addAll(end);
        return result;
    }

    private List<Integer> getUnbalancedSquareBrackets(String str) {
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
        List<Integer> result = new ArrayList<>(start);
        result.addAll(end);
        return result;
    }

    private void highlightElement(String regex, String text, Color color) {
        matcher = Pattern.compile(regex).matcher(text);
        painter = new DefaultHighlighter.DefaultHighlightPainter(color);
        while(matcher.find()) {
            try {
                highlighter.addHighlight(matcher.start(), matcher.end(), painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void highlightRegexElements() {
        for(Color color : inputTest.keySet()) {
             highlightElement(inputTest.get(color),field.getText(),color);
        }
    }

    private boolean backslashesAreBalanced() {
        String temp = field.getText().substring(0, field.getCaretPosition() < field.getText().length() ? field.getCaretPosition() : field.getText().length());
        int sum = 0;
        for(char ch : temp.toCharArray()) {
            if(ch=='\\') {
                sum++;
            } else {
                sum = 0;
            }

        }
        return sum%2==0;
    }

    private void completeBrackets(char openBracket) {
        char closeBracket = (openBracket == '(' ? ')' : ']');
        int pos = 0;
        if(backslashesAreBalanced()) {
            pos = field.getCaretPosition();
            field.setText(field.getText().substring(0, pos) + openBracket + closeBracket + field.getText().substring(pos));
            field.setCaretPosition(pos + 1);
        } else {
            field.setText(field.getText().substring(0, field.getCaretPosition()) + openBracket + field.getText().substring(field.getCaretPosition()));
        }
    }

    private void update(){
        highlighter.removeAllHighlights();
        highlightUnbalancedBrackets();
        highlightRegexElements();
        try {
            Pattern p = Pattern.compile(field.getText());
        }catch (PatternSyntaxException ex) {
            System.out.println(ex.getDescription());
            System.out.println(ex.getIndex());
            System.out.println(ex.getPattern());
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());

        }

    }



    private class ParenthesesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            completeBrackets('(');
        }
    }

    private class SquareBracketListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            completeBrackets('[');
        }
    }

    private class TextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }
    }


}
