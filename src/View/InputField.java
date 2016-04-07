package View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputField {
    private final JTextField field;
    private final Highlighter highlighter;

    public InputField() {
        field = new JTextField();
        highlighter = field.getHighlighter();
        config();
    }

    public void config() {
        Font basic = new Font("Arial",Font.BOLD,25);
        field.setFont(basic);
        field.setColumns(40);
        field.addKeyListener(new ClosureListener());
    }

    public JTextField getField() {
        return field;
    }

    public Highlighter getHighlighter() {
        return highlighter;
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
        char closeBracket = getClosingBracket(openBracket);
        int pos = field.getCaretPosition();
        if(backslashesAreBalanced()) {
            if(textIsSelected()) {
                field.setText(field.getText().substring(0, field.getSelectionStart()) +
                        field.getSelectedText() + closeBracket + field.getText().substring(field.getSelectionEnd()));
                field.setCaretPosition(field.getSelectionEnd());
            } else {
                field.setText(field.getText().substring(0, pos) + closeBracket + field.getText().substring(pos));
                field.setCaretPosition(pos + 1);
            }
        }
        field.setCaretPosition(pos);
    }

    private boolean textIsSelected() {
        return field.getSelectedText()!=null;
    }

    private char getClosingBracket(char opening) {
        switch (opening) {
            case '[':
                return ']';
            case '{':
                return '}';
            default:
                return ')';
        }
    }

    private class TextChanged implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {

        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    private class ClosureListener extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            char typed = e.getKeyChar();
            if(typed == '(' || typed == '[' || typed == '{') {
                completeBrackets(typed);
            }
        }
    }
}
