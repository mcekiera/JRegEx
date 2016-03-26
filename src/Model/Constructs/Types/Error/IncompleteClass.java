package Model.Constructs.Types.Error;

public class IncompleteClass extends Error{
    public IncompleteClass(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
