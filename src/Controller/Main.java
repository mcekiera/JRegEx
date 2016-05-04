package Controller;

import Controller.HighlightManager.InputHighlightManager;
import Controller.HighlightManager.MatchingHighlightManager;
import Controller.Listeners.MouseHoover;
import Controller.Listeners.SelectionHighlighter;
import Model.Expression;
import Model.Tree.RegExTree;
import View.Observer.Observed;
import View.Observer.Observer;
import View.Part;
import View.UserInterface;

public class Main implements Observer{
    private UserInterface anInterface;
    private InputHighlightManager inputHighlightManager;
    private MatchingHighlightManager matchingHighlightManager;
    private Expression expression;

    public Main() {
        init();
    }

    public void init() {
        expression = new Expression();
        setUpUserInterface();
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

    private void setUpUserInterface() {
        anInterface = new UserInterface();
        anInterface.addObserver(this);

        anInterface.setInputMouseMotionListener(new MouseHoover(expression,Part.INPUT));
        anInterface.setMatchingMouseMotionListener(new MouseHoover(expression, Part.MATCHING));
        inputHighlightManager = new InputHighlightManager(anInterface.getInputHighlighter());
        matchingHighlightManager = new MatchingHighlightManager(anInterface.getMatchingHighlighter());
        anInterface.setInputCaretListener(new SelectionHighlighter(inputHighlightManager));
    }


    private void updateView() {
        expression.set(anInterface.getInputText(), anInterface.getMatchingText());
        inputHighlightManager.process(expression.getRoot());
        anInterface.setTreeModel(new RegExTree(expression));
        if(expression.isValid()) {
            matchingHighlightManager.process(expression.getOverallMatch());

        }
    }

    private void updateHighlight(int i) {
        inputHighlightManager.process(i);
    }

    @Override
    public void update(Observed source) {
        if(source == anInterface) {
            updateView();
        }
    }
}

/**package Controller;

import Model.Constructs.*;
import Model.Expression.Expression;
import Model.Matching.InClassMatching;
import Model.Matching.Matched;
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
        for(Construct singular : sequence) {
            if(Construct.isComposed(singular)) {
                level++;
                highlightPattern((Sequence) singular);
            } else if (singular.getType() == Type.QUANTIFIER || singular.getType() == Type.INTERVAL) {
                if(Construct.isComposed(((Quantifier) singular).getConstruct())){
                    highlightPattern((Sequence)((Quantifier) singular).getConstruct());
                } else {
                    highlightByType(((Quantifier) singular).getConstruct());
                    highlightByType(singular);
                }
            } else {
                 highlightByType(singular);
            }
        }
        if (level > 0) level--;
    }

    private void highlightByType(Construct construct) {
        Highlighter h = ui.getInputHighlighter();
        DefaultHighlighter.DefaultHighlightPainter painter;
        switch (construct.getType()) {
            case EXPRESSION:
                break;
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
                System.out.println(construct.getType());
                System.out.println(construct.getParent().toString());
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
                for(Matched matched : expression.getMatch(i)) {
                    painter = new DefaultHighlighter.DefaultHighlightPainter(palette.getMatchingColor(i));
                    h.addHighlight(matched.getStart(),matched.getEnd(),painter);
                }
            }
        }catch (BadLocationException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void caretInMatch(int position) {
        System.out.println("in");
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

    public void displayMatchingAnalysis(Matched selected) {
        if(!isSameData()) {
            expression.getSeparateConstructsMatches(selected);
            ui.setUpperText(expression.getContent());
            ui.setLowerText(expression.getSelectedMatch());
            highlightAnalysis(expression.getSequence());
            ui.refresh();
        }
    }

    public boolean isSameData() {
        return ui.getUpperText().equals(expression.getContent()) && ui.getLowerText().equals(expression.getSelectedMatch()) ;
    }

    private void highlightAnalysis(Sequence sequence) {
        for(Construct c : sequence) {
            if(Singular.isComposed(c)) {
                inComposedCase(c);
            }else if(c.getType() == Type.QUANTIFIER) {
                inQuantifierCase(c);
            }else{
                inCommonCase(c);
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

    public void inCommonCase(Construct singular){
        if(singular.getType() == Type.COMPONENT) {
            inComponentCase(singular);
        }else {
            inSimpleCase(singular);
        }
    }

    private void inComponentCase(Construct singular) {
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(palette.getInputColor(level));
        try {
            ui.getUpperHighlighter().addHighlight(singular.getStart(), singular.getEnd(),p);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void inComposedCase(Construct singular) {
        if(singular.getType() == Type.CHAR_CLASS) {
            inCharacterClassCase(singular);
        } else {
            incrementLevel();
            highlightAnalysis((Sequence) singular);
        }
    }

    private void inSimpleCase(Construct singular) {
        Color color = getRandomColor();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(color);
        try {
            ui.getLowerHighlighter().addHighlight(((Singular)singular).getCurrentMatchStart(),((Singular)singular).getCurrentMatchEnd(), p);
            ui.getUpperHighlighter().addHighlight(singular.getStart(), singular.getEnd(), p);
        }catch (BadLocationException e) {
            try {
                ui.getUpperHighlighter().addHighlight(singular.getStart(), singular.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY));
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void inCharacterClassCase(Construct singular) {
        if(classMatching.setSequence((Sequence) singular,
                expression.getSelectedMatch())) {
            for(Construct interior : (Sequence) singular) {
                Color color = getRandomColor();
                DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(color);
                try {

                    if(interior.getType()!=Type.COMPONENT) {
                        for (Matched m : classMatching.getMatched(interior)) {
                            ui.getUpperHighlighter().addHighlight(interior.getStart(),interior.getEnd(),p);
                            ui.getLowerHighlighter().addHighlight(m.getStart(), m.getEnd(), p);

                        }
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
            DefaultHighlighter.DefaultHighlightPainter r = new DefaultHighlighter.DefaultHighlightPainter(HLColor.getColor(HLColor.CLASS));
            try {
            ui.getUpperHighlighter().addHighlight(singular.getStart(),singular.getEnd(),r);
             } catch (BadLocationException e) {
                 e.printStackTrace();
             }

        }
    }

    private void inQuantifierCase(Construct singular) {
        Construct interior = ((Quantifier) singular).getConstruct();
        if(Singular.isComposed(interior)) {
            highlightAnalysis((Sequence)interior);
        } else {
            inCommonCase(interior);
        }
        inSimpleCase(singular);

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

}      */


