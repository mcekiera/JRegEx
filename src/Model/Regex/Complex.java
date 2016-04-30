package Model.Regex;

/**
 * Interface for classes which objects could contain one or more another Construct objects.
 */

public interface Complex{

    /**
     * Add construct to collection within Complex object.
     * @param construct added Construct object
     */
    void addConstruct(Construct construct);

    /**
     * Returns the Construct object, which represent regular expression construct placed on given index within
     * processed text form of pattern.
     * @param index of wanted construct, within text form of pattern.
     * @return Construct
     */
    Construct getConstructFromPosition(int index);

    /**
     * Return the child Construct object that is part of Complex object, placed on given index position in interior
     * Collection object.
     * @param i place index within Collection object
     * @return Construct object
     */
    Construct getConstruct(int i);

    /**
     * Returns integer representing index on which fiven Construct is placed within Complex objects interior Collection
     * @param construct which position index is requested
     * @return integer
     */
    int getConstructIndex(Construct construct);

    /**
     * @return size (number of internal constructs) of given Complex object
     */
    int size();
}
