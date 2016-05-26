package View.Color;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Provide colors for strictly defined use highlighting character class elements.
 */
public enum ClassColor {
    NORMAL(new Color(255,165,79)),
    SPECIAL(new Color(225,135,49)),
    ERROR(Color.RED);

    /**
     * Specific color
     */
    Color color;
    /**
     * Map of painters with colors.
     */
    final static Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> PAINTERS = createPainters();

    ClassColor(Color color) {
        this.color = color;
    }

    /**
     * @return specific color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return map of DefaultHighlightPainters with chosen colors.
     */
    public static Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        return PAINTERS;
    }

    /**
     * Creates map of DefaultHighlightPainters for given collection of colors.
     * @return map of DefaultHighlightPainters.
     */
    private static Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> createPainters() {
        Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        int i = 0;
        for(ClassColor color : ClassColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
            i++;
        }
        return temp;
    }
}