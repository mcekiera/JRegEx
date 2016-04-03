package Model.Constructs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Composition extends Construct implements Iterable<Construct>{
    private int intStart;
    private int intEnd;

    protected final List<Construct> elements = new ArrayList<Construct>();

    public Composition(Type type, String patter, int start, int end) {
        super(type,patter,start,end);
        intStart = start;
        intEnd = end;
        if(type != Type.EXPRESSION) crateComponents();
    }

    public void crateComponents() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?|\\[").matcher(asString);
        if(matcher.find()) {
            int i = matcher.end();
            intStart = getStart() + i;
            intEnd = getEnd() - 1;
            this.elements.add(new Construct(Type.COMPONENT, pattern, getStart(), getStart() + i));
            this.elements.add(new Construct(Type.COMPONENT, pattern, getEnd() - 1, getEnd()));
        }
    }

    public void addConstruct(Construct construct) {
        System.out.println("in: " + construct.toString());
        elements.add(elements.size()-(getType() == Type.EXPRESSION ? 0 : 1),construct);
    }

    public int getInteriorStart() {
        return intStart;
    }

    public int getInteriorEnd() {
        return intEnd;
    }

    public void absorbLast(Quantifier construct) {
        construct.setConstruct(elements.get(elements.size()-(getType() == Type.EXPRESSION ? 1 : 2)));
        elements.remove(elements.size()-(getType() == Type.EXPRESSION ? 1 : 2));
    }

    @Override
    public String toString() {
        String result = "";
        for(Construct construct : this){
            result += "\n" + construct.toString();
        }
        return result;
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }
}
