package Re.Regex.Type;

import Model.Constructs.Type;

public enum Boundary implements SpecificType {

    BEGINNING_LINE("^","Beginning of a line"),
    END_LINE("$", "End of a line"),
    WORD_BOUNDARY("\b","A word boundary"),
    NON_WORD_BOUNDARY("\\B","A non-word boundary"),
    BEGINNING_INPUT("\\A","Beginning of input"),
    END_PREVIOUS_MATCH("\\G", "End of previous match"),
    END_INPUT("\\Z", "End of input"),
    VERY_END("\\z", "Very end of input");

    private final String form;
    private final String description;

    Boundary(String form, String description) {
        this.form = form;
        this.description = description;
    }

    public Type getType() {
        return Type.BOUNDARY;
    }

    public String getForm() {
        return form;
    }

    public String getDescription() {
        return description;
    }

    public static SpecificType getSpecificType(String str) {
        for(Boundary boundary : Boundary.values()) {
            if(str.equals(boundary.getForm())) {
                return boundary;
            }
        }
        return null;
    }


}
