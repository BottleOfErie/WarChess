package ck.ckrc.erie.warchess.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
