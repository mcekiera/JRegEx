package Model.Constructs.Types.Error;

public class UnbalancedStructure extends Error{
    public UnbalancedStructure(String pattern, int start, int end) {
        super(pattern, start, end);
    }
}
