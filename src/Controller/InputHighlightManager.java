package Controller;

import Model.Regex.*;
import Model.Regex.Composite;
import View.Color.ClassColor;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Map;

public class InputHighlightManager{
    private final Highlighter highlighter;
    private final Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> painters;
    private final Map<Integer,DefaultHighlighter.DefaultHighlightPainter> groupPainters;
    DefaultHighlighter.DefaultHighlightPainter painter;;
    private Construct selected;
    int count = 0;
    private Composite current;

    public InputHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        painters = InputColor.getPainters();
        groupPainters = GroupColor.getPainters();
    }

    public void process(Composite composite) {
        current = composite;
        highlighter.removeAllHighlights();
        highlight(composite, count);
        count = 0;
    }

    public void underline(int i) {
        selected = current.getConstructFromPosition(i);
        process(current);
    }

    private void highlight(Complex composite, int level) {
        for(Construct construct : composite) {
            highlightConstruct(construct,level);
            highlightGroup(construct);
        }

    }

    private void highlightConstruct(Construct construct, int level) {
        painter = getPainter(construct,level);
        if(painter!=null && (!construct.isComplex() || construct instanceof Quantifier)) {
            try {
                highlighter.addHighlight(construct.getStart(),construct.getEnd(),painter);
            } catch (BadLocationException e) {

                e.printStackTrace();
            }
        }
    }

    private void highlightGroup(Construct construct) {
        if(construct.isComplex()) {
            if(construct.getType() != Type.CHAR_CLASS){ count++;}
            highlight((Complex) construct, count);
        }
    }

    private DefaultHighlighter.DefaultHighlightPainter getPainter(Construct construct, int level) {
        if(selected != null && selected.getParent() == construct.getParent() && construct.getType() == Type.COMPONENT) {
            return new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        } else if(construct.getParent().getType() == Type.CHAR_CLASS) {
            return getColorInClassByType(construct.getType());
        } else {
            return getColorByType(construct.getType(), level);
        }
    }

    private DefaultHighlighter.DefaultHighlightPainter getColorByType(Type type, int level) {
        switch (type) {
            case ERROR:
            case INCOMPLETE:
            case UNBALANCED:
            case INVALID_BACKREFERENCE:
            case INVALID_INTERVAL:
            case INVALID_RANGE:
                return painters.get(InputColor.ERROR);
            case CHAR_CLASS:
                return painters.get(InputColor.CHAR_CLASS);
            case MODE:
                return painters.get(InputColor.MODE);
            case PREDEFINED:
            case QUANTIFIER:
            case INTERVAL:
            case SPECIFIC_CHAR:
            case BOUNDARY:
                return painters.get(InputColor.PREDEFINED);
            case COMPONENT:
                int c = level >= GroupColor.values().length ? (level % GroupColor.values().length) : level;
                return groupPainters.get(c);
            default:
                return null;

        }
    }

    public DefaultHighlighter.DefaultHighlightPainter getColorInClassByType(Type type) {
        switch (type) {
            case COMPONENT:
            case RANGE:
            case PREDEFINED:
            case SPECIFIC_CHAR:
                return ClassColor.getPainters().get(ClassColor.SPECIAL);
            default:
                return ClassColor.getPainters().get(ClassColor.NORMAL);

        }
    }
}
