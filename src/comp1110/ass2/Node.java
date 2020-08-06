package comp1110.ass2;

public class Node {
    private Piece piece;
    private boolean isoccupied;
    private int hasAhole;
    // hasAhole is int type, if there is a hole, it will not be -1, and different number means different color.
    //9 is red, 10 is blue, 11 is green, 12 is yellow, same as pegs
    public int peg;
    public int position;
    //the nodes current position

    Node() {
        piece = null;
        isoccupied = false;
        hasAhole = -1;
        peg = -1;
        position = -1;
    }

    Node(int position) {
        piece = null;
        isoccupied = false;
        hasAhole = -1;
        peg = -1;
        this.position = position;
    }

    Node(Piece piece, int position) {
        this.piece = piece;
        this.isoccupied = false;
        this.hasAhole = -1;
        this.peg = -1;
        this.position = position;
    }
    Node(Node node) {
        this.piece = node.getPiece();
        this.isoccupied = node.isOccupied();
        this.hasAhole = node.hasAhole();
        this.peg = node.peg;
        this.position = node.getposition();
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    void setIsoccupied() {
        this.isoccupied = true;
    }
    void setHasAhole(int color) {
        this.hasAhole = color;
    }

    Piece getPiece() {
        return this.piece;
    }
    boolean isOccupied() {
        return this.isoccupied;
    }
    int hasAhole() {
        return this.hasAhole;
    }
    private int getposition() {return this.position;}




    private String getPositionstring() {
        // use for encode, convert the position number to code
        // will return the current position code
        String str = "";
        str = getString(str, position);
        return String.format("%d%s", position % 8 + 1, str);
    }

    static String getString(String str, int position) {
        // use in getPositionstring(), convert the position number to code(A, B, C, D)
        if(position>31||position<0){
            return null;
        }
        switch (position /8) {
            case 0:
                str = "A";
                break;
            case 1:
                str = "B";
                break;
            case 2:
                str = "C";
                break;
            case 3:
                str = "D";
                break;
        }
        return str;
    }
    String pegencode() {
        // get the current peg placement String
        // if there is no peg, return empty String
        if (this.peg != -1) {
            String peg = "";
            switch (this.peg) {
                case 9:
                    peg = "i";
                    break;
                case 10:
                    peg = "j";
                    break;
                case 11:
                    peg = "k";
                    break;
                case 12:
                    peg = "l";
                    break;
            }
            if (!peg.equals("")) {
                return peg + this.getPositionstring() + "0";
            }
        }
        return "";
    }
    String encode() {
        // get the current piece placement String
        // if there is no piece, return empty string
        if (this.piece != null) {
            String piece = "";
            switch (this.piece.piecetype) {
                case 1:
                    piece = "a";
                    break;
                case 2:
                    piece = "b";
                    break;
                case 3:
                    piece = "c";
                    break;
                case 4:
                    piece = "d";
                    break;
                case 5:
                    piece = "e";
                    break;
                case 6:
                    piece = "f";
                    break;
                case 7:
                    piece = "g";
                    break;
                case 8:
                    piece = "h";
                    break;
            }
            if (!piece.equals("")) {
                return piece + this.getPositionstring() + this.piece.orientation;
            }
        }
        return "";
    }
    // return this piece's String code which contains their type, orient and pos.

    int[] getOccupation() {
        // get the corresponding piece's occupy positions
        if (piece != null) {
            return piece.getOccupyPositions(position);
        } else {
            return null;
        }
    }

    int[] getHoles() {
        // get the corresponding piece's hole positions
        if (piece != null) {
            for (int hole: piece.getHolepos(position)) {

                    if (hole == 7) {
                        for (int a : piece.getHolepos(position)) {
                            if (a == 8)
                                return null;
                        }
                    } else if (hole == 15) {
                        for (int a : piece.getHolepos(position)) {
                            if (a == 16)
                                return null;
                        }
                    } else if (hole == 23) {
                        for (int a : piece.getHolepos(position)) {
                            if (a == 24)
                                return null;
                        }
                    }

                if (hole > 31 || hole < 0)
                    return null;
            }
            return piece.getHolepos(position);
        } else {
            return null;
        }
    }
    int getsymorientation() {
        // get the piece weak sym orientation
        if (this.piece!= null) {
            return this.piece.getSymmetryOrientation();
        }
        return -1;
    }

}
