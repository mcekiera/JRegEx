package View;

import Model.Constructs.*;
import Model.Expression.Expression;
import Model.Matching.Matched;
import Model.Matching.Matching;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestField {
    JTextField field;
    Highlighter h;
    JTextArea area;
    Highlighter h2;
    List<Color> col = new ArrayList<>();

    public TestField() {
        JFrame f = new JFrame();
        field = new JTextField();
        field.getDocument().addDocumentListener(new DocList());
        h = field.getHighlighter();
        area = new JTextArea(10,20);
        h2 = area.getHighlighter();
        f.add(area, BorderLayout.CENTER);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(field, BorderLayout.NORTH);
        f.pack();
        f.setVisible(true);
        col.add(Color.cyan);
        col.add(Color.blue);
        col.add(Color.GREEN);
        col.add(Color.gray);
        col.add(Color.magenta);
        col.add(Color.orange);
        col.add(Color.ORANGE);
        col.add(Color.red);
        col.add(Color.pink);


    }

    public void highlight() {
        h.removeAllHighlights();
        h2.removeAllHighlights();
        String pattern = field.getText();
        Sequence ex = SequenceBuilder.getInstance().toComposition(pattern, Type.EXPRESSION);
        dodo(ex);
        Matching m = new Matching(pattern,area.getText());
        for(int i = m.groupCount(); i >=0; i--) {
            for (Matched n : m.getMatches(i)) {

                try {
                    h2.addHighlight(n.getStartIndex(), n.getEndIndex(), new DefaultHighlighter.DefaultHighlightPainter(col.get(i)));
                    //System.out.println(n.getStartIndex() + "      " + n.getEndIndex());
                } catch (BadLocationException e) {
                    //e.printStackTrace();
                }
            }
        }
        System.out.println("----------------------------------");
        printt(ex);
        Expression expression = new Expression();
        String input = area.getText();         //ex
        expression.reset();
        expression.getSeparateConstructsMatches(input,ex);

        for (Matched k : expression.getCurrentMatching()) {
            System.out.println(input.substring(k.getStartIndex(), k.getEndIndex()));
        }


    }

    public void printt(Sequence sequence) {
        for (Construct construct : sequence) {
            print(construct);
        }
    }

    public void print(Construct construct) {
        if(construct instanceof Quantifier) {
            print(((Quantifier) construct).getConstruct());
            System.out.println(construct.getType() + " " + construct.toString());
        } else if (construct instanceof Sequence) {
            printt((Sequence) construct);
        }else{
            System.out.println(construct.getType() + construct.toString());

        }
    }

    public void dodo(Sequence container) {
        for(Construct construct : (Iterable<Construct>)container) {
            //System.out.println("#" + construct.getClass().getName() + "#" + construct.toString());
            if(construct instanceof Sequence) {
                dodo((Sequence)construct);
            }
            if (construct instanceof Model.Constructs.Error) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    h.addHighlight(construct.getStart(), construct.getEnd(), p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }
            if (construct.getType() == Type.CHAR_CLASS) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
                    h.addHighlight(construct.getStart(), construct.getEnd(), p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }

            if (construct.getType() == Type.PREDEFINED) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.BLUE);
                    h.addHighlight(construct.getStart(), construct.getEnd(), p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }

        if (construct.getType() == Type.COMPONENT && (construct.getParent() == null || construct.getParent().getType() != Type.CHAR_CLASS)) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
                    h.addHighlight(construct.getStart(), construct.getEnd(), p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }

            if (construct.getType() == Type.MODE) {
                try {
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA);
                    h.addHighlight(construct.getStart(), construct.getEnd(), p);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }
        }
        return;

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
