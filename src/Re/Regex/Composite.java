package Re.Regex;

import Model.Constructs.Type;
import Re.Segment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Composite extends Construct implements Complex, Iterable<Construct> {
    private final List<Construct> elements;
    private int insertPosition;

    public Composite(Construct parent, Type type, Segment segment) {
        super(parent,type,segment);
        elements = new ArrayList<>();
        insertPosition = 0;
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    @Override
    public String getDescription() {
        return null;
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
        elements.set(index,construct);
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
}
