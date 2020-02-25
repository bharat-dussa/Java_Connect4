package com.bharat.connect_4;

import com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import sun.java2d.xr.XRSurfaceData;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {


  private static final int COL=7;
  private static final int ROW=6;

  private static final int cir_diameter=80;
  private static final String discColor1="#24303E";
  private static final String getDiscColor2="#4CAA88";

  private static String playerone="Player One";
  private static String playertwo="Player Two";

  private boolean isturn=true;

  private boolean ischeck=true;

  private Disc[][] insertDiscArray=new Disc[ROW][COL];



    @FXML
    public GridPane idgrid;
    @FXML
    public VBox VBOXID;
    @FXML
    public Label players;

    @FXML
    public TextField player1id;
    @FXML
    public TextField player2id;
    @FXML
    public Button setnameid;

    @FXML
    public Pane insertdiscspane;

    //String input1=player1id.getText();
    //String input2=player2id.getText();

    public void createplay(){

        Shape rectangleHoles=createHoles();
        idgrid.add(rectangleHoles,0,1);

        List<Rectangle> rectangleList=clickable();

        for(Rectangle rectangle:rectangleList){
            idgrid.add(rectangle,0,1);


        }



    }

    public Shape createHoles(){
        Shape rectangleHoles=new Rectangle((COL+1)*cir_diameter,(ROW+1)*cir_diameter);
        for(int row=0;row<ROW;row++){

            for(int col=0;col<COL;col++){

                Circle circle=new Circle();
                circle.setRadius((cir_diameter/2));
                circle.setCenterX(cir_diameter/2);
                circle.setCenterY(cir_diameter/2);
                //cut the corresponding circle
                circle.setSmooth(true);
                circle.setTranslateX(col*(cir_diameter+5)+cir_diameter/4);
                circle.setTranslateY(row*(cir_diameter+5)+cir_diameter/4);

                rectangleHoles=Shape.subtract(rectangleHoles,circle);
            }
        }



        rectangleHoles.setFill(Color.WHITE);
        return rectangleHoles;

    }
    private List<Rectangle> clickable(){

        List<Rectangle> rectangleList =new ArrayList<>();

        for(int col=0;col<COL;col++){

             Rectangle rect =new Rectangle((cir_diameter),(ROW+1)*cir_diameter);
             rect.setFill(Color.TRANSPARENT);
             rect.setTranslateX(col*(cir_diameter+5)+cir_diameter/4);

             rect.setOnMouseEntered(event -> {
                 rect.setFill(Color.valueOf("#eeeeee26"));
             });
             rect.setOnMouseExited(event -> {rect.setFill(Color.TRANSPARENT);});

            final int column=col;
            rect.setOnMouseClicked(event -> {

                if(ischeck) {                           /*To check the disk is inserted or not and again not to insert
                                                         * duplicate disks at a time
                                                         * Again we have to set the flag to true becoz, it able to insert only one disk*/
                    ischeck=false;
                    insertDisc(new Disc(isturn), column);
                }
            });
             rectangleList.add(rect);
        }

        return rectangleList;


    }

    private void insertDisc(Disc disc,int column){          //To insert the disc


        int row=ROW-1;

        while(row>=0){

            if(DiscArray(row,column)==null) break;
            row--;
        }
        if(row<0)
            return;

       // if(isturn?playerone:playertwo)

        insertDiscArray[row][column]=disc;
        insertdiscspane.getChildren().add(disc);

        disc.setTranslateX(column*(cir_diameter+5)+cir_diameter/4);

        int currentrow=row;

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(cir_diameter+5)+cir_diameter/4);

        translateTransition.setOnFinished(event -> {
           // playerone=input1;
           // playertwo=
            ischeck=true;                               //Able to enter another disk
            if(gameEnded(currentrow,column)){
                GameOver();
                return;
            }
            isturn=!isturn;
            players.setText(isturn?playerone:playertwo);
        });

        translateTransition.play();


        if(gameEnded(row,column)){
            GameOver();
            return;

        }


    }

    private boolean gameEnded(int row,int column){

        //vertical
        List<Point2D> verticalP=IntStream.rangeClosed(row-3,row+5).mapToObj(r->new Point2D(r,column )).collect(Collectors.toList());

        //horizontal
        List<Point2D> horizontalP=IntStream.rangeClosed(column-3,column+3).mapToObj(col->new Point2D(row,col)).collect(Collectors.toList());


        Point2D Start1=new Point2D(row-3,column+3);
        List<Point2D> DiagonalP1=IntStream.rangeClosed(0,6).mapToObj(i->Start1.add(i,-i)).collect(Collectors.toList());

        Point2D Start2=new Point2D(row-3,column-3);
        List<Point2D> DiagonalP2=IntStream.rangeClosed(0,6).mapToObj(i->Start2.add(i,i)).collect(Collectors.toList());



//        List<Point2D> DiagonalP2=


        boolean isEnded= checkFor4(verticalP) || checkFor4(horizontalP) || checkFor4(DiagonalP1) || checkFor4(DiagonalP2);

        return isEnded;

    }

    private boolean checkFor4(List<Point2D> verticalP) {

        int chain=0;

        for (Point2D point :verticalP) {

            int rowIndexForArray=(int) point.getX();
            int colIndexForArray=(int) point.getY();

            Disc disc=DiscArray(rowIndexForArray,colIndexForArray);

            if(disc!=null && disc.playerOne==isturn){
                chain++;

                if(chain==4){
                   // break;
                    return true;
                }
            }else{
                chain=0;
            }
            //return false;

        }


    return false;
    }


    private Disc DiscArray(int row , int col){             ///remove ArrayOutBoundException

        if(row>=ROW || row<0 || col<0 || col>=COL){
            return null;
        }
        return insertDiscArray[row][col];


    }

    private void GameOver(){

        String winner= isturn?playerone:playertwo;
        System.out.println(winner);

        Platform.runLater(()->{



            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Winner!");
            alert.setHeaderText(playerone+"Winner Winner Chicken Dinner!");
            alert.setContentText("Wanna Play Again!");
            ButtonType yes=new ButtonType("Play!");
            ButtonType no=new ButtonType("Exit");

            alert.getButtonTypes().setAll(yes,no);
            Optional<ButtonType> OnCLick=alert.showAndWait();

            if(OnCLick.isPresent()&&OnCLick.get()==yes){

                resetGame();


            }else{

                Platform.exit();
                System.exit(0);
            }

        });

    }

    public void resetGame() {

        insertdiscspane.getChildren().clear();

        for(int i=0;i<insertDiscArray.length;i++) {

            for (int j = 0; j < insertDiscArray.length; j++) {
                insertDiscArray[i][j] = null;
            }
        }
        isturn=true;            //check the condition that it has players turn
        players.setText(playerone); //it sets the actuallly player
        createplay();              //prepare the ground to play

    }



    private class Disc extends Circle {

        private final boolean playerOne;

        public Disc(boolean playerOne){

            this.playerOne=isturn;

            setRadius(cir_diameter/2);
            setFill(isturn? Color.valueOf(discColor1):Color.valueOf(getDiscColor2));
            setCenterX(cir_diameter/2);
            setCenterY(cir_diameter/2);
        }


    }


@Override
public void initialize(URL location, ResourceBundle resources){

}
}
