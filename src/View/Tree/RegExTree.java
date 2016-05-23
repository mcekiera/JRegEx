package View.Tree;

import Controller.HighlightManager.HighlightManager;
import Model.Expression;
import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Tree model by which an Expression object could be displayed as graphic trees.
 */
public class RegExTree implements TreeModel {
    /**
     * Expression to display.
     */
    private  final Expression expression;

    public RegExTree(Expression expression){
        this.expression = expression;
    }


    @Override
    public Object getRoot() {
        return expression.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((Complex)parent).getConstruct(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((Complex)parent).size();
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

    /**
     * Provides a new TreeRenderer object. Method put inside RegExTree, as it has all necessary references, it serves
     * to limit dependencies of classes which use it.
     * @param manager HighlightManager object which highlight connected displays.
     * @param valid determine is given pattern is valid, therefore determine how tree should be displayed.
     * @return DefaultTreeCellRenderer for RegExTree object.
     */
    public DefaultTreeCellRenderer getRenderer(HighlightManager manager, boolean valid) {
        TreeRenderer renderer = new TreeRenderer(manager,valid);
        return renderer;
    }

}
