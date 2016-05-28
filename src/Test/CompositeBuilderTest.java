package Test;

import Model.Regex.*;
import Model.Segment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompositeBuilderTest {
    private String pattern;
    private Composite composite;
    private CompositeBuilder builder = CompositeBuilder.getInstance();

    @Before
    public void setUp() throws Exception {
        pattern = "a+bc";
        Segment segment = new Segment(pattern,0,pattern.length());
        composite = new Composite(null, Type.EXPRESSION,segment);
    }

    @Test
    public void testToComposite() throws Exception {
        composite = builder.toComposite(pattern);
        Construct construct = new Single(composite,Type.SIMPLE,new Segment(pattern,2,4));
        System.out.println(composite.size());
        assertTrue(composite.size() == 2);
        assertEquals(composite.getConstruct(1),construct);
    }

    @Test
    public void testGetGroups() throws Exception {
        String p = "(a)(b)(c)";
        composite = builder.toComposite(p);

        assertEquals(builder.getGroups().get(1).getText(), "(a)");
        assertEquals(builder.getGroups().get(2).getText(),"(b)");
        assertEquals(builder.getGroups().get(3).getText(),"(c)");
    }

    @Test
    public void testGetNames() throws Exception {
        String p = "(?<a>a)(?<b>b)(?<c>c)";
        composite = builder.toComposite(p);

        assertEquals(builder.getNames().get(1),"a");
        assertEquals(builder.getNames().get(2),"b");
        assertEquals(builder.getNames().get(3),"c");
    }

    @Test
    public void testIsValid() throws Exception {
        String valid = "abc";
        String invalid = "(abc";
        builder.toComposite(valid);
        assertTrue(builder.isValid());
        builder.toComposite(invalid);
        assertFalse(builder.isValid());
    }
}