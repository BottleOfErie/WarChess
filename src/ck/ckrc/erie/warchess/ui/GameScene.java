package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Engine;
import ck.ckrc.erie.warchess.game.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * 这个类用于绘制游戏内界面
 */
public class GameScene {

    /**
     * 棋盘画布的边长和每个小格子的边长
     */
    public static final int canvasize=600,edgelength=canvasize/ Map.MapSize;

    /**
     * 用于绘制棋子的画布
     */
    private Canvas canvas = new Canvas(canvasize,canvasize );
    private GraphicsContext graphicsContext=canvas.getGraphicsContext2D();

    /**
     * 用于绘制棋子特效层的画布
     */
    private Canvas spcanvas=new Canvas(canvasize,canvasize);
    private GraphicsContext spgraphicsContext=spcanvas.getGraphicsContext2D();

    /**
     * 所有游戏内ui控件都是这个anchorpane的子节点
     */
    public static AnchorPane anchorPane;
    public static Scene gameScene;

    /**
     * 所有游戏内ui控件都是这个anchorpane的子节点
     */
    public static Repainter repainter;
    public static Stage stage;

    /**
     * 显示聊天内容的聊天框
     */
    public static TextArea textArea;

    /**
     * 初始化游戏界面
     */
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
        setshowPlayerData();
        setQuit();
        stage.setScene(scene);
        gameScene=stage.getScene();
        Play.initchessdetailvbox();
        repainter=new Repainter();
        repainter.start();
    }

    /**
     * 绘制棋盘
     */
    private void drawchessmap(){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(5);
        for(int i = 0; i <= 10; i++){
            graphicsContext.strokeLine(i*edgelength, 0, i*edgelength, canvasize);
            graphicsContext.strokeLine(0, i*edgelength, canvasize, i*edgelength);
        }
    }

    /**
     * 创建一个按钮，能够执行进入下一轮的操作
     */
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
                if(Play.gamemodel==0) {
                    Play.teamflag = nextteam;
                }
                Play.updatechessdetails();
            }
        });
    }

    /**
     * 创建返回主界面按钮，可以直接退出当前游戏，回到主界面
     */
    private void setQuit(){
        Button button=new Button("返回主界面");
        anchorPane.getChildren().add(button);
        button.setPrefSize(100, 30);
        button.setLayoutX(10);button.setLayoutY(600);
        button.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("返回主界面");
            alert.setHeaderText(null);
            alert.setContentText("是否返回主界面?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()&&result.get() == ButtonType.OK){
                if(Play.gamemodel==1){
                    try {
                        Main.syncThread.sendDisconnect();
                    }catch (IOException e){Main.log.addLog("fail to disconnect", this.getClass());}
                }
                StartFrame.loadtoStartFrame(Director.GetDirector().stage);
            }
        });
    }

    /**
     * 多人游戏失去连接后，会跳出弹窗提示
     */
    public static void showDisconnect(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "失去连接!", ButtonType.OK);
        alert.setTitle("disconnect");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            StartFrame.loadtoStartFrame(stage);
        }
    }

    /**
     * 创建一个按钮，点击后可以显示玩家信息
     */
    private void setshowPlayerData(){
        Button button=new Button("显示玩家信息");
        anchorPane.getChildren().add(button);
        button.setPrefSize(100, 30);
        button.setLayoutX(10);button.setLayoutY(550);
        button.setOnAction(actionEvent -> {
            if(button.getText().equals("显示玩家信息")) {
                Label label = new Label("玩家信息\n" + Main.currentGameEngine.getPlayer(Main.currentGameEngine.getCurrentTeam()).toString());
                ScrollPane Pane = new ScrollPane(label);
                Pane.setPrefSize(200, 100);
                Pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,null,null)));
                Pane.setId("playerdata");
                anchorPane.getChildren().add(Pane);
                Pane.setLayoutX(800);
                Pane.setLayoutY(600);
                button.setText("隐藏玩家信息");
            }
            else{
                Node Pane=anchorPane.lookup("#playerdata");
                anchorPane.getChildren().remove(Pane);
                button.setText("显示玩家信息");
            }
        });
    }

    /**
     * 创建聊天框
     */
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
        textArea=area;
        send.setOnAction(actionEvent -> {
            showchat(field.getText(), area, Play.teamflag);
            if(Main.syncThread!=null)
                try {
                    Main.syncThread.sendChat(field.getText(), Play.teamflag);
                } catch (IOException e) {
                    Main.log.addLog("failed to send chat",GameScene.class);
                    Main.log.addLog(e,GameScene.class);
                }
            field.setText("");
        });
        field.setOnAction(send.getOnAction());
    }

    /**
     * 将要发送的聊天内容发送到聊天框
     */
    public static void showchat(String text, TextArea textArea,int from){
        if(!text.isEmpty()){
            if(!textArea.getText().isEmpty()){textArea.appendText("\n");}
            textArea.appendText("team"+from+" : "+text);
        }
    }
}
