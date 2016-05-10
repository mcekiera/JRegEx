package Model.Match;

import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Quantifier;
import Model.Regex.Type.Type;
import Model.Segment;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detail {
    private Composite composite;
    private Segment matched;
    private Map<Construct,List<Segment>> detailMatch;

    public Detail(Composite composite, Segment matched) {
        detailMatch = new LinkedHashMap<>();
        this.composite = composite;
        this.matched = matched;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        process(composite, matched);
        print();
    }

    public void process(Complex complex, Segment segment) {
        Matcher matcher;
        Segment current;
        for(Construct construct : complex) {
            System.out.println(construct.getType() + "," +construct.getStart() +"," + construct.getEnd() + ": " + construct.getText());
            if(construct.getType() != Type.COMPONENT) {
                try {
                    matcher = Pattern.compile(getAdHocPattern((Construct) complex, construct)).matcher(segment.toString());
                    if (matcher.find()) {
                        System.out.println(">>" + matcher.pattern());
                        current = new Segment(matched.toString(),
                                segment.getStart() + matcher.start("NamedGroup"), segment.getStart() + matcher.end("NamedGroup"));
                        if(construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL) {
                            process((Quantifier)construct,current);
                        } else if (construct.isComplex() && construct.getType()!=Type.CHAR_CLASS) {
                            process((Complex) construct, current);
                        }
                        addMatch(construct, current);
                    } else {
                        continue;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                continue;
            }
        }
    }

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



    private String getAsGroup(Construct construct) {
        return "(?<NamedGroup>" + construct.getText() + ")";
    }

    private String getAdHocPattern(Construct complex, Construct construct) {
        return complex.getText().substring(0,construct.getStart()-complex.getStart())
                + getAsGroup(construct)
                + complex.getText().substring(construct.getEnd()-complex.getStart());
    }

    private void addMatch(Construct construct, Segment segment) {
        if(detailMatch.get(construct)!=null) {
            detailMatch.get(construct).add(segment);
        } else {
            detailMatch.put(construct,new ArrayList<Segment>());
            detailMatch.get(construct).add(segment);
        }
    }

    public void print() {
        for(Construct construct : detailMatch.keySet()) {
            for(Segment segment : detailMatch.get(construct)){
                System.out.println(construct.getType() + "," +construct.getStart() +"," + construct.getEnd() + ": " +  segment.getDescription());
            }
        }
    }


}
