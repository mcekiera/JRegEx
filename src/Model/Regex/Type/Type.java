package Model.Regex.Type;

/**
 * Represents categories of regular expression constructs, that could be treated same way on other stages
 * of processing. There are some obvious categories, as MODE, which contain only construct of same kind (modifiers),
 * but there are also more complicated categories, as QUOTATION (for simple "/" quote, and complex "\Qxxx\E").
 */
public enum Type {
    BOUNDARY(),
    CHAR_CLASS(),
    MODE(),
    LOGICAL(),
    PREDEFINED(),
    QUANTIFIER(),
    INTERVAL(),
    SPECIFIC_CHAR(),
    QUOTATION(),
    BACKREFERENCE(),
    SIMPLE(),

    ALTERNATION(),
    GROUP(),
    ATOMIC(),
    LOOK_AROUND(),
    CAPTURING(),
    NON_CAPTURING(),

    RANGE(),
    COMPONENT(),
    EXPRESSION(),

    UNBALANCED(),
    INCOMPLETE(),
    INVALID_RANGE(),
    INVALID_INTERVAL(),
    INVALID_BACKREFERENCE(),
    ERROR(),

    COMMENT();


    Type() {
    }
}

