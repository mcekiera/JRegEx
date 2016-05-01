package Controller.Listeners;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class OnFocusBorderChanger implements FocusListener {
    Border border;

    @Override
    public void focusGained(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        border = component.getBorder();
        Border border = BorderFactory.createCompoundBorder(new LineBorder(Color.cyan, 1), new EmptyBorder(4,4,4,4));
        component.setBorder(border);
        component.revalidate();
    }

    @Override
    public void focusLost(FocusEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setBorder(border);
        component.revalidate();
    }
}
