package Controller;

import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Type;
import View.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.HashMap;
import java.util.Map;

public class InputHighlightManager{
    private final Highlighter highlighter;
    private final Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> painters;
    private DefaultHighlighter.DefaultHighlightPainter painter;

    public InputHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
        painters = getPainters();
    }

    public void process(Composite composite) {
        highlighter.removeAllHighlights();
        highlight(composite);
    }

    private void highlight(Composite composite) {
        for(Construct construct : composite) {
            painter = getColorByType(construct.getType());
            if(painter!=null) {
                System.out.println("INSIDE: " + composite.toString());
                try {
                    highlighter.addHighlight(construct.getStart(),construct.getEnd(),painter);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

            }
            if(construct.isComplex() && construct.getType()!=Type.CHAR_CLASS) {
                highlight((Composite) construct);
            }
        }
    }

    private DefaultHighlighter.DefaultHighlightPainter getColorByType(Type type) {
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
            case SPECIFIC_CHAR:
            case BOUNDARY:
                return painters.get(InputColor.PREDEFINED);
            case COMPONENT:

            default:
                return null;

        }
    }

    private Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        for(InputColor color : InputColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
        }
        return temp;
    }
}
