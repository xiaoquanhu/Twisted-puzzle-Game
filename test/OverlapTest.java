package comp1110.ass2;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class OverlapTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(20000);

    private void test(String in,int pos, String expected) {
        assertTrue("Input was '"+in+"', expected "+expected, Node.getString(in, pos) == expected);
    }

    @Test
    public void testGetStringnull(){
        String expected = null;
        String in = "";
        int pos = -1 ;

        test(in,pos,expected);

    }

    @Test
    public void testGetStringA(){
        String expected = "A";
        String in = "a7A7";
        int pos = 2 ;

        test(in,pos,expected);

    }

    @Test
    public void testGetStringB(){
        String expected = "B";
        String in = "";
        int pos = 13 ;

        test(in,pos,expected);

    }

    @Test
    public void testGetStringC(){
        String expected = "C";
        String in = "a7C7";
        int pos = 22 ;

        test(in,pos,expected);

    }

    @Test
    public void testGetStringD(){
        String expected = "D";
        String in = "a2D6";
        int pos = 25 ;

        test(in,pos,expected);

    }



}