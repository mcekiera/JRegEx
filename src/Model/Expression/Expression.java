package Model.Expression;

import Model.Constructs.Composition;
import Model.Constructs.Construct;
import Model.Constructs.Quantifier;
import Model.Constructs.Type;
import Model.Matching.Matched;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Expression {
    private String pattern;
    private Composition composition;
    private Map<String,Composition> groups;
    private List<String> groupsNames;
    private List<Matched> currentMatching;

    public Expression(Composition composition){
        this.composition = composition;

    }

    public void reset() {
        currentMatching = new ArrayList<>();
    }


    public void getSeparateConstructsMatches(String matched, Composition composition) {
           for(Construct construct : composition) {
               if(construct instanceof Composition && construct.getType() != Type.CHAR_CLASS) {
                   getSeparateConstructsMatches(matched, (Composition) construct);
               } else if (construct.getType() == Type.QUANTIFIER) {
                   currentMatching.add(((Quantifier)construct).getConstruct().directMatch(matched));
                   currentMatching.add(construct.directMatch(matched));
               } else if(construct.getType() != Type.COMPONENT) {
                       currentMatching.add(construct.directMatch(matched));
               }
           }
    }

    public List<Matched> getCurrentMatching() {
        return currentMatching;
    }

}
