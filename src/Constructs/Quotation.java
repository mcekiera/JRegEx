package Constructs;

public class Quotation implements Construct {

    public Quotation(String mark) {
        System.out.println(mark);
    }


    @Override
    public int size() {
        return 0;
    }
}
