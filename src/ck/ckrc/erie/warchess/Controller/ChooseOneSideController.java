package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;
import ck.ckrc.erie.warchess.ui.StartFrame;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
/**
 * 这个类用于控制多人游戏选择客户端或服务端的按钮事件
 */
public class ChooseOneSideController {

    /**
     * 从ClientIP.fxml加载的ui
     */
    public static Node node;

    /**
     * 由客户端创建的client
     */
    public static Client client;
    private Stage stage=Director.GetDirector().stage;

    /**
     * 服务端是否被占用
     */
    public static boolean isserverchoose=false;

    public static CheckConnect checkConnect;
    /**
     * 返回开始界面
     */
    @FXML
    void BackToStartFrame(MouseEvent event) throws Exception{
        Director.GetDirector().BackToStartFrame();
    }

    /**
     * 作为客户端开始
     */
    @FXML
    void StartGameAsClient(MouseEvent event) {
        Play.gamemodel=1;
        try {
            client = new Client();
            Node root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/ClientIP.fxml"));
            node = root;
            Scene scene = new Scene((Parent) root);
            stage.setScene(scene);
            Main.log.addLog("waiting",this.getClass());
        } catch (IOException|NullPointerException e) {
            Main.log.addLog("Cannot get this FX resource:"+"/Fxml/ClientIP.fxml",this.getClass());
            Main.log.addLog(e,this.getClass());
        }
    }

    /**
     * 作为服务端开始
     */
    @FXML
    void StartGameAsServer(MouseEvent event) {
        Play.gamemodel=1;
        try {
            Client client = new Client();
            client.start();
            ChooseOneSideController.client =client;
            Node root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/Waiting.fxml"));
            if(!isserverchoose) {
                Scene scene = new Scene((Parent) root);
                stage.setScene(scene);
                checkConnect = new CheckConnect();
                checkConnect.start();
                isserverchoose=true;
            }
            else{
                Label label=new Label("服务端已有waiting");
                AnchorPane pane=(AnchorPane) stage.getScene().lookup("#chooseonesidepane");
                pane.getChildren().add(label);
                label.setLayoutX(400);
                label.setLayoutY(300);
                label.setPrefSize(200, 50);
                label.setAlignment(Pos.CENTER);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> label.setText("")));
                timeline.play();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务端的等待请求由CheckConnect线程来完成
     */
    public static class CheckConnect extends Thread{
        @Override
        public void run() {
            try {
                while (Main.syncThread == null && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(10);
                }
                if(!Thread.currentThread().isInterrupted()) {
                    Main.syncThread.start();
                    for (var clazz:
                         Main.chessClassLoader.getAllClass()) {
                        try {
                            Main.syncThread.sendLoad(clazz.getName());
                        } catch (IOException e) {
                            Main.log.addLog("failed to sendLoad:"+clazz,this.getClass());
                            Main.log.addLog(e,this.getClass());
                        }
                    }
                    Play.teamflag = 0;
                    Platform.runLater(Setting::makesetting);
                }
            } catch (InterruptedException e) {
                Main.log.addLog("Client interrupted!", this.getClass());
            }
        }
    }

}

