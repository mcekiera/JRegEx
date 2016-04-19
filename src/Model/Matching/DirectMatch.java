package Model.Matching;

import Model.Constructs.Construct;
import Model.Constructs.Sequence;
import Model.Constructs.Type;

import java.util.LinkedHashMap;
import java.util.Map;

public class DirectMatch {
    private final Sequence sequence;
    private final String text;

    private final Map<Construct,Matched> matchMap;

    public DirectMatch(Sequence sequence, String text) {
        this.sequence = sequence;
        this.text = text;
        matchMap = new LinkedHashMap<>();
    }

    public void process() {

    }

    public void match(Sequence part, String fragment) {
        for(Construct construct : part) {
            if (isCharacterClass(construct)) {

            } else if(isGroup(construct)) {

            } else if(isQuantifier(construct)) {

            } else {

            }
        }
    }

    public boolean isCharacterClass(Construct construct) {
        return construct.getType() == Type.CHAR_CLASS;
    }

    public boolean isQuantifier(Construct construct) {
        return construct.getType() == Type.INTERVAL || construct.getType() == Type.QUANTIFIER;
    }

    public boolean isGroup(Construct construct) {
        return construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.ATOMIC ||
                construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING ||
                construct.getType() == Type.CHAR_CLASS;
    }

    public String getAsSeparateGroup(String groupName) {
        String begin = pattern.substring(0,getStart());
        String mid = pattern.substring(getStart(),getEnd());
        String finish = pattern.substring(getEnd());
        String re = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        System.out.println(re);
        return re;
    }
}
