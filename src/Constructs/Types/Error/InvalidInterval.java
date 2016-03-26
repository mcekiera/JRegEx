package Constructs.Types.Error;

public class InvalidInterval extends Error {
    public InvalidInterval(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
