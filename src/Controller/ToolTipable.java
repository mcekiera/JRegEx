package Controller;

/**
 * Provides a interface for object from which data could be requested about it representation in JTextComponent.
 */
public interface ToolTipable {
    String getInfoFromPosition(int i);
}
