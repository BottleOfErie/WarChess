package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.StartFrame;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ClientIP {
    @FXML
    void Back(ActionEvent event) throws Exception{
        Director.GetDirector().ChooseOneSide();
    }
    @FXML
    void makelaunch(ActionEvent event) {
        try {
            Node root = ChooseOneSideController.node;

        Client client= ChooseOneSideController.client;
        Scene scene=root.getScene();
        String ip = ((TextField) root.lookup("#IPtext")).getText();
        if (ip.equals("127.0.0.1")) {
            try {
                client.connectTo(ip);
            }catch (IOException e){
                e.printStackTrace();
            }
            Main.syncThread = new MapSyncThread(client.getSocket());
            Main.syncThread.start();
            Main.currentGameEngine.setPlayer(1, Player.getNewPlayer(1));
            Play.teamflag=1;
            Director.GetDirector().gameStart();
        } else {
            Label label = new Label("请重新输入IP (·<w<))~");
            ((AnchorPane)root).getChildren().add(label);
            label.setLayoutX(300);
            label.setLayoutY(100);
            label.setPrefSize(200, 50);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> label.setText("")));
            timeline.play();
        }}catch (IOException e){e.printStackTrace();}
    }


}
