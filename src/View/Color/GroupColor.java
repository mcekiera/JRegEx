package View.Color;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public enum GroupColor {
    GROUP0(new Color(102,178,255)),
    GROUP1(new Color(102,255,102)),
    GROUP2(new Color(255,255,102)),
    GROUP3(new Color(255,153,204)),
    GROUP4(new Color(255,178,102)),
    GROUP5(new Color(255,102,102)),
    GROUP6(new Color(127,0,255)),
    GROUP7(new Color(0,204,102)),
    GROUP8(new Color(0,102,204)),
    GROUP9(new Color(255,51,153)),
    GROUP10(new Color(204,102,0)),
    GROUP11(new Color(204,0,0)),
    GROUP12(new Color(153,0,153)),
    GROUP13(new Color(0,102,0)),
    GROUP14(new Color(153,76,0)),
    GROUP15(new Color(102,0,51));


    Color color;
    final static Map<Integer,DefaultHighlighter.DefaultHighlightPainter> PAINTERS = createPainters();

    GroupColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Map<Integer,DefaultHighlighter.DefaultHighlightPainter> getPainters() {
        return PAINTERS;
    }

    private static Map<Integer,DefaultHighlighter.DefaultHighlightPainter> createPainters() {
        Map<Integer,DefaultHighlighter.DefaultHighlightPainter> temp = new HashMap<>();
        int i = 0;
        for(GroupColor color : GroupColor.values()) {
            temp.put(i,new DefaultHighlighter.DefaultHighlightPainter(color.getColor()));
            i++;
        }
        return temp;
    }
}
