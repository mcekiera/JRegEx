package Controller;

import Controller.HighlightManager.DescriptionHighlightManager;
import Controller.HighlightManager.InputHighlightManager;
import Controller.HighlightManager.MatchingHighlightManager;
import Controller.HighlightManager.SectionHighlightManager;
import Controller.Listeners.ExampleSelection;
import Controller.Listeners.MouseHoover;
import Controller.Listeners.SelectionHighlighter;
import Model.Expression;
import Model.Segment;
import View.Observer.Observed;
import View.Observer.Observer;
import View.Part;
import View.Tree.RegExTree;
import View.UserInterface;

import javax.swing.*;

public class Main implements Observer{
    /**
     * UserInterface object representing GUI.
     */
    private UserInterface theInterface;
    /**
     * Managing highlighting of input field.
     */
    private InputHighlightManager inputHighlightManager;
    /**
     * Managing highlighting of matching field.
     */
    private MatchingHighlightManager matchingHighlightManager;
    /**
     * Managing highlighting of description field.
     */
    private DescriptionHighlightManager descriptionHighlightManager;
    /**
     * Managing highlighting of comparing field.
     */
    private SectionHighlightManager sectionHighlightManager;
    /**
     * Represents regular expression.
     */
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

    /**
     * Creates and combine GUI elements.
     */
    private void setUpUserInterface() {
        theInterface = new UserInterface();
        theInterface.addObserver(this);
        theInterface.addInputMouseMotionListener(new MouseHoover(expression, Part.INPUT));
        theInterface.addMatchingMouseMotionListener(new MouseHoover(expression, Part.MATCHING));
        createHighlightManagers();
        theInterface.setInputCaretListener(new SelectionHighlighter(inputHighlightManager));
        theInterface.addMatchCaretListener(new SelectionHighlighter(matchingHighlightManager));
        theInterface.addMatchCaretListener(new ExampleSelection(this));
    }

    /**
     * Crates HighlightManager objects.
     */
    private void createHighlightManagers() {
        inputHighlightManager = new InputHighlightManager(theInterface.getInputHighlighter());
        matchingHighlightManager = new MatchingHighlightManager(theInterface.getMatchingHighlighter());
        descriptionHighlightManager = new DescriptionHighlightManager(theInterface.getDescriptionHighlighter());
        sectionHighlightManager = new SectionHighlightManager(theInterface.getUpperHighlighter(), theInterface.getLowerHighlighter());
    }

    /**
     * Updates all displayed data.
     */
    private void updateView() {
        expression.set(theInterface.getInputText(), theInterface.getMatchingText());
        inputHighlightManager.process(expression.getRoot());
        theInterface.setTreeModel(inputHighlightManager, new RegExTree(expression), expression.isValid());
        matchingHighlightManager.reset();

        if (expression.isValid()) {
            matchingHighlightManager.process(expression.getOverallMatch());
            theInterface.setDisplay(expression.getOverallMatch().getMatchDescription());
            descriptionHighlightManager.process(theInterface.getDescriptionText(), expression.getOverallMatch());

        }
    }

    /**
     * Updates comparing view after choosing example to analysis.
     * @param position position of caret during clicking.
     */
    public void updateCompareView(int position) {
        sectionHighlightManager.reset();
        theInterface.resetCompare();
        if((!theInterface.getMatchingText().equals("")) && (!theInterface.getInputText().equals(""))) {
            try {
                sectionHighlightManager.reset();
                if (expression.getOverallMatch().hasSegment(position)) {
                    Segment s = expression.getOverallMatch().getSegmentByPosition(position);
                    if(!s.getContent().equals("")) {

                        theInterface.setUpperText(expression.getRoot().getText());
                        theInterface.setLowerText(s.toString());

                        expression.detail(s);
                        sectionHighlightManager.process(expression, s.getStart());
                    }
                }
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                //e.printStackTrace();
            }
        }

    }

    @Override
    public void update(Observed source) {
        if(source == theInterface) {
            updateView();
        }
    }
}
