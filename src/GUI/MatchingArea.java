package GUI;

import javax.swing.*;
import javax.swing.text.Highlighter;
import java.awt.*;

public class MatchingArea {
    private final JTextArea area;
    private final Highlighter highlighter;

    public MatchingArea() {
        area = new JTextArea();
        highlighter = area.getHighlighter();
        config();
    }

    private void config() {
        area.setColumns(40);
        area.setRows(15);
        Font font = new Font("Arial",Font.PLAIN,20);
        area.setFont(font);
    }

    public JTextArea getArea() {
        return area;
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }
}
