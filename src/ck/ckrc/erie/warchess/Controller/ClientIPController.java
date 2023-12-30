package ck.ckrc.erie.warchess.Controller;

import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import ck.ckrc.erie.warchess.ui.Play;
import ck.ckrc.erie.warchess.ui.Setting;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

/**
 * 这个类用于控制多人游戏选择客户端后的界面，在这里输入IP以连接服务端
 */
public class ClientIPController {
    @FXML
    void Back(ActionEvent event) throws Exception{
        Director.GetDirector().ChooseOneSide();
    }
    @FXML
    void makelaunch(ActionEvent event) {
        try {
            Node root = ChooseOneSideController.node;
            Client client= ChooseOneSideController.client;
            String ip = ((TextField) root.lookup("#IPtext")).getText();
            client.connectTo(ip);
            if(client.getSocket()==null){
                Label label = new Label("连接失败\n"+client.getError());
                ((AnchorPane)root).getChildren().add(label);
                label.setLayoutX(400);
                label.setLayoutY(0);
                label.setPrefSize(400, 200);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> label.setText("")));
                timeline.play();
            }
            else {
                Main.syncThread = new MapSyncThread(client.getSocket());
                Main.syncThread.start();
                Play.teamflag = 1;
                Setting.makesetting();
            }
        }catch (IOException e){
            Main.log.addLog("Cannot create MapSyncThread",this.getClass());
            Main.log.addLog(e,this.getClass());
        }
    }


}
