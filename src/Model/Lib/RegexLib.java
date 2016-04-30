package Model.Lib;

import Model.Regex.Type;

import java.util.HashMap;
import java.util.Map;

import static Model.Regex.Type.*;

/**
 * Provide regular expression for recognition of particular pattern elements in given String.
 */

public class RegexLib {
    private final static RegexLib INSTANCE = new RegexLib();
    private final Map<Type,String> lib;

    private RegexLib() {
        lib = new HashMap<>();
        lib.put(BOUNDARY, "\\\\[bBAGZz]|[\\^\\$]");
        lib.put(CHAR_CLASS, "\\[");
        lib.put(MODE, "\\(\\?[imdsuxU]+-?([imdsuxU]+)?\\)");
        lib.put(COMPONENT, "\\||\\)");
        lib.put(PREDEFINED, "\\\\[dDsSwWpP](\\{[^}]*\\}?+)?+|\\.");
        lib.put(QUANTIFIER, "([?*+][?+]?)");
        lib.put(INTERVAL, "\\{\\d+(,(\\d+)?)?\\}[+?]?");
        lib.put(SPECIFIC_CHAR, "\\\\(0[0-3]?[0-7]?[0-7]|x([1-9A-F]{2}|\\{[^}]+\\})|u[1-9A-F]{4}|\\\\|[tnrfae]|c\\w)");
        lib.put(QUOTATION, "\\\\Q((?:(?!\\\\E).)*)\\\\E|\\\\.");
        lib.put(GROUP, "\\(");
        lib.put(ATOMIC, "\\(\\?\\>.+\\)");
        lib.put(LOOK_AROUND, "\\(\\?([=!]|<[=!]).+\\)");
        lib.put(CAPTURING, "^\\((?:(?!\\?)|\\?\\<(?<name>[^>]*)\\>)?.*\\)");
        lib.put(NON_CAPTURING, "\\(\\?([imdsuxU]+(-[imdsuxU]+)?)?:.+\\)");
        lib.put(BACKREFERENCE, "\\\\(?:(\\d+)|k\\<([^>]+)\\>)");
        lib.put(SIMPLE, ".");
        lib.put(RANGE, "\\p{ASCII}-[\\p{ASCII}&&[^]]]|\\\\\\w+-\\\\\\w+");

        lib.put(UNBALANCED, "\\((\\?(\\<(\\w+|[=!])?|[:=!>]|[\\w-]+:?)?)?");
        lib.put(INCOMPLETE, "\\\\([xcu]|p(\\{)?|k(<[^>]*)?)");
    }

    public String getRegEx(Type type) {
        return lib.get(type);
    }

    /**
     * @return only one available instance of class
     */
    public static RegexLib getInstance() {
        return INSTANCE;
    }

    /**
     * Checks if set of regular expression contains a pattern of given type.
     * @param type enum representing regular expression constructs types
     * @return true if set contains pattern of given type
     */
    public boolean contains(Type type) {
        return lib.containsKey(type);
    }


}
