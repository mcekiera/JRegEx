package Model.Constructs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Composition extends Construct implements Iterable<Construct>{
    private int intStart;
    private int intEnd;

    private final List<Construct> elements = new ArrayList<Construct>();

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
        System.out.println(elements.size());
        if(elements.size() > 0 && (construct.getType() == Type.QUANTIFIER || construct.getType() == Type.INTERVAL)){
            System.out.println(elements.get(getInsertionIndex()).asString);
            System.out.println(((Quantifier)construct).getConstruct());
            if(elements.get(getInsertionIndex()).equals(((Quantifier)construct).getConstruct())) {
                elements.set(getInsertionIndex(),construct);
                System.out.println(">> 1");
            } else {
                elements.add(getInsertionIndex(),construct);
                System.out.println(">> 2");
            }
        } else {
            elements.add(getInsertionIndex(), construct);
            System.out.println(">> 3");
        }
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
