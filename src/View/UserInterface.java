package View;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class UserInterface implements Observed {
    private final JFrame frame;
    private final JTextField inputField;
    private final JTextField upperField;
    private final JTextField lowerField;
    private final JTextArea matchingArea;
    JPanel doubleField;
    Border border;

    private final List<Observer> observerList;

    public UserInterface() {
        frame = new JFrame();
        BorderAdder focusListener = new BorderAdder();

        InputListener listener = new InputListener();

        inputField = new InputField().getField();
        inputField.getDocument().addDocumentListener(listener);
        inputField.addFocusListener(focusListener);

        matchingArea = buildTextArea();
        matchingArea.getDocument().addDocumentListener(listener);
        matchingArea.addFocusListener(focusListener);

        upperField = buildComparingField();
        upperField.addFocusListener(focusListener);
        lowerField = buildComparingField();
        lowerField.addFocusListener(focusListener);

        observerList = new ArrayList<>();

        buildInterface();
    }

    private void buildInterface() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(buildInputField(), BorderLayout.PAGE_START);
        frame.add(buildMatchingArea(), BorderLayout.CENTER);
        frame.add(buildComparingFields(), BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    private JPanel buildInputField() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Regular expression:");
        label.setFont(new Font("Arial", Font.BOLD, 25));
        JScrollPane inputPane = new JScrollPane(inputField);
        inputPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(label);
        panel.add(inputPane);
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
        pane.setViewportBorder(new EmptyBorder(1,1,1,1));
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

    public void setMatchCaretListener(CaretListener listener) {
        matchingArea.addCaretListener(listener);
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

    public String getLowerText() {
        return lowerField.getText();
    }

    public String getUpperText() {
        return upperField.getText();
    }

    public void setInputText(String text) {
        inputField.setText(text);
    }

    public void setMAtchingText(String text) {
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

    public class BorderAdder implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            JComponent component = (JComponent) e.getSource();
            border = component.getBorder();
            Border border = BorderFactory.createCompoundBorder(new LineBorder(Color.cyan, 1), new EmptyBorder(4,4,4,4));
            component.setBorder(border);
            component.revalidate();
        }

        @Override
        public void focusLost(FocusEvent e) {
            JComponent component = (JComponent) e.getSource();
            component.setBorder(border);
            component.revalidate();
        }
    }


}
