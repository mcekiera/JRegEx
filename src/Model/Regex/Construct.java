package Model.Regex;

import Model.Regex.Type.Type;
import Model.Segment;

/**
 * Abstract superclass for all logical representations of separate construct in regular expressions. It contains
 * a text fragment with individual element, with information about its type and position. Position is defined
 * by start and end index of fragment of text containing given construct.
 */

public abstract class Construct {
    /**
     * Segment Object that holds a text form of given Construct. It contains String, and int values for start and
     * end indices of text form within whole pattern.
     */
    private final Segment textual;
    /**
     * Enum Type type is a Object which designate what kind of regular expression construct given Construct object
     * represent. It serves to distinguish constructs of similar structure, to treat it different on other stages
     * of expression processing.
     */
    private final Type type;
    /**
     * Construct within which this Construct is placed in pattern.
     */
    private final Construct parent;

    private String description;

    public Construct(Construct parent, Type type, Segment segment) {
        this.textual = segment;
        this.type = type;
        this.parent = parent;
        description = "";
        //System.out.println(getType() + "," + getStart() + "," + getEnd() + "," + toString());
    }

    /**
     * @return Start index of given construct in tested String.
     */
    public int getStart() {
        return textual.getStart();
    }

    /**
     * @return End index of given construct in tested String.
     */
    public int getEnd() {
        return textual.getEnd();
    }

    /**
     * It returns enum Type object with serves to distinguish different kinds of regular expression constructs form
     * each other.
     * @return Type enum object,
     */
    public Type getType() {
        return type;
    }

    /**
     * @return another Construct object, within which it is positioned
     */
    public Construct getParent() {
        return parent;
    }

    /**
     * @return length, understood as number of positions the construct occupy in tested String.
     */
    public int length() {
        return textual.toString().length();
    }

    public String getText() {
        return textual.toString();
    }

    /**
     * @return full version of tested pattern.
     */
    public String getPattern() {
        return textual.getContent();
    }

    /**
     * @return textual representation of Construct.
     */
    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * Compare to another object
     * @param object anther object ot compere with.
     * @return true if objects are equal, if are same Class, has same parent, start and end index, type and text form.
     */
    @Override
    public boolean equals(Object object) {
        return object != null
                && object instanceof Construct
                && this.parent == ((Construct) object).getParent()
                && this.getStart() == ((Construct) object).getStart()
                && this.getEnd() == ((Construct) object).getEnd()
                && this.getType() == ((Construct) object).getType()
                && this.getText().equals(((Construct) object).getText());
    }

    /**
     * @return description of given Construct
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Identify if construct is complex, understood as composed of other constructs.
     * @return true if it is composed of other constructs.
     */
    public abstract boolean isComplex();
}
