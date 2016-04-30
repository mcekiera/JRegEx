package Re.Regex.Test;


import Re.Type;
import Re.Regex.Composite;
import Re.Regex.Construct;
import Re.Regex.ConstructsAbstractFactory;
import Re.Regex.Single;
import Re.Segment;
import static org.junit.Assert.*;



public class ConstructsAbstractFactoryTest {
    ConstructsAbstractFactory factory = ConstructsAbstractFactory.getInstance();

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void testCreateConstructPredefined() throws Exception {
        String regex = "\\d\\D\\s\\S\\w\\W";
        Segment p = new Segment(regex,0,regex.length());
        Construct parent = new Composite(null, Type.EXPRESSION,p);

        Segment segment1 = new Segment(regex,0,2);
        Segment segment2 = new Segment(regex,2,4);
        Segment segment3 = new Segment(regex,4,6);
        Segment segment4 = new Segment(regex,6,8);
        Segment segment5 = new Segment(regex,8,10);
        Segment segment6 = new Segment(regex,10,12);

        Construct result1 = new Single(parent,Type.PREDEFINED,segment1);
        Construct result2 = new Single(parent,Type.PREDEFINED,segment2);
        Construct result3 = new Single(parent,Type.PREDEFINED,segment3);
        Construct result4 = new Single(parent,Type.PREDEFINED,segment4);
        Construct result5 = new Single(parent,Type.PREDEFINED,segment5);
        Construct result6 = new Single(parent,Type.PREDEFINED,segment6);

        assertTrue(factory.createConstruct(parent,segment1.getContent(),0).equals(result1));
        assertTrue(factory.createConstruct(parent,segment2.getContent(),2).equals(result2));
        assertTrue(factory.createConstruct(parent,segment3.getContent(),4).equals(result3));
        assertTrue(factory.createConstruct(parent,segment4.getContent(),6).equals(result4));
        assertTrue(factory.createConstruct(parent,segment5.getContent(),8).equals(result5));
        assertTrue(factory.createConstruct(parent,segment6.getContent(),10).equals(result6));
    }

    @org.junit.Test
    public void testCreateConstructBoundary() throws Exception {
        String regex = "\\b\\B\\G\\A\\z\\Z^$";
        Segment p = new Segment(regex,0,regex.length());
        Construct parent = new Composite(null, Type.EXPRESSION,p);

        Segment segment1 = new Segment(regex,0,2);
        Segment segment2 = new Segment(regex,2,4);
        Segment segment3 = new Segment(regex,4,6);
        Segment segment4 = new Segment(regex,6,8);
        Segment segment5 = new Segment(regex,8,10);
        Segment segment6 = new Segment(regex,10,12);
        Segment segment7 = new Segment(regex,12,13);
        Segment segment8 = new Segment(regex,13,14);

        Construct result1 = new Single(parent,Type.BOUNDARY,segment1);
        Construct result2 = new Single(parent,Type.BOUNDARY,segment2);
        Construct result3 = new Single(parent,Type.BOUNDARY,segment3);
        Construct result4 = new Single(parent,Type.BOUNDARY,segment4);
        Construct result5 = new Single(parent,Type.BOUNDARY,segment5);
        Construct result6 = new Single(parent,Type.BOUNDARY,segment6);
        Construct result7 = new Single(parent,Type.BOUNDARY,segment7);
        Construct result8 = new Single(parent,Type.BOUNDARY,segment8);

        assertTrue(factory.createConstruct(parent,segment1.getContent(),0).equals(result1));
        assertTrue(factory.createConstruct(parent,segment2.getContent(),2).equals(result2));
        assertTrue(factory.createConstruct(parent,segment3.getContent(),4).equals(result3));
        assertTrue(factory.createConstruct(parent,segment4.getContent(),6).equals(result4));
        assertTrue(factory.createConstruct(parent,segment5.getContent(),8).equals(result5));
        assertTrue(factory.createConstruct(parent,segment6.getContent(),10).equals(result6));
        assertTrue(factory.createConstruct(parent,segment7.getContent(),12).equals(result7));
        assertTrue(factory.createConstruct(parent,segment8.getContent(),13).equals(result8));
    }

    @org.junit.Test
    public void testCreateConstructMode() throws Exception {
        String regex = "(?i)(?m)(?d)(?s)(?u)(?x)(?U)(?imds)(?iUx)";
        Segment p = new Segment(regex,0,regex.length());
        Construct parent = new Composite(null, Type.EXPRESSION,p);

        Segment segment1 = new Segment(regex,0,4);
        Segment segment2 = new Segment(regex,4,8);
        Segment segment3 = new Segment(regex,8,12);
        Segment segment4 = new Segment(regex,12,16);
        Segment segment5 = new Segment(regex,16,20);
        Segment segment6 = new Segment(regex,20,24);
        Segment segment7 = new Segment(regex,24,28);
        Segment segment8 = new Segment(regex,28,35);
        Segment segment9 = new Segment(regex,35,41);

        Construct result1 = new Single(parent,Type.MODE,segment1);
        Construct result2 = new Single(parent,Type.MODE,segment2);
        Construct result3 = new Single(parent,Type.MODE,segment3);
        Construct result4 = new Single(parent,Type.MODE,segment4);
        Construct result5 = new Single(parent,Type.MODE,segment5);
        Construct result6 = new Single(parent,Type.MODE,segment6);
        Construct result7 = new Single(parent,Type.MODE,segment7);
        Construct result8 = new Single(parent,Type.MODE,segment8);
        Construct result9 = new Single(parent,Type.MODE,segment9);

        assertTrue(factory.createConstruct(parent,segment1.getContent(),0).equals(result1));
        assertTrue(factory.createConstruct(parent,segment2.getContent(),4).equals(result2));
        assertTrue(factory.createConstruct(parent,segment3.getContent(),8).equals(result3));
        assertTrue(factory.createConstruct(parent,segment4.getContent(),12).equals(result4));
        assertTrue(factory.createConstruct(parent,segment5.getContent(),16).equals(result5));
        assertTrue(factory.createConstruct(parent,segment6.getContent(),20).equals(result6));
        assertTrue(factory.createConstruct(parent,segment7.getContent(),24).equals(result7));
        assertTrue(factory.createConstruct(parent,segment8.getContent(),28).equals(result8));
        assertTrue(factory.createConstruct(parent,segment9.getContent(),35).equals(result9));
    }

    @org.junit.Test
    public void testCreateCommonConstruct() throws Exception {

    }

    @org.junit.Test
    public void testCreateGroupConstruct() throws Exception {

    }

    @org.junit.Test
    public void testCreateInGroupConstruct() throws Exception {

    }

    @org.junit.Test
    public void testIsBeginningOfGroup() throws Exception {

    }

    @org.junit.Test
    public void testIsEndOfGroup() throws Exception {

    }

    @org.junit.Test
    public void testCreateConstructInCharClass() throws Exception {

    }

}