package comp1110.ass2;

public class Piece {
    int piecetype;
    int orientation;

    private static final int[][] occupyPositions = new int[][]{
        {0, 1, 2, 10}, // 0
        {1, 9, 16, 17}, //1
        {0, 8, 9, 10}, //2
        {0, 1, 8, 16}, //3
        {2, 8, 9, 10}, //4
        {0, 8, 16, 17}, //5
        {0, 1, 2, 8}, //6
        {0, 1, 9, 17}, //7
        {0, 1, 9, 10}, //8
        {1, 8, 9, 16}, //9
        {0, 1, 9, 10}, //10
        {1, 8, 9, 16}, //11
        {1, 2, 8, 9}, //12
        {0, 8, 9, 17}, // 13
        {1, 2, 8, 9}, //14
        {0, 8, 9, 17}, //15
        {0, 1, 2, 3}, //16
        {0, 8, 16, 24}, // 17
        {0, 1, 2, 3}, //18
        {0, 8, 16, 24}, //19
        {0, 1, 2, 3}, //20
        {0, 8, 16, 24}, // 21
        {0, 1, 2, 3}, //22
        {0, 8, 16, 24}, //23
        {0, 1, 2, 9, 10}, //24
        {1, 8, 9, 16, 17}, // 25
        {0, 1, 8, 9, 10}, //26
        {0, 1, 8, 9, 16}, //27
        {1, 2, 8, 9,10}, //28
        {0, 8, 9, 16, 17}, // 29
        {0, 1, 2, 8, 9}, //30
        {0, 1, 8, 9, 17}, //31
        {0, 1, 9}, //32
        {1, 8, 9}, // 33
        {0, 8, 9}, //34
        {0, 1, 8}, //35
        {1, 8, 9}, //36
        {0, 8, 9}, // 37
        {0, 1, 8}, //38
        {0, 1, 9}, //39
        {0, 1, 2, 9}, //40
        {1, 8, 9, 17}, //41
        {1, 8, 9, 10}, //42
        {0, 8, 9, 16}, //43
        {1, 8, 9, 10}, //44
        {0, 8, 9, 16}, //45
        {0, 1, 2, 9}, //46
        {1, 8, 9, 17}, //47
        {0, 8, 9, 10, 17}, //48
        {1, 2, 8, 9, 17}, //49
        {1, 8, 9, 10, 18}, //50
        {1, 9, 10, 16, 17}, //51
        {1, 8, 9, 10, 16}, //52
        {0, 1, 9, 10, 17}, //53
        {2, 8, 9, 10, 17}, //54
        {1, 8, 9, 17, 18}, //55
        {0, 1, 2}, //56
        {0, 8, 16}, //57
        {0, 1, 2}, //58
        {0, 8, 16}, //59
        {0, 1, 2}, //60
        {0, 8, 16}, //61
        {0, 1, 2}, //62
        {0, 8, 16} //63
    };

    public Piece() {
        piecetype = -1;
        orientation = -1;
    }
    public Piece(int piecetype, int orientation) {
        assert (piecetype > 0 && piecetype <=8) || (orientation >= 0 && orientation < 8) : "the piecetype is wrong or orientation is wrong!";
        this.piecetype = piecetype;
        this.orientation = orientation;
    }

    private int whichpiece() {
        // determine the current piece is which piece
        if (piecetype == 1 ) {
            if (orientation == 0) {
                return 0;
            } else if (orientation == 1) {
                return 1;
            } else if (orientation == 2) {
                return 2;
            } else if (orientation == 3) {
                return 3;
            } else if (orientation == 4) {
                return 4;
            } else if (orientation == 5) {
                return 5;
            } else if (orientation == 6) {
                return 6;
            } else if (orientation == 7) {
                return 7;
            }
        } else if (piecetype == 2) {
            if (orientation == 0) {
                return 8;
            } else if (orientation == 1) {
                return 9;
            } else if (orientation == 2) {
                return 10;
            } else if (orientation == 3) {
                return 11;
            } else if (orientation == 4) {
                return 12;
            } else if (orientation == 5) {
                return 13;
            } else if (orientation == 6) {
                return 14;
            } else if (orientation == 7) {
                return 15;
            }
        } else if (piecetype == 3) {
            if (orientation == 0) {
                return 16;
            } else if (orientation == 1) {
                return 17;
            } else if (orientation == 2) {
                return 18;
            } else if (orientation == 3) {
                return 19;
            } else if (orientation == 4) {
                return 20;
            } else if (orientation == 5) {
                return 21;
            } else if (orientation == 6) {
                return 22;
            } else if (orientation == 7) {
                return 23;
            }
        } else if (piecetype == 4) {
            if (orientation == 0) {
                return 24;
            } else if (orientation == 1) {
                return 25;
            } else if (orientation == 2) {
                return 26;
            } else if (orientation == 3) {
                return 27;
            } else if (orientation == 4) {
                return 28;
            } else if (orientation == 5) {
                return 29;
            } else if (orientation == 6) {
                return 30;
            } else if (orientation == 7) {
                return 31;
            }
        } else if (piecetype == 5) {
            if (orientation == 0) {
                return 32;
            } else if (orientation == 1) {
                return 33;
            } else if (orientation == 2) {
                return 34;
            } else if (orientation == 3) {
                return 35;
            } else if (orientation == 4) {
                return 36;
            } else if (orientation == 5) {
                return 37;
            } else if (orientation == 6) {
                return 38;
            } else if (orientation == 7) {
                return 39;
            }
        } else if (piecetype == 6) {
            if (orientation == 0) {
                return 40;
            } else if (orientation == 1) {
                return 41;
            } else if (orientation == 2) {
                return 42;
            } else if (orientation == 3) {
                return 43;
            } else if (orientation == 4) {
                return 44;
            } else if (orientation == 5) {
                return 45;
            } else if (orientation == 6) {
                return 46;
            } else if (orientation == 7) {
                return 47;
            }
        } else if (piecetype == 7) {
            if (orientation == 0) {
                return 48;
            } else if (orientation == 1) {
                return 49;
            } else if (orientation == 2) {
                return 50;
            } else if (orientation == 3) {
                return 51;
            } else if (orientation == 4) {
                return 52;
            } else if (orientation == 5) {
                return 53;
            } else if (orientation == 6) {
                return 54;
            } else if (orientation == 7) {
                return 55;
            }
        } else if (piecetype == 8) {
            if (orientation == 0) {
                return 56;
            } else if (orientation == 1) {
                return 57;
            } else if (orientation == 2) {
                return 58;
            } else if (orientation == 3) {
                return 59;
            } else if (orientation == 4) {
                return 60;
            } else if (orientation == 5) {
                return 61;
            } else if (orientation == 6) {
                return 62;
            } else if (orientation == 7) {
                return 63;
            }
        }
        return -1;
    }

    public int[] getHolepos(int position) {
        // get the corresponding piece's hole positions
        int i = whichpiece();
        assert i >= 0 && i <= 63 : " the piecetype is wrong or orientation is wrong!";

        if (i >= 8 && i <= 23 || i >= 56) {
            int[] holepos = new int[1];
            if (i == 8 || i == 11 || i == 13 || i == 14) {
                holepos[0] = position+9;
                return holepos;
            } else if (i == 9 || i == 15 || i == 17 || i == 21) {
                holepos[0] = position+8;
                return holepos;
            } else if (i == 10 || i == 12 || i == 16 || i ==20) {
                holepos[0] = position+1;
                return holepos;
            } else if (i == 18 || i == 22 || i == 58 || i == 62) {
                holepos[0] = position+2;
                return holepos;
            } else if (i == 19 || i == 23 || i == 59 || i == 63) {
                holepos[0] = position+16;
                return holepos;
            } else {
                holepos[0] = position;
                return holepos;
            }

        } else if (i >= 48) {
            int[] holepos = new int[3];
            if (i == 48) {
                holepos[0] = position;
                holepos[1] = position+8;
                holepos[2] = position+17;
                return holepos;
            } else if (i == 49) {
                holepos[0] = position+1;
                holepos[1] = position+2;
                holepos[2] = position+8;
                return holepos;
            } else if (i == 50) {
                holepos[0] = position+1;
                holepos[1] = position+10;
                holepos[2] = position+18;
                return holepos;
            } else if (i == 51) {
                holepos[0] = position+10;
                holepos[1] = position+16;
                holepos[2] = position+17;
                return holepos;
            } else if (i == 52) {
                holepos[0] = position+1;
                holepos[1] = position+8;
                holepos[2] = position+16;
                return holepos;
            } else if (i == 53) {
                holepos[0] = position;
                holepos[1] = position+1;
                holepos[2] = position+10;
                return holepos;
            } else if (i == 54) {
                holepos[0] = position+2;
                holepos[1] = position+10;
                holepos[2] = position+17;
                return holepos;
            } else {
                holepos[0] = position+8;
                holepos[1] = position+17;
                holepos[2] = position+18;
                return holepos;
            }
        } else {
            int[] holepos = new int[2];

            if (i == 0 || i == 6) {
                holepos[0] = position;
                holepos[1] = position+2;
                return holepos;
            } else if (i == 1 || i == 7) {
                holepos[0] = position+1;
                holepos[1] = position+17;
                return holepos;
            } else if (i == 2 || i == 4) {
                holepos[0] = position+8;
                holepos[1] = position+10;
                return holepos;
            } else if (i == 3 || i == 5) {
                holepos[0] = position;
                holepos[1] = position+16;
                return holepos;
            }         else if (i == 24) {
                holepos[0] = position+9;
                holepos[1] = position+10;
                return holepos;
            } else if (i == 25) {
                holepos[0] = position+8;
                holepos[1] = position+16;
                return holepos;
            } else if (i == 26 || i == 35 || i == 39) {
                holepos[0] = position;
                holepos[1] = position+1;
                return holepos;
            } else if (i == 27 || i ==32 || i == 36) {
                holepos[0] = position+1;
                holepos[1] = position+9;
                return holepos;
            } else if (i == 28) {
                holepos[0] = position+1;
                holepos[1] = position+2;
                return holepos;
            } else if (i == 29) {
                holepos[0] = position+9;
                holepos[1] = position+17;
                return holepos;
            } else if (i == 30 || i == 33 || i == 37) {
                holepos[0] = position+8;
                holepos[1] = position+9;
                return holepos;
            } else if (i == 31 || i ==34 || i ==38) {
                holepos[0] = position;
                holepos[1] = position+8;
                return holepos;
            } else if (i == 40) {
                holepos[0] = position+2;
                holepos[1] = position+9;
                return holepos;
            } else if (i == 41) {
                holepos[0] = position+8;
                holepos[1] = position+17;
                return holepos;
            } else if (i == 42 || i ==47) {
                holepos[0] = position+1;
                holepos[1] = position+8;
                return holepos;
            } else if (i == 43 || i == 46) {
                holepos[0] = position;
                holepos[1] = position+9;
                return holepos;
            } else if (i == 44) {
                holepos[0] = position+1;
                holepos[1] = position+10;
                return holepos;
            } else {
                holepos[0] = position+9;
                holepos[1] = position+16;
                return holepos;
            }
        }
    }

    int getSymmetryOrientation() {
        // get the piece weak sym orientation
        if (piecetype == 2) {
            switch (orientation) {
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 6:
                    return 4;
                case 7:
                    return 5;
            }
        } else if (piecetype == 5 ) {
            switch (orientation) {
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 0;
            }
        } else if (piecetype == 6) {
            switch (orientation) {
                case 4:
                    return 2;
                case 5:
                    return 3;
                case 6:
                    return 0;
                case 7:
                    return 1;
            }
        } else if (piecetype == 3 || piecetype == 8) {
            switch (orientation) {
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 0;
                case 5:
                    return 1;
                case 6:
                    return 0;
                case 7:
                    return 1;
            }
        }
        return -1;
    }

    public int[] getOccupyPositions(int position) {
        // get the corresponding piece's occupy positions
        int i = whichpiece();
        assert i >= 0 && i <= 63 : "In getOccupyPosition: the piecetype is wrong or orientation is wrong!";

        int[] occupied = new int[occupyPositions[i].length];

        for (int x = 0; x < occupied.length; x++) {
            occupied[x] = occupyPositions[i][x] + position;
        }


        for (int o : occupied) {
            if (o == 7) {
                for (int a : occupied) {
                    if (a == 8)
                        return null;
                }
            } else if (o == 15) {
                for (int a : occupied) {
                    if (a == 16)
                        return null;
                }
            } else if (o == 23) {
                for (int a : occupied) {
                    if (a == 24)
                        return null;
                }
            }
            // if the situation above occurs, the piece is off board, so return null

            if (o > 31 || o < 0) {
                return null;
            }
        }
        return occupied;
    }


}
