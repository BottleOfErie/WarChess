package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Controller.ChooseOneSideController;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    private Canvas spcanvas=new Canvas(canvasize,canvasize);
    private GraphicsContext spgraphicsContext=spcanvas.getGraphicsContext2D();

    public static AnchorPane anchorPane;

    public static Scene gameScene;

    public static Repainter repainter;
    public static Stage stage;



    public void Init(Stage stage){
        GameScene.stage =stage;
        AnchorPane anchorPane=new AnchorPane(canvas,spcanvas);
        GameScene.anchorPane =anchorPane;
        Play.anchorPane=anchorPane;
        anchorPane.setPrefWidth(Director.width);
        anchorPane.setPrefHeight(Director.height);
        canvas.setLayoutX(100);canvas.setLayoutY(0);
        spcanvas.setLayoutX(100);spcanvas.setLayoutY(0);
        drawchessmap();
        Scene scene=new Scene(anchorPane,Director.width,Director.height);
        anchorPane.setLayoutX(0);anchorPane.setLayoutY(0);
        spcanvas.setOnMouseClicked(new Play(graphicsContext,spgraphicsContext).setChessAction);
        setnextround();
        stage.setScene(scene);
        gameScene=stage.getScene();
        Play.initchessdetailvbox();
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
            if(Play.gamemodel==0 || (Play.gamemodel==1&&Play.teamflag==Main.currentGameEngine.getCurrentTeam())) {
                Play.clearRightSideBar();
                int currentTeam = Main.currentGameEngine.getCurrentTeam(), nextteam;
                if (currentTeam == Engine.playerNum - 1) {
                    nextteam = 0;
                } else {
                    nextteam = currentTeam + 1;
                }
                Play.syncLastEvent();
                if (Main.syncThread != null) {
                    for (int i = 0; i < Map.MapSize; i++) {
                        for (int j = 0; j < Map.MapSize; j++) {
                            try {
                                Main.syncThread.sendSync(i, j, Main.currentGameEngine.getChess(i, j));
                            } catch (IOException e) {
                                Main.log.addLog("Failed to sync chess at:" + i + "," + j, GameScene.class);
                                Main.log.addLog(e, GameScene.class);
                            }
                        }
                    }
                    try {
                        Main.syncThread.sendRepaint();
                        Main.syncThread.sendRound(nextteam);
                    } catch (IOException e) {
                        Main.log.addLog("Failed to send repaint/round", GameScene.class);
                        Main.log.addLog(e, GameScene.class);
                    }
                }
                Main.currentGameEngine.nextRound(nextteam);
                Play.updatechessdetails();
            }
            else{

            }
        });
    }
}
