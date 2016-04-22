package Re.Regex;

import Model.Constructs.Type;
import Re.Segment;

/**
 * Abstract superclass for all logical representations of separate construct of regular expressions. It contains
 * a tex fragment with individual element, information about its type and position.
 */

public abstract class Construct {
    private final Segment textual;
    private final Type type;
    private final Construct parent;

    public Construct(Construct parent, Type type, Segment segment) {
        this.textual = segment;
        this.type = type;
        this.parent = parent;
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
        return textual.toString();
    }

    /**
     * Compare to another object
     * @param other anther object ot compere with.
     * @return true if objects are equal, if are same Class, has same parent, start and end index, type and text form.
     */
    @Override
    public boolean equals(Object other) {
        return other != null
                && other instanceof Construct
                && this.parent == ((Construct) other).getParent()
                && this.getStart() == ((Construct) other).getStart()
                && this.getEnd() == ((Construct) other).getEnd()
                && this.getType() == ((Construct) other).getType()
                && this.toString().equals(other.toString());
    }

    /**
     * @return description of given Construct
     */
    public abstract String getDescription();

    /**
     * Identify if construct is complex, understood as composed of other constructs.
     * @return true if it is composed of other constructs.
     */
    public abstract boolean isComplex();
}
