package View.Tree;

import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

public class TreeAdapter implements TreeNode {
    private Construct construct;
    private Elements elements;

    public TreeAdapter(Construct construct) {
        this.construct = construct;
        if(construct.isComplex()) {
            elements = new Elements((Composite)construct);
        }
    }

    public Construct getConstruct() {
        return construct;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return new TreeAdapter(((Complex)construct).getConstruct(childIndex));
    }

    @Override
    public int getChildCount() {
        return ((Complex)construct).size();
    }

    @Override
    public TreeNode getParent() {
        return new TreeAdapter(construct.getParent());
    }

    @Override
    public int getIndex(TreeNode node) {
        return ((Complex)construct).getConstructIndex(((TreeAdapter)node).getConstruct());
    }

    @Override
    public boolean getAllowsChildren() {
        return construct.isComplex();
    }

    @Override
    public boolean isLeaf() {
        return !(construct.isComplex());
    }

    @Override
    public Enumeration children() {
        return elements;
    }

    public class Elements implements Enumeration<TreeAdapter> {
        private Composite construct;
        public Elements(Composite construct) {
             this.construct = construct;
        }

        @Override
        public boolean hasMoreElements() {
            return ((Composite)construct).iterator().hasNext();
        }

        @Override
        public TreeAdapter nextElement() {
            return new TreeAdapter(((Composite) construct).iterator().next());
        }
    }
}
