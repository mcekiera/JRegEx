package Model;


import Controller.ToolTipable;
import Model.Match.Detail;
import Model.Match.Overall;
import Model.Regex.Composite;
import Model.Regex.CompositeBuilder;
import Model.Regex.Construct;
import Model.Regex.Type;
import View.Part;

import java.util.List;
import java.util.Map;

/**
 * Represents currently processed regular expression, typed by User, and holds all its matching properties.
 */
public class Expression implements ToolTipable {
    private final CompositeBuilder builder = CompositeBuilder.getInstance();
    /**
     *  Object responsible for data about all matched fragments from tested String.
     */
    private Overall overallMatch;
    /**
     *  Object responsible for data about separate match by particular elements of pattern.
     */
    private Detail detailMatch;
    /**
     * Composite object which holds all Construct object of given pattern.
     */
    private Composite root;

    public Expression() {
        root = new Composite(null, Type.EXPRESSION, new Segment("", 0, 0));
    }

    /**
     * @return Composite object which holds all Construct object of given pattern.
     */
    public Composite getRoot() {
        return root;
    }

    /**
     * Sets new regular expression pattern and process it: brakes into constructs, find overall matches.
     *
     * @param pattern regular expression to analyze.
     * @param test    String to match against pattern.
     */
    public void set(String pattern, String test) {
        if (!root.getText().equals(pattern)) {
            root = builder.toComposite(pattern);
        }
        if (builder.isValid()) {
            overallMatch = new Overall(pattern, test, true);
            overallMatch.setNamed(builder.getNames());
        }
    }

    /**
     * Matches all constructs of current pattern against particular matched String, to get detailed data about
     * particular constructs matching.
     * @param selected example matched String.
     */
    public void detail(Segment selected) {
        try {
            detailMatch = new Detail(root, selected);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            //e.printStackTrace();
        }
    }

    /**
     * @return object containing all matched fragments from tested String.
     */
    public Overall getOverallMatch() {
        return overallMatch;
    }

    /**
     * @return map containing all fragments matched by elements of pattern separately.
     */
    public Map<Construct, List<Segment>> getDetailMatches() {
        return detailMatch.getDetailMatch();
    }


    /**
     * Determine if pattern typed by User is valid.
     * @return true if valid.
     */
    public boolean isValid() {
        return builder.isValid();
    }

    @Override
    public String getInfoFromPosition(int i, Part part) {
        switch (part) {
            case INPUT:
                return getRoot().getConstructFromPosition(i).toString();
            case MATCHING:
                return getOverallMatch().getMatchByPosition(i);
            default:
                return null;
        }
    }
}

