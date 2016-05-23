package Model.Lib;

import Model.Regex.Type;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton class. Provide Matcher objects for capturing particular fragments from given String. Matcher are
 * created from patterns available in RegexLib, but must be reset() to match desired String.
 */

public class MatcherLib {
    /**
     * Map with regex for particular regular expression constructs recognition.
     */
    private static final Map<Type,Matcher> lib = new TreeMap<>();
    /**
     * Only instance of DescLib class.
     */
    private static final MatcherLib INSTANCE = new MatcherLib();

    private MatcherLib() {
        for(Type type : Type.values()) {
            if(RegexLib.getInstance().contains(type)) {
                lib.put(type, Pattern.compile(RegexLib.getInstance().getRegEx(type)).matcher(""));
            }
        }
    }

    /**
     * @return only instance of class.
     */
    public static MatcherLib getInstance() {
        return INSTANCE;
    }

    /**
     * Returns Matcher object with compiled Pattern object of given type.
     * @param type of desired pattern
     * @return Matcher with chosen pattern
     */
    public Matcher getMatcher(Type type) {
        return lib.get(type);
    }

    /**
     * Returns end index of last matched fragment.
     * @param type of desired pattern.
     * @return int end index of fragment
     */
    public int getEndOfLastMatch(Type type) {
        return getMatcher(type).end();
    }
}
