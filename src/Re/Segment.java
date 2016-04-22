package Re;

/**
 * Holds section of a given String, for example fragment of content or matched text.
 */

public class Segment {
    private final int start;
    private final int end;
    private final String content;

    public Segment(String text, int start, int end) {
        this.start = start;
        this.end = end;
        this.content = text;
    }

    /**
     * @return index of text on which section starts.
     */
    public int getStart() {
        return start;
    }

    /**
     * @return index of text on which section ends.
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return Text from which section is taken.
     */
    public String getContent() {
        return content;
    }

    /**
     * @return String form of object. In this case String contains chosen fragment of text, between start
     * and end indices.
     */
    @Override
    public String toString() {
        return content.substring(start,end);
    }

}
