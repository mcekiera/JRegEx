package Model.Constructs.Types.Group;

import Model.Constructs.Complex;
import Model.Constructs.Construct;
import Model.Constructs.Types.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct implements Complex,Iterable<Construct>{
    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern, int start, int end) {
        super(pattern, start, end);

        crateConstructs();
    }


    public void crateConstructs() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(asString);
        matcher.find();
        int i = matcher.end();
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
}