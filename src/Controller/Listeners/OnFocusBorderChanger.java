package Controller.Listeners;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * FocusListener implementation, which changes Border object of given JComponent, between two provided versions.
 */
public class OnFocusBorderChanger implements FocusListener {
    /**
     * Secondary Border object - to which border of given JComponent is changed after gaining focus.
     */
    private final Border onFocus;
    /**
     * Primary Border object - original border of given JComponent.
     */
    private Border border;


    public OnFocusBorderChanger(Border onFocus) {
        this.onFocus = onFocus;

    }

    @Override
    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        border = component.getBorder();
        component.setBorder(onFocus);
        component.revalidate();
    }

    @Override
    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBorder(border);
        component.revalidate();
    }
}
