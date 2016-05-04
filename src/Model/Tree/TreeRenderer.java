package Model.Tree;

import Model.Lib.IconLib;
import Model.Regex.Construct;
import Model.Regex.Type.Type;
import View.Color.InputColor;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TreeRenderer extends DefaultTreeCellRenderer{
    private IconLib lib = IconLib.getInstance();

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

        if (((Construct)value).getType() != Type.COMPONENT) {
            JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);
            configNode(((Construct)value).getType(),c);
            return c;
        }

        return new JLabel();
    }

    private Component configNode(Type type, JComponent c) {
        c.setOpaque(true);
        switch (type) {
            case CHAR_CLASS:
                c.setBackground(InputColor.CHAR_CLASS.getColor());
                break;
            case EXPRESSION:
                c.setBackground(Color.ORANGE);
                break;
            case MODE:
                c.setBackground(InputColor.MODE.getColor());
                break;
            case PREDEFINED:
            case QUANTIFIER:
                c.setBackground(InputColor.PREDEFINED.getColor());
                break;
            case QUOTATION:
                c.setBackground(Color.LIGHT_GRAY);
                break;
            default:
                c.setBackground(Color.WHITE);
                break;
        }
        setIcon(lib.getIcon(type));
        return c;
    }
}
