package Model.Constructs;

public enum Type {
    BOUNDARY("\\\\[bBAGZz]|[\\^\\$]"),
    CHAR_CLASS("\\["),//"\\[\\]\\]|(\\[((?:\\\\\\[|\\\\\\]|[^\\[\\]])*(?:(?:&&\\[\\^|\\[)(?:\\\\\\[|\\\\\\]|[^\\[\\]])*\\])?(?:\\\\\\[|\\\\\\]|[^\\[\\]])*)+\\])"),
    MODE("\\(\\?[imdsuxU]+-?([imdsuxU]+)?\\)"),
    LOGICAL("\\|"),
    PREDEFINED("\\\\[dDsSwWpP](\\{[^}]*\\}?+)?+|\\."),
    QUANTIFIER("([?*+][?+]?)|\\{\\d+(,(\\d+)?)?\\}"),

    TOKEN("([?*+][?+]?)"),
    INTERVAL("\\{\\d+(,(\\d+)?)?\\}[+?]?"),

    SPECIFIC_CHAR("\\\\(0[0-3]?[0-7]?[0-7]|x([1-9A-F]{2}|\\{[^}]+\\})|u[1-9A-F]{4}|\\\\|[tnrfae]|c\\w)"),
    QUOTATION("\\\\Q((?:(?!\\\\E).)*)\\\\E|\\\\."),

    GROUP("\\("),
    ATOMIC("\\(\\?\\>.+\\)"),
    LOOK_AROUND("\\(\\?([=!]|<[=!]).+\\)"),
    CAPTURING("^\\((?:(?!\\?)|\\?\\<(?<name>[^>]*)\\>)?.*\\)"),
    NON_CAPTURING("\\(\\?([imdsuxU]+(-[imdsuxU]+)?)?:.+\\)"),
    BACKREFERENCE("\\\\(?:(\\d+)|k\\<([^>]+)\\>)"),
    SIMPLE("."),
    RANGE("\\p{ASCII}-\\p{ASCII}|\\\\\\w+-\\\\\\w+"),
    COMPONENT(),
    EXPRESSION(),

    UNBALANCED("\\((\\?(\\<(\\w+|[=!])?|[:=!>]|[\\w-]+:?)?)?"),
    INCOMPLETE("\\\\([xcu]|p(\\{)?|k(<[^>]*)?)"),
    INVALID_RANGE(),
    INVALID_INTERVAL(),
    INVALID_BACKREFERENCE(),
    ERROR();

    String regex;

    Type(String regex) {
        this.regex = regex;
    }

    Type() {
        regex = "";
    }

    public String getRegex() {
        return regex;
    }
}
