package Controller.HighlightManager;

import Model.Expression;
import Model.Regex.Construct;
import Model.Regex.Type;
import Model.Segment;
import View.Color.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Random;

public class SectionHighlightManager {
    private final Highlighter pattern;
    private final Highlighter text;

    private DefaultHighlighter.DefaultHighlightPainter backup;
    private DefaultHighlighter.DefaultHighlightPainter selection;


    public SectionHighlightManager(Highlighter pattern, Highlighter text) {
        this.pattern = pattern;
        this.text = text;
        selection = InputColor.getPainters().get(InputColor.SELECTION);
    }

    public void reset() {
        pattern.removeAllHighlights();
        text.removeAllHighlights();
    }

    public void process(Expression expression, int correction) {
        try {
            for (Construct construct : expression.getDetailMatches().keySet()) {
                int r = new Random().nextInt(255);
                int g = new Random().nextInt(255);
                int b = new Random().nextInt(255);
                DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(r, g, b));
                for (Segment segment : expression.getDetailMatches().get(construct)) {
                    try {
                        if (segment == null || (construct.isComplex() && construct.getType() != Type.CHAR_CLASS) || construct.getType() == Type.EXPRESSION || construct.getType() == Type.COMPONENT || construct.getType() == Type.LOGICAL) {
                            if(construct.getType() == Type.COMPONENT || construct.getType() == Type.LOGICAL) {
                                pattern.addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY));
                            } else {
                                pattern.addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
                            }
                        } else {

                            pattern.addHighlight(construct.getStart(), construct.getEnd(), painter);
                            text.addHighlight(segment.getStart() - correction, segment.getEnd() - correction, painter);

                        }
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                        System.out.println(construct.getType() + "," + construct.getStart());
                    }
                }
            }
        } catch (NullPointerException ex) {
            //ex.printStackTrace();
        }

    }
}