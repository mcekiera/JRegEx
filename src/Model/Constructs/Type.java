package Model.Constructs;

/**
 * Enum class which classify common types of constructs and errors in regular expression. The classification is
 * simplified, and capture just general structures.
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
    ERROR();


    Type() {
    }
}

