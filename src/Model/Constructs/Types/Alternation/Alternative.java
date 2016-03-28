package Model.Constructs.Types.Alternation;

import Model.Constructs.Complex;
import Model.Constructs.Construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Alternative extends Construct implements Complex, Iterable<Construct> {
    private final List<Construct> elements = new ArrayList<Construct>();
    public Alternative(String pattern, int start, int end) {
        super(pattern, start, end);
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }

    @Override
    public void addConstruct(Construct construct) {
        elements.add(construct);
    }
}
