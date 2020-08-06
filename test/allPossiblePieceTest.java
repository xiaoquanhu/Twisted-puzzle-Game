package comp1110.ass2;
//import comp1110.ass2.TwistGame;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static comp1110.ass2.TestUtility.*;


public class allPossiblePieceTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(20000);

    private void test(String in, ArrayList<String> possible, boolean expected) {
        ArrayList<String> result = new ArrayList<String>(); //= TwistGame.allPossiblePiece(in);
        boolean out = false;
        if (result == possible)out = true;
        assertTrue("Input was '"+in+"', expected "+expected+" but got "+out, out == expected);
    }
    @Test
    public void oncePlacementIsComplete(){
        Random rand = new Random();
        char type, column, row, orientation;
        String piece = "";
        ArrayList<String> possible = new ArrayList<>();
        for(int i=0; i<GOOD_PLACEMENTS.length; i++) {
            type = (char) (rand.nextInt(8)+97);
            column = (char) (rand.nextInt(8)+49);
            row = (char) (rand.nextInt(4)+65);
            orientation = (char) (rand.nextInt(8)+48);
            piece = String.valueOf(type)+String.valueOf(column)+String.valueOf(row)+String.valueOf(orientation);
            possible.add(piece);
            test(GOOD_PLACEMENTS[i], possible, false);
        }
    }
    @Test
    public void oncePlacementIsNotComplete(){
        Random rand = new Random();
        char type1, column, row, orientation;
        char type2,type3;
        String piece = "";
        String placement;
        int target;
        ArrayList<String> possible = new ArrayList<>();

        for(int i=0; i<GOOD_PLACEMENTS.length; i++) {
            target = rand.nextInt(8);
            placement =  GOOD_PLACEMENTS[i].substring(0, target * 4) + GOOD_PLACEMENTS[i].substring((1 + target) * 4, GOOD_PLACEMENTS[i].length());
            type1 = (char) (97+target);
            type2 = (char) (rand.nextInt(8)+97);
            if(type2!=type1) type3=type2;
            else type3 = ++type2;
            column = (char) (rand.nextInt(8)+49);
            row = (char) (rand.nextInt(4)+65);
            orientation = (char) (rand.nextInt(8)+48);
            piece = String.valueOf(type3)+String.valueOf(column)+String.valueOf(row)+String.valueOf(orientation);
            test(placement, possible, false);
        }
    }
}

