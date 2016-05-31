package View;

import Controller.HighlightManager.HighlightManager;
import Controller.Listeners.FocusChangeUpdate;
import Controller.Listeners.OnFocusBorderChanger;
import View.Observer.Observed;
import View.Observer.Observer;
import View.Tree.RegExTree;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide GUI.
 */
public class UserInterface implements Observed {
    /**
     * Main Frame object.
     */
    private final JFrame frame;
    /**
     * List of observers of UI.
     */
    private final List<Observer> observerList;
    /**
     * Self-formatting text area for user input of regular expression.
     */
    private JTextArea inputField;
    /**
     * Display of matching results.
     */
    private MatchDisplay display;
    /**
     * One of comparing fields.
     */
    private JTextField upperField;
    /**
     * One of comparing fields.
     */
    private JTextField lowerField;
    /**
     * Area for text to match against pattern.
     */
    private JTextArea matchingArea;
    /**
     * Explanatory tree.
     */
    private JTree tree;

    public UserInterface() {
        frame = new JFrame();
        OnFocusBorderChanger focusListener = new OnFocusBorderChanger(
                BorderFactory.createCompoundBorder(new LineBorder(Color.cyan, 1), new EmptyBorder(4,4,4,4)));
        observerList = new ArrayList<>();

        InputListener listener = new InputListener();
        buildInputField(listener, focusListener);
        buildMatchDisplay(focusListener);
        buildMatchingArea(listener, focusListener);
        configUIManager();
        buildInterface(focusListener);
    }

    /**
     * Display text with results of matching by given pattern on given test String.
     * @param match String with results of matching.
     */
    public void setDisplay(String match) {
        display.setText(match);
    }

    /**
     * Sets new tree model after changes in processed regular expression.
     * @param manager object responsible for highlighting of connected input field.
     * @param model new tree model.
     * @param valid flag for validity of pattern.
     */
    public void setTreeModel(HighlightManager manager, RegExTree model, boolean valid) {
        tree.removeAll();
        tree.setModel(model);
        tree.setCellRenderer(model.getRenderer(manager, valid));
        try {
            expandAllNodes(tree);
        }catch (ClassCastException e) {
            e.printStackTrace();
        }
        frame.revalidate();
    }

    /**
     * Adds CaretListener to matching area.
     * @param listener CaretListener.
     */
    public void addMatchCaretListener(CaretListener listener) {
        matchingArea.addCaretListener(listener);
    }

    /**
     * Adds MouseMotionListener to input field.
     * @param listener MouseMotionListener.
     */
    public void addInputMouseMotionListener(MouseMotionListener listener) {
        inputField.addMouseMotionListener(listener);
    }

    /**
     * Adds MouseMotionListener to matching area.
     * @param listener MouseMotionListener.
     */
    public void addMatchingMouseMotionListener(MouseMotionListener listener) {
        matchingArea.addMouseMotionListener(listener);
    }

    /**
     * Adds CaretListener to input field.
     * @param listener CaretListener.
     */
    public void setInputCaretListener(CaretListener listener) {
        inputField.addCaretListener(listener);
    }

    /**
     * @return String with content of input field.
     */
    public String getInputText() {
        return inputField.getText();
    }

    /**
     * @return String with content of matching area.
     */
    public String getMatchingText() {
        return matchingArea.getText();
    }

    /**
     * @return String with content of result display field.
     */
    public String getDescriptionText() {
        try {
            return display.getTextPane().getDocument().getText(0,display.getTextPane().getDocument().getLength()-1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sets test to upper comparing field.
     * @param text to set.
     */
    public void setUpperText(String text) {
        upperField.setText(text);
    }

    /**
     * Sets test to lower comparing field.
     * @param text to set.
     */
    public void setLowerText(String text) {
        lowerField.setText(text);
    }

    /**
     * @return Highlighter object of input field.
     */
    public Highlighter getInputHighlighter() {
        return inputField.getHighlighter();
    }

    /**
     * @return Highlighter object of matching area.
     */
    public Highlighter getMatchingHighlighter() {
        return matchingArea.getHighlighter();
    }

    /**
     * @return Highlighter object of result display field.
     */
    public Highlighter getDescriptionHighlighter() {return display.getHighlighter();}

    /**
     * @return Highlighter object of upper comparing field.
     */
    public Highlighter getUpperHighlighter() {
        return upperField.getHighlighter();
    }

    /**
     * @return Highlighter object of lower comparing field.
     */
    public Highlighter getLowerHighlighter() {
        return lowerField.getHighlighter();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    /**
     * Remove content of comparing fields.
     */
    public void resetCompare() {
        upperField.setText("");
        lowerField.setText("");
    }

    /**
     * Configures ToolTip properties of UIManager.
     */
    private void configUIManager() {
        UIManager.put("ToolTip.background", new ColorUIResource(255, 247, 200));
        Border border = BorderFactory.createLineBorder(new Color(76,79,83));
        UIManager.put("ToolTip.border", border);
    }

    /**
     * Builds display field for matching results.
     * @param focusListener added to field.
     */
    private void buildMatchDisplay(OnFocusBorderChanger focusListener) {
        display = new MatchDisplay();
        display.getTextPane().addFocusListener(focusListener);
    }

    /**
     * Builds field for user input.
     * @param listener added to field.
     * @param focusListener added to field.
     */
    private void buildInputField(InputListener listener, OnFocusBorderChanger focusListener) {
        InputArea field = new InputArea();
        inputField = field.getArea();
        inputField.getDocument().addDocumentListener(listener);
        inputField.addFocusListener(focusListener);
        inputField.addFocusListener(new FocusChangeUpdate(this));
    }

    /**
     * Builds area for displaying matching of pattern.
     * @param listener added to area.
     * @param focusListener added to area.
     */
    private void buildMatchingArea(InputListener listener, OnFocusBorderChanger focusListener) {
        matchingArea = buildTextArea();
        matchingArea.getDocument().addDocumentListener(listener);
        matchingArea.addFocusListener(focusListener);
        matchingArea.addFocusListener(new FocusChangeUpdate(this));
    }

    /**
     * Builds overall UI by composing various elements.
     * @param focusListener to pass to other methods.
     */
    private void buildInterface(OnFocusBorderChanger focusListener) {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(buildCentralPanel(focusListener),BorderLayout.CENTER);
        frame.add(buildSidePanel(), BorderLayout.EAST);
        frame.setTitle("Java Regular Expression analyzing tool");
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Builds central panel of UI, composing input, matching and comparing fields.
     * @param focusListener to pass to other methods.
     * @return JPanel.
     */
    private JPanel buildCentralPanel(OnFocusBorderChanger focusListener) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(buildPanel("REGULAR EXPRESSION:", inputField), BorderLayout.PAGE_START);
        panel.add(buildPanel("TEXT TO MATCH:", matchingArea), BorderLayout.CENTER);
        panel.add(buildComparingFields(focusListener), BorderLayout.PAGE_END);
        return panel;
    }

    /**
     * Builds side panel of UI, composing explain and result fields.
     * @return JPanel.
     */
    private JPanel buildSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(2,1,2,2));

        sidePanel.add(buildPanel("EXPLANATION:", buildTree()));
        sidePanel.add(buildPanel("MATCHED FRAGMENTS:", display.getTextPane()));
        sidePanel.setPreferredSize(new Dimension(300, 400));

        return sidePanel;
    }

    /**
     * Reusable method for creation one panel with one component and label, wrapped into scroll pane.
     * @param description for label.
     * @param element component.
     * @return JPanel.
     */
    private JPanel buildPanel(String description, JComponent element) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(buildLabel(description), BorderLayout.PAGE_START);
        panel.add(buildJScrollPane(element), BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(2, 2, 2, 2));

        return panel;
    }

    /**
     * Builds label.
     * @param description for label text.
     * @return JPanel.
     */
    private JLabel buildLabel(String description) {
        JLabel label = new JLabel(description);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    /**
     * Builds JScrollPane which wraps passed component.
     * @param component to wrap into JScrollPane.
     * @return JScrollPane.
     */
    private JScrollPane buildJScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    /**
     * Builds fields for display of matching of separate construct within regular expression.
     * @param focusListener added to fields.
     * @return JPanel.
     */
    private JPanel buildComparingFields(OnFocusBorderChanger focusListener) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel doubleField = new JPanel(new GridLayout(2,1,2,2));
        upperField = buildComparingField(focusListener);
        lowerField = buildComparingField(focusListener);
        doubleField.add(buildJScrollPane(upperField));
        doubleField.add(buildJScrollPane(lowerField));

        panel.add(buildLabel("MATCHING ANALYSIS:"), BorderLayout.PAGE_START);
        panel.add(doubleField, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds text area for user test text.
     * @return JTextArea.
     */
    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea(15,50);
        area.setFont(new Font("Arial", Font.PLAIN, 20));
        Border border = new EmptyBorder(5,5,5,5);
        area.setBorder(border);
        area.setWrapStyleWord(true);
        return area;
    }

    /**
     * Builds tree for display of structure explanator description.
     * @return JTree.
     */
    private JTree buildTree() {
        tree = new JTree();
        tree.setBorder(new EmptyBorder(1, 1, 1, 1));
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setModel(null);
        tree.addFocusListener(new OnFocusBorderChanger(new LineBorder(Color.cyan, 1)));
        return tree;
    }

    /**
     * Expands all nodes of given tree.
     * @param tree to expand nodes.
     */
    private void expandAllNodes(JTree tree) {
        int j = tree.getRowCount();
        int i = 0;
        while(i < j) {
            tree.expandRow(i);
            i += 1;
            j = tree.getRowCount();
        }
    }


    /**
     * Builds single field.
     * @param focusListener added to field.
     * @return JTextField.
     */
    private JTextField buildComparingField(OnFocusBorderChanger focusListener) {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBorder(BorderFactory.createCompoundBorder(new JTextField().getBorder(), new EmptyBorder(3,3,3,3)));
        Font font = new Font("Arial", Font.BOLD, 35);
        field.setFont(font);
        field.setForeground(Color.BLACK);
        field.addFocusListener(focusListener);
        return field;
    }

    /**
     * Listener for user input.
     */
    public class InputListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            notifyObservers();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            notifyObservers();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            notifyObservers();
        }
    }
}
