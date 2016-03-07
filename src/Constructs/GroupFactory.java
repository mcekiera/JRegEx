package Constructs;

import Constructs.Lib.MatcherLib;
import Constructs.Types.Group.*;
import Constructs.Types.Type;

import java.util.regex.Pattern;

public class GroupFactory {
    private static final GroupFactory instance = new GroupFactory();
    private final MatcherLib lib = MatcherLib.getInstance();

    private GroupFactory() {}

    public static GroupFactory getInstance() {
        return instance;
    }

    public Construct create(String pattern, String match) {
        if(Pattern.matches(Type.LOOK_AROUND.getRegex(), pattern)) {
            System.out.print(Type.LOOK_AROUND);
            return new LookAround(pattern,match);
        } else if(Pattern.matches(Type.ATOMIC.getRegex(), pattern)) {
            System.out.print(Type.ATOMIC);
            return new Atomic(pattern,match);
        } else if(Pattern.matches(Type.NON_CAPTURING.getRegex(), pattern)) {
            System.out.print(Type.NON_CAPTURING);
            return new NonCapturing(pattern,match);
        } else {
            System.out.print(Type.CAPTURING);
            return new Capturing(pattern,match);
        }

    }
}
