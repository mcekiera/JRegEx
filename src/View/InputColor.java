package View;

import java.awt.*;

public enum InputColor {
    ERROR(new Color(255,80,60)),
    CHAR_CLASS(new Color(255,165,79)),
    MODE(new Color(238,130,238)),
    PREDEFINED(new Color(100,149,237));

    Color color;

    InputColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
