package Model.Constructs.Types.Group;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.Types.Alternation.Alternation;
import Model.Constructs.Types.Component;
import Model.Constructs.Types.Quantifiable.Quantifiable;
import Model.Constructs.Types.Reversible;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct implements Complex,Iterable<Construct>,Reversible {
    private int intStart;

    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern, int start, int end) {
        super(pattern, start, end);

        crateConstructs();
    }


    public void crateConstructs() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(asString);
        matcher.find();
        int i = matcher.end();
        intStart = getStart() + i;
        this.elements.add(new Component(asString,this,0,i));
        this.elements.add(new Component(asString,this,asString.length()-1,asString.length()));
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

    @Override
    public void absorbAll(Alternation construct) {

    }
}
