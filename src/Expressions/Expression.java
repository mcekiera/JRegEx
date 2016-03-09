package Expressions;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression implements Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();
    private String pattern;
    private String currentMatch;

    public Expression(String pattern) {
        this.pattern = pattern;
        createConstructs(pattern);
    }

    public String getPattern() {
        return pattern;
    }

    private void createConstructs(String pattern) {
        int i = 0;
        while(i < pattern.length()) {
            Construct construct = ConstructsFactory.getInstance().createConstruct(pattern, i);
            i += construct.size();
            elements.add(construct);
        }
        for(Construct construct : elements) {
        }
    }

    public void setCurrentMatch(String match) {
        currentMatch = match;
    }

    public void matchElements() {
        Matcher matcher;
        for(Construct construct : elements) {
            String result = "";
            for(Construct element : elements) {
                if(element == construct) {
                    result += element.getAsSeparateGroup("test");
                } else {
                    result += element.getPattern();
                }
            }
            matcher = Pattern.compile(result).matcher(currentMatch);
            matcher.find();
            System.out.println(matcher.group("test"));
            }
    }

    @Override
    public String toString() {
        String result = "";
        for(Construct construct : elements) {
            result += "\n" + construct.toString();
        }
        return result;
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }
}
