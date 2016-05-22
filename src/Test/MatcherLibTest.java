package Test;

import Model.Lib.MatcherLib;
import Model.Lib.RegexLib;
import Model.Regex.Type;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MatcherLibTest {
    private MatcherLib lib;

    @Before
    public void setUp() throws Exception {
        lib = MatcherLib.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertEquals(lib, MatcherLib.getInstance());
    }

    @Test
    public void testGetMatcher() throws Exception {
        assertEquals(RegexLib.getInstance().getRegEx(Type.SIMPLE),lib.getMatcher(Type.SIMPLE).pattern().toString());

    }

    @Test
    public void testGetEndOfLastMatch() throws Exception {
        lib.getMatcher(Type.SIMPLE).reset("a");
        lib.getMatcher(Type.SIMPLE).find();
        assertEquals(1,lib.getEndOfLastMatch(Type.SIMPLE));
    }
}