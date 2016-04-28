package Re.Regex;

import Model.Constructs.Type;
import Re.Segment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Quantifier extends Construct implements Complex, Iterable<Construct> {
    private final List<Construct> elements;

    public Quantifier(Construct parent, Type type, Segment segment) {
        super(parent,type,segment);
        elements = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int getStart() {
        return elements.get(0).getStart();
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public void addConstruct(Construct construct) {
        elements.set(0,construct);
    }

    @Override
    public Construct getConstructFromPosition(int index) {
        if(elements.get(0).getStart() == index) {
            return elements.get(0);
        } else {
            return this;
        }
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }
}
