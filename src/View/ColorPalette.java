package View;

import java.awt.*;

public class ColorPalette {
    private final static ColorPalette INSTANCE = new ColorPalette();
    private static boolean matching = true;

    private ColorPalette() {}

    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    public void reset() {
        matching = true;
        System.out.println("ui");
    }

    public Color getMatchingColor(int i){
        switch (i) {
            case 0:
                matching = !matching;
                if (matching) {
                    return HLColor.getColor(HLColor.MATCH_ONE);
                } else {
                    return HLColor.getColor(HLColor.MATCH_TWO);
                }
            default:
                return HLColor.getColor(HLColor.CHAR_CLASS);
        }
    }

}
