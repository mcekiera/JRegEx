package Model.Constructs;

import Model.Constructs.Construct;
import Model.Constructs.Type;

public class Error extends Construct{
    public Error(String pattern, int start, int end) {
        super(pattern, start, end);
    }

    public Error(Type type, String pattern, int start, int end) {
        super(type, pattern, start, end);
    }
}
