package Test;

import Model.Regex.Composite;
import Model.Regex.Construct;
import Model.Regex.Single;
import Model.Regex.Type.Type;
import Model.Segment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConstructTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGetStart() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertEquals(0, construct.getStart());

    }

    @Test
    public void testGetEnd() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertEquals(1, construct.getEnd());
    }

    @Test
    public void testGetType() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertEquals(Type.SIMPLE, construct.getType());
    }

    @Test
    public void testGetParent() throws Exception {
        Construct parent = new Composite(null,Type.EXPRESSION,new Segment("a",0,1));
        Construct construct = new Single(parent, Type.SIMPLE,new Segment("a",0,1));
        assertEquals(parent, construct.getParent());
    }

    @Test
    public void testLength() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertEquals(1, construct.length());
    }

    @Test
    public void testGetText() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertEquals("a", construct.getText());
    }

    @Test
    public void testGetPattern() throws Exception {
        Construct construct = new Single(null, Type.SIMPLE,new Segment("abc",1,2));
        assertEquals("abc", construct.getPattern());
    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {
        Construct construct1 = new Single(null, Type.SIMPLE,new Segment("abc",1,2));
        Construct construct2 = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        Construct construct3 = new Single(null, Type.SIMPLE,new Segment("a",0,1));
        assertTrue(construct1.equals(construct1));
        assertFalse(construct1.equals(null));
        assertFalse(construct1.equals(construct2));
        assertTrue(construct2.equals(construct3));
    }

    @Test
    public void testGetDescription() throws Exception {

    }

    @Test
    public void testSetDescription() throws Exception {

    }

    @Test
    public void testIsComplex() throws Exception {
        Construct construct1 = new Single(null, Type.SIMPLE,new Segment("abc",1,2));
        assertFalse(construct1.isComplex());
    }

    @Test
    public void hashCodeTest() {
        String regex = "a\\d\\d(abc)";
        Construct root = new Composite(null,Type.EXPRESSION,new Segment(regex,0,regex.length()));
        Construct construct1 = new Single(root, Type.SIMPLE,new Segment(regex,0,1));
        Construct construct2 = new Single(root, Type.SIMPLE,new Segment(regex,0,1));
        assertEquals(construct1.hashCode(), construct2.hashCode());

        Construct construct3 = new Single(root, Type.PREDEFINED,new Segment(regex,1,3));
        Construct construct4 = new Single(root, Type.PREDEFINED,new Segment(regex,1,3));
        assertEquals(construct3.hashCode(), construct4.hashCode());
        assertFalse(construct1.hashCode() == construct3.hashCode());
        Construct construct5 = new Composite(root, Type.CAPTURING,new Segment(regex,5,10));
        Construct construct6 = new Composite(root, Type.CAPTURING,new Segment(regex,5,10));
        assertEquals(construct6.hashCode(), construct5.hashCode());
        assertFalse(construct1.hashCode() == construct5.hashCode());
    }
}