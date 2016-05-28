package Model.Lib;

import Model.Regex.Type;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class. Provide icons for regular expression types represented by Construct types. Icons are loaded
 * from files.
 */
public class IconLib {
    /**
     * Only instance of DescLib class.
     */
    private static final IconLib INSTANCE = new IconLib();
    /**
     * Map containing all loaded icons, with Type object as keys.
     */
    private final Map<Type,Icon> icons;

    private IconLib() {
        icons = new HashMap<>();
        icons.put(Type.BACKREFERENCE, new ImageIcon(IconLib.class.getResource("ico/BACKREFERENCE.png")));
        icons.put(Type.BOUNDARY, new ImageIcon(IconLib.class.getResource("ico/BOUNDARY.png")));
        icons.put(Type.CAPTURING, new ImageIcon(IconLib.class.getResource("ico/CAPTURING.png")));
        icons.put(Type.SIMPLE, new ImageIcon(IconLib.class.getResource("ico/LITERAL.png")));
        icons.put(Type.LOOK_AROUND, new ImageIcon(IconLib.class.getResource("ico/LOOKAROUND.png")));
        icons.put(Type.MODE, new ImageIcon(IconLib.class.getResource("ico/MODE.png")));
        icons.put(Type.PREDEFINED, new ImageIcon(IconLib.class.getResource("ico/BACKREFERENCE.png")));
        icons.put(Type.QUANTIFIER, new ImageIcon(IconLib.class.getResource("ico/QUANTIFIER.png")));
        icons.put(Type.QUOTATION, new ImageIcon(IconLib.class.getResource("ico/QUOTES.png")));
        icons.put(Type.SPECIFIC_CHAR, new ImageIcon(IconLib.class.getResource("ico/SPECIFIC.png")));
        icons.put(Type.CHAR_CLASS, new ImageIcon(IconLib.class.getResource("ico/CHARCLASS.png")));
        icons.put(Type.EXPRESSION, new ImageIcon(IconLib.class.getResource("ico/REGEX.png")));
        icons.put(Type.NON_CAPTURING, new ImageIcon(IconLib.class.getResource("ico/NONCAPTURING.png")));
        icons.put(Type.ATOMIC, new ImageIcon(IconLib.class.getResource("ico/ATOMIC.png")));
        icons.put(Type.INCOMPLETE, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.UNBALANCED, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.INVALID_BACKREFERENCE, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.INVALID_INTERVAL, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.INVALID_RANGE, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.INVALID_QUANTIFIER, new ImageIcon(IconLib.class.getResource("ico/ERROR.png")));
        icons.put(Type.COMMENT, new ImageIcon(IconLib.class.getResource("ico/COMMENT.png")));
    }

    /**
     * @return only instance of class.
     */
    public static IconLib getInstance() {
        return INSTANCE;
    }

    /**
     * Provide icon chosen for given type of Construct object.
     * @param type of Construct objects.
     * @return icon chosen for given type.
     */
    public Icon getIcon(Type type) {
        return icons.get(type);
    }



}
