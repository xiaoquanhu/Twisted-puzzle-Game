package comp1110.ass2.gui;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import comp1110.ass2.TwistGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/** class author: XIAOQUAN HU */
public class Board extends Application {
    private static final int whole_WIDTH = 920;
    private static final int whole_HEIGHT = 700;
    private static final int SQUARE_SIZE = 60;
    private static final int BOARD_X = 350;
    private static final int BOARD_Y = 345;
    private static final int BOARD_marginX = 380;
    private static final int BOARD_marginY = 380;
    private static final int BOARD_width = 535;
    private static final int BOARD_height = 295;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group board = new Group();
    private final Group background = new Group();
    private final Group controls = new Group();
    private final Group objective = new Group();
    private final Group hint = new Group();
    private final Group completion = new Group();

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* Define a drop shadow effect that we will appy to tiles */
    private static DropShadow dropShadow;

    private MediaPlayer loop;
    /* game variables */
    private boolean loopPlaying = true;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(10.0);
        dropShadow.setOffsetY(10.0);
        dropShadow.setColor(Color.color(0, 0, 0, .8));
    }


    private static String[] pieceplacement = new String[8];
    private static LinkedList<Integer> occupiedpostions = new LinkedList<>();
    private static LinkedList<String> objective_occupy = new LinkedList<>();
    private static String pegplacement;
    private static long start_time;
    private Text completion_time;
    private final Text wrongw_way = new Text("Wrong way!");
    private static int ob_number;
    private static int difficulty_value;

    // FIXME Task 7: Implement a basic playable Twist Game in JavaFX that only allows pieces to be placed in valid places

    // FIXME Task 8: Implement starting placements

    // FIXME Task 10: Implement hints

    // FIXME Task 11: Generate interesting starting placements


    class Piece extends ImageView {
        int orient;
        char type;
        int mirror = 0;

        Piece(char type) {
            if (type > 'l' || type < 'a')
                throw new IllegalArgumentException("bad piece " + type);
            this.type = type;

            setImage(new Image(Viewer.class.getResource(URI_BASE + type + ".png").toString()));
            switch (type) {
                case 'a':
                case 'b':
                case 'd':
                case 'f':
                    setFitHeight(SQUARE_SIZE * 2);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'c':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE * 4);
                    setEffect(dropShadow);
                    break;
                case 'e':
                    setFitHeight(SQUARE_SIZE * 2);
                    setFitWidth(SQUARE_SIZE * 2);
                    setEffect(dropShadow);
                    break;
                case 'g':
                    setFitHeight(SQUARE_SIZE * 3);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'h':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                    setEffect(dropShadow);
                    break;
            }
        }

        Piece(char type, int x, int y) {
            if (type > 'l' || type < 'a')
                throw new IllegalArgumentException("bad piece " + type);
            this.type = type;

            setImage(new Image(Viewer.class.getResource(URI_BASE + type + ".png").toString()));
            switch (type) {
                case 'a':
                case 'b':
                case 'd':
                case 'f':
                    setFitHeight(SQUARE_SIZE * 2);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'c':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE * 4);
                    setEffect(dropShadow);
                    break;
                case 'e':
                    setFitHeight(SQUARE_SIZE * 2);
                    setFitWidth(SQUARE_SIZE * 2);
                    setEffect(dropShadow);
                    break;
                case 'g':
                    setFitHeight(SQUARE_SIZE * 3);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'h':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE * 3);
                    setEffect(dropShadow);
                    break;
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                    setEffect(dropShadow);
                    break;
            }


            setLayoutX(BOARD_marginX + x * SQUARE_SIZE);
            setLayoutY(BOARD_marginY + y * SQUARE_SIZE);
        }
    }


    class DraggablePiece extends Piece {
        int homeX, homeY;           // the position in the window where the piece should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)

        DraggablePiece(char type) {
            super(type);
            switch (type) {
                case 'a':
                    homeX = 40;
                    setLayoutX(homeX);
                    homeY = 20;
                    setLayoutY(homeY);
                    break;
                case 'b':
                    homeX = 260;
                    setLayoutX(homeX);
                    homeY = 20;
                    setLayoutY(homeY);
                    break;
                case 'd':
                    homeX = 480;
                    setLayoutX(homeX);
                    homeY = 20;
                    setLayoutY(homeY);
                    break;
                case 'f':
                    homeX = 700;
                    setLayoutX(homeX);
                    homeY = 20;
                    setLayoutY(homeY);
                    break;
                case 'c':
                    homeX = 450;
                    setLayoutX(homeX);
                    homeY = 210;
                    setLayoutY(homeY);
                    break;
                case 'e':
                    homeX = 730;
                    setLayoutX(homeX);
                    homeY = 180;
                    setLayoutY(homeY);
                    break;
                case 'g':
                    homeX = 40;
                    setLayoutX(homeX);
                    homeY = 150;
                    setLayoutY(homeY);
                    break;
                case 'h':
                    homeX = 260;
                    setLayoutX(homeX);
                    homeY = 210;
                    setLayoutY(homeY);
                    break;
            }

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                remove_occupied();
                hideCompletion();
                rotate();
                rotatesound();
                event.consume();
            });


            setOnMouseClicked(event -> {                            // flip the piece, if mirror is odd, this piece has been flipped.
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (onBoard()) {
                        snapToHome();
                    } else {
                        mirror++;
                        if (mirror % 2 == 1) {
                            setScaleY(-1.0);
                        } else {
                            setScaleY(1.0);
                        }
                    }
                }
            });


            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                remove_occupied();
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                if (!onBoard()) {
                    snapToHome();
                }
                snapToGrid();
            });


        }

        private void remove_occupied() {
            if (onBoard()) {
                int[] remove = (new comp1110.ass2.Piece(type - 'a' + 1, get_colum_row_rotate()[2])).getOccupyPositions(get_colum_row_rotate()[0] + 8 * get_colum_row_rotate()[1]);
                for (Integer j : remove) {
                        occupiedpostions.remove(j);
                }
            }
        }

        private void snapToGrid() {
            if (onBoard()) {

                if ((type == 'a' || type == 'b' || type == 'd' || type == 'f') && (orient == 90 || orient == 270)) {

                    if (getLayoutX() > (BOARD_X - SQUARE_SIZE / 2) && getLayoutX() <= (BOARD_X + SQUARE_SIZE / 2)) {
                        setLayoutX(BOARD_X);
                    } else if (getLayoutX() > (BOARD_X + SQUARE_SIZE / 2) && getLayoutX() <= (BOARD_X + 1.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 1.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 2.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 2 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 2.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 3.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 3 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 3.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 4.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 4 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 4.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 5.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 5 * SQUARE_SIZE);
                    } else {
                        setLayoutX(BOARD_X + 6 * SQUARE_SIZE);
                    }

                    if ((getLayoutY() > (BOARD_Y + 0.5 * SQUARE_SIZE)) && (getLayoutY() < (BOARD_marginY + SQUARE_SIZE))) {
                        setLayoutY(BOARD_marginY + 0.5 * SQUARE_SIZE);
                    } else {
                        setLayoutY(BOARD_marginY + 1.5 * SQUARE_SIZE);
                    }

                } else if (type == 'c' && (orient == 90 || orient == 270)) {
                    if (getLayoutX() > (BOARD_X - 1.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X - 0.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X - SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X - 0.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 0.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X);
                    } else if (getLayoutX() > (BOARD_X + 0.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 1.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 1.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 2.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 2 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 2.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 3.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 3 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 3.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 4.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 4 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 4.5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 5.5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 5 * SQUARE_SIZE);
                    } else {
                        setLayoutX(BOARD_X + 6 * SQUARE_SIZE);
                    }

                    setLayoutY(BOARD_marginY + 1.5 * SQUARE_SIZE);

                } else if (type == 'h' && (orient == 90 || orient == 270)) {

                    if (getLayoutX() > (BOARD_X - SQUARE_SIZE) && getLayoutX() <= (BOARD_X)) {
                        setLayoutX(BOARD_X - 0.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X) && getLayoutX() <= (BOARD_X + SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 0.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 2 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 1.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 2 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 3 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 2.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 3 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 4 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 3.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 4 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 4.5 * SQUARE_SIZE);
                    } else if (getLayoutX() > (BOARD_X + 5 * SQUARE_SIZE) && getLayoutX() <= (BOARD_X + 6 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 5.5 * SQUARE_SIZE);
                    } else {
                        setLayoutX(BOARD_X + 6.5 * SQUARE_SIZE);
                    }

                    if ((getLayoutY() > (BOARD_Y + SQUARE_SIZE)) && (getLayoutY() < (BOARD_marginY + 1.5 * SQUARE_SIZE))) {
                        setLayoutY(BOARD_marginY + SQUARE_SIZE);
                    } else {
                        setLayoutY(BOARD_marginY + 2 * SQUARE_SIZE);
                    }
                } else {
                    if (getLayoutX() >= BOARD_X && getLayoutX() <= (BOARD_X + SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + SQUARE_SIZE / 2);
                    } else if ((getLayoutX() > BOARD_X + SQUARE_SIZE) && (getLayoutX() <= BOARD_X + 2 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + SQUARE_SIZE * 1.5);
                    } else if ((getLayoutX() >= BOARD_X + 2 * SQUARE_SIZE) && (getLayoutX() < BOARD_X + 3 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 2.5 * SQUARE_SIZE);
                    } else if ((getLayoutX() >= BOARD_X + 3 * SQUARE_SIZE) && (getLayoutX() < BOARD_X + 4 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 3.5 * SQUARE_SIZE);
                    } else if ((getLayoutX() >= BOARD_X + 4 * SQUARE_SIZE) && (getLayoutX() < BOARD_X + 5 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 4.5 * SQUARE_SIZE);
                    } else if ((getLayoutX() >= BOARD_X + 5 * SQUARE_SIZE) && (getLayoutX() < BOARD_X + 6 * SQUARE_SIZE)) {
                        setLayoutX(BOARD_X + 5.5 * SQUARE_SIZE);
                    } else {
                        setLayoutX(BOARD_X + 6.5 * SQUARE_SIZE);
                    }

                    if ((getLayoutY() >= BOARD_Y) && (getLayoutY() < (BOARD_marginY + (SQUARE_SIZE / 2)))) {
                        setLayoutY(BOARD_marginY);
                    } else if ((getLayoutY() >= (BOARD_marginY + (SQUARE_SIZE / 2))) && (getLayoutY() < (BOARD_marginY + 1.5 * SQUARE_SIZE))) {
                        setLayoutY(BOARD_marginY + SQUARE_SIZE);
                    } else if ((getLayoutY() >= (BOARD_marginY + 1.5 * SQUARE_SIZE)) && (getLayoutY() < (BOARD_marginY + 2.5 * SQUARE_SIZE))) {
                        setLayoutY(BOARD_marginY + 2 * SQUARE_SIZE);
                    } else {
                        setLayoutY(BOARD_marginY + 3 * SQUARE_SIZE);
                    }
                }

                if (!(onBoard()) || isOverlap() || objectivesOverlap()) {
                    snapToHome();
                } else {
                    setPosition();
                    snapgrid();
                }
            } else {
                snapToHome();
            }
            updateAndCheck();

        }


        private boolean onBoard() {
            if (type == 'a' || type == 'b' || type == 'd' || type == 'f') {
                if (orient == 0 || orient == 180) {
                    return getLayoutX() > (BOARD_X) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 3)
                            && getLayoutY() > (BOARD_Y) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 2);
                } else {
                    return getLayoutX() > (BOARD_X - SQUARE_SIZE / 2) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 2.5)
                            && getLayoutY() > (BOARD_X + SQUARE_SIZE / 2) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 2.5);
                }

            } else if (type == 'g') {
                return getLayoutX() > (BOARD_X) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 3)
                        && getLayoutY() > (BOARD_Y) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 3);
            } else if (type == 'h') {
                if (orient == 0 || orient == 180) {
                    return getLayoutX() > (BOARD_X) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 3)
                            && getLayoutY() > (BOARD_Y) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE);
                } else {
                    return getLayoutX() > (BOARD_X - SQUARE_SIZE) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 2)
                            && getLayoutY() > (BOARD_Y + SQUARE_SIZE) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 2);
                }
            } else if (type == 'c') {
                if (orient == 0 || orient == 180) {
                    return getLayoutX() > (BOARD_X) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 4)
                            && getLayoutY() > (BOARD_Y) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE);
                } else {
                    return getLayoutX() > (BOARD_X - SQUARE_SIZE * 1.5) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 2.5)
                            && getLayoutY() > (BOARD_Y + SQUARE_SIZE * 1.5) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 2.5);
                }
            } else {
                return getLayoutX() > (BOARD_X) && getLayoutX() < (BOARD_X + BOARD_width - SQUARE_SIZE * 2)
                        && getLayoutY() > (BOARD_Y) && getLayoutY() < (BOARD_Y + BOARD_height - SQUARE_SIZE * 2);
            }
        }


        private int[] get_colum_row_rotate() {
            int[] result = new int[3];
            if ((type == 'c' || type == 'h') && (orient == 90 || orient == 270)) {
                result[0] = (int) ((getLayoutX() - BOARD_X) / SQUARE_SIZE + 1);
                result[1] = (int) (getLayoutY() - BOARD_marginY) / SQUARE_SIZE - 1;
            } else {
                result[0] = (int) (getLayoutX() - BOARD_X) / SQUARE_SIZE;
                result[1] = (int) (getLayoutY() - BOARD_marginY) / SQUARE_SIZE;
            }
            if (mirror % 2 == 1) {
                result[2] = (4 + orient / 90);
            } else {
                result[2] = (orient / 90);
            }
            return result;
        }

        private boolean isOverlap() {
            int[] currentpiece = (new comp1110.ass2.Piece(type - 'a' + 1, get_colum_row_rotate()[2])).getOccupyPositions(get_colum_row_rotate()[0] + 8 * get_colum_row_rotate()[1]);

            for (int i:currentpiece) {
                for (Integer j : occupiedpostions) {
                    if (j == i) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean objectivesOverlap() {
            int[] nowpiece = (new comp1110.ass2.Piece(type - 'a' + 1, get_colum_row_rotate()[2])).getOccupyPositions(get_colum_row_rotate()[0] + 8 * get_colum_row_rotate()[1]);
            int[] nowpieceholes = (new comp1110.ass2.Piece(type - 'a' + 1, get_colum_row_rotate()[2])).getHolepos(get_colum_row_rotate()[0] + 8 * get_colum_row_rotate()[1]);

            Set<Integer> holesofthispiece = new HashSet<>();
            for (int o : nowpieceholes) {
                holesofthispiece.add(o);
            }

            boolean[] result = new boolean[nowpiece.length];
            for (boolean cof : result) {
                cof = false;
            }

            for (int i = 0; i < nowpiece.length; i++) {
                for (String j : objective_occupy) {
                    if (nowpiece[i] == Integer.parseInt(j.substring(1))) {
                        if (holesofthispiece.contains(Integer.parseInt(j.substring(1)))
                                && ((type == 'a' && j.charAt(0) == 'i') ||
                                (type == 'b' && j.charAt(0) == 'i') ||
                                (type == 'c' && j.charAt(0) == 'j') ||
                                (type == 'd' && j.charAt(0) == 'j') ||
                                (type == 'e' && j.charAt(0) == 'k') ||
                                (type == 'f' && j.charAt(0) == 'k') ||
                                (type == 'g' && j.charAt(0) == 'l') ||
                                (type == 'h' && j.charAt(0) == 'l'))) {
                            continue;
                        } else {
                            result[i] = true;
                            break;
                        }
                    }
                }
            }

            for (boolean b : result) {
                if (b) {
                    return true;
                }
            }
            return false;
        }


        private void snapToHome() {
            flyby();
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            setPosition();
        }


        private void rotate() {
            this.orient = (int) (getRotate() + 90) % 360;
            setRotate(orient);
            if (getLayoutY() != homeY) {
                snapToGrid();
            }
        }

        private void rotatesound() {
            Media rs = new Media(Viewer.class.getResource(URI_BASE) + "rotate.mp3");
            MediaPlayer rosound = new MediaPlayer(rs);
            rosound.setVolume(5.0);
            rosound.play();
        }

        private void snapgrid() {
            Media sg = new Media(Viewer.class.getResource(URI_BASE) + "snaptogrid.mp3");
            MediaPlayer snap = new MediaPlayer(sg);
            snap.setVolume(6.0);
            snap.play();
        }

        private void flyby() {
            Media fb = new Media(Viewer.class.getResource(URI_BASE) + "flyby.mp3");
            MediaPlayer fly = new MediaPlayer(fb);
            fly.setVolume(9.0);
            fly.play();
        }


        /**
         * Determine the grid-position of the origin of the tile
         * or "" if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            int x;
            char y;
            int orienttype;
            String val;

            if (!onBoard()) {
                pieceplacement[type - 'a'] = "";
            } else {
                if ((type == 'c' || type == 'h') && (orient == 90 || orient == 270)) {
                    x = (int) ((getLayoutX() - BOARD_X) / SQUARE_SIZE + 2);
                    y = (char) ('A' + (int) ((getLayoutY() - BOARD_marginY) / SQUARE_SIZE - 1));
                } else {
                    x = (int) ((getLayoutX() - BOARD_X) / SQUARE_SIZE + 1);
                    y = (char) ('A' + (int) ((getLayoutY() - BOARD_marginY) / SQUARE_SIZE));
                }
                if (mirror % 2 == 1) {
                    orienttype = (4 + orient / 90);
                    val = "" + type + x + y + orienttype;
                    pieceplacement[type - 'a'] = val;
                } else {
                    orienttype = (orient / 90);
                    val = "" + type + x + y + orienttype;
                    pieceplacement[type - 'a'] = val;
                }

                int[] currentpiece = (new comp1110.ass2.Piece(type - 'a' + 1, orienttype)).getOccupyPositions(x - 1 + 8 * (y - 'A'));
                for (int i:currentpiece) {
                    occupiedpostions.add(i);
                }
            }
        }


    }

    /**
     * Turn the array of palcement strings into one single string indicating the current placement
     *
     * param "String[]" The array of string segmentations of current placement.
     */
    private String array_of_placement_to_string(String[] a) {
        String result = "";
        for (int i = 0; i < 8; i++) {
            result = result + a[i];
        }
        return result;
    }

    /**
     * Set up initial start window when entering the game
     */
    private void Intro() {
        ImageView Intro1 = new ImageView();
        Intro1.setImage(new Image(Board.class.getResource(URI_BASE + "background1.jpg").toString()));
        Intro1.setFitWidth(whole_WIDTH);
        Intro1.setFitHeight(whole_HEIGHT);

        ImageView words = new ImageView();
        words.setImage(new Image(Board.class.getResource(URI_BASE + "Intro_words.png").toString()));
        words.setFitWidth(500);
        words.setFitHeight(260);
        words.setLayoutX(BOARD_width / 2);
        words.setLayoutY(BOARD_height / 2 - 50);


        ImageView button1 = new ImageView();
        button1.setImage(new Image(Board.class.getResource(URI_BASE + "Intro_button_start.png").toString()));
        button1.setFitWidth(120);
        button1.setFitHeight(45);
        button1.setLayoutX(BOARD_width / 2 + 30);
        button1.setLayoutY(BOARD_height / 2 + 280);

        ImageView button2 = new ImageView();
        button2.setImage(new Image(Board.class.getResource(URI_BASE + "Intro_button_help.png").toString()));
        button2.setFitWidth(120);
        button2.setFitHeight(45);
        button2.setLayoutX(BOARD_width / 2 + 260);
        button2.setLayoutY(BOARD_height / 2 + 280);

        Button help = new Button("Help");//create BACK button direct to the open scene page
        help.setPrefSize(120, 45);
        help.setLayoutX(BOARD_width / 2 + 260);
        help.setLayoutY(BOARD_height / 2 + 280);
        help.setOpacity(0.0001);

        Button start = new Button("Start");//create NEXT button direct to the next introduction page
        start.setPrefSize(120, 45);
        start.setLayoutX(BOARD_width / 2 + 30);
        start.setLayoutY(BOARD_height / 2 + 280);
        start.setOpacity(0.0001);

        root.getChildren().addAll(Intro1, words, button1, button2, help, start);

        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                click();
                startgame();
            }
        });

        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                click();
                Help();
            }
        });

    }

    /**
     * Set up help window when initializing the game
     */
    private void Help() {
        ImageView help_window = new ImageView();
        help_window.setImage(new Image(Board.class.getResource(URI_BASE + "background3.png").toString()));
        help_window.setFitWidth(whole_WIDTH);
        help_window.setFitHeight(whole_HEIGHT);


        ImageView button1 = new ImageView();
        button1.setImage(new Image(Board.class.getResource(URI_BASE + "help_back.png").toString()));
        button1.setFitWidth(120);
        button1.setFitHeight(45);
        button1.setLayoutX(BOARD_width / 2);
        button1.setLayoutY(BOARD_height / 2 + 440);

        ImageView button2 = new ImageView();
        button2.setImage(new Image(Board.class.getResource(URI_BASE + "help_next.png").toString()));
        button2.setFitWidth(120);
        button2.setFitHeight(45);
        button2.setLayoutX(BOARD_width / 2 + 290);
        button2.setLayoutY(BOARD_height / 2 + 440);

        Button back = new Button("back");//create BACK button direct to the open scene page
        back.setPrefSize(120, 45);
        back.setLayoutX(BOARD_width / 2);
        back.setLayoutY(BOARD_height / 2 + 440);
        back.setOpacity(0.0001);

        Button next = new Button("next");//create NEXT button direct to the next introduction page
        next.setPrefSize(120, 45);
        next.setLayoutX(BOARD_width / 2 + 290);
        next.setLayoutY(BOARD_height / 2 + 440);
        next.setOpacity(0.0001);

        root.getChildren().addAll(help_window, button1, button2, back, next);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                click();
                Intro();
            }
        });

        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                click();
                startgame();
            }
        });

    }

    /**
     * Set up event sound for the click
     */
    private void click() {
        Media click = new Media(Viewer.class.getResource(URI_BASE) + "click.mp3");
        MediaPlayer clickSound = new MediaPlayer(click);
        clickSound.setVolume(5.0);
        clickSound.setRate(0.6);
        clickSound.play();
    }

    /**
     * Set up event handlers for the main game
     *
     * @param scene The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                hint_fromstring();
                hint.setOpacity(1.0);
                pieces.setOpacity(0);
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                hint.setOpacity(0);
                pieces.setOpacity(1.0);
                event.consume();
            }
        });
    }

    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        Media music = new Media(Board.class.getResource(URI_BASE) + "heispirate.mp3");
        loop = new MediaPlayer(music);
        loop.setCycleCount(MediaPlayer.INDEFINITE);
        loop.setVolume(0.3);
        loop.play();
    }

    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }

    /**
     * Set up the group that represents the Hint (and make it transparent)
     */
    private void hint_fromstring(){
        hint.getChildren().clear();
        hide_wrong_way();
        String solution = objt[difficulty_value-1][ob_number][1];
        for(String t:pieceplacement){
            if(!solution.contains(t)){
                makewrongway();
                show_wrong_way();
                return;
            }
        }
        for(int j=0;j<solution.length()/4;j++){
            String viable=solution.substring(j*4,j*4+4);
            if( !array_of_placement_to_string(pieceplacement).contains(viable)){
                Piece h = new Piece(viable.charAt(0), (Character.getNumericValue(viable.charAt(1))) - 1, viable.charAt(2) - 'A');
                h.setRotate((Character.getNumericValue(viable.charAt(3)) % 4) * 90);
                if (Character.getNumericValue(viable.charAt(3)) > 3) {
                    h.setScaleY(-1.0);
                }
                if ((viable.charAt(0) == 'a' || viable.charAt(0) == 'b' || viable.charAt(0) == 'd' || viable.charAt(0) == 'f') && (viable.charAt(3) == '1' || viable.charAt(3) == '3' || viable.charAt(3) == '5' || viable.charAt(3) == '7')) {
                    h.setLayoutX(h.getLayoutX() - 30);
                    h.setLayoutY(h.getLayoutY() + 30);
                }
                if ((viable.charAt(0) == 'c') && (viable.charAt(3) == '1' || viable.charAt(3) == '3' || viable.charAt(3) == '5' || viable.charAt(3) == '7')) {
                    h.setLayoutX(h.getLayoutX() - 90);
                    h.setLayoutY(h.getLayoutY() + 90);
                }
                if ((viable.charAt(0) == 'h') && (viable.charAt(3) == '1' || viable.charAt(3) == '3' || viable.charAt(3) == '5' || viable.charAt(3) == '7')) {

                    h.setLayoutX(h.getLayoutX() - 60);
                    h.setLayoutY(h.getLayoutY() + 60);
                }

                hint.getChildren().add(h);
                break;
            }
        }
        hint.setOpacity(0);
    }


    /**
     * Set up the group that represents the Board
     */
    private void makeBoard() {
        board.getChildren().clear();
        ImageView baseboard = new ImageView();
        baseboard.setImage(new Image(Board.class.getResource(URI_BASE + "board.png").toString()));
        baseboard.setFitWidth(BOARD_width);
        baseboard.setFitHeight(BOARD_height);
        baseboard.setLayoutX(BOARD_X);
        baseboard.setLayoutY(BOARD_Y);
        baseboard.setEffect(dropShadow);
        board.getChildren().add(baseboard);
        board.toBack();
    }

    /**
     * Set up each of the eight pieces
     */
    private void makePieces() {
        pieces.getChildren().clear();
        for (char m = 'a'; m <= 'h'; m++) {
            pieces.getChildren().add(new DraggablePiece(m));
        }
    }

    private void random_objective(){
        difficulty_value = (int) difficulty.getValue();
        Random rand = new Random();
        ob_number = rand.nextInt(objt[difficulty_value - 1].length);
    }

    /**
     * Add the objective to the board
     */
    private void addObjectiveToBoard() {
        objective.getChildren().clear();
        random_objective();
        String ob = objt[difficulty_value-1][ob_number][0];
        pegplacement = ob;
        for (int i = 0; i < ob.length() / 4; i++) {
            String location = "" + ((Character.getNumericValue(ob.charAt(4 * i + 1)) - 1) + 8 * (ob.charAt(4 * i + 2) - 'A'));
            objective_occupy.add(ob.charAt(i * 4) + location);
            objective.getChildren().add(new Piece(ob.charAt(4 * i), Character.getNumericValue(ob.charAt(4 * i + 1)) - 1, ob.charAt(4 * i + 2) - 'A'));
        }
    }

    /**
     * Check game completion and update status
     */
    private void updateAndCheck() {
        String state = array_of_placement_to_string(pieceplacement);
        if (state.length() == 32) {
            showCompletion();
        }
    }

    /**
     * Put all of the pieces back in their home position
     */
    private void resetPieces() {
        pieces.toFront();
        for (javafx.scene.Node n : pieces.getChildren()) {
            ((DraggablePiece) n).snapToHome();
            occupiedpostions = new LinkedList<>();
            for (int i = 0; i < 8; i++) {
                pieceplacement[i] = "";
            }
        }
        start_time = System.nanoTime();
    }


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {

        Button button = new Button("anewgame");
        button.setPrefSize(120, 45);
        button.setLayoutX(110);
        button.setLayoutY(BOARD_Y + 45);
        button.setOpacity(0.0001);

        ImageView restart = new ImageView();
        restart.setImage(new Image(Board.class.getResource(URI_BASE + "newgame.png").toString()));
        restart.setFitWidth(120);
        restart.setFitHeight(45);
        restart.setLayoutX(110);
        restart.setLayoutY(BOARD_Y + 45);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                objective_occupy = new LinkedList<String>();
                hideCompletion();
                newGame();
            }
        });


        Button buttonclear = new Button("clear");
        buttonclear.setPrefSize(120, 45);
        buttonclear.setLayoutX(110);
        buttonclear.setLayoutY(BOARD_Y + 220);
        buttonclear.setOpacity(0.0001);

        ImageView clear = new ImageView();
        clear.setImage(new Image(Board.class.getResource(URI_BASE + "restart.png").toString()));
        clear.setFitWidth(120);
        clear.setFitHeight(45);
        clear.setLayoutX(110);
        clear.setLayoutY(BOARD_Y + 220);

        buttonclear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hideCompletion();
                resetPieces();
            }
        });
        controls.getChildren().addAll(restart, button, clear, buttonclear);

        difficulty.setMin(1);
        difficulty.setMax(3);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(0);
        difficulty.setSnapToTicks(true);


        difficulty.setLayoutX(100);
        difficulty.setLayoutY(500);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.PURPLE);
        difficultyCaption.setLayoutX(140);
        difficultyCaption.setLayoutY(470);
        controls.getChildren().add(difficultyCaption);
    }

    /**
     * Create the window to be displayed when the player completes the puzzle.
     * Count the time spent on this game.
     */
    private void makeCompletion() {
        ImageView com = new ImageView();
        com.setImage(new Image(Board.class.getResource(URI_BASE + "win.png").toString()));
        com.setFitHeight(whole_HEIGHT);
        com.setFitWidth(whole_WIDTH);

        Button back = new Button("back");
        back.setPrefSize(120, 45);
        back.setLayoutX(whole_WIDTH / 2 - 180);
        back.setLayoutY(whole_HEIGHT / 2 + 180);
        back.setOpacity(0.0001);

        ImageView backgame = new ImageView();
        backgame.setImage(new Image(Board.class.getResource(URI_BASE + "backgame.png").toString()));
        backgame.setFitWidth(120);
        backgame.setFitHeight(45);
        backgame.setLayoutX(whole_WIDTH / 2 - 180);
        backgame.setLayoutY(whole_HEIGHT / 2 + 180);

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                objective_occupy = new LinkedList<String>();
                completion.getChildren().remove(completion_time);
                hideCompletion();
                newGame();
            }
        });

        Button exit = new Button("back");
        exit.setPrefSize(120, 45);
        exit.setLayoutX(whole_WIDTH / 2 + 60);
        exit.setLayoutY(whole_HEIGHT / 2 + 180);
        exit.setOpacity(0.0001);

        ImageView exitgame = new ImageView();
        exitgame.setImage(new Image(Board.class.getResource(URI_BASE + "exitgame.png").toString()));
        exitgame.setFitWidth(120);
        exitgame.setFitHeight(45);
        exitgame.setLayoutX(whole_WIDTH / 2 + 60);
        exitgame.setLayoutY(whole_HEIGHT / 2 + 180);

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });


        completion.getChildren().addAll(com, backgame, back, exitgame, exit);
    }

    private void caculate_completion_time() {
        String cost_time = String.format("%.2f", (System.nanoTime() - start_time) / 60000000000.0);
        completion_time = new Text("You finished in " + cost_time + " minutes!");
        completion_time.setFill(Color.PURPLE);
        completion_time.setCache(true);
        completion_time.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 50));
        completion_time.setLayoutX(whole_WIDTH / 2 - 340);
        completion_time.setLayoutY(whole_HEIGHT / 2 - 10);
        completion_time.setTextAlignment(TextAlignment.CENTER);

        completion.getChildren().add(completion_time);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        caculate_completion_time();
        completion.toFront();
        completion.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completion.toBack();
        completion.setOpacity(0);
    }

    /**
     * Set up the wrong way message
     */
    private void makewrongway() {
        wrongw_way.setFill(Color.PURPLE);
        wrongw_way.setCache(true);
        wrongw_way.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 90));
        wrongw_way.setLayoutX(whole_WIDTH / 2-110);
        wrongw_way.setLayoutY(whole_HEIGHT / 2+150);
        wrongw_way.setTextAlignment(TextAlignment.CENTER);
    }

    private void show_wrong_way() {
        hint.getChildren().add(wrongw_way);
        wrongw_way.toFront();
    }

    private void hide_wrong_way() {
        hint.getChildren().remove(wrongw_way);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            makePieces();
            addObjectiveToBoard();
            start_time = System.nanoTime();
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
    }

    /**
     * Set up the background for the main game.
     */
    private void makebackground() {
        ImageView bgd = new ImageView();
        bgd.setImage(new Image(Board.class.getResource(URI_BASE + "background2.jpg").toString()));
        bgd.setFitWidth(whole_WIDTH);
        bgd.setFitHeight(whole_HEIGHT);
        bgd.setOpacity(0.6);
        background.getChildren().add(bgd);
        background.toBack();

    }

    /**
     * Start the main game.
     */
    private void startgame() {
        root.getChildren().add(background);
        root.getChildren().add(pieces);
        root.getChildren().add(board);
        root.getChildren().add(controls);
        root.getChildren().add(objective);
        root.getChildren().add(hint);
        root.getChildren().add(completion);

        makeBoard();
        makeControls();
        makeCompletion();
        makebackground();
        newGame();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Twist Game");
        Scene scene = new Scene(root, whole_WIDTH, whole_HEIGHT);
        setUpSoundLoop();
        Intro();
        setUpHandlers(scene);

        Image image = new Image(Viewer.class.getResource(URI_BASE) + "cursor.png");
        scene.setCursor(new ImageCursor(image));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static final String[][][] objt = {
            {  {"i4D0j6A0j8B0k6C0k1D0l3A0", "a1A3b3C6c8A1d4A4e1C1f5C2g2A1h7A1"}, {"i2D0j6A0j7D0k4C0k3D0l7B0", "a1B6b1C6c5A0d7B1e4A2f3C2g5B1h1A0"} ,{"i1A0j3D0j6D0k5A0k3B0l1C0", "a1A6b6A4c2D0d5C0e5A3f3A1g1B4h8B1"} ,{"i6A0j2C0j3C0k2B0k7B0l2A0", "a6A0b5A5c5D0d1C4e7B2f1A3g3B5h2A0"}, {"i4A0j2A0j6D0k8A0k1D0l3B0", "a6B2b3A2c5D0d1A3e7A4f1C2g3B0h5A0"} ,{"i6D0j7A0j1D0k2A0k4C0", "a6C4b6B4c5A2d1B1e1A3f3C2g4A0h3A1"}, {"i2D0j5B0j1C0k4A0k3B0", "a2B5b7A1c1A3d5B7e7C1f2A0g3B7h5A0"},  {"i5B0j1B0j8C0k6B0k6D0", "a1C2b4A5c8A3d1A6e5C1f5A0g2B2h7B1"}, {"i8C0j5A0j2B0k8B0k2C0", "a4B0b7B5c3A2d1A3e7A0f1C2g3B0h5D0"}, {"i4C0j2A0j7B0k1B0k8C0", "a1C2b3C2c1A0d6A6e7B1f1B6g4A7h6D0"}, {"i4B0j2B0j3C0k4A0k5B0", "a4B5b5C0c3A3d1A3e1C1f4A6g6A1h8B1"}, {"i5C0j8A0j8B0k6B0k3C0l1A0l3B0", "a6C4b4B5c3A0d7A3e5B0f2C2g1A5h1B1"}, {"i4C0j1C0j7C0k2A0k6A0l6C0l6D0", "a2B5b3C2c1A3d7A1e6A3f2A6g4A7h6D0"}, {"i6B0j5A0j5B0k7C0k8C0l3D0l4D0", "a1B5b5B4c5D0d4A3e7B1f1A0g2B7h6A0"}, {"i6A0j1B0j2B0k8A0k7D0l1A0l5D0", "a6A3b3A4c1D0d1B2e7C2f7A7g4B7h1A0"},{"i6B0j8C0j3D0k2A0k3B0l1D0", "a6A0b5B4c2D0d6C4e1A3f3B6g1B4h3A0"}, {"i5D0j6A0j7C0k8B0k3D0l6B0", "a1A3b4C6c4A2d6C4e7A1f1C4g2A7h4B0"}, {"i5C0j1C0j7C0k4B0k6B0l3D0", "a2A3b4C4c1A3d7A1e4A2f5A0g2B3h6D0"}, {"i3D0j7A0j6B0k3A0k6D0l5B0", "a1C2b1A5c5A2d6B7e2A0f4C4g3A3h8B1"}, {"i8B0j3A0j5B0k2B0k4D0l4B0", "a6C4b7A3c1A2d5A6e2B3f2C4g4B0h1B1"},{"i5A0j2B0j8C0k4A0k2D0", "a5A0b6B5c8A3d1B3e3A4f2C2g4B7h1A0"},{"i6B0j5B0j5D0k8B0k4C0", "a1B5b5B4c4D0d3A0e3C3f7B7g1A5h6A0"}},
            {  {"i4A0j7B0j3D0k3C0k7C0l8B0l6D0", "a1A6b3A4c1D2d6A6e6C0f1B4g4B7h8B1"}, {"i2A0j8A0j1B0k2D0k8D0", "a2A3b3A4c1A1d7A3e7C1f2C2g4B7h6A1"}, {"i8C0j3A0j4D0k2B0k6D0", "a7A7b2B4c1A2d4B1e1B3f6C2g5A5h1D0"}, {"i6A0j1C0j3C0k7A0k8A0", "a4A0b2A0c1A3d2B5e7A3f7B1g4B4h5D0"}, {"i7B0j5B0j2D0k1B0k1C0", "a6C4b7A1c1D0d5A6e1B2f4C0g2A6h1A0"} ,{"i2A0j5B0j3D0k6D0k7D0", "a1B2b1A2c1D2d3A0e6C1f6A0g4B6h8B1"}, {"i6C0j5B0k7B0k8B0" ,"a5A1b2C4c4D0d3A0e7A6f7B7g1A5h1B1"}, {"j3C0k5A0k7C0l4B0", "a1A3b5B4c4D0d1C4e4A0f7B1g2A2h6A0"}, {"i4C0k2B0l4A0l6B0", "a2C0b2A0c8A1d5C4e6A0f1A3g4A5h1D0"}, {"j6A0j6D0k3A0l3D0", "a1B5b7A1c4A2d4C0e7C1f1A0g2B7h4B0"}, {"i6C0j5A0j2D0k7B0", "a3C4b5B0c4A0d1C6e1A3f7A1g2A2h6D0"}, {"i6D0j6B0k3D0l3B0", "a6A0b5C6c4B2d1A3e7C1f1C4g3B0h3A0"}, {"i5A0k2C0k8B0l5C0", "a3A0b1A0c6A1d7B5e7A0f1B3g3B2h2D0"}, {"j4B0k8B0k5D0l3C0", "a1A3b5A4c5C0d3A6e7A1f3C4g1B3h6D0"}, {"i3D0j5A0l7B0l5C0", "a1C2b1A5c4D0d4A4e7A0f2A0g6B2h3C2"}, {"j3A0j5B0k1B0k3C0", "a6C4b7A1c1A2d5A6e2B0f1B3g4B0h2D0"}, {"k6A0k8B0l4B0l6C0", "a1A6b6A4c4D0d1C2e5A3f7B7g2A6h4C2"}, {"i3A0k8C0l3C0l5C0", "a1A3b2A4c6A1d7A3e7C4f1C2g3B7h5A3"}, {"j5D0j7B0k3C0l4B0", "a1C2b1A5c4D0d6A6e3B2f7B1g4A7h2A0"}, {"i3B0j4C0k7D0l5A0", "a1B6b1C4c1A0d3C4e6C1f6A0g4A2h8B1"}, {"i7C0j5B0k5D0l3C0", "a1A3b7B7c1D0d4A0e7A0f5C2g2A4h3C0"}, {"i3D0j5D0k3A0l5A0", "a1B5b2C0c5B0d5C6e7C1f1A0g3A1h6A0"}, {"i4A0j4C0k7C0l6A0l5B0", "a1C2b3A4c2C2d1A6e7C2f7A1g5A1h4D0"}, {"i5B0k6A0k2C0l3A0l3D0", "a6B4b4A0c5D0d1A3e1C4f6A6g3B3h3A1"}, {"j7A0j6C0k4C0k7D0l2B0", "a1A6b3A4c6A3d7A7e7C2f4B1g1B4h2D0"}, {"j3B0j2D0k5B0l6D0l7D0", "a6A0b7B5c1B2d1C6e4A0f3C2g5B7h1A0"}, {"j3C0j3D0k6D0l5A0l8A0" ,"a7B7b4C4c1A1d2B5e6C1f2A0g4A4h6A2"}, {"j6C0j3D0k1A0k8C0l4A0", "a3A5b1B1c2D0d6C2e1A3f7A1g4A0h5A0"}, {"i1C0k5C0k4D0l8A0l4C0","a1A6b1B1c5B0d6C0e4A3f4C2g2B3h6A2"}, {"j3A0j2B0k6A0l5C0l7D0", "a1C4b7B5c2A0d1A5e7A0f5A7g3B5h5D2"}, {"i5D0j2B0j2C0k6A0l5B0", "a7A7b4C0c1A0d1B3e5A0f2C2g6B7h3B2"}, {"i7B0j3D0j7D0k3A0l6A0" ,"a1C6b6A6c2D0d7B1e1A3f2A2g4B2h4A2"}, {"i3B0j3D0j8B0k4A0k2C0" ,"a6C4b2A0c1D2d7A3e2B2f4A6g4B6h1A1"}, {"i2B0j4A0j7B0k3C0k3D0", "a4B3b1A0c3A0d7A7e2C4f1B3g5B4h6D0"}, {"i3C0j3A0j5B0k8A0k1D0", "a7B1b3B1c2A0d5B2e1C2f6A0g1A0h4D0"}, {"j4A0j4D0k1D0k7B0l6B0", "a7B1b5C4c2A2d3C6e1C2f6A0g1A0h4B2"}, {"i6A0j5C0k3A0k4B0l3D0", "a1B5b5A4c3C2d6C4e7A1f3A6g1A5h3D0"}, {"i6C0j5B0k7C0k8D0l3D0", "a7A7b5B6c1A1d4A6e2A3f6C4g2B4h3D0"}, {"i5B0k2B0k8C0l3C0l5D0", "a1B5b4B4c4A0d6C2e1A0f7A1g2A3h3D2"}, {"j2B0k7C0k7D0l1A0l6B0", "a7A7b3C0c1B0d1C2e7C2f4A0g5B1h1A0"}  },
            {  {"j3B0j5C0", "a7B1b2C4c1B2d4C4e1C3f4A0g6A1h1A0"}, {"j2C0j8D0", "a6A0b2A4c1C0d7B5e1A3f4C2g4A2h1D0"}, {"i2D0l2C0", "a2C4b5C4c1A1d6A0e7C1f3A3g4A5h2A3"}, {"j1A0j6B0", "a1C2b5C4c5B0d1A7e7C1f3A0g3B0h6A0"}, {"i7D0j7C0", "a5C2b3B5c1A0d7B7e1B3f1C2g4A2h6A0"}, {"i2B0l2C0", "a1B5b1A0c3D0d6C0e7A0f3A0g5A4h2C0"}, {"k5D0l4D0l6D0", "a1B5b3A0c5A0d6B0e1A0f4C0g2B7h6D0"}, {"j4C0l2D0l5B0", "a1A3b6A4c3A0d3C4e6C2f7B1g1B3h3B2"}, {"i4B0k6D0l7C0", "a7B1b3A6c1C0d1A6e7A3f4C4g5A7h1D0"}, {"i2D0j4C0k5D0", "a1B1b7B5c2A0d3B5e7A0f5C2g4A6h1A1"}, {"i5B0l1A0l7D0", "a1C2b4B2c2A0d6A6e3C0f7B1g1A0h5D2"}, {"j4C0k5A0l6B0", "a1B5b1A0c2C2d6A0e7C1f3A0g5B5h3D0"},{"i4C0j5B0j7B0", "a1A3b3C4c3B2d6A0e7C0f5C2g1B3h3A0"}, {"j7B0j4C0k2B0", "a1B5b3A4c2C2d6A0e7C1f1A0g5B5h3D0"}, {"i6C0k5C0k8B0", "a5A7b3A0c1A1d7B5e7A0f4C2g2B3h2A1"}, {"k3D0k5D0l6C0", "a7A7b3B5c3A0d1A3e5C2f1C4g6B7h4B0"}, {"j5B0k4C0k6A0", "a1B5b1A0c8A1d3A0e6A7f2C0g5B2h4D0"},{"j4C0k5D0k8C0", "a1A6b1B1c5A0d2C4e7C0f5C2g3A2h6B0"}, {"k4A0l2C0l7B0", "a1B5b5A4c8A1d4C0e4A3f1A0g2B7h7B1"},{"i7A0k1B0k8C0", "a5A6b2C4c1A0d4C4e7C4f1B3g6A6h2B0"}, {"j5C0k6C0l2C0", "a1B5b5A4c8A1d4C2e1A0f6B1g2B1h3A0"},{"i5D0k6B0l4B0", "a3C2b7A1c1A0d1B5e7C1f5A0g4B2h2B2"},{"i5A0j3C0l6C0", "a5A0b3A0c1A1d2C2e2A2f6C2g4B6h8A1"}, {"j5C0j5D0k5B0", "a1A6b7A1c4A0d5B1e7C1f3B0g1B4h2D0"},{"i6D0j5C0k5A0", "a6C4b1A0c3C2d1B5e7A0f3A0g5A2h3D0"}, {"i8D0j7D0l2D0", "a7B7b4A5c5A0d6B5e2A0f3C2g1B3h1A1"}, {"k4B0k8B0l7A0", "a1B5b1A0c2C0d6C4e7A1f3A0g5A1h3D0"}, {"i3C0k2C0l4C0", "a3A3b5C0c2D0d7B7e1A0f1B3g4A4h6A0"} }
    };
}
