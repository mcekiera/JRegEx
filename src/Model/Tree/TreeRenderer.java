package Model.Tree;

import Model.Regex.Construct;
import Model.Regex.Type.Type;
import View.Color.InputColor;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TreeRenderer extends DefaultTreeCellRenderer{

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

        if (((Construct)value).getType()!= Type.COMPONENT) {
            Component c =  super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4, arg5, arg6);
            if(((Construct)value).getType()== Type.CHAR_CLASS) {
                c.setBackground(InputColor.CHAR_CLASS.getColor());
            }
            return c;
        }

        return new JLabel();
    }
}
