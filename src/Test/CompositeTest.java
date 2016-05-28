package Test;

import Model.Regex.*;
import Model.Segment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompositeTest {
    private Composite composite;
    private String pattern;

    @Before
    public void setUp() throws Exception {
        pattern = "a+bc";
        Segment segment = new Segment(pattern,0,pattern.length());
        composite = new Composite(null, Type.EXPRESSION,segment);
    }

    @Test
    public void testIsComplex() throws Exception {
         assertTrue(composite.isComplex());
    }

    @Test
    public void testAddConstruct() throws Exception {
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct);
        assertTrue(composite.size()==1);

    }

    @Test
    public void testAddConstruct1() throws Exception {
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        Quantifier quantifier = new Quantifier(composite,Type.QUANTIFIER,new Segment(pattern,0,2));
        Construct construct2 = new Single(quantifier,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct);
        composite.addConstruct(quantifier,construct,construct2);
        assertTrue(composite.size()==1);

    }

    @Test
    public void testGetConstructFromPosition() throws Exception {
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct);
        assertEquals(composite.getConstructFromPosition(0),construct);
    }

    @Test
    public void testGetConstruct() throws Exception {
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct);
        assertEquals(composite.getConstruct(0),construct);
    }

    @Test
    public void testGetConstructIndex() throws Exception {
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct);
        assertEquals(composite.getConstructIndex(construct),0);
    }

    @Test
    public void testSize() throws Exception {
        Construct construct1 = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct1);
        assertTrue(composite.size()==1);
        Construct construct2 = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct2);
        assertTrue(composite.size() == 2);
        Construct construct3 = new Single(composite,Type.SIMPLE,new Segment(pattern,0,1));
        composite.addConstruct(construct3);
        assertTrue(composite.size() == 3);
    }
}