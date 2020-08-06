package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the twist game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */

/** class author: XIAOQUAN HU */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 750;
    private static final int VIEWER_HEIGHT = 500;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private Group pieces = new Group();
    TextField textField;



    class Piece extends ImageView {
        int orient;
        char type;

        Piece(char type, int x, int y, int o) {
            if (type > 'l' || type < 'a')
               throw new IllegalArgumentException("bad piece " + type);
            this.type = type;
            this.orient=o;

            setImage(new Image(Viewer.class.getResource(URI_BASE + type + ".png").toString()));
            switch (type){
                case 'a': case 'b': case 'd': case 'f':
                    setFitHeight(SQUARE_SIZE*2);
                    setFitWidth(SQUARE_SIZE*3);
                    break;
                case 'c':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE*4);
                    break;
                case 'e':
                    setFitHeight(SQUARE_SIZE*2);
                    setFitWidth(SQUARE_SIZE*2);
                    break;
                case 'g':
                    setFitHeight(SQUARE_SIZE*3);
                    setFitWidth(SQUARE_SIZE*3);
                    break;
                case 'h':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE*3);
                    break;
                case 'i': case 'j': case 'k': case 'l':
                    setFitHeight(SQUARE_SIZE);
                    setFitWidth(SQUARE_SIZE);
                    break;
            }

            if((orient/90)>3){
                setScaleY(-1.0);
            }
            setRotate(orient);
            setLayoutX(x);
            setLayoutY(y);

        }

        //push
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {
        pieces.getChildren().clear();
        int i = 0;
        while(i<placement.length()){
            char c =placement.charAt(i);
            int x= 60*((placement.charAt(i+1)-'0'));
            int y= 60*((placement.charAt(i+2)-'A'+1));
            int o= 90*(placement.charAt(i+3)-'0');
            if(c=='a'|| c=='b'||c=='d'||c=='f'){
                if(o==90||o==270||o==450||o==630){
                    x=x-30;
                    y=y+30;
                }
            }
            if(c=='c'&&(o==90||o==270||o==450||o==630)){
                    x=x-90;
                    y=y+90;

            }
            if(c=='h'&&(o==90||o==270||o==450||o==630)){
                x=x-90;
                y=y+90;


            }
            pieces.getChildren().add(new Piece(c,x,y,o));
            i = i+4;
        }


        // FIXME Task 4: implement the simple placement viewer
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        controls.getChildren().add(pieces);
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().addAll(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TwistGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);


        makeControls();


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
