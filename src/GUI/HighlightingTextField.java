package GUI;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class HighlightingTextField extends JTextField{
    private Highlighter highlighter;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private List<TextObserver> observers;

    public HighlightingTextField() {
        highlighter = getHighlighter();
        observers = new ArrayList<>();
        getDocument().addDocumentListener(new TextChanged());
        addKeyListener(new ClosureListener());
        setFont(new Font("Arial",Font.BOLD,20));
        setColumns(35);
    }

    public void highlightFragment(int start, int end, Color color) {
        painter = new DefaultHighlighter.DefaultHighlightPainter(color);
        try {
            highlighter.addHighlight(start, end, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void removeHighlight() {
        highlighter.removeAllHighlights();
    }

    public void addObserver(TextObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for(TextObserver o : observers) {
            o.update();
        }
    }

    private boolean backslashesAreBalanced() {
        String temp = getText().substring(0, getCaretPosition() < getText().length() ? getCaretPosition() : getText().length());
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
        int pos = getCaretPosition();
        if(backslashesAreBalanced()) {
            pos = getCaretPosition();
            setText(getText().substring(0, pos) + closeBracket + getText().substring(pos));
            setCaretPosition(pos + 1);
        }
        setCaretPosition(pos);
    }

    private char getClosingBracket(char opening) {
        switch (opening) {
            case '[':
                return ']';
            case '<':
                return '>';
            case '{':
                return '}';
            default:
                return ')';
        }
    }

    private class TextChanged implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            notifyObservers();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            notifyObservers();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            notifyObservers();
        }
    }

    private class ClosureListener extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            char typed = e.getKeyChar();
            if(typed == '(' || typed == '[' || typed == '<' || typed == '{') {
                completeBrackets(typed);
            }
        }
    }
}
