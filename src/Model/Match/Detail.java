package Model.Match;

import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Quantifier;
import Model.Regex.Type.Type;
import Model.Segment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides data about how every separate construct within given pattern, match on particular test string.
 */
public class Detail {
    /**
     * Test string
     */
    private Segment matched;
    /**
     * Map containing constructs as key, and lists of fragments matched by it
     */
    private Map<Construct,List<Segment>> detailMatch;

    public Detail(Composite composite, Segment matched) {
        detailMatch = new LinkedHashMap<>();
        this.matched = matched;
        process(composite, matched);

        //print();
    }

    /**
     * Match every construct, within given pattern, on test string. As a result it create a map of matches
     * by particular construct.
     * @param complex Complex object representing pattern
     * @param segment Test string
     */
    public void process(Complex complex, Segment segment) {
        Matcher matcher;
        Segment current;
        for(Construct construct : complex) {
            System.out.println(construct.getType() + "," +construct.getStart() +"," + construct.getEnd() + ": " + construct.getText());
            if(construct.getType() != Type.COMPONENT) {
                try {
                    //System.out.println(((Construct)complex).getType() + "," + construct.getType() + "," + construct.getStart());
                    //System.out.println(segment.toString());
                    matcher = Pattern.compile(getAdHocPattern((Construct) complex, construct)).matcher(segment.toString());
                    if (matcher.find()) {
                        System.out.println(">>" + matcher.pattern());
                        current = new Segment(matched.toString(),
                                segment.getStart() + matcher.start("NamedGroup"), segment.getStart() + matcher.end("NamedGroup"));
                        System.out.println(current.getDescription());
                        if(construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL) {
                            process((Quantifier)construct,current);
                        } else if (construct.isComplex() && construct.getType()!=Type.CHAR_CLASS) {
                            process((Complex) construct, current);
                        }
                        addMatch(construct, current);
                    } else {
                        addMatch(construct, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                continue;
            }
        }
    }

    /**
     * Match every construct, within given quantifier, on test string.
     * @param quantifier
     * @param segment
     */
    public void process(Quantifier quantifier,Segment segment) {
        Construct construct = quantifier.getConstruct(0);
        Segment current;
        try {
            Matcher matcher = Pattern.compile(construct.getText()).matcher(segment.toString());
            while (matcher.find()) {
                current = new Segment(matched.toString(),
                        segment.getStart() + matcher.start(), segment.getStart() + matcher.end());
                if (construct.isComplex() && construct.getType() != Type.CHAR_CLASS) {
                    process((Complex) construct, current);
                }
                addMatch(construct, current);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates string with given construct in a named group.
     * @param construct for new pattern creation
     * @return String with pattern, within given construct is as separate named group.
     */
    private String getAsGroup(Construct construct) {
        return "(?<NamedGroup>" + construct.getText() + ")";
    }

    /**
     * Creates String representing regular expression, with given construct treated as separate named group.
     * @param complex object representing whole pattern
     * @param construct construct to try as separate named group
     * @return whole pattern with given construct as named group.
     */
    private String getAdHocPattern(Construct complex, Construct construct) {
        return complex.getText().substring(0,construct.getStart()-complex.getStart())
                + getAsGroup(construct)
                + complex.getText().substring(construct.getEnd()-complex.getStart());
    }


    /**
     * Adds given match to list of matches by constructs of given pattern.
     * @param construct currently matched construct
     * @param segment text fragment matched directly by tried construct
     */
    private void addMatch(Construct construct, Segment segment) {
        if(detailMatch.get(construct)!=null) {
            detailMatch.get(construct).add(segment);
        } else {
            detailMatch.put(construct,new ArrayList<Segment>());
            detailMatch.get(construct).add(segment);
        }
    }

    /**
     * @return Map of matches
     */
    public Map<Construct, List<Segment>> getDetailMatch() {
        return detailMatch;
    }


}
