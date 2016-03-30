package Model;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.Types.Alternation.Alternation;
import Model.Constructs.Types.Quantifiable.Quantifiable;
import Model.Constructs.Types.Reversible;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Expression implements Complex,Iterable<Construct>, Reversible{
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

    @Override
    public int getInteriorStart() {
        return 0;
    }

    @Override
    public int getInteriorEnd() {
        return pattern.length();
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

    @Override
    public void absorbLast(Quantifiable construct) {
        construct.setConstruct(elements.get(elements.size()-1));
        elements.set(elements.size()-1,(Construct)construct);
    }

    @Override
    public void absorbAll(Alternation construct) {

    }
}
