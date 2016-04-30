/**package Model.GlobalMatching;

import Model.Constructs.Construct;
import Model.Constructs.Quantifier;
import Model.Constructs.Sequence;
import Re.Type;

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
        directMatch(construct);
        System.out.println(construct.getType() + ";" + fragment + ";" + construct.getCurrentMatch());
        if (isCharacterClass(construct)) {
            new InClassMatching().setSequence((Sequence) construct, construct.getCurrentMatch().toString());
        } else if(isGroup(construct)) {
            process((Sequence) construct, construct.getCurrentMatch().toString());
        } else if(isQuantifier(construct)) {
            System.out.println("isQuantifier");
            matchInQuantifier(construct,((Quantifier) construct).getConstruct());
        } else {
            directMatch(construct);
        }
    }

    public void matchInQuantifier(Construct quantifier, Construct construct) {
        directMatchInQuantifier(quantifier,construct);
        if(isGroup(construct)) {
            process((Sequence)construct,construct.getCurrentMatch().toString());
        } else if (isQuantifier(construct)) {
            matchInQuantifier(((Quantifier) construct).getConstruct(), construct);
        } else if(isCharacterClass(construct)) {
            new InClassMatching().setSequence((Sequence) construct, construct.getCurrentMatch().toString());
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

    public String getAsSeparateGroup(Construct construct,String groupName) {
        String begin = construct.getPattern().substring(0, construct.getStart());
        String mid = construct.getPattern().substring(construct.getStart(), construct.getEnd());
        String finish = construct.getPattern().substring(construct.getEnd());
        String re = begin + "(?<" + groupName + ">" + mid + ")" + finish;
        return re;
    }

    public void directMatch(Construct construct) {
        try {
            Matcher matcher = Pattern.compile(getAsSeparateGroup(construct,"test")).matcher(text);
            System.out.println(matcher.pattern() + " ... " + text + " ... " + construct.toString());

            if (matcher.find()) {
                construct.setCurrentMatch(new Matched(matcher.start("test"), matcher.end("test"),matcher.group("test")));
            }

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void directMatchInQuantifier(Construct quantifier, Construct construct) {
        System.out.println(construct.toString() + " <<< " + quantifier.getCurrentMatch().toString());
        try{
            Matcher matcher = Pattern.compile(construct.toString()).matcher(text);
            matcher.region(quantifier.getCurrentMatchStart(),quantifier.getCurrentMatchEnd());

            if(matcher.lookingAt()) {
                System.out.println(matcher.pattern() + " >>> " + quantifier.getCurrentMatch().toString() + " >>> " + matcher.group());
                construct.setCurrentMatch(new Matched(matcher.start(),matcher.end(),matcher.group()));
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }
}
     */