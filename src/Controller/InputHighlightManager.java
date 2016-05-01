package Controller;

import Model.Regex.*;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.HashMap;
import java.util.Map;

public class InputHighlightManager{
    private final Highlighter highlighter;
    private final Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> painters;
    private final Map<Integer,DefaultHighlighter.DefaultHighlightPainter> groupPainters;
    int count;

    public InputHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        painters = getInputPainters();
        groupPainters = getGroupPainters();
    }

    public void process(Composite composite) {
        highlighter.removeAllHighlights();
        count = 1;
        highlight(composite, count);
    }

    private void highlight(Complex composite, int level) {
        for(Construct construct : composite) {
            DefaultHighlighter.DefaultHighlightPainter painter = getColorByType(construct.getType(),level);    //todo ochydny test logiczny
            if(painter!=null && (!construct.isComplex() || construct instanceof Quantifier || construct.getType() == Type.CHAR_CLASS)) {
                try {
                    highlighter.addHighlight(construct.getStart(),construct.getEnd(),painter);
                } catch (BadLocationException e) {

                    e.printStackTrace();
                }
            }
            if(construct.isComplex() && construct.getType()!=Type.CHAR_CLASS) {
                highlight((Complex) construct, count++);
            }
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
                int c = level > 12 ? level%12 : level;
                if(c != 0) return groupPainters.get(c);
            default:
                return null;

        }
    }

    private Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> getInputPainters() {
        Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        for(InputColor color : InputColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
        }
        return temp;
    }

    private Map<Integer,DefaultHighlighter.DefaultHighlightPainter> getGroupPainters() {
        Map<Integer,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        int i = 0;
        for(GroupColor color : GroupColor.values()) {
            temp.put(i,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
            i++;
        }
        return temp;
    }
}
