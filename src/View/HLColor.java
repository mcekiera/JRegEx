package View;

import java.awt.*;

public enum HLColor {
    ERROR(new Color(255,0,0)),
    CHAR_CLASS(new Color(255,200,100)),

    MATCH_ONE(new Color(75,150,255)),
    MATCH_TWO(new Color(100,200,255));

    Color color;

    HLColor(Color color) {
        this.color = color;
    }

    public static Color getColor(HLColor hl) {
        return hl.color;
    }
}
