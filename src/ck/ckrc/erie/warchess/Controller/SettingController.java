package ck.ckrc.erie.warchess.Controller;
import ck.ckrc.erie.warchess.ui.GameScene;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SettingController {
    @FXML
    private Button back;

    @FXML
    private Button decompile;

    @FXML
    private Button loadclass;

    @FXML
    void Back(MouseEvent event) {
        GameScene.gameScene.setRoot(GameScene.anchorPane);
    }

    @FXML
    void DeCompile(MouseEvent event) {

    }

    @FXML
    void LoadClass(MouseEvent event) {
        double opacity=loadclass.getOpacity();
        if(opacity==1){loadclass.setOpacity(0.5);}
        else{loadclass.setOpacity(1);}
    }
}
