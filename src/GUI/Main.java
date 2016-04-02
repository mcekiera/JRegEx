/**package GUI;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        InputFieldWrapper field = new InputFieldWrapper();
        MatchingFieldWrapper area = new MatchingFieldWrapper();
        JTextField f = new JTextField();


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(field.getField(), BorderLayout.NORTH);
        panel.add(area.getField(), BorderLayout.CENTER);
        panel.add(f,BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    private void buildInterface() {
        buildWindow();
        buildPatternField();
        buildMatchingArea();
        buildAnalyzingField();
        buildDecomposer();
    }

    private void buildWindow() {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void buildPatternField() {
    }

    private void buildMatchingArea() {
    }

    private void buildAnalyzingField() {
    }

    private void buildDecomposer() {

    }
}
           */