package Controller.Listeners;

import Controller.ToolTipable;

import javax.swing.text.JTextComponent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Implementation of MouseMotionListener, which provides dynamic ToolTip addition to given JTextComponent.
 */
public class MouseHoover implements MouseMotionListener {
    /**
     * Object from which mouse position is taken.
     */
    JTextComponent component;
    /**
     * Object from which data about object on given position is taken.
     */
    ToolTipable toolTipable;

    public MouseHoover(ToolTipable toolTipable) {
        this.toolTipable = toolTipable;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            component = (JTextComponent)e.getSource();
            component.setToolTipText(toolTipable.getInfoFromPosition(component.viewToModel(e.getPoint())));
        }catch (NullPointerException ex) {
            //ex.printStackTrace();
        }

    }
}