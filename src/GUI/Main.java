package GUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        InputTextField field = new InputTextField();
        frame.add(field);
        frame.pack();
        frame.setVisible(true);

    }
}
