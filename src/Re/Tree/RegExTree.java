package Re.Tree;

import Re.Expression;
import Re.Regex.Composite;
import Re.Regex.Construct;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class RegExTree implements TreeModel {
    Expression expression;
    public RegExTree(Expression expression){
        this.expression = expression;
    }

    @Override
    public Object getRoot() {
        return expression.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((Composite)parent).getConstruct(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((Composite)parent).size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return (!((Construct)node).isComplex());
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((Composite)parent).getConstructIndex((Construct)child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }

    public DefaultTreeCellRenderer getRenderer() {
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        return renderer;
    }
}
