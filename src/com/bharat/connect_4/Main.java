package com.bharat.connect_4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.bharat.connect_4.Controller.*;

import static javafx.application.Platform.exit;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Load the file
        FXMLLoader loader=new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane idgrid=loader.load();

        controller = loader.getController();
        controller.createplay();

        MenuBar menu=createMenu();
        menu.prefWidthProperty().bind(primaryStage.widthProperty());
        //pane
         Pane menuPane=(Pane)idgrid.getChildren().get(0);
         menuPane.getChildren().addAll(menu);
        //creates scene
         Scene scene =new Scene(idgrid);

         primaryStage.setScene(scene);
         primaryStage.setTitle("Connect Four");
         primaryStage.setResizable(false);
         primaryStage.show();

        

    }
    public MenuBar createMenu(){

        Menu filename=new Menu("File");

        //New Game
        MenuItem file=new MenuItem("New Game");
        file.setOnAction(event -> controller.resetGame());

        //Reset game
        MenuItem Reset=new MenuItem("Reset Game");
        Reset.setOnAction(event -> controller.resetGame());

        //separator
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        MenuItem Exit =new MenuItem("Exit Game");
        Exit.setOnAction(event -> exit());

        //About
        Menu Help=new Menu("Help");
        MenuItem about =new MenuItem("About Connect4");
        about.setOnAction(event -> aboutGame());
        MenuItem me=new MenuItem("About Me");
        me.setOnAction(event -> Aboutme());


        filename.getItems().addAll(file,Reset,separatorMenuItem,Exit);
        Help.getItems().addAll(about,me);


        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(filename,Help);

        return menuBar;



    }

    private void aboutGame() {

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
       // alert.setWraptext
        alert.setTitle("About the Conect4 Game");
        alert.setHeaderText("How to play?");
        alert.setContentText("Connect four is a two player game in which the\n"+
                "Players first choose a color and then take turns dropping colored discs\n"+
                "from the top into a seven-column, six-row vertically suspended grid\n"+
                "The pieces fall straight down,occupying the next \n availale space within the column\n"+
                "The objective of the game is to be the first to form a horizontal,vertical,\n"+
                "or Diagonal line of four of one's own discs. Connect Four is a solved game.\n"+
                "The First player can always win by playing the right moves\n");
        alert.show();
    }

    private void Aboutme() {


        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Bharat Dussa");
        alert.setContentText("I love to play this Game");
        alert.show();



    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
