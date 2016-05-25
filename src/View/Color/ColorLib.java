package View.Color;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Provide palette of colors.
 */
public class ColorLib {
    /**
     * Only instance of class.
     */
    private static final ColorLib INSTANCE = new ColorLib();
    /**
     * List of colors.
     */
    private final List<Color> colors;
    /**
     * Index of next color to retrieve.
     */
    private int index;

    private ColorLib() {
        colors = new ArrayList<>();
        colors.add(new Color(175, 238, 238));
        colors.add(new Color(72, 209, 204));
        colors.add(new Color(176, 196, 222));
        colors.add(new Color(32, 178, 170));
        colors.add(new Color(0, 128, 128));
        colors.add(new Color(65, 105, 225));
        colors.add(new Color(25,25,112));
        colors.add(new Color(100, 149, 237));
        colors.add(new Color(147, 112, 219));
        colors.add(new Color(221, 160, 221));
        colors.add(new Color(148, 0, 211));
        colors.add(new Color(75, 0, 130));
        colors.add(new Color(139, 0, 139));
        colors.add(new Color(186, 85, 211));
        colors.add(new Color(255, 105, 180));
        colors.add(new Color(205, 92, 92));
        colors.add(new Color(233, 150, 122));
        colors.add(new Color(244, 164, 96));
        colors.add(new Color(255, 127, 80));
        colors.add(new Color(205, 133, 63));
        colors.add(new Color(210, 105, 30));
        colors.add(new Color(184, 134, 11));
        colors.add(new Color(160, 82, 45));
        colors.add(new Color(139, 69, 19));
        colors.add(new Color(128, 128, 0));
        colors.add(new Color(85, 107, 47));
        colors.add(new Color(107, 142, 35));
        colors.add(new Color(154, 205, 50));
        colors.add(new Color(240, 230, 140));
        colors.add(new Color(255, 215, 0));
        colors.add(new Color(255, 255, 0));
        colors.add(new Color(0, 255, 0));
        colors.add(new Color(60, 179, 113));
        colors.add(new Color(143, 188, 143));
    }

    /**
     * @return Only instance of class.
     */
    public static ColorLib getInstance() {
        return INSTANCE;
    }

    /**
     * @return next available Color object from list.
     */
    public Color getNextColor() {
        Color color = colors.get(index);
        index = index+1 >= colors.size() ? 0 : index+1;
        return color;
    }

    /**
     * Reset colors list counter.
     */
    public void reset() {
        index = 0;
    }
}
