package Controller;

import Model.Expression.Expression;
import View.Observed;
import View.Observer;
import View.UserInterface;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        expression.getSeparateConstructsMatches(userInterface.getMatchingText());
        highlightMatches();
    }

    public void highlightMatches() {
        userInterface.getMatchingHighlighter().removeAllHighlights();
        try {
            Matcher matcher = Pattern.compile(expression.getPattern()).matcher(userInterface.getMatchingText());
            DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(155, 155, 255));
            if (matcher.find()) {
                userInterface.getMatchingHighlighter().addHighlight(matcher.start(), matcher.end(), painter);
            }
        }catch (BadLocationException | PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}
