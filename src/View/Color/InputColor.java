package View.Color;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum InputColor {
    ERROR(new Color(255,80,60)),
    CHAR_CLASS(new Color(255,165,79)),
    MODE(new Color(238,130,238)),
    PREDEFINED(new Color(100,149,237));

    Color color;
    final static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> PAINTERS = createPainters();

    InputColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        return PAINTERS;
    }

    private static Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> createPainters() {
        Map<InputColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        for(InputColor color : InputColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
        }
        return temp;
    }
}
