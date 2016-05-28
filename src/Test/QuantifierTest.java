package Test;

import Model.Regex.Construct;
import Model.Regex.Quantifier;
import Model.Regex.Single;
import Model.Regex.Type;
import Model.Segment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuantifierTest {
    private Quantifier quantifier;
    private Construct construct;

    @Before
    public void setUp() throws Exception {
        String pattern = "a+";
        quantifier = new Quantifier(null, Type.QUANTIFIER,new Segment(pattern,0,2));
        construct = new Single(quantifier,Type.SIMPLE,new Segment(pattern,0,1));
        quantifier.addConstruct(construct);
    }

    @Test
    public void testIsComplex() throws Exception {
        assertTrue(quantifier.isComplex());
    }

    @Test
    public void testAddConstruct() throws Exception {
        assertTrue(quantifier.size()==1);
    }

    @Test
    public void testGetConstructFromPosition() throws Exception {
        assertEquals(quantifier.getConstructFromPosition(0),construct);
    }

    @Test
    public void testGetConstruct() throws Exception {
        assertEquals(quantifier.getConstruct(0),construct);
    }

    @Test
    public void testGetConstructIndex() throws Exception {
        assertEquals(quantifier.getConstructIndex(construct),0);
    }

    @Test
    public void testSize() throws Exception {
        quantifier.addConstruct(construct);
        assertTrue(quantifier.size() > 0);
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals(quantifier.getText(),"aa+");
    }

    @Test
    public void testGetStart() throws Exception {
        assertEquals(quantifier.getStart(),0);
    }
}