package Controller.HighlightManager;

import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Quantifier;
import Model.Regex.Type.Type;
import View.Color.ClassColor;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 * Controls highlighting of JTextComponent using its Highlighter object. It diverse color of highlight on
 * Type property of given Construct represented in text.
 */
public class InputHighlightManager extends HighlightManager{
    /**
     * Highlighter of chosen JTextComponent
     */
    private final Highlighter highlighter;
    /**
     * Currently selected in text representation of Construct object.
     */
    private Construct selected;
    /**
     * Counter which serves to differentiate highlight color of groups and other complex structures.
     */
    int count = 0;
    /**
     * Composite object which representation in text is currently highlighted.
     */
    private Composite current;

    public InputHighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    /**
     * Highlights the representation of given Construct in text.
     * @param composite Composite object
     */
    public void process(Composite composite) {
        current = composite;
        highlighter.removeAllHighlights();
        highlight(composite, count);
        count = 0;
    }

    /**
     * Highlights given representation.
     * @param composite Complex interface implementing object
     * @param group ordinal number of complex object, serves to color selection
     */
    private void highlight(Complex composite, int group) {
        for(Construct construct : composite) {
            highlightConstruct(construct,group);
            highlightGroup(construct);
        }

    }

    /**
     * Highlights given representation of construct.
     * @param construct Construct object
     * @param group ordinal number of complex object, serves to color selection
     */
    private void highlightConstruct(Construct construct, int group) {
        DefaultHighlighter.DefaultHighlightPainter painter = getPainter(construct, group);
        if(construct.equals(selected)) {
            try {
                highlighter.addHighlight(construct.getStart(),construct.getEnd(), InputColor.getPainters().get(InputColor.SELECTION));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        if(painter !=null && (!construct.isComplex() || construct instanceof Quantifier)) {
            try {
                highlighter.addHighlight(construct.getStart(),construct.getEnd(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Highlights given representation of construct.
     * @param construct Construct object
     */
    private void highlightGroup(Construct construct) {
        if(construct.isComplex()) {
            if(construct.getType() != Type.CHAR_CLASS && construct.getType() != Type.ALTERNATION && !(construct instanceof Quantifier)){ count++;}        //TODO boolean ;(
            highlight((Complex) construct, count);
        }
    }

    /**
     * Provide Painter object, with chosen by Construct Type, color.
     * @param construct object which representation will be highlighted.
     * @param group ordinal number of complex object, serves to color selection
     * @return DefaultHighlightPainter object
     */
    private DefaultHighlighter.DefaultHighlightPainter getPainter(Construct construct, int group) {
        if(isComponentOfSelectedComposite(construct)) {
            return InputColor.getPainters().get(InputColor.SELECTION);
        } else if(construct.getParent().getType() == Type.CHAR_CLASS) {
            return getColorInClassByType(construct.getType());
        } else {
            return getColorByType(construct.getType(), group);
        }
    }

    /**
     * Provide Painter of color selected by Type parameter and int group.
     * @param type Type representing categories of regular expression constructs
     * @param group ordinal number of complex object, serves to color selection
     * @return DefaultHighlightPainter with selected color
     */
    private DefaultHighlighter.DefaultHighlightPainter getColorByType(Type type, int group) {
        switch (type) {
            case ERROR:
            case INCOMPLETE:
            case UNBALANCED:
            case INVALID_BACKREFERENCE:
            case INVALID_INTERVAL:
            case INVALID_RANGE:
                return InputColor.getPainters().get(InputColor.ERROR);
            case CHAR_CLASS:
                return InputColor.getPainters().get(InputColor.CHAR_CLASS);
            case MODE:
                return InputColor.getPainters().get(InputColor.MODE);
            case PREDEFINED:
            case QUANTIFIER:
            case INTERVAL:
            case SPECIFIC_CHAR:
            case BOUNDARY:
                return InputColor.getPainters().get(InputColor.PREDEFINED);
            case COMPONENT:
                int c = group >= GroupColor.values().length ? (group % GroupColor.values().length) : group;
                return GroupColor.getPainters().get(c);
            default:
                return null;

        }
    }

    /**
     * Provide Painter of color selected by Type parameter and int group for construct within character class
     * constructs.
     * @param type Type representing categories of regular expression constructs
     * @return DefaultHighlightPainter with selected color
     */
    public DefaultHighlighter.DefaultHighlightPainter getColorInClassByType(Type type) {
        switch (type) {
            case COMPONENT:
            case RANGE:
            case PREDEFINED:
            case SPECIFIC_CHAR:
                return ClassColor.getPainters().get(ClassColor.SPECIAL);
            case INVALID_RANGE:
                return ClassColor.getPainters().get(ClassColor.ERROR);
            default:
                return ClassColor.getPainters().get(ClassColor.NORMAL);

        }
    }

    /**
     * Defines, if given Construct is element of selected Composite object.
     * @param construct Construct object
     * @return true if given Construct is element of selected Composite object.
     */
    private boolean isComponentOfSelectedComposite(Construct construct) {
        return selected != null && selected.getParent() == construct.getParent()
                && construct.getType() == Type.COMPONENT;
    }

    @Override
    public void selectionHighlight(int position) {
        try {
            selected = current.getConstructFromPosition(position);
            process(current);
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }
}
