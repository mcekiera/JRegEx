package Controller;

import Model.Constructs.Construct;
import Model.Constructs.Quantifier;
import Model.Constructs.Sequence;
import Model.Constructs.Type;
import Model.Expression.Expression;
import Model.Matching.Fragment;
import Model.Matching.InClassMatching;
import View.*;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.Random;
import java.util.regex.PatternSyntaxException;

public class Main implements Observer {
    private final ColorPalette palette = ColorPalette.getInstance();
    private static int level = 0;
    private Construct selected;
    private Expression expression;
    private UserInterface ui;
    private InClassMatching classMatching;


    public Main() {
        expression = new Expression();
        classMatching = new InClassMatching();
        ui = new UserInterface();
        ui.addObserver(this);
        ui.setMatchCaretListener(new MatchCaretListener());
        ui.setInputCaretListener(new InputCaretListener());
    }


    @Override
    public void update(Observed source) {
        palette.reset();
        if(expression.use(ui.getInputText(), ui.getMatchingText())) {
            expression.getConstructsDirectMatch(ui.getMatchingText());
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
                highlightPattern((Sequence) construct);
            } else if (construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL) {
                if(Construct.isComposed(((Quantifier) construct).getConstruct())){
                    highlightPattern((Sequence)((Quantifier) construct).getConstruct());
                } else {
                    highlightByType(((Quantifier) construct).getConstruct());
                    highlightByType(construct);
                }
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
                highlightType(construct, painter);
                break;
            case CHAR_CLASS:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.CLASS));
                highlightType(construct, painter);
                break;
            case MODE:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.MODE));
                highlightType(construct, painter);
                break;
            case PREDEFINED:
            case SPECIFIC_CHAR:
            case BOUNDARY:
                painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.PREDEFINED));
                highlightType(construct, painter);
                break;
            case COMPONENT:
                if(construct.getParent().getType() == Type.CHAR_CLASS) {
                    Color color;
                    if(selected != null && construct.getParent() == selected.getParent()) {
                        color = Color.CYAN;
                    } else {
                        color = HLColor.getColor(HLColor.CLASS);
                    }
                    painter = new DefaultHighlighter.DefaultHighlightPainter(color);
                    highlightType(construct, painter);
                } else {
                    Color color;
                    if(selected != null && construct.getParent() == selected.getParent()) {
                         color = Color.CYAN;
                    } else {
                         color = palette.getInputColor(level);
                    }
                    painter = new DefaultHighlighter.DefaultHighlightPainter(color);
                    highlightType(construct, painter);
                }
            default:
                if(construct.getParent().getType() == Type.CHAR_CLASS) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.CLASS));
                    highlightType(construct, painter);

                }

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
                for(Fragment fragment : expression.getMatch(i)) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(palette.getMatchingColor(i));
                    h.addHighlight(fragment.getStart(), fragment.getEnd(),painter);
                }
            }
        }catch (BadLocationException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void caretInMatch(int position) {

        if(expression.getMatchByIndex(position) != null) {
            displayMatchingAnalysis(expression.getMatchByIndex(position));
        }
    }

    public void changeSelectedConstructColor(int position) {
        try {
            selected = expression.getSequence().getConstructByPosition(position);
            highlightInput();
        }catch (NullPointerException e){
            e.printStackTrace();
            highlightInput();
        }
    }

    public void displayMatchingAnalysis(Fragment selected) {
        if(!isSameData()) {
            expression.getConstructsDirectMatch(selected.getFragment());
            ui.setUpperText(expression.getPattern());
            ui.setLowerText(expression.getSelectedMatch());
            highlightAnalysis(expression.getSequence());
            ui.refresh();
        }
    }

    public boolean isSameData() {
        return ui.getUpperText().equals(expression.getPattern()) && ui.getLowerText().equals(expression.getSelectedMatch()) ;
    }

    private void highlightAnalysis(Sequence sequence) {
        for(Construct construct : sequence) {
            if(Construct.isComposed(construct)) {
                inComposedCase(construct);
            }else if(construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL) {
                //TODO kwantifikatory bezpoœrednie dopasowani mog¹ dopasowywaæ na z³apanym fragmencie, nie ca³ym wyra¿eniem na pew³nym dopasowanym tekœcie
                //TODO maybe create construct for part of regex (not whole) and then correct star nad end indices?
                //TODO create new Expression object for regex part, then copy direct matches and correct matches
                inQuantifierCase(construct);
            }else{
                inCommonCase(construct);
            }
        }
        decrementLevel();
    }

    public void incrementLevel() {
        level++;
    }

    public void decrementLevel() {
        if(level>0) level--;
    }

    public void inCommonCase(Construct construct){
        if(construct.getType() == Type.COMPONENT) {
            inComponentCase(construct);
        }else {
            inSimpleCase(construct);
        }
    }

    private void inComponentCase(Construct construct) {
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(palette.getInputColor(level));
        try {
            ui.getUpperHighlighter().addHighlight(construct.getStart(),construct.getEnd(),p);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void inComposedCase(Construct construct) {
        if(construct.getType() == Type.CHAR_CLASS) {
            inCharacterClassCase(construct);
        } else {
            incrementLevel();
            highlightAnalysis((Sequence) construct);
        }
    }

    private void inSimpleCase(Construct construct) {
        Color color = getRandomColor();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(color);
        try {
            ui.getLowerHighlighter().addHighlight(construct.getCurrentMatchStart(), construct.getCurrentMatchEnd(), p);
            ui.getUpperHighlighter().addHighlight(construct.getStart(), construct.getEnd(), p);
        }catch (BadLocationException | NullPointerException e) {
            try {
                ui.getUpperHighlighter().addHighlight(construct.getStart(), construct.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY));
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void inCharacterClassCase(Construct construct) {
        if(classMatching.setSequence((Sequence)construct,
                expression.getSelectedMatch())) {
            for(Construct interior : (Sequence)construct) {
                Color color = getRandomColor();
                DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(color);
                try {

                    if(interior.getType()!=Type.COMPONENT) {
                        for (Fragment m : classMatching.getMatched(interior)) {
                            ui.getUpperHighlighter().addHighlight(interior.getStart(),interior.getEnd(),p);
                            ui.getLowerHighlighter().addHighlight(m.getStart(), m.getEnd(), p);

                        }
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
            DefaultHighlighter.DefaultHighlightPainter r = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.CLASS));
            // try {
            //ui.getUpperHighlighter().addHighlight(construct.getStart(),construct.getEnd(),r);
            // } catch (BadLocationException e) {
            //     e.printStackTrace();
            // }

        }
    }

    private void inQuantifierCase(Construct construct) {
        Construct interior = ((Quantifier)construct).getConstruct();
        if(Construct.isComposed(interior)) {
            highlightAnalysis((Sequence)interior);
        } else {
            inCommonCase(interior);
        }
        inSimpleCase(construct);
    }

    private Color getRandomColor() {
        int r = new Random().nextInt(255);
        int g = new Random().nextInt(255);
        int b = new Random().nextInt(255);
        return new Color(r,g,b);
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

    public class MatchCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            int position = e.getDot();
            caretInMatch(position);
        }
    }

    public class InputCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            int position = e.getDot();
            changeSelectedConstructColor(position);
        }
    }

}


