package Controller.HighlightManager;

import Model.Expression;
import Model.Regex.Construct;
import Model.Regex.Type;
import Model.Segment;
import View.Color.ColorLib;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

/**
 * Controls highlighting of JTextComponent using its Highlighter object. It diverse color of highlight on
 * Type property of given Construct represented in text. It controls highlighting of comparing fields.
 */
public class SectionHighlightManager {
    /**
     * Highlighter of field for pattern.
     */
    private final Highlighter pattern;
    /**
     * Highlighter of field for example matched text.
     */
    private final Highlighter text;
    /**
     * Library of colors.
     */
    private final ColorLib lib;

    /**
     * Constructor of class.
     * @param pattern Highlighter of field displaying regular expression.
     * @param text Highlighter of field displaying matched text.
     */
    public SectionHighlightManager(Highlighter pattern, Highlighter text) {
        this.pattern = pattern;
        this.text = text;
        lib = ColorLib.getInstance();
    }

    /**
     * Reset object data to prepare it for new task.
     */
    public void reset() {
        pattern.removeAllHighlights();
        text.removeAllHighlights();
        lib.reset();
    }

    /**
     * Process given Expression object, by iterating through its components, and retrieve detail match data, to highlight
     * fragments of test text matched by separate constructs within pattern.
     * @param expression to process.
     * @param correction as matched examples start index could be different than 0, for proper highlighting of
     *                   separated text the correction of highlights start and end indexes is necessary.
     */
    public void process(Expression expression, int correction) {
        try {
            for (Construct construct : expression.getDetailMatches().keySet()) {
                DefaultHighlighter.DefaultHighlightPainter painter;
                for (Segment segment : expression.getDetailMatches().get(construct)) {
                    try {
                        if (segment == null || (construct.isComplex() && construct.getType() != Type.CHAR_CLASS)
                                || construct.getType() == Type.EXPRESSION || construct.getType() == Type.COMPONENT
                                || construct.getType() == Type.LOGICAL || construct.getType() == Type.COMMENT) {
                            if(construct.getType() == Type.COMPONENT || construct.getType() == Type.LOGICAL) {
                                pattern.addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY));
                            } else {
                                pattern.addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
                            }
                        } else {
                            painter = new DefaultHighlighter.DefaultHighlightPainter(lib.getNextColor());
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
