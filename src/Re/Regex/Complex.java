package Re.Regex;

/**
 * Interface for classes which objects could contain Construct objects.
 */

public interface Complex{

    /**
     * Add construct to collection within Complex object.
     * @param construct added Construct object
     */
    void addConstruct(Construct construct);
    Construct getConstructFromPosition(int index);
    Construct getConstruct(int i);
    int getConstructIndex(Construct construct);
    int size();
}
