package Test;

import Model.Lib.RegexLib;
import org.junit.Before;
import org.junit.Test;

import static Model.Regex.Type.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class RegexLibTest {
    RegexLib lib;

    @Before
    public void setUp() throws Exception {
        lib = RegexLib.getInstance();
    }

    @Test
    public void testGetRegEx() throws Exception {
        assertEquals(".",lib.getRegEx(SIMPLE));
    }

    @Test
    public void testGetInstance() throws Exception {
        assertEquals(lib, RegexLib.getInstance());
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(lib.contains(BOUNDARY));
        assertTrue(lib.contains(CHAR_CLASS));
        assertTrue(lib.contains(MODE));
        assertTrue(lib.contains(COMPONENT));
        assertTrue(lib.contains(PREDEFINED));
        assertTrue(lib.contains(QUANTIFIER));
        assertFalse(lib.contains(INVALID_BACKREFERENCE));
        assertFalse(lib.contains(INVALID_INTERVAL));
    }


}