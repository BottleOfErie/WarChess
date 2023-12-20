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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class ChooseOneSideController {

    @FXML
    private Button BackToStartButton;

    @FXML
    private Button ClientButton;

    @FXML
    private Button ServerButton;

    public static Node node;

    public static Client client;
    private Stage stage=Director.GetDirector().stage;


    public static boolean isserverchoose=true;

    public static CheckConnect checkConnect;

    @FXML
    void BackToStartFrame(MouseEvent event) throws Exception{
        Director.GetDirector().BackToStartFrame();
    }

    @FXML
    void StartGameAsClient(MouseEvent event) {
        Play.gamemodel=1;
        try {
            Client client = new Client();
            this.client = client;
            Node root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/ClientIP.fxml"));
            this.node = root;
            Scene scene = new Scene((Parent) root);
            stage.setScene(scene);
            Main.log.addLog("waiting",this.getClass());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void StartGameAsServer(MouseEvent event) {
        Play.gamemodel=1;
        try {
            Client client = new Client();
            client.start();
            this.client=client;
            Node root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/Waiting.fxml"));
            if(isserverchoose) {
                Scene scene = new Scene((Parent) root);
                stage.setScene(scene);
                checkConnect = new CheckConnect();
                checkConnect.start();
            }
            else{
                Label label=new Label("服务端已有waiting");
                AnchorPane pane=(AnchorPane) stage.getScene().lookup("#chooseonesidepane");
                pane.getChildren().add(label);
                label.setLayoutX(300);
                label.setLayoutY(100);
                label.setPrefSize(200, 50);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> label.setText("")));
                timeline.play();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class CheckConnect extends Thread{
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

