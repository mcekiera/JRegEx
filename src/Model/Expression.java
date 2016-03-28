package Model;

import Model.Constructs.Complex;
import Model.Constructs.Construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Expression implements Complex,Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();
    private String pattern;
    private String currentMatch;

    public Expression(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void addConstruct(Construct construct) {
        elements.add(construct);
    }

    public Construct getConstruct(int index) {
        return elements.get(index);
    }

    public void replaceConstruct(int index, Construct construct) {
        elements.set(index,construct);
    }

    public void setCurrentMatch(String match) {
        currentMatch = match;
    }



    public String getCurrentMatch() {
        return currentMatch;
    }

    public void matchElements() {
        for(Construct construct : elements) {
            System.out.println(construct.directMatch(currentMatch));
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
