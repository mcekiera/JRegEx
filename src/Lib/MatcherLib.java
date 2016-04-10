package Lib;

import Model.Constructs.Type;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherLib {
    private static final Map<Type,Matcher> lib = new TreeMap<Type, Matcher>();
    private static final MatcherLib INSTANCE = new MatcherLib();

    private MatcherLib() {
        for(Type type : Type.values()) {
            if(RegexLib.getInstance().contains(type)) {
                lib.put(type, Pattern.compile(RegexLib.getInstance().getRegEx(type)).matcher(""));
            }
        }
    }

    public static MatcherLib getInstance() {
        return INSTANCE;
    }

    public Matcher getMatcher(Type type) {
        return lib.get(type);
    }

    public int getEndOfLastMatch(Type type) {
        return getMatcher(type).end();
    }

    public String getGroup(Type type, String key) {
        try {
            return lib.get(type).group(key);
        }catch (IllegalStateException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public String getGroup(Type type, int key) {
        try {
            return lib.get(type).group(key);
        }catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }
}
