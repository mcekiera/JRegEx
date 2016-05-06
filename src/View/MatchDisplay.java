package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Highlighter;
import java.awt.*;

/**
 * Display text with HTML elements.
 */
public class MatchDisplay {
    private final JTextPane textPane;

    public MatchDisplay() {
        textPane = new JTextPane();
        textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
     * @return interior JTextPane object.
     */
    public JTextPane getTextPane() {
        return textPane;
    }

    /**
     * @return Highlighter of interior JTextPane object.
     */
    public Highlighter getHighlighter() {
        return textPane.getHighlighter();
    }

    /**
     * Sets text to interior JTextPane object.
     */
    public void setText(String text) {
        textPane.setText(text);
    }
}
