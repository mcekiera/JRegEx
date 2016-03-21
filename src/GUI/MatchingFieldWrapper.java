package GUI;

import Processor.TextObserver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MatchingFieldWrapper{
    private final JTextArea area;
    private DefaultHighlighter.DefaultHighlightPainter painter;
    private Highlighter highlighter;
    private List<TextObserver> observers;

    public MatchingFieldWrapper() {
        area = new JTextArea();
        observers = new ArrayList<>();
        highlighter = area.getHighlighter();
        area.getDocument().addDocumentListener(new UpdateListener());
        area.setColumns(35);
        area.setRows(20);
        area.setFont(new Font("Arial",Font.BOLD,20));
    }

    public String getText() {
        return area.getText();
    }

    public JComponent getField() {
        return area;
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

    public void notifyObservers() {
        for(TextObserver observer : observers) {
            observer.update();
        }
    }

    public void addObserver(TextObserver observer) {
        observers.add(observer);
    }

    private class UpdateListener implements DocumentListener {

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
}
