package Syntax;

public class Element {
    private final String representation;
    private final String mark;
    private final String description;

    public Element(String representation, String mark, String description) {
        this.representation = representation;
        this.mark = mark;
        this.description = description;
    }

    public String getRepresentation() {
        return representation;
    }

    public String getMark() {
        return mark;
    }

    public String getDescription() {
        return description;
    }
}

