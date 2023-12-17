package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Controller.ChooseOneSideController;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class GameScene {
    public static final int canvasize=600,edgelength=canvasize/ Map.MapSize;
    private Canvas canvas = new Canvas(canvasize,canvasize );
    private GraphicsContext graphicsContext=canvas.getGraphicsContext2D();

    public static AnchorPane anchorPane;

    public static Scene gameScene;

    public static Repainter repainter;
    public static Stage stage;



    public void Init(Stage stage){
        this.stage=stage;
        AnchorPane anchorPane=new AnchorPane(canvas);
        this.anchorPane=anchorPane;
        Play.anchorPane=anchorPane;
        anchorPane.setPrefWidth(Director.width);
        anchorPane.setPrefHeight(Director.height);
        canvas.setLayoutX(100);
        canvas.setLayoutY(0);
        drawchessmap();
        Scene scene=new Scene(anchorPane,Director.width,Director.height);
        anchorPane.setLayoutX(0);anchorPane.setLayoutY(0);
        canvas.setOnMouseClicked(new Play(graphicsContext).setChessAction);
        setnextround();
        stage.setScene(scene);
        this.gameScene=stage.getScene();
        repainter=new Repainter();
        repainter.start();
    }
    private void drawchessmap(){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        for(int i = 0; i <= 10; i++){
            graphicsContext.strokeLine(i*edgelength, 0, i*edgelength, canvasize);
            graphicsContext.strokeLine(0, i*edgelength, canvasize, i*edgelength);
        }
    }
    private void setnextround(){
        Button button=new Button("nextround");
        anchorPane.getChildren().add(button);
        button.setPrefSize(75, 30);
        button.setLayoutX(10);button.setLayoutY(500);
        button.setOnAction(actionEvent -> {
            int currentTeam=Main.currentGameEngine.getCurrentTeam(),nextteam;
            if(currentTeam== Engine.playerNum-1){nextteam=0;}
            else{nextteam=currentTeam+1;}
            Main.currentGameEngine.nextRound(nextteam);
            Play.updatechessdetails();
        });
    }
}
