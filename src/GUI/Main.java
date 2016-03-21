package GUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        HighlightingTextField field = new HighlightingTextField();
        frame.add(field);
        frame.pack();
        frame.setVisible(true);

    }
}
