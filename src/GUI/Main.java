package GUI;

import Processor.InputProcessor;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        InputFieldWrapper field = new InputFieldWrapper();
        MatchingFieldWrapper area = new MatchingFieldWrapper();
        InputProcessor processor = new InputProcessor(field,area);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(field.getField(), BorderLayout.NORTH);
        panel.add(area.getField(), BorderLayout.CENTER);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }
}
