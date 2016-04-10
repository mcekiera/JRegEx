package Model.Constructs;

public class Error extends Construct{

    public Error(Type type, String pattern, int start, int end) {
        super(type, pattern, start, end);
        System.out.println(type);
    }
}
