package Model.Constructs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sequence extends Construct implements Iterable<Construct>{
    private int intStart;
    private int intEnd;

    private final List<Construct> elements = new ArrayList<Construct>();

    public Sequence(Type type, String patter, int start, int end) {
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
            elements.add(getInsertionIndex(), construct);
    }

    public int getInteriorStart() {
        return intStart;
    }

    public int getInteriorEnd() {
        return intEnd;
    }

    public int getInsertionIndex() {
        try {
            System.out.println(getType());
            return elements.size() - (elements.size() == 0 ? 0 : (getType() == Type.EXPRESSION ? 0 : 1));
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public void removeLastAdded() {
        elements.remove(getInsertionIndex()-1);
    }

    @Override
    public String toString() {
        return asString;
    }

    @Override
    public Iterator<Construct> iterator() {
        return elements.listIterator();
    }
}