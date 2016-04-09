package View;

import java.awt.*;

public class ColorPalette {
    private final static ColorPalette INSTANCE = new ColorPalette();
    private static boolean matching = true;
    private static boolean group = true;

    private ColorPalette() {}

    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    public void reset() {
        matching = true;
        group = true;
        System.out.println("ui");
    }

    public Color getMatchingColor(int i){

        switch (i) {
            case 0:
                matching = !matching;
                return HLColor.getColor(HLColor.MATCH_ONE,matching);

            default:
                group = !group;
                    return HLColor.getColor(HLColor.GROUP1,group);
        }
    }

}
