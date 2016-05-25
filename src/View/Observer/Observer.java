package View.Observer;

/**
 * Simple implementation of Observer design pattern.
 */
public interface Observer {
    /**
     * Updates state of object.
     * @param source sort of event.
     */
    void update(Observed source);
}
