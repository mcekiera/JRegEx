package Model.Constructs.Types;

import Model.Constructs.Construct;
import Model.Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CharClass extends Construct implements Iterable<Construct>{
    private final List<Construct> elements = new ArrayList<Construct>();

    public CharClass(String pattern, int start, int end) {
        super(pattern, start, end);
        createConstructs();
    }


    private void createConstructs() {
        int i = 1;
        elements.add(new Component(asString,this,0,1));
        while(i < asString.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstructInCharClass(asString, i);
            i += construct.size();
            this.elements.add(construct);
        }
        elements.add(new Component(asString,this,i,i+1));
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
