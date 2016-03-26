package GUI;

import Model.Constructs.Construct;
import Model.Constructs.Types.CharClass;
import Model.Constructs.Types.Error.Error;
import Model.Expression;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public class TestField {
    JTextField field;
    Highlighter h;

    public TestField() {
        JFrame f = new JFrame();
        field = new JTextField();
        field.getDocument().addDocumentListener(new DocList());
        h = field.getHighlighter();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(field);
        f.pack();
        f.setVisible(true);


    }

    public void highlight() {
        h.removeAllHighlights();
        String pattern = field.getText();
        Expression ex = new Expression(pattern);

        for(Construct construct : ex) {
            if(construct instanceof Error) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    h.addHighlight(construct.getStart(),construct.getEnd(),p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }
            if(construct instanceof CharClass) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
                    h.addHighlight(construct.getStart(),construct.getEnd(),p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public class DocList implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
             highlight();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
             highlight();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            highlight();
        }
    }

    public static void main(String[] args) {
        TestField f = new TestField();
    }
}
