package Processor;

import GUI.Highlight;
import GUI.InputFieldWrapper;
import GUI.MatchingFieldWrapper;

import java.util.List;

public class InputProcessor implements TextObserver{
    private InputFieldWrapper input;
    private MatchingFieldWrapper matching;
    private InputTester tester;
    private RegexMatcher matcher;

    public InputProcessor(InputFieldWrapper input, MatchingFieldWrapper matching) {
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

    @Override
    public void update() {
        input.removeHighlight();
        matching.removeHighlight();
        validateInput();
        matchPattern();

    }
}
