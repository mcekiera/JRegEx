package View;

import java.awt.*;

public enum HLColor {

    GROUP1(new Color(75,255,75),new Color(150,255,150)),
    GROUP2(new Color(222,184,45), new Color(255,211,155)),
    GROUP3(new Color(173,225,45),new Color(202,255,118)),
    MATCH_ONE(new Color(75,150,255),new Color(100,200,255));

    Color c1;
    Color c2;
    boolean clock;

    HLColor(Color c1, Color c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public static Color getColor(HLColor hl, boolean ver) {
        if (ver) {
            return hl.c1;
        } else {
            return hl.c2;
        }
    }
}
