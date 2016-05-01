package Controller.Listeners;

import Model.Expression;
import Model.Regex.Construct;

import javax.swing.text.JTextComponent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseHoover implements MouseMotionListener {
    JTextComponent component;
    Expression expression;

    public MouseHoover(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            component = (JTextComponent)e.getSource();
            Construct construct = expression.getRoot().getConstructFromPosition(component.viewToModel(e.getPoint()));
            component.setToolTipText(construct.getType() + "," + construct.toString() + "," + construct.getStart() + "," + construct.getEnd());
        }catch (NullPointerException ex) {
            //ex.printStackTrace();
        }

    }
}