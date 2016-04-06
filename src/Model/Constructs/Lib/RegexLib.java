package Model.Constructs.Lib;

import Model.Constructs.Type;

import java.util.HashMap;
import java.util.Map;

import static Model.Constructs.Type.*;
public class RegexLib {
    private final static RegexLib INSTANCE = new RegexLib();
    private final Map<Type,String> expressions;

    private RegexLib() {
        expressions = new HashMap<>();
        expressions.put(BOUNDARY,"\\\\[bBAGZz]|[\\^\\$]");
        expressions.put(CHAR_CLASS,"\\[");
        expressions.put(MODE,"\\(\\?[imdsuxU]+-?([imdsuxU]+)?\\)");
        expressions.put(LOGICAL,"\\|");
        expressions.put(PREDEFINED,"\\\\[dDsSwWpP](\\{[^}]*\\}?+)?+|\\.");
        expressions.put(QUANTIFIER,"([?*+][?+]?)");
        expressions.put(INTERVAL,"\\{\\d+(,(\\d+)?)?\\}[+?]?");
        expressions.put(SPECIFIC_CHAR,"\\\\(0[0-3]?[0-7]?[0-7]|x([1-9A-F]{2}|\\{[^}]+\\})|u[1-9A-F]{4}|\\\\|[tnrfae]|c\\w)");
        expressions.put(QUOTATION,"\\\\Q((?:(?!\\\\E).)*)\\\\E|\\\\.");
        expressions.put(GROUP,"\\(");
        expressions.put(ATOMIC,"\\(\\?\\>.+\\)");
        expressions.put(LOOK_AROUND,"\\(\\?([=!]|<[=!]).+\\)");
        expressions.put(CAPTURING,"^\\((?:(?!\\?)|\\?\\<(?<name>[^>]*)\\>)?.*\\)");
        expressions.put(NON_CAPTURING,"\\(\\?([imdsuxU]+(-[imdsuxU]+)?)?:.+\\)");
        expressions.put(BACKREFERENCE,"\\\\(?:(\\d+)|k\\<([^>]+)\\>)");
        expressions.put(SIMPLE,".");
        expressions.put(RANGE,"\\p{ASCII}-[\\p{ASCII}&&[^]]]|\\\\\\w+-\\\\\\w+");
        expressions.put(UNBALANCED,"\\((\\?(\\<(\\w+|[=!])?|[:=!>]|[\\w-]+:?)?)?");
        expressions.put(INCOMPLETE,"\\\\([xcu]|p(\\{)?|k(<[^>]*)?)");
    }

    public String getRegEx(Type type) {
        return expressions.get(type);
    }


}
