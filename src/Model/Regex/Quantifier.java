package Model.Regex;

import Model.Regex.Type.Type;
import Model.Segment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents single Construct object that wraps another Construct objects, multiplying its range.
 */

public class Quantifier extends Construct implements Complex{
    private final List<Construct> elements;
    private int start;

    public Quantifier(Construct parent, Type type, Segment segment) {
        super(parent,type,segment);
        elements = new ArrayList<>();
        start = segment.getStart();
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public void addConstruct(Construct construct) {
        if(elements.size() == 0) {
            elements.add(construct);
        } else {
            elements.set(0,construct);
        }
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
    public Construct getConstruct(int i) {
        return elements.get(i);
    }

    @Override
    public int getConstructIndex(Construct construct) {
        return elements.indexOf(construct);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    @Override
    public String getText() {
        return elements.get(0).getText() + super.getText();
    }

    @Override
    public int getStart() {
        return elements.isEmpty() ? start : elements.get(0).getStart();
    }
}
