package Model.Match;

import Model.Regex.Complex;
import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Type.Type;
import Model.Segment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Detail {
    private Composite composite;
    private Segment matched;
    private Map<Construct,Segment> detailMatch;

    public Detail(Composite composite, Segment matched) {
        detailMatch = new HashMap<>();
        this.composite = composite;
        this.matched = matched;
        doit(composite, matched, 0);
    }

    public void process(Complex composite, Segment matched, int index, int corr) {
        int i = index;
        Matcher matcher;
        Segment current;
        for(Construct construct : composite) {
            current = new Segment("",0,0);
            if(construct.getType()!= Type.COMPONENT) {
                try {
                    matcher = Pattern.compile(construct.getExtendedPattern()).matcher(matched.getContent());
                    System.out.println(construct.getExtendedPattern() + "," + i + "," + corr + ", " + matched.getContent());
                    if (matcher.lookingAt()) {
                        current = new Segment(matched.getContent(), matcher.start("Named") + corr, matcher.end("Named") + corr);
                        detailMatch.put(construct, current);
                    }
                    if (construct.isComplex()) {
                        System.out.println(construct.getType() + " >>" + construct.getText() + " >> " + current.getStart() + " >>" + current.toString());
                        process((Complex) construct, current, i, current.getStart());
                    }
                    System.out.println("current.toString().length();" + current.toString().length());
                    i += current.toString().length();
                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                }
            }

        }
        print(composite);

    }

    public void doit(Complex complex, Segment matched, int startIndex) {
        Segment segment = null;
        for(Construct construct : complex) {
            if(construct.getType()!=Type.COMPONENT) {
                Matcher matcher = Pattern.compile(construct.getText()).matcher(matched.toString().substring(startIndex));
                if (matcher.lookingAt()) {
                    segment = new Segment(matched.toString(), matcher.start() + startIndex, matcher.end() + startIndex);
                    detailMatch.put(construct, segment);
                }
                System.out.println(construct.getDescription() + " >> " + segment.getDescription());
                if (construct.isComplex()) {
                    doit((Complex) construct, segment, startIndex);
                }
                startIndex += segment.toString().length();
            }


        }
    }

    //TODO nie quantifiery ³api¹ wszystko co mo¿liwe, musi byæ pattern z named group

    public void print(Complex composite) {
        System.out.println("PRINT" + composite.size());
        for(Construct construct : composite) {
            if(construct.getType()!=Type.COMPONENT) {
                System.out.println(construct.getText() + "!" + detailMatch.get(construct).getDescription());
                if (construct.isComplex()) {
                    print((Complex) construct);
                }
            }
        }
    }


}
