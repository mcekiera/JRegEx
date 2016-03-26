package GUI;

import Processor.Highlight;
import Processor.TextObserver;
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

public class InputFieldWrapper{
    private final JTextField field;
    private Highlighter highlighter;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private List<TextObserver> observers;

    public InputFieldWrapper() {
        field = new JTextField();
        highlighter = field.getHighlighter();
        observers = new ArrayList<>();
        field.getDocument().addDocumentListener(new TextChanged());
        field.addKeyListener(new ClosureListener());
        field.setFont(new Font("Arial", Font.BOLD, 20));
        field.setColumns(35);
    }

    public String getText() {
        return field.getText();
    }

    public JComponent getField() {
        return field;
    }

    public void highlightFragment(List<Highlight> highlights) {
        for(Highlight h : highlights) {

            painter = new DefaultHighlighter.DefaultHighlightPainter(h.getColor());
            try {
                highlighter.addHighlight(h.getStart(), h.getEnd(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeHighlight() {
        highlighter.removeAllHighlights();
    }

    public void addObserver(TextObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        observers.forEach(Processor.TextObserver::update);
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
            if(typed == '(' || typed == '[' || typed == '{') {
                completeBrackets(typed);
            }
        }
    }
}
