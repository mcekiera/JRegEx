package Processor;

import Constructs.Construct;
import Expressions.Expression;
import GUI.InputFieldWrapper;
import GUI.MatchingFieldWrapper;

import javax.swing.*;
import java.util.List;

public class InputProcessor implements TextObserver{
    private InputFieldWrapper input;
    private MatchingFieldWrapper matching;
    private InputTester tester;
    private RegexMatcher matcher;
    JTextField f;

    public InputProcessor(InputFieldWrapper input, MatchingFieldWrapper matching, JTextField f) {
        this.f = f;
        this.input = input;
        this.input.addObserver(this);
        this.matching = matching;
        this.matching.addObserver(this);
        tester = new InputTester();
        matcher = new RegexMatcher();
    }

    public void validateInput() {
        List<Highlight> temp;
        temp = tester.getUnbalancedBrackets(input.getText());
        temp.addAll(tester.getRegExElements(input.getText()));
        input.highlightFragment(temp);
    }

    public void matchPattern() {
        matching.highlightFragment(matcher.match(input.getText(),matching.getText()));
    }

    public void express() {
        f.setText("");
        Expression ex = new Expression(input.getText());
        ex.setCurrentMatch(matcher.getMAtch());
        for(Construct c : ex) {
            f.setText(c.directMatch(matcher.getMAtch()));

        }
    }

    @Override
    public void update() {
        input.removeHighlight();
        matching.removeHighlight();
        validateInput();
        matchPattern();
        express();

    }
}
