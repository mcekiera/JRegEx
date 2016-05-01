package Controller.HighlightManager;

/**
 * Abstract class providing interface for objects managing highlighting fragments within given JTextComponent.
 */
public abstract class HighlightManager {
    /**
     * Changes highlight color of object represented on given position in text.
     * @param position position in text.
     */
    public abstract void selectionHighlight(int position);
}
