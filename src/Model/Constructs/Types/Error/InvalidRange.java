package Model.Constructs.Types.Error;

public class InvalidRange extends Error {
    public InvalidRange(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
