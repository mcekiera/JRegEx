package Model.Constructs.Types.Alternation;

import Model.Constructs.Complex;
import Model.Constructs.Construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Alternation extends Construct implements Complex, Iterable<Alternative>{
    private final List<Alternative> elements = new ArrayList<Alternative>();


    public Alternation(String pattern, int start, int end) {
        super(pattern, start, end);
    }

    @Override
    public void addConstruct(Construct construct) {
         if (construct instanceof Alternative) {
             elements.add((Alternative)construct);
         } else {
             Alternative a = new Alternative(construct.getPattern(),construct.getStart(),construct.getEnd());
             a.addConstruct(construct);
             elements.add(a);
         }
    }

    @Override
    public int getInteriorStart() {
        return getStart();
    }

    @Override
    public int getInteriorEnd() {
        return getEnd();
    }

    @Override
    public Iterator<Alternative> iterator() {
        return elements.listIterator();
    }
}
