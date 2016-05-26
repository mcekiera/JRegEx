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
    private final JTextField inputField;
    private final MatchDisplay display;
    private JTextField upperField;
    private JTextField lowerField;
    private final JTextArea matchingArea;
    private JTree tree;
    private OnFocusBorderChanger focusListener;

    private final List<Observer> observerList;

    public UserInterface() {
        frame = new JFrame();
        focusListener = new OnFocusBorderChanger(
                BorderFactory.createCompoundBorder(new LineBorder(Color.cyan, 1), new EmptyBorder(4,4,4,4)));

        InputListener listener = new InputListener();
        InputField field = new InputField();
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



        observerList = new ArrayList<>();

        UIManager.put("ToolTip.background", new ColorUIResource(255, 247, 200)); //#fff7c8
        Border border = BorderFactory.createLineBorder(new Color(76,79,83));    //#4c4f53
        UIManager.put("ToolTip.border", border);

        buildInterface();
    }

    public void setDisplay(String match) {
        display.setText(match);
    }

    private void buildInterface() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(buildCentralPanel(),BorderLayout.CENTER);
        frame.add(buildSidePanel(), BorderLayout.EAST);
        frame.setTitle("Java Regular Expression decomposer v" +
                "er 1.2");
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel buildCentralPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(buildPanel("REGULAR EXPRESSION:", inputField), BorderLayout.PAGE_START);
        panel.add(buildPanel("TEXT TO MATCH:", matchingArea), BorderLayout.CENTER);
        panel.add(buildPanel("SEPARATE CONSTRUCTS MATCHING ANALYSIS:",buildComparingFields()), BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel buildSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(2,1,2,2));

        sidePanel.add(buildPanel("EXPLANATION:", buildTree()));
        sidePanel.add(buildPanel("MATCHED FRAGMENTS:",display.getTextPane()));
        sidePanel.setPreferredSize(new Dimension(300, 400));

        return sidePanel;
    }

    private JPanel buildPanel(String description, JComponent element) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(description);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(element);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(label, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildComparingFields() {
        JPanel doubleField = new JPanel(new GridLayout(2, 1, 2, 2));
        upperField = buildComparingField();
        upperField.addFocusListener(focusListener);
        lowerField = buildComparingField();
        lowerField.addFocusListener(focusListener);
        doubleField.add(upperField);
        doubleField.add(lowerField);
        return doubleField;
    }

    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea(15, 40);
        area.setFont(new Font("Arial", Font.PLAIN, 20));
        Border border = new EmptyBorder(5,5,5,5);
        area.setBorder(border);
        area.setWrapStyleWord(true);
        return area;
    }

    private JTree buildTree() {
        tree = new JTree();
        tree.setBorder(new EmptyBorder(1, 1, 1, 1));
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setModel(null);
        tree.addFocusListener(new OnFocusBorderChanger(new LineBorder(Color.cyan, 1)));
        //tree.addFocusListener(new FocusChangeUpdate(this));
        return tree;
    }

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
        JTextField field = new JTextField(30);
        field.setEditable(false);
        field.setBorder(BorderFactory.createCompoundBorder(new JTextField().getBorder(), new EmptyBorder(3,3,3,3)));
        Font font = new Font("Arial", Font.BOLD, 35);
        field.setFont(font);
        field.setForeground(Color.BLACK);
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

    public void resetCompare() {
        upperField.setText("");
        lowerField.setText("");
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
