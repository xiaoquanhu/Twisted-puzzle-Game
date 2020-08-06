package comp1110.ass2;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;
import static comp1110.ass2.TestUtility.GOOD_PLACEMENTS;
public class ObjectiveTest {

    @Test
    public void getallsolutionswithoutpegs() {
        HashSet<String> outset = Objective.getallsolutionswithoutpegs();
        assertTrue("No solutions returned for problem ", !outset.isEmpty());
        try {
            File writename = new File("C:\\Users\\ai316\\Desktop\\output.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for (String str: outset) {
                out.write(str+"\r\n");
                out.flush();
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void getAllPegPlacements() {
        HashMap<String, String> outhashmap = Objective.getAllPegPlacements(6);
        assertTrue("No solutions returned for problem ", !outhashmap.isEmpty());
        try {
            File writename = new File("C:\\Users\\ai316\\Desktop\\justtest.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            for (String key: outhashmap.keySet()) {
                out.write(key + " "+ outhashmap.get(key) +"\r\n");
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reduceStirctsym() {
        HashSet<String> outset = Objective.reduceStirctsym();
        assertTrue("No solutions returned for problem ", !outset.isEmpty());
        try {
            File writename = new File("C:\\Users\\ai316\\Desktop\\reduceStrictSymoutput.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for (String str: outset) {
                out.write(str+"\r\n");
                out.flush();
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}