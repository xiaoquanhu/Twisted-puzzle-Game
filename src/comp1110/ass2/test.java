package comp1110.ass2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class test {
    public static void main(String[] args) {

        try {
            String pathname = "C:\\Users\\ai316\\Desktop\\test.txt";

            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                HashSet<String> ans = Objective.getallsolutionswithpegs(line);
                if (ans != null) {
                    System.out.print(line + " ");
                for (String str : ans) {
                    System.out.println(str);
                }
                }

                if (ans != null && ans.size() == 1) {
                    System.out.println(line + " is good pegplacement");
                }

                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}




