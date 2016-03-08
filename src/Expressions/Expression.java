package Expressions;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Expression implements Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();
    private String pattern;

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
