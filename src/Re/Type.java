package Re;

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

    public boolean isQuantifier() {
        return this == QUANTIFIER || this == INTERVAL;
    }

    public boolean isError() {
        return this == INVALID_RANGE || this == INVALID_BACKREFERENCE || this == INVALID_INTERVAL ||
                this == INCOMPLETE || this == UNBALANCED || this == ERROR;
    }

    public boolean isSequence() {
        return this == CHAR_CLASS || this == CAPTURING || this == NON_CAPTURING || this == ATOMIC ||
                this == LOOK_AROUND || this == GROUP;
    }

    public boolean isSingular() {
        return (!(isError() || isQuantifier() || isSequence()));
    }
}

