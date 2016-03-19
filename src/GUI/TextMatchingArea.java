package GUI;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

public class TextMatchingArea extends JTextArea{
    Highlighter highlighter;
    Highlighter.HighlightPainter painter;

    public TextMatchingArea() {
        highlighter = getHighlighter();
    }

    public void highlight(int from, int to, Color color) {
        painter = new DefaultHighlighter.DefaultHighlightPainter(color);
        try {
            highlighter.addHighlight(from, to, painter);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public void removeHighlight() {
        highlighter.removeAllHighlights();
    }
}
