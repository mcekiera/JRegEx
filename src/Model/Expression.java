package Model;


import Model.Match.Overall;
import Model.Regex.Composite;
import Model.Regex.CompositeBuilder;
import Model.Regex.Construct;
import Model.Tree.RegExTree;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Expression {
    private final CompositeBuilder builder = CompositeBuilder.getInstance();
    private Overall overallMatch;
    private Composite root;

    public Composite getRoot() {
        return root;
    }

    public void set(String pattern,String test) {
        root = builder.toComposite(pattern);
        if(builder.isValid()) {
            overallMatch = new Overall(pattern,test,true);
        }
    }

    private Iterator<Construct> elements() {
        return root.iterator();
    }

    private Map<Integer,List<Segment>> matchMap() {
        return overallMatch.matchMap();
    }

    public static void main(String[] args) {
        String pattern = "\\a\\bc\\d";
        CompositeBuilder builder = CompositeBuilder.getInstance();
        Expression expression = new Expression();
        Composite composite = builder.toComposite(pattern);

        for(Construct c : composite) {
            System.out.println(c.getType() + "," + c.toString());
            if(c.isComplex()) {
                for(Construct b : (Iterable<Construct>)c) {
                    System.out.println("    " + b.getType() + "," + b.toString());
                    if(b.isComplex()) {
                        for(Construct e : (Iterable<Construct>)b) {
                            System.out.println("        " + e.getType() + "," + e.toString());
                            if(e.isComplex()) {
                                for(Construct x : (Iterable<Construct>)e) {
                                    System.out.println("            " + x.getType() + "," + x.toString());
                                    if(x.isComplex()) {
                                        for(Construct z : (Iterable<Construct>)x) {
                                            System.out.println("                " + z.getType() + "," + z.toString());
                                            if(z.isComplex()) {
                                                for(Construct v : (Iterable<Construct>)z) {
                                                    System.out.println("                    " + v.getType() + "," + v.toString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RegExTree model = new RegExTree(expression);
        JTree tree = new JTree();
        tree.setModel(model);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(model.getRenderer());
        frame.add(tree);
        frame.pack();
        frame.setVisible(true);
    }
}
