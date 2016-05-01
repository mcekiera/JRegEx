package View.Color;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum ClassColor {
    NORMAL(new Color(255,165,79)),
    SPECIAL(new Color(225,135,49));

    Color color;

    ClassColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        Map<ClassColor,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        int i = 0;
        for(ClassColor color : ClassColor.values()) {
            temp.put(color,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
            i++;
        }
        return temp;
    }
}