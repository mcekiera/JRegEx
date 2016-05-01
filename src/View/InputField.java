package View;

import View.Observer.Observed;
import View.Observer.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class InputField implements Observed {
    private final JTextField field;
    private final Highlighter highlighter;
    private final List<Observer> observers;

    public InputField() {
        field = new JTextField();
        highlighter = field.getHighlighter();
        field.addFocusListener(new OnFocusBorderChanger());
        observers = new ArrayList<>();
        config();
    }

    public void config() {
        Font basic = new Font("Arial",Font.BOLD,25);
        field.setFont(basic);
        field.setColumns(40);
        field.addKeyListener(new ClosureListener());
        Border border = new EmptyBorder(5,5,5,5);
        field.setBorder(border);
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

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
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
