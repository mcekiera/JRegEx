package Model;


import Controller.ToolTipable;
import Model.Match.Detail;
import Model.Match.Overall;
import Model.Regex.Composite;
import Model.Regex.CompositeBuilder;
import Model.Regex.Construct;
import Model.Regex.Type;
import View.Part;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Expression implements ToolTipable{
    private final CompositeBuilder builder = CompositeBuilder.getInstance();
    private Overall overallMatch;
    private Detail detailMatch;
    private Composite root;

    public Expression() {
        root = new Composite(null, Type.EXPRESSION,new Segment("",0,0));
    }

    public Composite getRoot() {
        return root;
    }

    public void set(String pattern,String test) {
        if(!root.getText().equals(pattern)) {
            root = builder.toComposite(pattern);
        }
        if(builder.isValid()) {
            overallMatch = new Overall(pattern,test,true);
            overallMatch.setNamed(builder.getNames());
        }
    }

    public void detail(Segment selected) {
        try {
            detailMatch = new Detail(root,selected);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            //e.printStackTrace();
        }
    }

    public Overall getOverallMatch() {
        return overallMatch;
    }

    public boolean isValid() {
        return builder.isValid();
    }

    private Iterator<Construct> elements() {
        return root.iterator();
    }

    private Map<Integer,List<Segment>> matchMap() {
        return overallMatch.matchMap();
    }

    @Override
    public String getInfoFromPosition(int i, Part part) {
        switch (part) {
            case INPUT:
                return  getRoot().getConstructFromPosition(i).toString();
            case MATCHING:
                return getOverallMatch().getMatchByPosition(i);
            default:
                return null;
        }
    }

    public Map<Construct, List<Segment>> getDetailMatches() {
        return detailMatch.getDetailMatch();
    }
}
