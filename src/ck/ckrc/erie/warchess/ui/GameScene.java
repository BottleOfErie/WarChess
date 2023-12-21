package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Controller.ChooseOneSideController;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.PreMain;
import ck.ckrc.erie.warchess.example.GunTower;
import ck.ckrc.erie.warchess.game.ClassDecompilerWrapper;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

//TODO add exit and chat
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
    public static TextField textField;
    public static TextArea textArea;


    public void Init(Stage stage){
        GameScene.stage =stage;
        AnchorPane anchorPane=new AnchorPane(canvas,spcanvas);
        GameScene.anchorPane =anchorPane;
        Play.anchorPane=anchorPane;
        anchorPane.setPrefWidth(Director.width);
        anchorPane.setPrefHeight(Director.height);
        canvas.setLayoutX(200);canvas.setLayoutY(0);
        spcanvas.setLayoutX(200);spcanvas.setLayoutY(0);
        drawchessmap();
        Scene scene=new Scene(anchorPane,Director.width,Director.height);
        anchorPane.setLayoutX(0);anchorPane.setLayoutY(0);
        spcanvas.setOnMouseClicked(new Play(graphicsContext,spgraphicsContext).setChessAction);
        setnextround();
        setchat();
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
        button.setPrefSize(100, 30);
        button.setLayoutX(10);button.setLayoutY(500);
        button.setOnAction(actionEvent -> {
            Play.clearRightSideBar();
            if(Play.gamemodel==0 || (Play.gamemodel==1&&Play.teamflag==Main.currentGameEngine.getCurrentTeam())) {
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
    private void setchat(){
        TextArea area=new TextArea();
        area.setEditable(false);
        area.setStyle("-fx-text-alignment: right;");
        ScrollPane chatpane=new ScrollPane(area);
        chatpane.setPrefSize(180, 400);
        area.setPrefSize(180, 400);
        anchorPane.getChildren().add(chatpane);
        TextField field=new TextField();
        field.setPrefSize(130,50);
        Button send=new Button("发送");
        send.setPrefSize(40, 30);
        HBox hBox=new HBox(field,send);
        hBox.setPrefSize(150, 50);
        Label label=new Label("聊天区域");
        label.setPrefSize(150, 30);
        VBox vBox=new VBox(label,chatpane,hBox);
        vBox.setPrefSize(180, 490);
        anchorPane.getChildren().add(vBox);
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
        vBox.setLayoutX(10);vBox.setLayoutY(0);
        this.textField=field;this.textArea=area;
        send.setOnAction(actionEvent -> showchat(field, area));
        field.setOnAction(event -> showchat(field, area));
    }
    public static void showchat(TextField textField, TextArea textArea){
        String text=textField.getText();
        if(!text.equals("")){
            if(!textArea.getText().equals("")){textArea.appendText("\n");}
            textArea.appendText(text + "(来自team"+Main.currentGameEngine.getCurrentTeam()+")");
            textField.setText("");
            try {
                if (Play.gamemodel == 1) {
                    Main.syncThread.sendChat(text);
                }
            }catch (IOException e){e.printStackTrace();}
        }
    }


    public static void gameEnd(int winnerFlag){
        try {
            Parent root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/GameOver.fxml"));
            Scene scene=new Scene(root);
            Label label=(Label) root.lookup("#gameoverlabel");
            if(Play.gamemodel==0){
                label.setText("游戏结束,team"+winnerFlag+"获胜");
            } else if (Play.gamemodel==1) {
                if(winnerFlag==Main.currentGameEngine.getCurrentTeam()){
                    label.setText("游戏结束,你赢了!");
                }
                else{
                    label.setText("游戏结束,你输了!");
                }
            }
            stage.setScene(scene);
        } catch (IOException e){e.printStackTrace();}
    }
}
