package View;

import View.Observer.Observed;
import View.Observer.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds to wrapped JTextField additional feature: auto-completion of brackets and observers notifying on user input.
 */
public class InputField implements Observed {
    /**
     * Wrapped JTextField.
     */
    private final JTextField field;
    /**
     * List of observers.
     */
    private final List<Observer> observers;

    public InputField() {
        field = new JTextField();
        observers = new ArrayList<>();
        config();
    }

    /**
     * Configures display properties of field.
     */
    public void config() {
        Font basic = new Font("Arial",Font.BOLD,25);
        field.setFont(basic);
        field.addKeyListener(new ClosureListener());
        Border border = new EmptyBorder(5,5,5,5);
        field.setBorder(border);
    }

    /**
     * @return wrapped field.
     */
    public JTextField getField() {
        return field;
    }

    /**
     * Determine if given bracket is escaped with backslash, and therefore should not be completed.
     * @return true if backslashes are balanced.
     */
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

    /**
     * Complete given type of brackets, for example, if '[' is passed, the ']' will be added. Similar with '(' and '{'
     * @param openBracket of type of bracket to complete.
     */
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

    /**
     * Determines if any part of text field content is selected.
     * @return true if is selected.
     */
    private boolean textIsSelected() {
        return field.getSelectedText()!=null;
    }

    /**
     * @param opening opening bracket of given type.
     * @return proper type of closing bracket.
     */
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

    /**
     * Listener for chosen type of keys, serves for bracket completion.
     */
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
