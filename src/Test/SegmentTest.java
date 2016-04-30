package Test;

import Model.Segment;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SegmentTest {
    Segment segment;
    String text;

    @org.junit.Before
    public void setUp() throws Exception {
        text = "test string";
        segment = new Segment(text,0,4);
    }

    @Test
    public void testGetStart() throws Exception {
        assertEquals(0,segment.getStart());
    }

    @Test
    public void testGetEnd() throws Exception {
        assertEquals(4,segment.getEnd());
    }

    @Test
    public void testGetContent() throws Exception {
        assertEquals(text,segment.getContent());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("test",segment.toString());
    }
}