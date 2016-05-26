package View.Color;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Provide colors for strictly defined use in highlighting user input of regular expression.
 */
public enum InputColor {
    ERROR(new Color(255,80,60)),
    CHAR_CLASS(new Color(255,165,79)),
    MODE(new Color(238,130,238)),
    PREDEFINED(new Color(100,149,237)),
    SELECTION(Color.CYAN);

    /**
     * Specific color
     */
    Color color;
    /**
     * Map of painters with colors.
     */
    final static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> PAINTERS = createPainters();

    InputColor(Color color) {
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
    public static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        return PAINTERS;
    }

    /**
     * Creates map of DefaultHighlightPainters for given collection of colors.
     * @return map of DefaultHighlightPainters.
     */
    private static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> createPainters() {
        Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        for(InputColor color : InputColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
        }
        return temp;
    }
}
