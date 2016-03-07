package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.Type;

public class test {

    public static void main(String[] args) {
        String str = "(\\d([a-z]))";
        MatcherLib lib = MatcherLib.getInstance();
        ConstructsFactory cf = ConstructsFactory.getInstance();

        System.out.println(cf.extractGroup(str,3));

        for(Type type : Type.values()) {
            if(lib.getMatcher(type).reset(str).lookingAt()) {
                //System.out.println(lib.getMatcher(type).group());

            }
        }

    }
}


//todo inny podzia³: pointmaching, charactermaching, conceptmatching, simple and composed
