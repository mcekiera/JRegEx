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
 * Adds to wrapped JTextArea additional feature: auto-completion of brackets and observers notifying on user input.
 */
public class InputArea implements Observed {
    /**
     * Wrapped JTextArea.
     */
    private final JTextArea area;
    /**
     * List of observers.
     */
    private final List<Observer> observers;

    public InputArea() {
        area = new JTextArea();
        observers = new ArrayList<>();
        config();
    }

    /**
     * Configures display properties of area.
     */
    public void config() {
        Font basic = new Font("Arial",Font.BOLD,25);
        area.setFont(basic);
        area.addKeyListener(new ClosureListener());
        Border border = new EmptyBorder(5,5,5,5);
        area.setBorder(border);
    }

    /**
     * @return wrapped area.
     */
    public JTextArea getArea() {
        return area;
    }

    /**
     * Determine if given bracket is escaped with backslash, and therefore should not be completed.
     * @return true if backslashes are balanced.
     */
    private boolean backslashesAreBalanced() {
        String temp = area.getText().substring(0, area.getCaretPosition() < area.getText().length() ? area.getCaretPosition() : area.getText().length());
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
        int pos = area.getCaretPosition();
        if(backslashesAreBalanced()) {
            if(textIsSelected()) {
                area.setText(area.getText().substring(0, area.getSelectionStart()) +
                        area.getSelectedText() + closeBracket + area.getText().substring(area.getSelectionEnd()));
                area.setCaretPosition(area.getSelectionEnd());
            } else {
                area.setText(area.getText().substring(0, pos) + closeBracket + area.getText().substring(pos));
                area.setCaretPosition(pos + 1);
            }
        }
        area.setCaretPosition(pos);
    }

    /**
     * Determines if any part of text area content is selected.
     * @return true if is selected.
     */
    private boolean textIsSelected() {
        return area.getSelectedText()!=null;
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
