package Model.Matching;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Constructs.Type;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InClassMatching {
    private Map<Construct,List<Matched>> interiorMatch;
    private Sequence sequence;
    private String matched;

    public boolean setSequence(Sequence sequence, String matched) {
        if(sequence.getType() == Type.CHAR_CLASS) {
            interiorMatch = new LinkedHashMap<>();
            this.sequence = sequence;
            this.matched = matched;
            fillMap();
            match();
            return true;
        } else {
            return false;
        }
    }

    private void fillMap() {
        for(Construct construct : sequence) {

            if(construct.getType() != Type.COMPONENT) {
                interiorMatch.put(construct, new ArrayList<Matched>());
            }
        }
        for(Construct c : interiorMatch.keySet()) {
            System.out.println(c.toString());
        }
    }

    private void match() {
        Matcher matcher;
        for(Construct construct : interiorMatch.keySet()) {
            matcher = Pattern.compile("[" + construct.toString() + "]").matcher(matched);
            //matcher.region(sequence.getCurrentMatchStart(), sequence.getCurrentMatchEnd());
            while (matcher.find()) {
                interiorMatch.get(construct).add(new Matched(matcher.start(), matcher.end(),matcher.group()));
                System.out.println(matched + " - " + matcher.start() + "," + matcher.end());
            }

        }
    }

    public List<Matched> getMatched(Construct construct) {
        return interiorMatch.get(construct);
    }
}
