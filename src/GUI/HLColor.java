package GUI;

import java.awt.*;

public enum HLColor {
    ERROR(new Color(255,0,0)),
    CHAR_CLASS(new Color(255,200,100));

    Color color;

    HLColor(Color color) {
        this.color = color;
    }
}
