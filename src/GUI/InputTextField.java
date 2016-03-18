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
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTextField extends JTextField {
    Highlighter highlighter;
    Highlighter.HighlightPainter painter;

    public InputTextField() {
        highlighter = this.getHighlighter();
        getDocument().addDocumentListener(new TextListener());
        registerKeyboardAction(new Backslash(), KeyStroke.getKeyStroke('('), JComponent.WHEN_FOCUSED);
        setFont(new Font("Arial", Font.BOLD, 16));
        setColumns(30);

    }

    private void highlightSingleBracket() {
        for(int i : parenthesisBalanceCheck(getText())) {
            painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,100,100));
            try {
                highlighter.addHighlight(i, i + 1, painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    }

    private List<Integer> parenthesisBalanceCheck(String str) {
        Stack<Integer> start = new Stack<Integer>();
        Stack<Integer> end = new Stack<Integer>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
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

    private void predefineCheck(String text) {
        Matcher m = Pattern.compile(Type.PREDEFINED.getRegex() +
                "|" + Type.QUANTIFIER.getRegex() +
                "|" + Type.SPECIFIC_CHAR.getRegex() +
                "|" + "(?<!(\\\\\\\\){0,999}\\\\)\\|").matcher(text);
        painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(100,200,255));
        while(m.find()) {
            try {
                highlighter.addHighlight(m.start(), m.end(), painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void quotationCheck(String text) {
        Matcher m = Pattern.compile(Type.QUOTATION.getRegex()).matcher(text);
        painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(200,200,200));
        while(m.find()) {
            try {
                highlighter.addHighlight(m.start(), m.end(), painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void modeCheck(String text) {
        Matcher m = Pattern.compile(Type.MODE.getRegex()).matcher(text);
        painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(200,0,200));
        while(m.find()) {
            try {
                highlighter.addHighlight(m.start(), m.end(), painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void chartClassCheck(String text) {
            Matcher m = Pattern.compile("(?<!(\\\\\\\\){0,999}\\\\)" + Type.CHAR_CLASS.getRegex()).matcher(text);
            painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 200, 0));
            while (m.find()) {
                try {
                    highlighter.addHighlight(m.start(), m.end(), painter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
    }

    private void groupCheck(String text) {
        Matcher m = Pattern.compile("\\((\\?(<\\w+>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|<))?|(?<!\\\\)\\)(" + Type.QUANTIFIER.getRegex() + ")?").matcher(text);
        painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(100,255,100));
        while(m.find()) {
            try {
                highlighter.addHighlight(m.start(), m.end(), painter);
            }catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean backslahBalance() {
        String temp = getText().substring(0,getCaretPosition() < getText().length() ? getCaretPosition() : getText().length());
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



    private void fire(){
        highlighter.removeAllHighlights();
        modeCheck(getText());
        highlightSingleBracket();
        chartClassCheck(getText());
        groupCheck(getText());
        predefineCheck(getText());
        quotationCheck(getText());


    }

    private class Backslash implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(backslahBalance()) {
                setText(getText().substring(0, getCaretPosition()) + "()" + getText().substring(getCaretPosition()));
                setCaretPosition(getCaretPosition() - 1);
            } else {
                setText(getText().substring(0, getCaretPosition()) + "(" + getText().substring(getCaretPosition()));
            }
        }
    }

    private class TextListener implements DocumentListener {
        /**
         * Reacts on text insertion
         */
        @Override
        public void insertUpdate(DocumentEvent e) {

            fire();
        }

        /**
         * Reacts on text removal
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            fire();
        }

        /**
         * Reacts on changes in text
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            fire();
        }
    }


}
