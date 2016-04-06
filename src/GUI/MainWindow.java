package GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private final JFrame frame;
    private final JTextField inputField;
    private final JTextArea matchingArea;

    public MainWindow() {
        frame = new JFrame();
        inputField = new InputField().getField();
        matchingArea = new MatchingArea().getArea();
        config();
    }

    private void config() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(inputField, BorderLayout.PAGE_START);
        frame.add(matchingArea,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }
}
