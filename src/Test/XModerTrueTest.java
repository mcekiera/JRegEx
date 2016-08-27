package Test;

import Model.Regex.XModer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class XModerTrueTest {
    private String regex;
    private int index;
    private XModer moder;

    @Before
    public void setUp() {
        moder = XModer.getInstance();
    }

    public XModerTrueTest(String regex, int index) {
        this.regex = regex;
        this.index = index;
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]{
                {"(?x)a#aa", 5},
                {"(?x)a#aa", 6},
                {"(?x)a#aa", 7},
                {"(?x)a  #aa", 5},
                {"(?x)a  #aa", 6},
                {"(?x)a  #aa", 7},
                {"(?x)a  #aa", 8},
                {"(?x)a  #aa", 9},
                {"(?x)a  (?-x:#aa)", 5},
                {"(?x)a  (?-x:#aa)", 6},
                {"(?x)\\Q2#34\\E#678", 12},
                {"(?x)\\Q2#34\\E#678", 13},
                {"(?x)\\Q2#34\\E#678", 14},
                {"(?x)\\Q2#34\\E#678", 15},
                {"(?x)a#a\nb#b\nc#c", 5},
                {"(?x)a#a\nb#b\nc#c", 6},
                {"(?x)a#a\nb#b\nc#c", 9},
                {"(?x)a#a\nb#b\nc#c", 10},
                {"(?x)a#a\nb#b\nc#c", 13},
                {"(?x)a#a\nb#b\nc#c", 14},

        });
    }

    @Test
    public void trueXModerTest() {
        moder.process(regex);
        assertTrue(moder.isInCommentRange(index));
    }
}
