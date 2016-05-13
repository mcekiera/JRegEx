package Controller;

import Controller.HighlightManager.DescriptionHighlightManager;
import Controller.HighlightManager.InputHighlightManager;
import Controller.HighlightManager.MatchingHighlightManager;
import Controller.HighlightManager.SectionHighlightManager;
import Controller.Listeners.MouseHoover;
import Controller.Listeners.SelectionHighlighter;
import Model.Expression;
import View.Observer.Observed;
import View.Observer.Observer;
import View.Part;
import View.Tree.RegExTree;
import View.UserInterface;

import javax.swing.*;

public class Main implements Observer{
    private UserInterface anInterface;
    private InputHighlightManager inputHighlightManager;
    private MatchingHighlightManager matchingHighlightManager;
    private DescriptionHighlightManager descriptionHighlightManager;
    private SectionHighlightManager sectionHighlightManager;
    private Expression expression;

    public Main() {
        init();
    }

    public void init() {
        expression = new Expression();
        SwingUtilities.invokeLater(this::setUpUserInterface);
    }

    public static void main(String[] args) {
        new Main();
    }

    private void setUpUserInterface() {
        anInterface = new UserInterface();
        anInterface.addObserver(this);

        anInterface.setInputMouseMotionListener(new MouseHoover(expression, Part.INPUT));
        anInterface.setMatchingMouseMotionListener(new MouseHoover(expression, Part.MATCHING));
        inputHighlightManager = new InputHighlightManager(anInterface.getInputHighlighter());
        matchingHighlightManager = new MatchingHighlightManager(anInterface.getMatchingHighlighter());
        descriptionHighlightManager = new DescriptionHighlightManager(anInterface.getDescriptionHighlighter());
        sectionHighlightManager = new SectionHighlightManager(anInterface.getUpperHighlighter(),anInterface.getLowerHighlighter());
        anInterface.setInputCaretListener(new SelectionHighlighter(inputHighlightManager));
        anInterface.setMatchCaretListener(new SelectionHighlighter(matchingHighlightManager));
    }

    private void updateView() {
        expression.set(anInterface.getInputText(), anInterface.getMatchingText());
        inputHighlightManager.process(expression.getRoot());
        anInterface.setTreeModel(inputHighlightManager, new RegExTree(expression), expression.isValid());
        if (expression.isValid()) {
            matchingHighlightManager.process(expression.getOverallMatch());
            anInterface.setDisplay(expression.getOverallMatch().getMatchDescription());
            descriptionHighlightManager.process(anInterface.getDescriptionText(), expression.getOverallMatch());
            try {
                anInterface.setUpperText(expression.getRoot().getText());
                anInterface.setLowerText(expression.getOverallMatch().getMatch(0).get(0).toString());
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                //e.printStackTrace();
            }
            expression.detail();
            sectionHighlightManager.process(expression.getDetailMatches());
        }

    }

    @Override
    public void update(Observed source) {
        if(source == anInterface) {
            updateView();
        }
    }
}
