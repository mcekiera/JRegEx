package Test;

import Model.Regex.XModer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class XModerFalseTest {
    private String regex;
    private int index;
    private XModer moder;

    @Before
    public void setUp() {
        moder = XModer.getInstance();
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]{
                {"(?x)a#aa", 0},
                {"(?x)a#aa", 1},
                {"(?x)a#aa", 2},
                {"(?x)a#aa", 3},
                {"(?x)a#aa", 4},
                {"(?x)a  #aa", 0},
                {"(?x)a  #aa", 1},
                {"(?x)a  #aa", 2},
                {"(?x)a  #aa", 3},
                {"(?x)a  #aa", 4},
                {"(?x)a  (?-x:#aa)", 0},
                {"(?x)a  (?-x:#aa)", 1},
                {"(?x)a  (?-x:#aa)", 2},
                {"(?x)a  (?-x:#aa)", 3},
                {"(?x)a  (?-x:#aa)", 4},
                {"(?x)a  (?-x:#aa)", 7},
                {"(?x)a  (?-x:#aa)", 8},
                {"(?x)a  (?-x:#aa)", 9},
                {"(?x)a  (?-x:#aa)", 10},
                {"(?x)a  (?-x:#aa)", 11},
                {"(?x)a  (?-x:#aa)", 12},
                {"(?x)a  (?-x:#aa)", 13},
                {"(?x)a  (?-x:#aa)", 14},
                {"(?x)a  (?-x:#aa)", 15},
                {"(?x)\\Q2#34\\E#678", 0},
                {"(?x)\\Q2#34\\E#678", 1},
                {"(?x)\\Q2#34\\E#678", 2},
                {"(?x)\\Q2#34\\E#678", 3},
                {"(?x)\\Q2#34\\E#678", 4},
                {"(?x)\\Q2#34\\E#678", 5},
                {"(?x)\\Q2#34\\E#678", 6},
                {"(?x)\\Q2#34\\E#678", 7},
                {"(?x)\\Q2#34\\E#678", 8},
                {"(?x)\\Q2#34\\E#678", 9},
                {"(?x)\\Q2#34\\E#678", 10},
                {"(?x)\\Q2#34\\E#678", 11},
                {"(?x)a#a\nb#b\nc#c", 0},
                {"(?x)a#a\nb#b\nc#c", 1},
                {"(?x)a#a\nb#b\nc#c", 2},
                {"(?x)a#a\nb#b\nc#c", 3},
                {"(?x)a#a\nb#b\nc#c", 4},
                {"(?x)a#a\nb#b\nc#c", 7},
                {"(?x)a#a\nb#b\nc#c", 8},
                {"(?x)a#a\nb#b\nc#c", 11},
                {"(?x)a#a\nb#b\nc#c", 12},

        });
    }

    public XModerFalseTest(String regex, int index){
        this.regex = regex;
        this.index = index;
    }

    @Test
    public void falseXModerTest() throws Exception{
        moder.process(regex);
        assertFalse(moder.isInCommentRange(index));
    }
}
