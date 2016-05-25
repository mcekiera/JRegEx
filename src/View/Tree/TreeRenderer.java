package View.Tree;

import Controller.HighlightManager.HighlightManager;
import Model.Lib.IconLib;
import Model.Regex.Construct;
import Model.Regex.Quantifier;
import Model.Regex.Type;
import View.Color.GroupColor;
import View.Color.InputColor;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Renders custom look of RegExTree, it paints icons and highlight text in different color for diverse
 * categories of constructs. Works only with RegExTree objects.
 */
public class TreeRenderer extends DefaultTreeCellRenderer{
    /**
     * Provides icon for given constructs categories.
     */
    private IconLib lib = IconLib.getInstance();
    /**
     * Determines if processed regular expression is valid.
     */
    private boolean valid;
    /**
     * Object managing highlighting of other, separate, interconnected display part.
     */
    private HighlightManager manager;


    public TreeRenderer(HighlightManager manager, boolean valid) {
        this.valid = valid;
        this.manager = manager;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
        if(valid) {
            if (((Construct) value).getType() != Type.COMPONENT) {
                JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);
                configNode(((Construct) value), c);

                if(arg2) {
                    c.setBackground(Color.CYAN);
                    if(value instanceof Quantifier) {
                        manager.selectionHighlight(((Construct) value).getEnd()-1);
                    } else {
                        manager.selectionHighlight(((Construct) value).getStart());
                    }
                }
                return c;
            }
        } else {
            if (isInvalid(((Construct) value).getType())) {
                JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);
                configNode(((Construct) value), c);
                if(arg2) {
                    c.setBackground(Color.CYAN);
                    manager.selectionHighlight(((Construct) value).getStart());
                }
                return c;
            }

        }

        return new JLabel();
    }

    /**
     * Configure look of given Components, depending on Type of represented Construct.
     * @param construct Construct object which node representation is modified.
     * @param c JComponent representing node
     * @return modified JComponent
     */
    private JComponent configNode(Construct construct, JComponent c) {
        c.setOpaque(true);
            switch (construct.getType()) {
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
                case BACKREFERENCE:
                case BOUNDARY:
                case INTERVAL:
                    c.setBackground(InputColor.PREDEFINED.getColor());
                    break;
                case CAPTURING:
                case NON_CAPTURING:
                case LOOK_AROUND:
                case ALTERNATION:
                    c.setBackground(GroupColor.GROUP1.getColor());
                    break;
                case QUOTATION:
                    c.setBackground(Color.LIGHT_GRAY);
                    break;
                case INVALID_RANGE:
                case INVALID_BACKREFERENCE:
                case INVALID_INTERVAL:
                case INVALID_QUANTIFIER:
                case UNBALANCED:
                case INCOMPLETE:
                    c.setBackground(Color.RED);
                    break;
                default:
                    c.setBackground(Color.WHITE);
                    break;
            }
            setIcon(lib.getIcon(construct.getType()));
        return c;
    }

    /**
     * Determine if given Type object represents error in regualar expressions.
     * @param type Type object to check
     * @return true if represents error.
     */
    public boolean isInvalid(Type type) {
        return type == Type.UNBALANCED || type == Type.INCOMPLETE || type == Type.INVALID_BACKREFERENCE ||
                type == Type.INVALID_INTERVAL || type == Type.INVALID_RANGE || type == Type.INVALID_QUANTIFIER;
    }
}
