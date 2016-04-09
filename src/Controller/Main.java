package Controller;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Expression.Expression;
import Model.Matching.Matched;
import View.*;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.regex.PatternSyntaxException;

public class Main implements Observer {
    private final ColorPalette palette = ColorPalette.getInstance();
    private static int level = 0;

    private Expression expression;
    private UserInterface ui;


    public Main() {
        expression = new Expression();
        ui = new UserInterface();
        ui.addObserver(this);
    }


    @Override
    public void update(Observed source) {
        palette.reset();
        if(expression.use(ui.getInputText(), ui.getMatchingText())) {
            expression.getSeparateConstructsMatches(ui.getMatchingText());
            highlightMatches();
        }
        highlightInput();
    }

    public void highlightInput() {
        ui.getInputHighlighter().removeAllHighlights();
        highlightPattern(expression.getSequence());
    }

    private void highlightPattern(Sequence sequence) {
        for(Construct construct : sequence) {
            if(Construct.isComposed(construct)) {
                level++;
                highlightPattern((Sequence)construct);
            } else {
                 highlightByType(construct);
            }
        }
        if (level > 0) level--;
    }

    private void highlightByType(Construct construct) {
        Highlighter h = ui.getInputHighlighter();
        DefaultHighlighter.DefaultHighlightPainter painter;
        switch (construct.getType()) {
            case ERROR:
            case INCOMPLETE:
            case UNBALANCED:
            case INVALID_BACKREFERENCE:
            case INVALID_INTERVAL:
            case INVALID_RANGE:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.ERROR));
                highlightType(construct,painter);
                break;
            case CHAR_CLASS:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.CLASS));
                highlightType(construct, painter);
                break;
            case MODE:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.MODE));
                highlightType(construct,painter);
                break;
            case PREDEFINED:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.PREDEFINED));
                highlightType(construct, painter);
                break;
            case COMPONENT:
                painter = new DefaultHighlighter.DefaultHighlightPainter(palette.getMatchingColor(level,false));
                highlightType(construct, painter);
        }
    }

    private void highlightType(Construct construct, DefaultHighlighter.DefaultHighlightPainter painter) {
        try {
            ui.getInputHighlighter().addHighlight(construct.getStart(), construct.getEnd(), painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void highlightMatches() {
        ui.getMatchingHighlighter().removeAllHighlights();
        try {
            Highlighter h = ui.getMatchingHighlighter();
            DefaultHighlighter.DefaultHighlightPainter painter;
            for(int i = expression.groupCount(); i >= 0; i--){
                for(Matched matched : expression.getMatch(i)) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(palette.getMatchingColor(i,true));
                    h.addHighlight(matched.getStartIndex(),matched.getEndIndex(),painter);
                }
            }
        }catch (BadLocationException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
