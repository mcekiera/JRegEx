package View;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserInterface implements Observed{
    private final JFrame frame;
    private final JTextField inputField;
    private final JTextField upperField;
    private final JTextField lowerField;
    private final JTextArea matchingArea;

    private final List<Observer> observerList;

    public UserInterface() {
        frame = new JFrame();

        InputListener listener = new InputListener();

        inputField = new InputField().getField();
        inputField.getDocument().addDocumentListener(listener);

        matchingArea = buildTextArea();
        matchingArea.getDocument().addDocumentListener(listener);

        upperField = buildComparingField();
        lowerField = buildComparingField();

        observerList = new ArrayList<>();

        buildInterface();
    }

    private void buildInterface() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(buildInputField(), BorderLayout.PAGE_START);
        frame.add(buildMatchingArea(), BorderLayout.CENTER);
        frame.add(buildComparingFields(),BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    private JPanel buildInputField() {
        JPanel panel = new JPanel(new GridLayout(2,1));
        JLabel label = new JLabel("Regular expression:");
        label.setFont(new Font("Arial",Font.BOLD,25));
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

    private JPanel buildComparingFields() {
        JScrollPane upperPane = new JScrollPane(upperField);
        upperPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JScrollPane lowerPane = new JScrollPane(lowerField);
        lowerPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel doubleField = new JPanel(new GridLayout(2,1,2,2));
        doubleField.add(upperPane);
        doubleField.add(lowerPane);
        return doubleField;
    }

    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea(15,40);
        area.setFont(new Font("Arial", Font.PLAIN, 20));
        Border border = new EmptyBorder(5,5,5,5);
        area.setBorder(border);

        area.setWrapStyleWord(true);
        return area;
    }

    private JTextField buildComparingField() {
        JTextField field = new JTextField();
        field.setEditable(false);
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

    @Override
    public void notifyObservers() {
        for(Observer observer : observerList) {
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
