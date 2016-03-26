package Model.Constructs.Types.Error;

public class IncompleteConstruct extends Error {
    public IncompleteConstruct(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
