package View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorPalette {
    private final static ColorPalette INSTANCE = new ColorPalette();
    private static boolean matching = true;
    private static List<Boolean> groups = getGroup();

    private ColorPalette() {}

    public static ColorPalette getInstance() {
        return INSTANCE;
    }

    public void reset() {
        matching = true;
        groups = getGroup();
        System.out.println("ui");
    }

    public Color getMatchingColor(int i){

        switch (i) {
            case 0:
                matching = !matching;
                return HLColor.getColor(HLColor.MATCH_ONE,matching);
            case 1:
                groups.set(i,!groups.get(i));
                return HLColor.getColor(HLColor.GROUP1,groups.get(i));
            case 2:
                groups.set(i,!groups.get(i));
                return HLColor.getColor(HLColor.GROUP2,groups.get(i));
            case 3:
                groups.set(i,!groups.get(i));
                return HLColor.getColor(HLColor.GROUP3,groups.get(i));
            default:
                return HLColor.getColor(HLColor.GROUP1,groups.get(i));
        }
    }

    public Color getInputColor(int i) {
        switch (i) {
            case 0:
                return HLColor.getColor(HLColor.MATCH_ONE);
            case 1:
                return HLColor.getColor(HLColor.GROUP1);
            case 2:
                return HLColor.getColor(HLColor.GROUP2);
            default:
                return HLColor.getColor(HLColor.GROUP3);
        }
    }

    private static List<Boolean> getGroup() {
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(true);
        }
        return list;
    }

}
