package Controller;

import Model.Expression.Expression;
import Model.Matching.Matched;
import View.Observed;
import View.Observer;
import View.UserInterface;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.regex.PatternSyntaxException;

public class Main implements Observer {
    private Expression expression;
    private UserInterface userInterface;

    public Main() {
        expression = new Expression();
        userInterface = new UserInterface();
        userInterface.addObserver(this);
    }


    @Override
    public void update(Observed source) {
        System.out.println("+");
        expression.reset();
        expression.setPattern(userInterface.getInputText());
        expression.setMatch(userInterface.getMatchingText());
        expression.getSeparateConstructsMatches(userInterface.getMatchingText());
        highlightMatches();
    }

    public void highlightMatches() {
        userInterface.getMatchingHighlighter().removeAllHighlights();
        try {
            Highlighter h = userInterface.getMatchingHighlighter();
            DefaultHighlighter.DefaultHighlightPainter painter;
            for(int i = expression.groupCount(); i > 0; i--){
                for(Matched matched : expression.getMatch(i)) {
                    System.out.println(matched.getEndIndex());
                    painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(0, (100+(i*25)), 0));
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
