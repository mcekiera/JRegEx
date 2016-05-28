package Test;

import Model.Regex.Single;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingleTest {

    @Test
    public void testIsComplex() throws Exception {
         assertFalse(new Single(null,null,null).isComplex());
    }
}