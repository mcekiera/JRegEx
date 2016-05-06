package Model.Regex;

import Model.Regex.Type.Type;
import Model.Segment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents divisible Construct object, that acts as container and could contain multiple other Construct objects.
 */

public class Composite extends Construct implements Complex{
    /**
     * List containing Construct object representing internal parts of Composite construct.
     */
    private final List<Construct> elements;

    public Composite(Construct parent, Type type, Segment segment) {
        super(parent,type,segment);
        elements = new ArrayList<>();
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public void addConstruct(Construct construct) {
        elements.add(construct);
    }

    public void addConstruct(Quantifier construct, Construct previous) {
        int index = elements.indexOf(previous);
        construct.addConstruct(previous);
        elements.set(index, construct);
    }

    @Override
    public Construct getConstructFromPosition(int index) {
        for(Construct c : this) {
            if(c.getStart() <= index && c.getEnd() > index) {
                if(c.isComplex()) {
                    return ((Complex)c).getConstructFromPosition(index);
                } else {
                    return c;
                }
            }
        }
        return this;
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
}
