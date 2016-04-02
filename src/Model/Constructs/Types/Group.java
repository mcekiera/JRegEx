package Model.Constructs.Types;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.Type;
import Model.Constructs.Types.Quantifiable.Quantifiable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct implements Complex,Iterable<Construct>,Reversible {
    private int intStart;
    private Complex parent;

    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern, int start, int end) {
        super(pattern, start, end);

        crateConstructs();
    }

    public Group(Type type, String patter, int start, int end) {
        super(type,patter,start,end);
        crateConstructs();
    }


    public void crateConstructs() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?|\\[").matcher(asString);
        matcher.find();
        int i = matcher.end();
        intStart = getStart() + i;
        this.elements.add(new Construct(Type.COMPONENT,pattern,getStart(),getStart()+i));
        this.elements.add(new Construct(Type.COMPONENT,pattern,getEnd()-1,getEnd()));
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

    @Override
    public void addConstruct(Construct construct) {
        System.out.println("in: " + construct.toString());
        elements.add(elements.size()-1,construct);
    }

    @Override
    public int getInteriorStart() {
        return intStart;
    }

    @Override
    public int getInteriorEnd() {
        return getEnd() - 1;
    }

    @Override
    public void absorbLast(Quantifiable construct) {
        construct.setConstruct(elements.get(elements.size()-2));
        elements.remove(elements.size()-2);
    }
}
