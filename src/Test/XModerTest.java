package Test;

import Model.Regex.XModer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XModerTest {
    XModer moder;

    @Before
    public void setUp() {
        moder = XModer.getInstance();
    }


    @Test
         public void testXModer1() throws Exception {
        moder.process("(?x)a#aa");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertTrue(moder.isInCommentRange(5));
        assertTrue(moder.isInCommentRange(6));
        assertTrue(moder.isInCommentRange(7));
    }

    @Test
    public void testXModer2() throws Exception {
        moder.process("(?x)a  #aa");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertTrue(moder.isInCommentRange(5));
        assertTrue(moder.isInCommentRange(6));
        assertTrue(moder.isInCommentRange(7));
        assertTrue(moder.isInCommentRange(8));
        assertTrue(moder.isInCommentRange(9));
    }

    @Test
    public void testXModer3() throws Exception {
        moder.process("(?x)a  (?-x:#aa)");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertTrue(moder.isInCommentRange(5));
        assertTrue(moder.isInCommentRange(6));
        assertFalse(moder.isInCommentRange(7));
        assertFalse(moder.isInCommentRange(8));
        assertFalse(moder.isInCommentRange(9));
        assertFalse(moder.isInCommentRange(10));
        assertFalse(moder.isInCommentRange(11));
        assertFalse(moder.isInCommentRange(12));
        assertFalse(moder.isInCommentRange(13));
        assertFalse(moder.isInCommentRange(14));
        assertFalse(moder.isInCommentRange(15));
    }

    @Test
    public void testXModer4() throws Exception {
        moder.process("(?x)\\Q2#34\\E#678");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertFalse(moder.isInCommentRange(5));
        assertFalse(moder.isInCommentRange(6));
        assertFalse(moder.isInCommentRange(7));
        assertFalse(moder.isInCommentRange(8));
        assertFalse(moder.isInCommentRange(9));
        assertFalse(moder.isInCommentRange(10));
        assertFalse(moder.isInCommentRange(11));
        assertTrue(moder.isInCommentRange(14));
        assertTrue(moder.isInCommentRange(15));
        assertTrue(moder.isInCommentRange(12));
        assertTrue(moder.isInCommentRange(13));
    }

    @Test
    public void testXModer5() throws Exception {
        moder.process("(?x)a#a\nb#b\nc#c");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertTrue(moder.isInCommentRange(5));
        assertTrue(moder.isInCommentRange(6));
        assertFalse(moder.isInCommentRange(7));
        assertFalse(moder.isInCommentRange(8));
        assertTrue(moder.isInCommentRange(9));
        assertTrue(moder.isInCommentRange(10));
        assertFalse(moder.isInCommentRange(11));
        assertFalse(moder.isInCommentRange(12));
        assertTrue(moder.isInCommentRange(13));
        assertTrue(moder.isInCommentRange(14));
    }

    @Test
    public void testXModer6() throws Exception {
        moder.process("(?x)a#a\nb#b\nc#c");

        assertFalse(moder.isInCommentRange(0));
        assertFalse(moder.isInCommentRange(1));
        assertFalse(moder.isInCommentRange(2));
        assertFalse(moder.isInCommentRange(3));
        assertFalse(moder.isInCommentRange(4));
        assertTrue(moder.isInCommentRange(5));
        assertTrue(moder.isInCommentRange(6));
        assertFalse(moder.isInCommentRange(7));
        assertFalse(moder.isInCommentRange(8));
        assertTrue(moder.isInCommentRange(9));
        assertTrue(moder.isInCommentRange(10));
        assertFalse(moder.isInCommentRange(11));
        assertFalse(moder.isInCommentRange(12));
        assertTrue(moder.isInCommentRange(13));
        assertTrue(moder.isInCommentRange(14));
    }
}