package View.Observer;

/**
 * Simple implementation of Observer patter, provides interface of observed object that could provide information about
 * changes.
 */
public interface Observed {
    /**
     * Notify object, added as Observers, about changes within observed object.
     */
    void notifyObservers();

    /**
     * Adds Observer object ot list of Observers.
     * @param observer object.
     */
    void addObserver(Observer observer);
}
