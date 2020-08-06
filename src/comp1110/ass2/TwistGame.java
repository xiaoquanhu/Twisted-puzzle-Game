package comp1110.ass2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides the text interface for the Twist Game
 * <p>
 * The game is based directly on Smart Games' IQ-Twist game
 * (http://www.smartgames.eu/en/smartgames/iq-twist)
 */
public class TwistGame {

    private static Node[] nodes;

    TwistGame() {
        initializeBoard();
    }

    private void initializeBoard() {
        nodes = new Node[32];
    }

    /**
     * Determine whether a piece or peg placement is well-formed according to the following:
     * - it consists of exactly four characters
     * - the first character is in the range a .. l (pieces and pegs)
     * - the second character is in the range 1 .. 8 (columns)
     * - the third character is in the range A .. D (rows)
     * - the fourth character is in the range 0 .. 7 (if a piece) or is 0 (if a peg)
     *
     * @param piecePlacement A string describing a single piece or peg placement
     * @return True if the placement is well-formed
     */

    public static boolean isPlacementWellFormed(String piecePlacement) {
        // determine if it is a well-formed piece or peg
        if (piecePlacement.length() != 4)
            return false;
        if (piecePlacement.charAt(0) >= 'a' && piecePlacement.charAt(0) <= 'l')
            if ((piecePlacement.charAt(1) - '0') >= 1 && (piecePlacement.charAt(1) - '0') <= 8)
                if (piecePlacement.charAt(2) >= 'A' && piecePlacement.charAt(2) <= 'D')
                    if (piecePlacement.charAt(0) == 'i' || piecePlacement.charAt(0) == 'k' || piecePlacement.charAt(0) == 'j' || piecePlacement.charAt(0) == 'l') {
                        return (piecePlacement.charAt(3) - '0') == 0;
                    } else {
                        return (piecePlacement.charAt(3) - '0') >= 0 && (piecePlacement.charAt(3) - '0') <= 7;
                    }
        return false;
    }

    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 15);
     * - each piece or peg placement is well-formed
     * - each piece or peg placement occurs in the correct alphabetical order (duplicate pegs can be in either order)
     * - no piece or red peg appears more than once in the placement
     * - no green, blue or yellow peg appears more than twice in the placement
     *
     * @param placement A string describing a placement of one or more pieces and pegs
     * @return True if the placement is well-formed
     */

    // author: Jing Huang, Liang Yuyuan
    public static boolean isPlacementStringWellFormed(String placement) {
        // determine if it is a well-formed placement
        if (!(placement.length() % 4 == 0 && placement.length() / 4 >= 1 && placement.length() / 4 <= 15))
            return false;

        int i = 0;
        int[] gby = {0, 0, 0}; // use this array to store the peg num, to determine if the peg number is over the limit
        String str = "";
        while (i < placement.length()) {
            if (!isPlacementWellFormed(placement.substring(i, i + 4)))
                return false;

            if (placement.charAt(i) == 'k') {
                gby[0]++;
                if (gby[0] > 2)
                    return false;
            } else if (placement.charAt(i) == 'j') {
                gby[1]++;
                if (gby[1] > 2)
                    return false;
            } else if (placement.charAt(i) == 'l') {
                gby[2]++;
                if (gby[2] > 2)
                    return false;
            } else if (str.indexOf(placement.charAt(i)) != -1) {
                return false;
            }

            str = str + placement.charAt(i);
            i = i + 4;
        }

        for (int x = 0; x < str.length(); x++) {
            if (x == 0)
                continue;
            if (str.charAt(x) < str.charAt(x - 1))
                return false;
        }

        return true;
    }


    private static int getposition(char str1, char str2) {
        // the method is used to get position of the piece or peg from string
        int i = 0;
        if (str2 == 'B') {
            i = i + 8;
        } else if (str2 == 'C') {
            i = i + 16;
        } else if (str2 == 'D') {
            i = i + 24;
        }
        switch (str1) {
            case '2':
                i += 1;
                break;
            case '3':
                i += 2;
                break;
            case '4':
                i += 3;
                break;
            case '5':
                i += 4;
                break;
            case '6':
                i += 5;
                break;
            case '7':
                i += 6;
                break;
            case '8':
                i += 7;
                break;
        }
        return i;
    }
    // author: Liang Yuyuan
    public static int[] decode(String smallplacement) {
        // this method can convert the small piece or peg string to int array
        int[] placement = new int[3]; // 1st is piece type, 2nd is orientation, 3rd is position
        switch (smallplacement.charAt(0)) {
            case 'a':
                placement[0] = 1;
                break;
            case 'b':
                placement[0] = 2;
                break;
            case 'c':
                placement[0] = 3;
                break;
            case 'd':
                placement[0] = 4;
                break;
            case 'e':
                placement[0] = 5;
                break;
            case 'f':
                placement[0] = 6;
                break;
            case 'g':
                placement[0] = 7;
                break;
            case 'h':
                placement[0] = 8;
                break;
            case 'i':
                placement[0] = 9;
                break;
            case 'j':
                placement[0] = 10;
                break;
            case 'k':
                placement[0] = 11;
                break;
            case 'l':
                placement[0] = 12;
                break;
            default:
                placement[0] = -1;
                break;
        }
        if (placement[0] == -1)
            return null;
        placement[1] = smallplacement.charAt(3) - '0';
        placement[2] = getposition(smallplacement.charAt(1), smallplacement.charAt(2));
        if (placement[2] > 31 || placement[2] < 0)
            return null;
        return placement;
    }

    // author: Liang Yuyuan
    static boolean placePieceplacement(String piecePlacement) {
        // the var is piece placement
        // this method is used for placing the piece into the nodes
        for (int i = 0; i < piecePlacement.length() / 4; i++) {
            int[] pieceIntplacement = decode(piecePlacement.substring(i * 4, i * 4 + 4));
            assert pieceIntplacement!=null : "decode function generate a null array!";

            nodes[pieceIntplacement[2]].setPiece(new Piece(pieceIntplacement[0], pieceIntplacement[1]));
            //place piece firstly then use the method to get corresponding occupied and hole positions
            int[] occupied = nodes[pieceIntplacement[2]].getOccupation();
            int[] holes = nodes[pieceIntplacement[2]].getHoles();

            if (occupied == null) {
                return false;
            }
            if (holes == null) {
                return false;
            }

            if (!Objective.placeHoles(nodes, pieceIntplacement[0], holes)) {
                //place holes
                return false;
            }

            for (int o : occupied) {
                // place occupied pos
                if (!nodes[o].isOccupied()) {
                    nodes[o].setIsoccupied();
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isOnBoardorNotOverlap(String piecePlacement) {
        // place the pieces
        //determine if the piece is overlap or offboard and then check the peg that if it is matching the hole
        if (!placePieceplacement(piecePlacement)) {
            return false;
        }
        for (Node node : nodes) {
            if ( node.isOccupied() && (node.peg != -1) && node.hasAhole() != node.peg) {
                return false;
            }
        }
        return true;
    }


    static boolean notOverlapPeg(String placements) {
        //place the pegs and check if them are overlap
        for (int i = 0; i < placements.length() / 4; i++) {
            int[] pegIntplacement = decode(placements.substring(i * 4, i * 4 + 4));
            assert pegIntplacement!=null : "decode function generate a null array!";
            if (nodes[pegIntplacement[2]].peg == -1) {
                nodes[pegIntplacement[2]].peg = pegIntplacement[0];
            } else {
                return false;
            }
        }
        return true;
    }


     static String[] divideString(String placement) {
        // use this method to divide the placement string into two String
         // the 1st is pieceplacement and the other one is pegplacement
        if (placement.contains("i")) {
            int i = placement.indexOf("i");
            String[] placements = new String[2];
            placements[0] = placement.substring(0, i);
            placements[1] = placement.substring(i);
            return placements;
        } else if (placement.contains("j")) {
            int i = placement.indexOf("j");
            String[] placements = new String[2];
            placements[0] = placement.substring(0, i);
            placements[1] = placement.substring(i);
            return placements;
        } else if (placement.contains("k")) {
            int i = placement.indexOf("k");
            String[] placements = new String[2];
            placements[0] = placement.substring(0, i);
            placements[1] = placement.substring(i);
            return placements;
        } else if (placement.contains("l")) {
            int i = placement.indexOf("l");
            String[] placements = new String[2];
            placements[0] = placement.substring(0, i);
            placements[1] = placement.substring(i);
            return placements;
        } else {
            String[] placements = new String[1];
            placements[0] = placement;
            return placements;
        }
    }

    /**
     * Determine whether a placement string is valid.  To be valid, the placement
     * string must be well-formed and each piece placement must be a valid placement
     * according to the rules of the game.
     * - pieces must be entirely on the board
     * - pieces must not overlap each other
     * - pieces may only overlap pegs when the a) peg is of the same color and b) the
     * point of overlap in the piece is a hole.
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     */

    // author: Liang Yuyuan
    public static boolean isPlacementStringValid(String placement) {
        // Task 5: determine whether a placement string is valid
        assert isPlacementStringWellFormed(placement) : "the placement string is not well formed!";
        String[] placements = divideString(placement);
        nodes = new Node[32];
        for (int j = 0; j < 32; j++) {
            nodes[j] = new Node(j);
        }
        //initial the nodes

        if (placements.length == 1) {
            // if length == 1, it means that there is no pegplacement in the placement
            return isOnBoardorNotOverlap(placements[0]);
        } else {
            if (notOverlapPeg(placements[1])) {
                //place peg firstly and then place piece
                return isOnBoardorNotOverlap(placements[0]);
            }
            return false;
        }

    }


    /**
     * Given a string describing a placement of pieces and pegs, return a set
     * of all possible next viable piece placements.   To be viable, a piece
     * placement must be a valid placement of a single piece.  The piece must
     * not have already been placed (ie not already in the placement string),
     * and its placement must be valid.   If there are no valid piece placements
     * for the given placement string, return null.
     * <p>
     * When symmetric placements of the same piece are viable, only the placement
     * with the lowest rotation should be included in the set.
     *
     * @param placement A valid placement string (comprised of peg and piece placements)
     * @return An set of viable piece placements, or null if there are none.
     */
    // author: Liang Yuyuan
    public static Set<String> getViablePiecePlacements(String placement) {
        // FIXME Task 6: determine the set of valid next piece placements
        Set<String> nextPiece = new HashSet<>();
        String[] placements = divideString(placement);
        nodes = new Node[32];
        for (int j = 0; j < 32; j++) {
            nodes[j] = new Node(j);
        }

        if (placements.length == 1) {
            if (placePieceplacement(placements[0])) {
                findNextPiece(nodes, nextPiece);
            }
        } else {
            if (notOverlapPeg(placements[1]) && placePieceplacement(placements[0])) {
                findNextPiece(nodes, nextPiece);
            }
        }

        if (nextPiece.isEmpty()) {
            return null;
        }
        check(nextPiece);
        return nextPiece;
    }

    // author: Jing Huang, Liang Yuyuan
    public static Set<String> hint(String placement) {
        // hints, similar to task6, but it only contains the string which can complete the game
        Set<String> hints = new HashSet<>();

        nodes = new Node[32];
        for (int j = 0; j < 32; j++) {
            nodes[j] = new Node(j);
        }
        String[] placements = divideString(placement);
        if (placements.length == 1) {
            if (placePieceplacement(placements[0])) {
                findhints(nodes, hints);
            }
        } else {
            if (notOverlapPeg(placements[1]) && placePieceplacement(placements[0])) {
                findhints(nodes, hints);
            }
        }

        if (hints.isEmpty()) {
            return null;
        }
        check(hints);
        return hints;
    }
    // author: Jing Huang
    static void check(Set<String> nextPiece) {
        // this method is used for reducing the weak sym piece String
        Set<String> toremove = new HashSet<>();

        for (String str1 : nextPiece) {
            for (String str2 : nextPiece) {
                if (!str1.equals(str2)) {
                    if (str1.substring(0, 3).equals(str2.substring(0, 3))) {
                        if (str1.charAt(0) == 'c' || str1.charAt(0) == 'h') {
                            if (str1.charAt(3)-'0' == 0 && str2.charAt(3)-'0' == 2) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 1 && str2.charAt(3)-'0' == 3) {
                                toremove.add(str2);
                            }
                        } else if (str1.charAt(0) == 'b') {
                            if (str1.charAt(3)-'0' == 0 && str2.charAt(3)-'0' == 2) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 1 && str2.charAt(3)-'0' == 3) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 4 && str2.charAt(3)-'0' == 6) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 5 && str2.charAt(3)-'0' == 7) {
                                toremove.add(str2);
                            }
                        } else if (str1.charAt(0) == 'e') {
                            if (str1.charAt(3)-'0' == 0 && str2.charAt(3)-'0' == 7) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 1 && str2.charAt(3)-'0' == 4) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 2 && str2.charAt(3)-'0' == 5) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 3 && str2.charAt(3)-'0' == 6) {
                                toremove.add(str2);
                            }
                        } else if (str1.charAt(0) == 'f') {
                            if (str1.charAt(3)-'0' == 0 && str2.charAt(3)-'0' == 6) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 1 && str2.charAt(3)-'0' == 7) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 2 && str2.charAt(3)-'0' == 4) {
                                toremove.add(str2);
                            } else if (str1.charAt(3)-'0' == 3 && str2.charAt(3)-'0' == 5) {
                                toremove.add(str2);
                            }
                        }
                    }
                }
            }
        }
        nextPiece.removeAll(toremove);
    }

    // author: Jing Huang
    static void findNextPiece(Node[] nodes, Set<String> nextPiece) {
        // find every possible next piece
        boolean[] needpiecetype = new boolean[8];
        for (Node n : nodes) {
            if (n.getPiece() != null) {
                needpiecetype[n.getPiece().piecetype -1] = true;
            }
        }
        // the code above is used for find all the piece which isn't put into the nodes

        for (int i = 0; i < 32; i++) {
            //j = piece type
            for (int j = 0; j < 8; j++) {
                if (!needpiecetype[j]) {
                    //x = orientation
                    for (int x = 0; x < 8; x++) {
                        //if the type of piece is 'c' or 'h', only have 4 orientations
                        // because it need to reduce the strong sym
                        if (j+1 == 3 || j+1 == 8 ) {
                            if ( x >= 4) {
                                continue;
                            }
                        }
                        Node[] newnodes = new Node[32];
                        for (int y = 0; y < 32; y++) {
                            newnodes[y] = new Node(nodes[y]);
                        }
                        if (placePiece(newnodes, j+1, x, i)) {
                            // if place the piece successfully, the piece is a viable piece
                            nextPiece.add(newnodes[i].encode());
                        }
                    }
                }
            }
        }
    }

    // author: Jing Huang
    static void findhints(Node[] nodes, Set<String> hints) {
        //similar to find next piece but it has a method (isviable) which used for determine the piece if it can complete the game
        boolean[] needpiecetype = new boolean[8];
        for (Node n : nodes) {
            if (n.getPiece() != null) {
                needpiecetype[n.getPiece().piecetype -1] = true;
            }
        }

        for (int i = 0; i < 32; i++) {
            //j = piece type
            for (int j = 0; j < 8; j++) {
                if (!needpiecetype[j]) {
                    //x = orientation
                    for (int x = 0; x < 8; x++) {
                        //if the type of piece is 'c' or 'h', only have 4 orientations
                        if (j+1 == 3 || j+1 == 8 ) {
                            if ( x >= 4) {
                                continue;
                            }
                        }
                        Node[] newnodes = new Node[32];
                        for (int y = 0; y < 32; y++) {
                            newnodes[y] = new Node(nodes[y]);
                        }
                        if (placePiece(newnodes, j+1, x, i)) {
                            if (isViable(newnodes)) {
                                hints.add(newnodes[i].encode());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // author: Liang Yuyuan
    static boolean isViable(Node[] nodes) {
        //determine the piece if it can complete the game
        //it is a recursion, that will try to complete the game
        boolean[] needpiecetype = new boolean[8];
        for (Node n : nodes) {
            if (n.getPiece() != null) {
                needpiecetype[n.getPiece().piecetype -1] = true;
            }
        }
        boolean isFull = true;
        for (boolean b : needpiecetype) {
            if (!b) {
                isFull = false;
            }
        }
        if (isFull) {
            return true;
        } else {
            for (int i = 0; i < 32; i++) {
                for (int j = 0; j < 8; j++) {
                    if (!needpiecetype[j]) {
                        for (int x = 0; x < 8; x++) {
                            if (j+1 == 3 || j+1 == 8 ) {
                                if ( x >= 4) {
                                    continue;
                                }
                            }
                            Node[] newnodes = new Node[32];
                            for (int y = 0; y < 32; y++) {
                                newnodes[y] = new Node(nodes[y]);
                            }

                            if (placePiece(newnodes, j+1, x, i)) {
                                if (isViable(newnodes)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
        //if it can not complete the game, return false
    }

    // author: Liang Yuyuan
    public static boolean placePiece(Node[] nodes, int piecetype, int orientation, int position) {
        // place the piece into the nodes
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

        if (!Objective.placeHoles(nodes, piecetype, holes)) return false;
        if (placePieceOccupy(nodes, occupied)) return false;

        for (Node node : nodes) {
            if ((node.isOccupied()) && (node.peg != -1) && node.hasAhole() != node.peg) { //
                return false;
            }
        }
        return true;
    }

    // author: Liang Yuyuan
    private static boolean placePieceOccupy(Node[] nodes, int[] occupied) {
        // place the occupied position, check if there are some overlap cases
        for (int o : occupied) {
            if (!nodes[o].isOccupied()) {
                if (nodes[o].peg != -1) {
                    if (nodes[o].hasAhole() == nodes[o].peg) {
                        nodes[o].setIsoccupied();
                    } else {
                        return true;
                    }
                } else {
                    nodes[o].setIsoccupied();
                }

            } else {
                return true;
            }
        }
        return false;
    }





    /**
     * Return an array of all unique solutions for a given starting placement.
     * <p>
     * Each solution should be a 32-character string giving the placement sequence
     * of all eight pieces, given the starting placement.
     * <p>
     * The set of solutions should not include any symmetric piece placements.
     * <p>
     * In the IQ-Twist game, valid challenges can have only one solution, but
     * other starting placements that are not valid challenges may have more
     * than one solution.  The most obvious example is the unconstrained board,
     * which has very many solutions.
     *
     * @param placement A valid piece placement string.
     * @return An array of strings, each 32-characters long, describing a unique
     * unordered solution to the game given the starting point provided by placement.
     */

    // author: Liang Yuyuan
    public static String[] getSolutions(String placement) {
        //put all solutions in a array
        //according to the current placement to find all solutions
        // FIXME Task 9: determine all solutions to the game, given a particular starting placement
        HashSet<String> solutions = new HashSet<>();

        String[] placements = divideString(placement);
        getSolution(placement, solutions);
        // use the method to get all solution
        if (solutions.isEmpty()) {
            return null;
        } else {
            String[] allsolutions = new String[solutions.size()];
            int i = 0;
            for (String str : solutions) {
                allsolutions[i] = replaceSym(str+placements[1]);
                // reduce the weak sym
                i++;
            }
            return allsolutions;
        }
    }

    // author: Liang Yuyuan, Xiaoquan Hu
    static String replaceSym(String placement) {
        // if there are some weak symmetrical piece, they would be replaced by same shape but lower orientation
        String[] placements = divideString(placement);
        nodes = new Node[32];
        for (int j = 0; j < 32; j++) {
            nodes[j] = new Node(j);
        }
        if (notOverlapPeg(placements[1]) && placePieceplacement(placements[0])) {
            for (Node n : nodes) {
                if (n.getPiece()!= null && n.getsymorientation()!=-1) {
                    Node[] newnodes = new Node[32];
                    for (int i = 0; i < 32; i++) {
                        newnodes[i] = new Node(nodes[i]);
                    }

                    if (replacePiece(newnodes, n.getPiece().piecetype, n.getsymorientation(), n.position)) {
                        replacePiece(nodes, n.getPiece().piecetype, n.getsymorientation(), n.position);
                    }

                }
            }
            int p = 1;
            String newplacement = "";
            newplacement = Objective.getPieceplacementString(nodes, newplacement);
            return newplacement;
        }
        return placement;
    }

    // author: Liang Yuyuan
    static void getSolution(String placement, HashSet<String> solutions) {
        // get all solutions, recursion
        String solution = verify(placement);
        // check the solution, if it is completed, it will return a good placement
        // if not, it will return empty String which is ""
        if (!solution.equals("")) {
            solutions.add(solution);
            return;
        }
        Set<String> nextPiece = getViablePiecePlacements(placement);
        //if there is no next piece, it can not finish, then jump out of this method
        if (nextPiece == null||nextPiece.isEmpty()) {
            return;
        }

        for (String str : nextPiece) {
            getSolution(str+placement, solutions);
        }
    }


    // author: Liang Yuyuan, Xiaoquan Hu
    static boolean replacePiece(Node[] nodes, int piecetype, int orientation, int position) {
        //try replace the nodes[position]'s piece into a lower orientation piece
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
            nodes[o].setHasAhole(-1);
        }
        for (int hole : holes) {
            Objective.setHoleType(nodes, piecetype, hole);
        }

        for (Node node : nodes) {
            if (node.isOccupied() && (node.peg != -1) && node.hasAhole() != node.peg) {
                return false;
            }
        }
        //if the peg is not compatible with the new piece, it will return false
        return true;
    }

    // author: Liang Yuyuan
    static String verify(String placement) {
        //determine whether the "placement" is a completed solution
        //if it is completed, will return its placement String
        //if not , will return empty String
        String[] placements = divideString(placement);
        nodes = new Node[32];
        for (int j = 0; j < 32; j++) {
            nodes[j] = new Node(j);
        }
        if (notOverlapPeg(placements[1]) && placePieceplacement(placements[0])) {
            for (Node n: nodes) {
                if (!n.isOccupied()) {
                    return "";
                } else {
                    if (n.peg != -1 && n.peg != n.hasAhole()) {
                        return "";
                    }
                }
            }
            int p = 1;
            String solution = "";

            solution = Objective.getPieceplacementString(nodes, solution);
            return solution;
        }

        return "";
    }

}
