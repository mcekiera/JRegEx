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

public class UserInterface implements Observed {
    private final JFrame frame;
    private final InputField field;
    private final JTextField inputField;
    private final MatchDisplay display;
    private JTextField upperField;
    private JTextField lowerField;
    private final JTextArea matchingArea;
    private JPanel doubleField;
    private JTree tree;

    private final List<Observer> observerList;

    public UserInterface() {
        frame = new JFrame();
        OnFocusBorderChanger focusListener = new OnFocusBorderChanger(
                BorderFactory.createCompoundBorder(new LineBorder(Color.cyan, 1), new EmptyBorder(4,4,4,4)));

        InputListener listener = new InputListener();
        field = new InputField();
        inputField = field.getField();
        inputField.getDocument().addDocumentListener(listener);
        inputField.addFocusListener(focusListener);
        inputField.addFocusListener(new FocusChangeUpdate(this));

        display = new MatchDisplay();
        display.getTextPane().addFocusListener(focusListener);

        matchingArea = buildTextArea();
        matchingArea.getDocument().addDocumentListener(listener);
        matchingArea.addFocusListener(focusListener);
        matchingArea.addFocusListener(new FocusChangeUpdate(this));

        upperField = buildComparingField();
        upperField.addFocusListener(focusListener);
        lowerField = buildComparingField();
        lowerField.addFocusListener(focusListener);

        observerList = new ArrayList<>();

        UIManager.put("ToolTip.background", new ColorUIResource(255, 247, 200)); //#fff7c8
        Border border = BorderFactory.createLineBorder(new Color(76,79,83));    //#4c4f53
        UIManager.put("ToolTip.border", border);

        buildInterface();
    }

    public void setDisplay(String match) {
        display.setText(match);
    }

    public InputField getInputField() {
       return field;
    }

    public JTextField getinputfiel() {
        return inputField;
    }

    private void buildInterface() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildInputField(), BorderLayout.PAGE_START);
        panel.add(buildMatchingArea(), BorderLayout.CENTER);
        frame.add(panel,BorderLayout.CENTER);
        JPanel explainPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("EXPLANATION:");
        explainPanel.setPreferredSize(new Dimension(400, 400));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        explainPanel.add(label, BorderLayout.PAGE_START);

        JPanel interior = new JPanel(new GridLayout(2,1));

        interior.add(buildTree());
        interior.add(new JScrollPane(display.getTextPane()));

        explainPanel.add(interior, BorderLayout.CENTER);
        panel.add(buildComparingFields(), BorderLayout.PAGE_END);

        frame.add(explainPanel, BorderLayout.EAST);
        frame.setTitle("Java Regular Expression decomposer ver 1.2");
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel buildInputField() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("REGULAR EXPRESSION:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane inputPane = new JScrollPane(inputField);
        inputPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(label, BorderLayout.NORTH);
        panel.add(inputPane, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane buildMatchingArea() {
        JScrollPane matchingPane = new JScrollPane(matchingArea);
        matchingPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        matchingPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return matchingPane;
    }

    private JScrollPane buildComparingFields() {
        doubleField = new JPanel(new GridLayout(2, 1, 2, 2));
        JScrollPane pane = new JScrollPane(doubleField);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        pane.setViewportBorder(new EmptyBorder(1, 1, 1, 1));
        doubleField.add(upperField);
        doubleField.add(lowerField);
        return pane;
    }

    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea(15, 40);
        area.setFont(new Font("Arial", Font.PLAIN, 20));
        Border border = new EmptyBorder(5,5,5,5);
        area.setBorder(border);

        area.setWrapStyleWord(true);
        return area;
    }

    private JScrollPane buildTree() {
        tree = new JTree();
        tree.setBorder(new EmptyBorder(1, 1, 1, 1));
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setModel(null);
        JScrollPane pane = new JScrollPane(tree);
        tree.addFocusListener(new OnFocusBorderChanger(new LineBorder(Color.cyan, 1)));
        tree.addFocusListener(new FocusChangeUpdate(this));
        return pane;
    }

    public void setTreeModel(HighlightManager manager, RegExTree model, boolean valid) {
        tree.setModel(model);
        tree.setCellRenderer(model.getRenderer(manager, valid));
        try {
            expandAllNodes(tree);
        }catch (ClassCastException e) {
            e.printStackTrace();
        }
        frame.revalidate();
    }

    private void expandAllNodes(JTree tree) {
        int j = tree.getRowCount();
        int i = 0;
        while(i < j) {
            tree.expandRow(i);
            i += 1;
            j = tree.getRowCount();
        }
    }

    public void setMatchCaretListener(CaretListener listener) {
        matchingArea.addCaretListener(listener);
    }

    public void setInputMouseMotionListener(MouseMotionListener listener) {
        inputField.addMouseMotionListener(listener);
    }

    public void setMatchingMouseMotionListener(MouseMotionListener listener) {
        matchingArea.addMouseMotionListener(listener);
    }

    public void setInputCaretListener(CaretListener listener) {
        inputField.addCaretListener(listener);
    }

    private JTextField buildComparingField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBorder(BorderFactory.createCompoundBorder(new JTextField().getBorder(), new EmptyBorder(3,3,3,3)));
        Font font = new Font("Arial", Font.BOLD, 35);
        field.setFont(font);
        return field;
    }

    public String getInputText() {
        return inputField.getText();
    }

    public String getMatchingText() {
        return matchingArea.getText();
    }

    public String getDescriptionText() {
        try {
            return display.getTextPane().getDocument().getText(0,display.getTextPane().getDocument().getLength()-1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLowerText() {
        return lowerField.getText();
    }

    public String getUpperText() {
        return upperField.getText();
    }

    public void setInputText(String text) {
        inputField.setText(text);
    }

    public void setMatchingText(String text) {
        matchingArea.setText(text);
    }

    public void setUpperText(String text) {
        upperField.setText(text);
    }

    public void setLowerText(String text) {
        lowerField.setText(text);
    }

    public Highlighter getInputHighlighter() {
        return inputField.getHighlighter();
    }

    public Highlighter getMatchingHighlighter() {
        return matchingArea.getHighlighter();
    }

    public Highlighter getDescriptionHighlighter() {return display.getHighlighter();}

    public Highlighter getUpperHighlighter() {
        return upperField.getHighlighter();
    }

    public Highlighter getLowerHighlighter() {
        return lowerField.getHighlighter();
    }

    public void refresh() {
        doubleField.revalidate();
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
