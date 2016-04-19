package Model.Matching;

import Model.Constructs.Construct;
import Model.Constructs.Quantifier;
import Model.Constructs.Sequence;
import Model.Constructs.Type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DirectMatch {
    private final Sequence sequence;
    private final String text;

    private final Map<Construct,Matched> matchMap;

    public DirectMatch(Sequence sequence, String text) {
        this.sequence = sequence;
        this.text = text;
        matchMap = new LinkedHashMap<>();
        process(sequence,text);
    }

    public void process(Sequence part, String fragment) {
        for(Construct construct : part) {
            match(construct,fragment);
        }
    }

    public void match(Construct construct,String fragment) {
            if (isInQuantifier(construct)) {
                directMatchInQuantifier(construct,construct.getParent().getCurrentMatch().toString());
            } else if (isCharacterClass(construct)) {
                directMatch(construct,fragment);
                new InClassMatching().setSequence((Sequence)construct,construct.getCurrentMatch().toString());
            } else if(isGroup(construct)) {
                directMatch(construct,fragment);
                process((Sequence) construct, construct.getCurrentMatch().toString());
            } else if(isQuantifier(construct)) {
                directMatch(construct,fragment);
                match(((Quantifier)construct).getConstruct(),construct.getCurrentMatch().toString());
            } else {
                directMatch(construct, fragment);
            }
    }

    public boolean isCharacterClass(Construct construct) {
        return construct.getType() == Type.CHAR_CLASS;
    }

    public boolean isQuantifier(Construct construct) {
        return construct.getType() == Type.INTERVAL || construct.getType() == Type.QUANTIFIER;
    }

    public boolean isInQuantifier(Construct construct) {
        return isQuantifier(construct.getParent());
    }

    public boolean isGroup(Construct construct) {
        return construct.getType() == Type.LOOK_AROUND ||
                construct.getType() == Type.ATOMIC ||
                construct.getType() == Type.CAPTURING ||
                construct.getType() == Type.NON_CAPTURING ||
                construct.getType() == Type.CHAR_CLASS;
    }

    public String getAsSeparateGroup(Construct construct,String groupName) {
        String begin = construct.getPattern().substring(0, construct.getStart());
        String mid = construct.getPattern().substring(construct.getStart(), construct.getEnd());
        String finish = construct.getPattern().substring(construct.getEnd());
        String re = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        System.out.println(re);
        return re;
    }

    public void directMatch(Construct construct, String match) {
        try {
            Matcher matcher = Pattern.compile(getAsSeparateGroup(construct,"test")).matcher(match);
            System.out.println(matcher.pattern() + " ... " + match);

            if (matcher.find()) {
                construct.setCurrentMatch(new Matched(matcher.start("test"), matcher.end("test"),matcher.group()));
            }

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void directMatchInQuantifier(Construct construct, String match) {
        try{
            Matcher matcher = Pattern.compile(construct.toString()).matcher(match);
            if(matcher.lookingAt()) {
                construct.setCurrentMatch(new Matched(matcher.start(),matcher.end(),matcher.group()));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }
}
