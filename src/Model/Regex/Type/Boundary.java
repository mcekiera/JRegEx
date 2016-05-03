package Model.Regex.Type;

public enum Boundary implements SpecificType {
    BEGINNING_OF_A_LINE("^","The beginning of a line"),
    END_OF_A_LINE("$","The end of a line"),
    WORD_BOUNDARY("\\b","A word boundary"),
    NONWORD_BOUNDARY("\\B","A non-word boundary"),
    INPUT_BEGINNING("\\A","The beginning of the input"),
    PREVIOUS_MATCH("\\G","The end of the previous match"),
    FINAL_INPUT_END("\\Z","The end of the input but for the final terminator, if any"),
    INPUT_END("\\z","The end of the input");

    private static Type type = Type.BOUNDARY;
    private String desc;
    private String basic;

    Boundary(String basic, String desc) {

    }

    @Override
    public Type getGeneralType() {
        return type;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public String getBasicForm() {
        return basic;
    }

    /**
     * Provide a specific Enum class object describing particular regular expression construct.
     * @param form basic String form of captured construct
     * @return specific Enum type, or null if does not find any
     */
    public static Boundary getSpesificType(String form) {
        for(Boundary b : values()) {
            if(form.equals(b.getBasicForm())) {
                return b;
            }
        }
        return null;
    }

}
