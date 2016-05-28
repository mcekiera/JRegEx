package Test;

import Model.Lib.DescLib;
import Model.Regex.Construct;
import Model.Regex.Single;
import Model.Regex.Type;
import Model.Segment;
import org.junit.Test;

import static org.junit.Assert.*;

public class DescLibTest {
    private DescLib lib = DescLib.getInstance();

    @Test
    public void testGetDescription() throws Exception {
        Construct construct1 = new Single(null, Type.UNBALANCED,new Segment("(a",0,1));
        assertEquals(lib.getDescription(construct1),"Unbalanced structure on index 0:  (");
        Construct construct2 = new Single(null, Type.INCOMPLETE,new Segment("\\p",0,2));
        assertEquals(lib.getDescription(construct2),"Incomplete structure on index 0:  \\p");
        Construct construct3 = new Single(null, Type.INVALID_BACKREFERENCE,new Segment("\\1",0,2));
        assertEquals(lib.getDescription(construct3),"Invalid backreference on index 0:  \\1");
        Construct construct4 = new Single(null, Type.INVALID_RANGE,new Segment("[2-3]",1,4));
        assertEquals(lib.getDescription(construct4),"Invalid range on index 1:  2-3");
        Construct construct5 = new Single(null, Type.INVALID_INTERVAL,new Segment("{9,5}",0,5));
        assertEquals(lib.getDescription(construct5),"Invalid interval on index 0:  {9,5}");
        Construct construct6 = new Single(null, Type.INVALID_QUANTIFIER,new Segment("++",0,2));
        assertEquals(lib.getDescription(construct6),"Invalid quantifier on index 0:  ++");

    }  //TODO rest

    @Test
    public void testContains() throws Exception {
        assertTrue(lib.contains("\\d"));
        assertTrue(lib.contains("++"));
        assertFalse(lib.contains("@"));
    }
}