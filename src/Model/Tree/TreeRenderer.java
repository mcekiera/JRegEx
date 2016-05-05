package Model.Tree;

import Model.Lib.IconLib;
import Model.Regex.Construct;
import Model.Regex.Type.Type;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Renders custom look of RegExTree, it paints different icons and highlight text in different color for
 * diverse categories of constructs.
 */
public class TreeRenderer extends DefaultTreeCellRenderer{
    private IconLib lib = IconLib.getInstance();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

        if (((Construct)value).getType() != Type.COMPONENT) {
            JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);
            configNode(((Construct)value).getType(),c);
            return c;
        }

        return new JLabel();
    }

    /**
     * Configure look of given Components, depending on Type of represented Construct.
     * @param type of Construct represented by node
     * @param c node component
     * @return Component with chosen icon and highlight
     */
    private Component configNode(Type type, JComponent c) {
        c.setOpaque(true);
        switch (type) {
            case CHAR_CLASS:
            case RANGE:
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
            case BOUNDARY:
            case INTERVAL:
                c.setBackground(InputColor.PREDEFINED.getColor());
                break;
            case CAPTURING:
            case NON_CAPTURING:
            case LOOK_AROUND:
            case BACKREFERENCE:
                c.setBackground(GroupColor.GROUP1.getColor());
                break;
            case QUOTATION:
                c.setBackground(Color.LIGHT_GRAY);
                break;
            case INVALID_RANGE:
            case INVALID_BACKREFERENCE:
            case INVALID_INTERVAL:
            case UNBALANCED:
            case INCOMPLETE:
                c.setBackground(Color.RED);
                break;
            default:
                c.setBackground(Color.WHITE);
                break;
        }
        setIcon(lib.getIcon(type));
        return c;
    }
}
