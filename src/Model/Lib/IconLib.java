package Model.Lib;

import Model.Regex.Type.Type;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class IconLib {
    private static final IconLib INSTANCE = new IconLib();
    private final Map<Type,Icon> icons;

    private IconLib() {
        icons = new HashMap<>();
        icons.put(Type.BACKREFERENCE, new ImageIcon(IconLib.class.getResource("ico/PREDEFINED.gif")));
        icons.put(Type.BOUNDARY, new ImageIcon(IconLib.class.getResource("ico/BOUNDARY.gif")));
        icons.put(Type.CAPTURING, new ImageIcon(IconLib.class.getResource("ico/CAPTURING.gif")));
        icons.put(Type.SIMPLE, new ImageIcon(IconLib.class.getResource("ico/LITERAL.gif")));
        icons.put(Type.LOOK_AROUND, new ImageIcon(IconLib.class.getResource("ico/LOOKAROUND.gif")));
        icons.put(Type.MODE, new ImageIcon(IconLib.class.getResource("ico/MODE.gif")));
        icons.put(Type.PREDEFINED, new ImageIcon(IconLib.class.getResource("ico/BACKREFERENCE.gif")));
        icons.put(Type.QUANTIFIER, new ImageIcon(IconLib.class.getResource("ico/QUANTIFIER.gif")));
        icons.put(Type.QUOTATION, new ImageIcon(IconLib.class.getResource("ico/QUOTES.gif")));
        icons.put(Type.SPECIFIC_CHAR, new ImageIcon(IconLib.class.getResource("ico/SPECIFIC.gif")));
        icons.put(Type.CHAR_CLASS, new ImageIcon(IconLib.class.getResource("ico/CHARCLASS.gif")));
        icons.put(Type.EXPRESSION, new ImageIcon(IconLib.class.getResource("ico/REGEX.gif")));
    }

    public static IconLib getInstance() {
        return INSTANCE;
    }

    public Icon getIcon(Type type) {
        return icons.get(type);
    }



}
