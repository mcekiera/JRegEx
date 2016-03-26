package Model.Constructs.Lib;

import Model.Constructs.Types.Type;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherLib {
    private static final Map<Type,Matcher> lib = new TreeMap<Type, Matcher>();
    private static final MatcherLib instance = new MatcherLib();

    private MatcherLib() {
        for(Type type : Type.values()) {
            lib.put(type,Pattern.compile(type.getRegex()).matcher(""));
        }
    }

    public static MatcherLib getInstance() {
        return instance;
    }

    public Matcher getMatcher(Type type) {
        return lib.get(type);
    }

    public int getEndOfLastMatch(Type type) {
        return getMatcher(type).end();
    }
}
