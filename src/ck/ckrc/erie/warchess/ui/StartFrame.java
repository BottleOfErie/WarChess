package ck.ckrc.erie.warchess.ui;

import ck.ckrc.erie.warchess.Controller.ChooseOneSideController;
import ck.ckrc.erie.warchess.Director;
import ck.ckrc.erie.warchess.Main;
import ck.ckrc.erie.warchess.game.Player;
import ck.ckrc.erie.warchess.net.Client;
import ck.ckrc.erie.warchess.net.MapSyncThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class StartFrame {
    public static void loadtoStartFrame(Stage stage) {
        try {
            Parent root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/StartFrame.fxml"));
            Scene scene=new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadChooseOneSide(Stage stage) {
        try {
            Parent root = FXMLLoader.load(StartFrame.class.getResource("/Fxml/ChooseOneSide.fxml"));
            Scene scene=new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
