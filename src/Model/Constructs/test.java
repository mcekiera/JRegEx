package Model.Constructs;

import Model.Constructs.Types.Composition;

public class test {

    public static void main(String[] args) {
        String pattern = "((\\uFFFF{2,}\\d\\s)+(?<=aaa)(?!\\p{Latin}))(([a-c1-9])b{4,6})[]]\\1[a-z&&[ghl]]";
        Composition e = new Composition(Type.EXPRESSION,pattern,0,pattern.length());
        Composition expression = (new ExpressionBuilder()).divideIntoConstructs(e,e.getPattern(),0,e.getPattern().length());
        //expression.setCurrentMatch("marcin.cekiera@gmail.com");
        int i = 0;
        for(Construct construct : (Composition)expression) {
            System.out.println(construct.toString());
        }
    }

}