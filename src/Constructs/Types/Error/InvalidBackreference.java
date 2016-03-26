package Constructs.Types.Error;

public class InvalidBackreference extends Error {
    public InvalidBackreference(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
