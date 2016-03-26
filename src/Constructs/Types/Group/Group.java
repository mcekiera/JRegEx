package Constructs.Types.Group;

import Constructs.Construct;
import Constructs.ConstructsFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group extends Construct implements Iterable<Construct>{
    protected final List<Construct> elements = new ArrayList<Construct>();

    public Group(String pattern, int start, int end) {
        super(pattern, start, end);
        read();
    }


    public void read() {
        Matcher matcher = Pattern.compile("\\((\\?(\\<\\w+\\>|[idmsuxU-]+:|[<>!=:]?([=!]+)?|\\<))?").matcher(asString);
        matcher.find();
        int i = matcher.end();
        while(i < asString.length()-1) {
            Construct construct = ConstructsFactory.getInstance().createConstruct(asString.substring(0,asString.length()-1), i);
            i += construct.size();
            this.elements.add(construct);
        }
    }

    @Override
    public String toString() {
        String result = getClass().getName()  + ": " + asString;
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
