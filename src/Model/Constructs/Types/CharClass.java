package Model.Constructs.Types;

import Model.Constructs.Complex;
import Model.Constructs.Construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CharClass extends Construct implements Complex,Iterable<Construct>{
    private final List<Construct> elements = new ArrayList<Construct>();

    public CharClass(String pattern, int start, int end) {
        super(pattern, start, end);
        createConstructs();
    }


    private void createConstructs() {
        elements.add(new Component(asString,this,0,1));
        elements.add(new Component(asString,this,asString.length()-1,asString.length()));
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
        elements.add(elements.size()-1, construct);
    }

    @Override
    public int getInteriorStart() {
        return getStart() + 1;
    }

    @Override
    public int getInteriorEnd() {
        return getEnd()-1;
    }
}
