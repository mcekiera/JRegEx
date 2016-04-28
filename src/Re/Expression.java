package Re;

import Re.Regex.Composite;
import Re.Regex.Construct;
import Re.Regex.ConstructsAbstractFactory;
import Re.Regex.Type.CompositeBuilder;

public class Expression {

    public static void main(String[] args) {
        ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();
        String pattern = "(\\d(\\s|a))[a1-9]";
        CompositeBuilder builder = CompositeBuilder.getInstance();
        Composite composite = builder.toComposite(pattern);

        for(Construct c : composite) {
            System.out.println(c.getType() + "," + c.toString());
            if(c.isComplex()) {
                for(Construct b : (Iterable<Construct>)c) {
                    System.out.println("    " + b.getType() + "," + b.toString());
                    if(b.isComplex()) {
                        for(Construct e : (Iterable<Construct>)b) {
                            System.out.println("        " + e.getType() + "," + e.toString());
                        }
                    }
                }
            }
        }
    }
}
