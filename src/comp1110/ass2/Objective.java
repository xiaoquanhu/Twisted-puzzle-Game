package comp1110.ass2;

import com.sun.xml.internal.bind.marshaller.NoEscapeHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.HashSet;

/** Main author: Liang Yuyuan */
public class Objective {
    private final static  int[] pegtypeary = {9,10,11,12};

    private final static int[][] difficulty2= {
            {0, 2, 0, 0},
            {1, 0, 0, 1},
            {1, 1, 0, 0}
    };

    private final static int[][] difficulty3 = {
            {1, 1, 0, 1},
            {0, 0, 2, 1},
            {1, 0, 1, 1},
            {0, 0, 2, 1},
            {0, 1, 0, 2},
            {1, 2, 0, 0},
            {1, 1, 1, 0},
            {1, 0, 2, 0},
            {1, 0, 0, 2},
            {0, 1, 1, 1},
            {0, 2, 1, 0},
            {0, 1, 2, 0}
    };

    private final static int[][] difficulty4 = {
            {1, 1, 1, 1},
            {0, 2, 1, 1},
            {0, 1, 1, 2},
            {0, 1, 2, 1},
            {1, 1, 2, 0},
            {1, 0, 1, 2},
            {1, 2, 1, 0},
            {1, 0, 2, 1}
    };
    private final static int[][] difficulty5 = {
            {0, 2, 2, 1},
            {0, 2, 1, 2},
            {0, 1, 2, 2},
            {1, 1, 2, 1},
            {1, 1, 1, 2},
            {1, 2, 1, 1}
    };
    private final static int[][] difficulty6 = {
            {0, 2, 2, 2},
            {1, 2, 2, 1},
            {1, 1, 2, 2},
            {1, 2, 1, 2}
    };
    private final static int[][] difficulty7 = {{1,2,2,2}};
    // difficulty is use for find the pegplacement, 1st is red peg number
    // 2nd is blue peg number, 3rd is green peg number, 4rd is yellow peg number

    static HashMap<String, String> getAllPegPlacements(int difficultyrate) {
        // difficulty rate means the peg number
        // get all the pegplacement through trying every peg in the all solutions
        HashMap<String, String> allplacementandsolution = new HashMap<>();
        //allplacementandsolution is used for store the pegplacement and corresponding solution
        HashSet<String> multisolutionpegs = new HashSet<>();
        // multisolutionpegs is store the pegplacement which have multi solutions
        int[][] difficulty = null;
        switch (difficultyrate) {
            case 7:
                difficulty = difficulty7;
                break;
            case 6:
                difficulty = difficulty6;
                break;
            case 5:
                difficulty = difficulty5;
                break;
            case 4:
                difficulty = difficulty4;
                break;
            case 3:
                difficulty = difficulty3;
                break;
            case 2:
                difficulty = difficulty2;
                break;
        }
        if (difficulty == null) {
            return null;
        }

        try {
            String pathname = "C:\\Users\\ai316\\Desktop\\reduceStrictSymoutput.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            // every line is a solution
            while (line != null) {
                Node[] nodes = new Node[32];
                for (int x = 0; x < 32; x++) {
                    nodes[x] = new Node(x);
                }
                for (int i = 0; i < line.length() / 4; i++) {
                    int[] pieceIntplacement = TwistGame.decode(line.substring(i * 4, i * 4 + 4));
                    assert pieceIntplacement!=null : "decode function generate a null array!";
                    boolean signal = placePiece(nodes, pieceIntplacement[0], pieceIntplacement[1], pieceIntplacement[2]);
                    // place the solution
                    if (!signal) throw new AssertionError(" pieceplacement " + line + " is wrong");
                }
                for (int[] ary : difficulty) {
                    // according the solution to find all possible peg placement
                    seekPeg(line, nodes, allplacementandsolution, multisolutionpegs, ary);
                }
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allplacementandsolution;
    }


    //author: Liang Yuyuan, Xiaoquan Hu, Huang Jing
    private static void seekPeg(String pieceplacement, Node[] nodes, HashMap<String, String> hashMap, HashSet<String> multisolutionpegs, int[] maxpegnum) {
        //find all possible pegplacement
        int[] newmaxpegnum = new int[maxpegnum.length];
        if (maxpegnum.length >= 0) System.arraycopy(maxpegnum, 0, newmaxpegnum, 0, maxpegnum.length);

        int pegtype = 0;
        for (int i = 0; i < newmaxpegnum.length; i++) {
            if (newmaxpegnum[i] != 0) {
                if (i == 0) {
                    pegtype = 9;
                } else if (i == 1) {
                    pegtype = 10;
                } else if (i == 2) {
                    pegtype = 11;
                } else if (i == 3) {
                    pegtype = 12;
                }
                newmaxpegnum[i]--;

                assert pegtype >= 9 : "pegtype wrong!";
                for (int j = 0; j < 32; j++) {
                    if (seekHole(nodes, pegtype, j)){
                        Node[] new_nodes = new Node[32];
                        for (int y = 0; y < 32; y++) {
                            new_nodes[y] = new Node(nodes[y]);
                        }
                        new_nodes[j].peg = pegtype;

                        boolean full = true;
                        for (int p : newmaxpegnum) {
                            if (p != 0) {
                                full = false;
                            }
                        }

                        if (full) {
                            String pegplacement = "";
                            for (int p : pegtypeary){
                                for (Node n : new_nodes) {
                                    if (n.peg == p) {
                                        pegplacement = pegplacement + n.pegencode();
                                    }
                                }
                            }
                            pieceplacement = TwistGame.replaceSym(pieceplacement + pegplacement);
                            pieceplacement = TwistGame.divideString(pieceplacement)[0];

                            if (!multisolutionpegs.contains(pegplacement)) {
                                // if the peg placement is in the multisolutionpegs, it have more than one solution
                                if (hashMap.containsKey(pegplacement) && !hashMap.get(pegplacement).equals(pieceplacement)) {
                                    // check the new peg placement if it is already in the map
                                    hashMap.remove(pegplacement);
                                    multisolutionpegs.add(pegplacement);
                                } else {
                                    hashMap.put(pegplacement, pieceplacement);
                                }
                            }
                        } else {
                            seekPeg(pieceplacement, new_nodes, hashMap, multisolutionpegs, newmaxpegnum);
                        }
                    }
                }
            }
        }


    }

     private static boolean seekHole(Node[] nodes, int pegtype, int position) {
        // determine if nodes[position] can match the peg
         return nodes[position].peg == -1 && nodes[position].hasAhole() == pegtype;
     }






    private static boolean placePiece(Node[] nodes, int piecetype, int orientation, int position) {
        //place one piece into nodes
        if (nodes[position].getPiece() != null) {
            return false;
        }

        nodes[position].setPiece(new Piece(piecetype, orientation));
        int[] occupied = nodes[position].getOccupation();
        int[] holes = nodes[position].getHoles();

        if (occupied == null) {
            return false;
        }
        if (holes == null) {
            return false;
        }

        for (int o : occupied) {
            if (!nodes[o].isOccupied()) {
                nodes[o].setIsoccupied();
            } else {
                return false;
            }
        }
        if (!placeHoles(nodes, piecetype, holes)) {
            return false;
        }
        for (int o : occupied) {
            if (nodes[o].peg != -1) {
                if (nodes[o].hasAhole() != nodes[o].peg) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean placeHoles(Node[] nodes, int piecetype, int[] holes) {
        // place holes into nodes
        for (int hole : holes) {
            if (nodes[hole].hasAhole() == -1) {

                setHoleType(nodes, piecetype, hole);
            } else {
                return false;
            }
        }
        return true;
    }

    static void setHoleType(Node[] nodes, int piecetype, int hole) {
        // set the hole type according the piece type
        if (piecetype == 1 || piecetype == 2) {
            nodes[hole].setHasAhole(9);
        } else if (piecetype == 3 || piecetype == 4) {
            nodes[hole].setHasAhole(10);
        } else if (piecetype == 5 || piecetype == 6) {
            nodes[hole].setHasAhole(11);
        } else if (piecetype == 7 || piecetype == 8) {
            nodes[hole].setHasAhole(12);
        }
    }




    static HashSet<String> getallsolutionswithoutpegs() {
        // find all the solution without the pegplacement
        // the solutions set contains all the solution in this game
        HashSet<String> hashSet = new HashSet<>();

        for (int i = 0; i < 32; i++) { // every position
            for (int j = 0; j < 8; j++) { // flip or rotation
                Node[] nodes = new Node[32];
                for (int x = 0; x < 32; x++) {
                    nodes[x] = new Node(x);
                }
                if (placePiece(nodes, 1, j, i)) {
                    getsolution(2, nodes, hashSet);
                }
            }
        }
        return hashSet;
    }
    static HashSet<String> getallsolutionswithpegs(String placement) {
        //find all the solutions correspond the pegplacement
        //use for checking if the pegplacement only have one solution
        String[] placements = TwistGame.divideString(placement);
        HashSet<String> hashSet = new HashSet<>();
        HashSet<String> newhashSet = new HashSet<>();
        Node[] nodes = new Node[32];
        for (int x = 0; x < 32; x++) {
            nodes[x] = new Node(x);
        }
        if (placements.length == 1) {
            return null;
        }
        if (placePeg(nodes, placements[1])&& placePieceplacement(nodes, placements[0])) {
            for (int i = 0; i < 32; i++) { // every position
                for (int j = 0; j < 8; j++) { // flip or rotation
                    Node[] newnodes = new Node[32];
                    for (int x = 0; x < 32; x++) {
                        newnodes[x] = new Node(nodes[x]);
                    }
                    if (placePiece(newnodes, 1, j, i)) {
                        getsolution(2, newnodes, hashSet);
                    }
                }
            }
        }

        for (String str : hashSet) {
            String newsolution = TwistGame.replaceSym(str+placement);
            newhashSet.add(newsolution);
        }
        return newhashSet;
    }

    static boolean placePeg(Node[] nodes, String placements) {
        //place the peg into nodes
        for (int i = 0; i < placements.length() / 4; i++) {
            int[] pegIntplacement = TwistGame.decode(placements.substring(i * 4, i * 4 + 4));
            assert pegIntplacement!=null : "decode function generate a null array!";
            if (nodes[pegIntplacement[2]].peg == -1) {
                nodes[pegIntplacement[2]].peg = pegIntplacement[0];
            } else {
                return false;
            }
        }
        return true;
    }

    static boolean placePieceplacement(Node[] nodes, String piecePlacement) {
        // place all the pieces in the pieceplacement
        for (int i = 0; i < piecePlacement.length() / 4; i++) {
            int[] pieceIntplacement = TwistGame.decode(piecePlacement.substring(i * 4, i * 4 + 4));
            assert pieceIntplacement!=null : "decode function generate a null array!";

            nodes[pieceIntplacement[2]].setPiece(new Piece(pieceIntplacement[0], pieceIntplacement[1]));
            int[] occupied = nodes[pieceIntplacement[2]].getOccupation();
            int[] holes = nodes[pieceIntplacement[2]].getHoles();

            if (occupied == null) {
                return false;
            }
            if (holes == null) {
                return false;
            }
            if (!placeHoles(nodes, pieceIntplacement[0], holes)) {
                return false;
            }

            for (int o : occupied) {
                if (!nodes[o].isOccupied()) {
                    nodes[o].setIsoccupied();
                } else {
                    return false;
                }
            }
        }
        return true;
    }



    private static void getsolution(int piecetype, Node[] nodes, HashSet<String> hashSet) {
        //recursion, use for find all the solutions
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 8; j++) {
                if (piecetype == 3 || piecetype == 8) {
                    if (j>= 4)
                        continue;
                }
                Node[] new_nodes = new Node[32];
                for (int x = 0; x < 32; x++) {
                    new_nodes[x] = new Node(nodes[x]);
                }
                if (placePiece(new_nodes, piecetype, j, i)) {
                    if (piecetype == 8) {
                        String solution = "";
                        solution = getPieceplacementString(new_nodes, solution);
                        hashSet.add(solution);
                    } else {
                        getsolution(piecetype + 1, new_nodes, hashSet);
                    }
                }
            }
        }
    }

    static String getPieceplacementString(Node[] new_nodes,String solution) {
        // get the piece placement String in the current nodes
        int p = 1;
        while (p != 9) {
            for (Node n : new_nodes) {
                if (n.getPiece() != null && n.getPiece().piecetype == p) {
                    solution = solution + n.encode();
                }
            }
            p++;
        }
        return solution;
    }

    //author: Liang Yuyuan, Xiaoquan Hu
    static HashSet<String> reduceStirctsym() {
        // use for reducing the strict sym in the all-solutions txt
        HashSet<String> oldsolutions = new HashSet<>();
        HashSet<String> newsolutions = new HashSet<>();
        try {

            String pathname = "C:\\Users\\ai316\\Desktop\\output.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                oldsolutions.add(line);
                line = br.readLine();
            }
            for (String str : oldsolutions) {
                String newstr = "";
                for (int i = 0; i < str.length() / 4; i++) {
                    String lilPiece = str.substring(i * 4, i * 4 + 4);

                    if (lilPiece.charAt(0) == 'c' || lilPiece.charAt(0) == 'h') {
                        if (lilPiece.charAt(3) == '4') {
                            String temp = lilPiece;
                            lilPiece = temp.substring(0, 3) + 0;
                        } else if (lilPiece.charAt(3) == '5') {
                            String temp = lilPiece;
                            lilPiece = temp.substring(0, 3) + 1;
                        } else if (lilPiece.charAt(3) == '6') {
                            String temp = lilPiece;
                            lilPiece = temp.substring(0, 3) + 2;
                        } else if (lilPiece.charAt(3) == '7') {
                            String temp = lilPiece;
                            lilPiece = temp.substring(0, 3) + 3;
                        }
                    }
                    newstr = newstr + lilPiece;
                }
                newsolutions.add(newstr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsolutions;
    }











}
