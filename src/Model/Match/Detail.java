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
     * Test string, text matched by checked pattern.
     */
    private Segment matched;
    /**
     * Map containing constructs as key, and lists of fragments matched by particular constructs.
     */
    private Map<Construct,List<Segment>> detailMatch;

    private String restrainedPattern = "";

    public Detail(Composite composite, Segment matched) {
        detailMatch = new LinkedHashMap<>();
        this.matched = matched;
        process(composite, matched);
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
        try {
        for(Construct construct : complex) {
            if(construct.getType() != Type.COMPONENT) {

                    matcher = Pattern.compile(getAdHocPattern((Construct) complex, construct)).matcher(segment.getContent()).region(segment.getStart(),segment.getEnd());;
                matcher.useTransparentBounds(true);

                    if (matcher.find()) {
                        current = new Segment(matched.getContent(),
                                matcher.start("NamedGroup"), matcher.end("NamedGroup"));
                        if(construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL) {
                            process((Quantifier)construct,current);
                        } else if (construct.isComplex() && construct.getType()!=Type.CHAR_CLASS  && construct.getType() != Type.LOOK_AROUND) {
                            process((Complex) construct, current);
                        }
                        addMatch(construct, current);
                    } else {
                        addMatch(construct, null);
                    }

            } else {
                continue;
            }

        }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Match every construct, within given quantifier, on test String, until whole test String is matched. In result
     * every construct could match more than one fragment.
     * @param quantifier to decompose and match for elements.
     * @param segment String matched by whole quantifier.
     */
    public void process(Quantifier quantifier,Segment segment) {
        Construct construct = quantifier.getConstruct(0);
        Segment current;
        try {
            Matcher matcher = Pattern.compile(construct.getText()).matcher(segment.toString());
            while (matcher.find()) {
                current = new Segment(matched.getContent(),
                        segment.getStart() + matcher.start(), segment.getStart() + matcher.end());
                if (construct.isComplex() && construct.getType() != Type.CHAR_CLASS && construct.getType() != Type.LOOK_AROUND) {
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
